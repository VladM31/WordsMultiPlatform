# Firebase Remote Config - Инструкция по настройке

## 1. Создание проекта Firebase

1. Перейдите на [Firebase Console](https://console.firebase.google.com/)
2. Создайте новый проект или используйте существующий
3. Добавьте ваше приложение в проект

## 2. Настройка для разных платформ

### Для Android (если будете использовать)
1. В Firebase Console добавьте Android приложение
2. Скачайте файл `google-services.json`
3. Поместите его в `composeApp/` директорию

### Для iOS (если будете использовать)
1. В Firebase Console добавьте iOS приложение
2. Скачайте файл `GoogleService-Info.plist`
3. Поместите его в `iosApp/iosApp/` директорию

### Для Web/JS/WASM
1. В Firebase Console перейдите в Project Settings
2. Добавьте Web приложение
3. Скопируйте конфигурацию Firebase

## 3. Конфигурация для Web платформы

Создайте файл `firebaseConfig.js` в `composeApp/src/webMain/resources/`:

```javascript
// Firebase configuration
const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_PROJECT_ID.firebaseapp.com",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_PROJECT_ID.appspot.com",
  messagingSenderId: "YOUR_SENDER_ID",
  appId: "YOUR_APP_ID"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
```

## 4. Настройка Remote Config в Firebase Console

1. Откройте Firebase Console
2. Перейдите в **Remote Config**
3. Добавьте параметры:
   - `app_version_min` (String) = "1.0.0"
   - `maintenance_mode` (Boolean) = false
   - `feature_login_with_telegram` (Boolean) = true
   - `welcome_message` (String) = "Welcome to WordsMultiPlatform!"
   - `max_login_attempts` (Number) = 3

4. Нажмите **Publish changes**

## 5. Использование в коде

### Пример 1: В ViewModel
```kotlin
class MyViewModel : ViewModel() {
    private val remoteConfig = RemoteConfigManager.getInstance()
    
    init {
        viewModelScope.launch {
            remoteConfig.initialize(RemoteConfigDefaults.defaults)
            
            val isEnabled = remoteConfig.getBoolean("feature_login_with_telegram")
            val message = remoteConfig.getString("welcome_message")
        }
    }
}
```

### Пример 2: Использование готового RemoteConfigViewModel
```kotlin
@Composable
fun MyScreen(viewModel: RemoteConfigViewModel = viewModel()) {
    val isTelegramEnabled by viewModel.isTelegramLoginEnabled.collectAsState()
    val welcomeMessage by viewModel.welcomeMessage.collectAsState()
    
    if (isTelegramEnabled) {
        // Показать кнопку входа через Telegram
    }
    
    Text(welcomeMessage)
}
```

### Пример 3: Прямое использование
```kotlin
val remoteConfig = RemoteConfigManager.getInstance()

// Инициализация
remoteConfig.initialize(RemoteConfigDefaults.defaults)

// Получение значений
val message = remoteConfig.getString("welcome_message")
val isEnabled = remoteConfig.getBoolean("feature_login_with_telegram")
val maxAttempts = remoteConfig.getLong("max_login_attempts")
```

## 6. Тестирование

1. Запустите приложение
2. Измените значения в Firebase Console
3. В приложении вызовите `remoteConfig.fetchAndActivate()`
4. Проверьте, что новые значения применились

## 7. Важные замечания

- GitLive Firebase SDK работает на всех платформах (JVM, JS, WASM, iOS, Android)
- Remote Config кеширует значения локально
- По умолчанию fetch происходит каждые 12 часов
- Можно настроить минимальный интервал между fetch запросами
- В режиме разработки рекомендуется устанавливать минимальный интервал в 0

## 8. Дополнительные настройки

### Изменение интервала fetch
```kotlin
remoteConfig.settings {
    minimumFetchInterval = 3600 // 1 час в секундах
}
```

### Обработка ошибок
```kotlin
try {
    remoteConfig.fetchAndActivate()
} catch (e: Exception) {
    // Используйте кешированные или дефолтные значения
    println("Error: ${e.message}")
}
```

