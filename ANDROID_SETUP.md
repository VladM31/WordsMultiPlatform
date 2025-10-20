# Запуск Android приложения

## Что было сделано

Проект теперь поддерживает Android! Были добавлены:

### 1. Gradle конфигурация
- Добавлен Android Gradle Plugin (AGP 8.6.1)
- Добавлен Google Services Plugin для Firebase
- Настроен `androidTarget` в Kotlin Multiplatform
- Добавлена зависимость на `:shared` модуль

### 2. Android исходные файлы
- `MainActivity.kt` - главная Activity с Compose UI
- `BackHandlerAndroid.kt` - обработка системной кнопки "Назад"
- `DomainIdnAndroid.kt` - конвертация доменов в ASCII
- `TokenCipher.android.kt` - шифрование токенов для Android
- `SettingsFactory.android.kt` - хранилище настроек через SharedPreferences
- `AuthHistorySettingsFactory.android.kt` - история авторизации
- `Platform.android.kt` - платформенная информация

### 3. Android Manifest
- Добавлено разрешение INTERNET
- Настроена MainActivity как LAUNCHER
- Настроена тема приложения

## Как запустить

### Из командной строки (Windows):

```cmd
# Сборка Debug APK
gradlew.bat :composeApp:assembleDebug

# Установка на подключенное устройство/эмулятор
gradlew.bat :composeApp:installDebug
```

### Из Android Studio / IntelliJ IDEA:

1. Откройте проект в Android Studio
2. Дождитесь синхронизации Gradle
3. Выберите конфигурацию запуска "composeApp" с Android target
4. Нажмите Run (зеленый треугольник)

### Требования:

- Android SDK 24+ (Android 7.0+)
- Target SDK 35
- JDK 17
- Gradle 8.14.3

## Структура проекта

```
composeApp/
├── src/
│   ├── commonMain/          # Общий код для всех платформ
│   ├── androidMain/         # Android-специфичный код
│   │   ├── AndroidManifest.xml
│   │   └── kotlin/
│   │       └── vm/words/ua/
│   │           ├── MainActivity.kt
│   │           ├── navigation/
│   │           ├── validation/
│   │           ├── core/
│   │           └── auth/
│   ├── jvmMain/            # Desktop (JVM)
│   ├── jsMain/             # Web (JS)
│   └── wasmJsMain/         # Web (WASM)
└── build.gradle.kts

shared/
├── src/
│   ├── commonMain/
│   ├── androidMain/        # Android-специфичный код shared модуля
│   └── ...
└── build.gradle.kts
```

## Firebase

Проект использует Firebase Remote Config через `firebaseMain` source set, который доступен для Android, JVM и JS платформ. Firebase автоматически инициализируется из файла `google-services.json`.

## Отладка

APK файл после сборки находится в:
```
composeApp/build/outputs/apk/debug/composeApp-debug.apk
```

Для просмотра логов:
```cmd
adb logcat | findstr "Words"
```

## Следующие шаги

1. Запустите синхронизацию Gradle в IDE
2. Подключите Android устройство или запустите эмулятор
3. Выполните `gradlew.bat :composeApp:installDebug`
4. Приложение "Words" появится в списке приложений на устройстве

Готово! 🎉

