# Code Style

Stack: Kotlin Multiplatform (Android, iOS, Desktop, JS, WasmJS), Compose Multiplatform, Material 3, Kodein DI,
Ktor Client, kotlinx.serialization, kotlinx.coroutines / Flow, kotlinx.datetime, multiplatform-settings,
AndroidX `ViewModel` (lifecycle).

Backend: a separate REST API (`api/*.json` contains its contracts). The app is a client of it: no database access
of its own beyond local caches/settings.

## 1. Package Structure

Root package: `vm.words.ua`. Source sets: `commonMain` holds almost everything; `androidMain`, `iosMain`,
`desktopMain`, `jsMain`, `wasmJsMain` hold only `actual` implementations and platform entry points.

```
vm.words.ua
├── App.kt                          # root composable
├── di/                             # AppModule (root DI), rememberInstance, initDi
├── navigation/                     # Screen routes, SimpleNavController, AppNavGraph, providers/, di/
├── db/                             # local persistence: dao/, entities/, sync/, drivers/
└── features/
    ├── core/                       # shared across several features (was `shared`), same layers as a feature
    │   ├── analytics/  config/  platform/  utils/
    │   ├── di/         # coreModule: network, managers, analytics
    │   ├── domain/     # managers/ (+impls/), models/ (+enums/, filters/), crypto/
    │   ├── net/        # HttpClientFactory, client/ (+impls/), requests/, responds/, exceptions/, utils/
    │   └── ui/         # AppTheme, components/, screen/, states/, models/, hints/, icons/, animation/, utils/
    ├── utils/                      # technical modules reused across features (hints, storage, validation, net status)
    └── <feature>/                  # auth, words, playlist, learning, exercise, settings, subscribes
        ├── di/         # <Feature>Module + one file per kind (clients, managers, view models)
        ├── domain/     # business logic
        │   ├── managers/ (+impls/)   mappers/   models/ (+enums/, filters/)   usecases/   exceptions/   factories/
        ├── net/        # clients of the backend / third-party APIs
        │   ├── clients/ (+impls/)   requests/   responds/
        └── ui/         # screens and MVI
            ├── screen/   components/   states/   actions/   effects/   vms/   bundles/   validation/   hints/
```

> Migration note: in the current code `core`, `utils` and the features still sit directly under `vm.words.ua`.
> The target layout moves them under `features/`; new code and moved files follow the target.

Rules:

- Code is split **by feature**; within a feature — **by layer** `di` / `domain` / `net` / `ui`.
- Dependencies: `ui → domain → net, core`. `net` knows nothing about `domain` and `ui`; `domain` knows nothing about
  `ui` (no Compose, no `ViewModel`, no navigation).
- A feature may use another feature's `domain` (through its interface), never its `ui` or `net`.
- **Shared code:** code used by one feature stays in that feature, even if it is generic (`<feature>/ui/components`,
  `<feature>/domain/models`). Do not move it to `features/core` "just in case". It moves to `features/core` (same
  layers: `domain` / `net` / `ui`) only when a second feature starts using it; the move replaces the copy, it does not
  add one. `features/utils` is for technical modules without business meaning.
- The interface lives in the package, the implementation in the `impls/` subpackage (`impl/` in older packages
  is legacy; new code uses `impls/`).
- One top-level declaration per file; the file is named after it. Closely tied declarations (a sealed hierarchy,
  a state with its helper enum) may share a file.
- The package matches the directory.

## 2. The `net` Package

Clients of the backend and third-party APIs. No business logic, no caching, no token storage.

```
net/
├── clients/        XxxClient interfaces
│   └── impls/      KtorXxxClient
├── requests/       input DTOs (@Serializable)
└── responds/       output DTOs (@Serializable)
```

| What    | Rule                                                                                                                                             | Example                        |
|---------|--------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|
| Client  | `XxxClient` interface + `KtorXxxClient(private val client: HttpClient)` in `impls/`                                                              | `AuthClient`, `KtorAuthClient` |
| Request | `@Serializable data class`, `Request` suffix; query-only calls take plain parameters or a `Map`                                                  | `LoginRequest`                 |
| Respond | `@Serializable data class`, `Respond` suffix; nested `data class`es for sub-structures; fields with defaults for everything the backend may omit | `AuthRespond`                  |
| Paged   | the shared `PagedRespond<T>` from `core.net.responds`                                                                                            |                                |

Rules:

- Methods are `suspend` and named after the API action: `logIn`, `findBy`, `save`, `delete`. The entity is not repeated
  when the client name already says it (`UserWordClient.findBy`, not `findUserWords`).
- A method takes one `XxxRequest` (or a few simple parameters) and returns one `XxxRespond`.
- The base URL comes from `AppRemoteConfig.baseUrl`; endpoints are written in the client, not scattered in `domain`.
- Errors: every call goes through `.throwIfError()`, which converts a non-2xx status into `ApiException` with the
  backend text. A client does not swallow exceptions and does not return `null`/`false` instead of an error; the one
  exception is a probe call like `isRegistered`, where "not found" is a normal answer.
- Authorization header and `401` handling are a part of the shared `HttpClient` (`core.net`), not of each client.
- The `HttpClient` is created once in `core/di` (`NetworkCoreModule`) and injected; clients never create it.
- Legacy: the `Krot…Client` prefix and the `responses/` package in some features are typos/legacy; new code uses
  `Ktor…Client` and `responds/`.

## 3. The `domain` Package

Business logic and orchestration. Platform-independent Kotlin; the only place that knows both `net` and local storage.

| Subpackage       | Role                                                     | Naming                                                    |
|------------------|----------------------------------------------------------|-----------------------------------------------------------|
| `managers`       | Business operations of the feature                       | `XxxManager` + `impls/XxxManagerImpl`                     |
| `usecases`       | A single reusable action/rule, method `invoke`/`execute` | `XxxUseCase`                                              |
| `mappers`        | `net` DTO → domain model and back                        | extension functions `toXxx()`, file `XxxMapper.kt`        |
| `models`         | Domain data                                              | by meaning; `XxxDto`, `XxxResult` only as a fallback      |
| `models/filters` | Search/filter parameters                                 | `XxxFilter`, with `toQueryMap()` when sent to the backend |
| `models/enums`   | Domain enums                                             | `XxxStatus`, `XxxType`, values in `UPPER_SNAKE_CASE`      |
| `exceptions`     | Domain exceptions                                        | `XxxException : Exception`                                |
| `factories`      | `expect` factories for platform objects                  | `XxxFactory`                                              |

Rules:

- A manager is an interface; the implementation is a class with constructor-injected dependencies
  (`private val xxxClient: XxxClient`, `private val userCacheManager: UserCacheManager`).
- Managers are `suspend` where they do I/O and never touch UI types. Blocking work runs inside the manager on the
  proper dispatcher (`withContext(Dispatchers.Default)`), not in the screen.
- Managers return **domain models** (`UserWord`, `PagedModels<T>`, `AuthResult`), never `Respond`/`Request` classes.
  Mapping happens in `mappers` (extensions, `internal` unless another feature needs them).
- Expected business outcomes (wrong password, "not found") are returned as a result model
  (`AuthResult(success, message)`); unexpected failures (no network, `5xx`) are thrown and caught by the view model.
- Early `return` for empty input and "nothing to do" cases.
- Names in a manager do not repeat its entity: `WordManager.findBy`, `save`, `delete`, not `findWord`.
  A search takes one filter model, not a method per condition.
- Parallel work — `coroutineScope { … async { … } … awaitAll() }`, not raw `GlobalScope`.
- Type-dependent behavior — via strategies (interface + implementations) or `when` over a sealed type/enum,
  not chains of `if/else`.
- **Model names** say what the thing *is* in the domain: `UserWord`, `Playlist`, `ExerciseSelection`. Do not use
  `Args` or verbs in a name when a real noun exists.
- Platform differences are hidden behind an `expect`/`actual` (see 8) or an interface in `domain` with a factory.

## 4. The `ui` Package and MVI

### 4.1 Layout

```
ui/
├── screen/        @Composable XxxScreen — the only place where state is collected and navigation is called
├── components/    reusable composables of the feature; stateless (state in, callbacks out)
├── states/        XxxState — immutable UI state
├── actions/       XxxAction — sealed interface of user intents
├── effects/       @Composable effects reacting to state (EndExerciseEffect)
├── vms/           XxxViewModel
├── bundles/       data passed between screens (XxxBundle)
├── validation/    XxxValidator — field validation over the state
└── hints/         onboarding hint controllers
```

(`vm/`, `screans/`, `componets/`, `validations/` in a few older packages are legacy spellings; use the names above.)

### 4.2 The MVI pattern

Unidirectional data flow, one screen = one `State` + one `Action` + one `ViewModel`.

```
        ┌───────────── state: StateFlow<State> ─────────────┐
        ▼                                                    │
   Screen (Compose)                                    ViewModel
        │                                                    ▲
        └──────────── sent(action: Action) ──────────────────┘
                                 │
                                 ▼
                      Manager (domain) → Client (net)
```

- **State (Model)** — the single source of truth for what the screen shows. An immutable `data class` with default
  values for every field, so `XxxState()` is a valid initial state. It holds only data the UI needs: field values,
  flags (`isLoading`, `isEnd`), lists, `errorMessage`. Derived values are computed properties (`val isPhone get() = …`),
  not stored fields. Shared behavior comes from the interfaces in `core.ui.states`:
  `ErrorableState` (`errorMessage: ErrorMessage?`) and `EndetableState` (`isEnd: Boolean`).
- **Action (Intent)** — everything the user or the screen can ask for. A `sealed interface XxxAction`:
  `data object` for an action without parameters (`Submit`), `data class` with `value` for an action with data
  (`SetUsername(val value: String)`). Names are commands or events in the imperative (`Submit`, `SetPassword`,
  `DismissErrorMessage`). The state is never mutated from the UI directly.
- **ViewModel (Reducer)** — extends `androidx.lifecycle.ViewModel`:
  ```kotlin
  class LoginViewModel(
      private val authManager: AuthManager,
      private val analytics: Analytics,
  ) : ViewModel() {

      private val mutableState = MutableStateFlow(LoginState())
      val state: StateFlow<LoginState> = mutableState

      fun sent(action: LoginAction) {
          when (action) {
              is LoginAction.Submit -> submit()
              is LoginAction.SetUsername -> mutableState.update { it.copy(username = action.value) }
              is LoginAction.DismissErrorMessage -> mutableState.update { it.copy(errorMessage = null) }
          }
      }
  }
  ```
    - Exactly one public entry point `sent(action)` and one public `state: StateFlow<State>`; the mutable flow is
      `private val mutableState`.
    - `when (action)` is exhaustive (no `else`), each branch is one line or a call to a private method named after
      the intent (`submit()`, `handleGoogleSignIn()`).
    - State changes only through `copy` (`mutableState.update { it.copy(...) }` is preferred over
      `value = value.copy(...)`, because it is atomic).
    - Work goes through `viewModelScope.launch(Dispatchers.Default)`; managers are called from there; the result is
      folded into the state. Every `suspend` call that can throw is wrapped in `try/catch` that writes
      `errorMessage` into the state (the view model never lets an exception escape the coroutine).
    - Validation runs in the view model before the call (`validator.validate(...)`); an invalid form sets
      `errorMessage` and returns.
    - Analytics events are sent from the view model, not from the screen.
    - A view model knows nothing about `Composable`, `NavController` or `Context`.
- **View (Screen)** — renders the state and sends actions:
  ```kotlin
  @Composable
  fun LoginScreen(navController: SimpleNavController, modifier: Modifier = Modifier) {
      val viewModel = rememberInstance<LoginViewModel>()
      val state by viewModel.state.collectAsState()

      LaunchedEffect(state.isEnd) {
          if (state.isEnd) navController.navigateAndClear(Screen.Home)
      }

      LoginForm(
          state = state,
          onSubmit = { viewModel.sent(LoginAction.Submit) },
      )
  }
  ```
    - A screen gets the view model through `rememberInstance<…>()` (see 5) and collects `state` with
      `collectAsState()`.
    - A screen is the only place that calls navigation. The view model signals "navigate" by a state flag (`isEnd`,
      `isNotFoundGoogle`); the screen reacts in a `LaunchedEffect(flag)` and, if the flag is one-shot,
      immediately resets it by sending an action (`ClearNotFound`).
    - One-shot reactions that are bigger than a few lines are extracted to an effect composable in `effects/`
      (`EndExerciseEffect(state, bundle, navController)`).
    - Components in `components/` are stateless: they take values and lambdas (`onSubmit: () -> Unit`), not the view
      model. A component never creates or looks up a view model.
    - Errors are shown from `state.errorMessage` (`ErrorMessageBox`, `AppToast`) and dismissed by an action.

Rules:

- Every user-visible change goes through `State`; there is no hidden mutable UI state in the view model. Purely visual
  state of one composable (scroll position, a focus request, an animation) stays in `remember`.
- Do not keep `Composable` lambdas, `Context`, `Color` or navigation objects in a `State`; keep data.
- Lists in a state are immutable `List<…>`; changing one element creates a new list.
- A state field changed by exactly one action has the same name as the action (`SetPassword` → `password`).
- Data that a screen passes to the next one goes through a `Bundle` (`bundles/`), a plain `data class` with defaults.
- Typed errors are `ErrorMessage` subclasses (`GoogleLoginErrorMessage`), so the screen can pick a special reaction
  by type.

### 4.3 Validation

- `XxxValidator` lives in `ui/validation` and builds a `Validator<XxxState>` from `ValidationScheme`s
  (`stringSchema("Password").length(8, 60).notBlank()`), one `add(State::field, scheme)` per field.
- A validator is a top-level `internal val xxxValidator: (StateFlow<XxxState>) -> Validator<XxxState>`;
  `validate(separator)` returns an empty string when valid, otherwise the joined messages.
- Import `Validator` and `ValidationScheme` instead of using fully-qualified names inside the lambda.

### 4.4 Hints

Onboarding hints are an `enum XxxScreenHintStep : ViewHintStep` (text + position), a `createXxxScreenHintController()`
composable built on `createDefaultHintController(key, lastIndex, undefinedStep)` and a `SimpleHintHost` around the
screen. The storage key includes a version suffix (`login_screen_v2`); bump it to show hints again.

### 4.5 Compose conventions

- Every screen/component takes `modifier: Modifier = Modifier` as the first optional parameter and applies it to the
  root element.
- Colors and fonts come from `AppTheme`; sizes that depend on the device use `rememberScaleFactor()`; no hard-coded
  colors in screens.
- Text is passed to components as parameters; a composable does not read a manager directly. The one exception is
  an effect composable that needs a manager (`rememberInstance<ExerciseStatisticalManager>()`).
- Previews and platform code do not go into `commonMain` screens unless they are `expect`/`actual`.
- Heavy work never runs in a composable body; it goes into the view model or `LaunchedEffect`.

## 5. Dependency Injection (Kodein)

Library: **Kodein DI** (`org.kodein.di`). The graph is built once (`initDi()` → `DiContainer.di`) from the feature
modules.

```
vm.words.ua
├── di/AppModule.kt            val appModules = DI { import(navigationModule); import(coreModule); import(authModules); … }
└── <feature>/di/
    ├── <Feature>Module.kt     val xxxModules = DI.Module("xxx") { import(client); import(managerDi); import(viewModelDi) }
    ├── ClientDi.kt            clients
    ├── ManagerDi.kt           managers
    └── ViewModelDi.kt         view models
```

| What            | Binding                                                                                              | Scope                    |
|-----------------|------------------------------------------------------------------------------------------------------|--------------------------|
| `HttpClient`    | `bind<HttpClient>() with singleton { … }` in `core/di`                                               | singleton                |
| Client          | `bind<AuthClient>() with singleton { KtorAuthClient(instance()) }`                                   | singleton                |
| Manager         | `bind<AuthManager>() with singleton { AuthManagerImpl(authClient = instance(), …) }`                 | singleton                |
| ViewModel       | `bind<LoginViewModel>() with factory { LoginViewModel(authManager = instance(), …) }`                | factory (new per screen) |
| Platform object | `bind<XxxManager>() with singleton { XxxManagerImpl() }` where `XxxManagerImpl` is an `expect class` | singleton                |

Rules:

- The module of a feature is `val <feature>Module(s) = DI.Module("<feature>")` and is registered in `appModules`.
  Split by kind into `internal val` modules in separate files; only the feature module is public.
- Bind **interfaces**, resolve **interfaces**: `bind<AuthManager>() with singleton { AuthManagerImpl(…) }`; no code
  outside the DI module and its platform factories mentions an `…Impl` class.
- Constructor injection only. Arguments are passed **by name** (`authClient = instance()`), which keeps the binding
  readable when a constructor grows.
- Managers and clients are `singleton`; view models are `factory`, never `singleton` (state must not outlive the
  screen).
- Do not use the DI container as a service locator inside `domain`: a class receives its dependencies in the
  constructor. The container is read only by `rememberInstance` (UI) and by the DI modules.
- In composables use `rememberInstance<T>()` (declared `expect` in `di/AppModule.kt`, `actual` per platform; for a
  `ViewModel` the Android `actual` ties the instance to the lifecycle owner, so it survives recomposition and
  configuration change).
- Circular dependencies are a design error; extract the shared part to a third class.
- Comments in a module explain *why* a binding is special, not what it is.

## 6. Navigation

- Routes are `data object`s of `sealed class Screen(val route: String)` in `navigation/Screen.kt`; the route is
  `snake_case`.
- `SimpleNavController` is passed to screens as a parameter; calls: `navigate(Screen.X)`, `navigateAndClear(Screen.X)`,
  `popBackStack()`.
- The graph is declared in `AppNavGraph`; one screen = one route = one composable `XxxScreen`.
- Data between screens: a `Bundle` or a parameter stored by the navigation layer, never a view model reference.

## 7. Local Data, Settings and Storage

- Key–value data goes through `KeyValueStorage` (`utils/storage`), built on multiplatform-settings; secrets (tokens) go
  through the `StorageSecurity`/`TokenCipher` crypto wrappers.
- The current user and token are held by `UserCacheManager` (`core/domain`); features read them from there and never
  from the storage directly.
- Persistent app data (`db/`) is accessed only through DAOs/managers; entities do not leave `domain`.
- Time — `kotlinx.datetime` (`Clock.System.now()`, `Instant`, `LocalDate`); no `java.time` in `commonMain`.

## 8. Multiplatform

- Platform-specific code is `expect`/`actual`. Prefer an **interface in `domain` + factory** for anything with a
  non-trivial API (`GoogleSignInServiceFactory`, `TelegramWebAppManagerFactory`, `SettingsFactory`); use
  `expect class XxxImpl() : Xxx` when the implementation is trivially constructed (`SoundManagerImpl`).
- The `actual` has the same file name with a platform suffix: `AppModule.android.kt`, `Platform.ios.kt`.
- `commonMain` never imports `android.*`, `java.*`, `platform.*`, `kotlinx.browser.*`. If it needs them, add a seam.
- Platform entry points (`MainActivity`, `main()` of desktop/web, iOS `MainViewController`) only call `initDi()`
  and render `App()`.
- Keep `expect` declarations small; do not copy business logic into several `actual`s.

## 9. General Code Style

- **Kotlin**: `data class` for data, `sealed interface` for closed hierarchies, `data object` for singleton cases,
  `when` as an expression, scope functions (`let`, `apply`) in moderation, no `!!` (use `?:`, `?.let`,
  `requireNotNull` with a message).
- **Immutability**: `val` by default; collections exposed as read-only types; state via `copy`.
- **Nullability**: a nullable type means "legitimately absent" (`val user: User? = null`), not "not loaded yet".
  Backend fields that may be missing have defaults in the `Respond`.
- **Coroutines**: `suspend` in `net` and `domain`; scopes are owned by the view model (`viewModelScope`) or by
  `LaunchedEffect`; never `GlobalScope`. `CancellationException` is rethrown, not swallowed, by a bare
  `catch (e: Exception)`: catch specific exceptions or rethrow it.
- **Naming**: classes/objects — `PascalCase`; functions and properties — `camelCase`; constants — `UPPER_SNAKE_CASE`
  (`private const val` at the top of a file); composables — `PascalCase` nouns (`LoginForm`);
  booleans — `isXxx`/`hasXxx`; conversions — `toXxx`.
- **Suffixes**: `Client`/`KtorXxxClient`, `Manager`/`ManagerImpl`, `UseCase`, `Request`, `Respond`, `State`, `Action`,
  `ViewModel`, `Screen`, `Bundle`, `Filter`, `Validator`, `Factory`, `Exception`, `Module`.
- **Imports**: explicit, no `*` in new code, no fully-qualified names inside the code (import them).
- **Formatting**: 4 spaces, K&R braces, trailing commas in multi-line parameter lists, a blank line between members,
  line length ≤ 120. Files end with a newline.
- **Comments / KDoc**: describe the contract and the *why* (an endpoint, a backend quirk), in English; KDoc on public
  interfaces of `net` and `domain`. No commented-out code, no `TODO("Not yet implemented")` left in a bound
  implementation.
- **Logging and analytics**: user-facing analytics events are constants in `AnalyticsEvents`; do not send personal
  data (phone, email) in event parameters in new code.
- **Strings and config**: no hard-coded URLs, keys or tokens in code; URLs come from `AppRemoteConfig`, secrets from
  build config or remote config. User-visible text is not duplicated in several places.
- **Do not leave behind**: magic numbers (extract to constants), unused imports/parameters, debug prints,
  `runCatching { … }.getOrDefault(false)` that hides a real error without a log.
