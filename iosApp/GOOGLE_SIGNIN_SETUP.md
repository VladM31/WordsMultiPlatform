# Google Sign-In Setup for iOS

This document explains how to configure Google Sign-In for the iOS app in this Kotlin Multiplatform project.

## Prerequisites

1. Firebase project with iOS app configured
2. Google Sign-In enabled in Firebase Console (Authentication → Sign-in method → Google)
3. Xcode with Swift Package Manager support

## Step 1: Configure OAuth Client ID

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Select your Firebase project
3. Navigate to **APIs & Services** → **Credentials**
4. Click **Create Credentials** → **OAuth client ID**
5. Select **iOS** as the application type
6. Enter your Bundle ID: `vm.words.ua`
7. Click **Create**
8. Note the **Client ID** (format: `XXXX.apps.googleusercontent.com`)

## Step 2: Update GoogleService-Info.plist

Open `iosApp/iosApp/GoogleService-Info.plist` and replace the placeholder values:

```xml
<key>CLIENT_ID</key>
<string>YOUR_IOS_CLIENT_ID.apps.googleusercontent.com</string>
<key>REVERSED_CLIENT_ID</key>
<string>com.googleusercontent.apps.YOUR_IOS_CLIENT_ID</string>
```

**Alternative:** Download a fresh `GoogleService-Info.plist` from Firebase Console after enabling Google Sign-In. The CLIENT_ID will be auto-included.

## Step 3: Update Info.plist

Open `iosApp/iosApp/Info.plist` and update the URL scheme to match your REVERSED_CLIENT_ID:

```xml
<key>CFBundleURLSchemes</key>
<array>
    <string>com.googleusercontent.apps.YOUR_IOS_CLIENT_ID</string>
</array>
```

## Step 4: Verify Xcode Dependencies

The following Swift Package Manager dependencies should already be configured in `project.pbxproj`:

- **firebase-ios-sdk** (FirebaseCore, FirebaseAnalytics, FirebaseAuth)
- **GoogleSignIn-iOS** (GoogleSignIn, GoogleSignInSwift)

To verify in Xcode:
1. Open `iosApp/iosApp.xcodeproj`
2. Select the project in Navigator
3. Go to **Package Dependencies** tab
4. Ensure both packages are listed

## Step 5: Build and Test

1. Open `iosApp.xcodeproj` in Xcode
2. Build the project (Cmd + B)
3. Run on a simulator or device
4. Navigate to the login screen
5. Tap "Sign in with Google"

## Architecture

### Swift Side
- `GoogleSignInHelper.swift` - Native Google Sign-In implementation using GIDSignIn SDK
- Handles OAuth flow and Firebase Authentication
- Configured as a bridge to Kotlin via `GoogleSignInHelperBridge`

### Kotlin Side
- `GoogleSignInServiceFactory.ios.kt` - Kotlin implementation of `GoogleApiManager`
- `GoogleSignInHelperBridge` object receives callbacks from Swift
- Bridge is configured at app startup in `iOSApp.swift`

### Flow
1. App starts → `AppDelegate.didFinishLaunchingWithOptions` calls `GoogleSignInHelper.shared.configureBridge()`
2. User taps Sign In → Kotlin calls `GoogleSignInHelperBridge.signIn()`
3. Bridge invokes Swift handler → `GoogleSignInHelper.signIn()` shows Google Sign-In UI
4. User authenticates → Swift gets ID token → Firebase Auth signs in
5. Result returned to Kotlin via callback → UI updated

## Troubleshooting

### "Firebase configuration error: missing client ID"
- Ensure `CLIENT_ID` is added to `GoogleService-Info.plist`
- Verify `FirebaseApp.configure()` is called before `configureBridge()`

### "Cannot find root view controller"
- Ensure the app's window is properly set up
- Check that SwiftUI's `WindowGroup` is correctly configured

### Google Sign-In button not appearing
- Check `isAvailable()` returns `true`
- Verify `GoogleSignInHelperBridge` is configured

### URL callback not working
- Verify URL scheme in `Info.plist` matches `REVERSED_CLIENT_ID`
- Ensure `handleURL` is implemented in `AppDelegate`

### Build errors with GoogleSignIn
- Run `File → Packages → Resolve Package Versions` in Xcode
- Clean build folder (Cmd + Shift + K)
- Delete `~/Library/Developer/Xcode/DerivedData` if needed

## Security Notes

- Never commit real `CLIENT_ID` values to public repositories
- Use environment variables or `.xcconfig` files for sensitive configuration
- Consider using Firebase App Check for additional security

