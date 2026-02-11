import Foundation
import GoogleSignIn
import FirebaseCore
import FirebaseAuth
import ComposeApp

/// A singleton helper for Google Sign-In on iOS
/// Bridges to Kotlin via GoogleSignInHelperBridge
public class GoogleSignInHelper {

    public static let shared = GoogleSignInHelper()
    
    /// Web Client ID from Firebase Console - MUST match the one used on Android
    /// This is required for Firebase Auth to validate the ID token
    private let webClientID = "1005445939198-rlkom295h4c9rmuf4jd43cv87nm9qpsk.apps.googleusercontent.com"

    private init() {}

    /// Configure the Kotlin bridge at app startup
    /// Call this from AppDelegate.didFinishLaunchingWithOptions
    public func configureBridge() {
        GoogleSignInHelperBridge.shared.configure(
            signIn: { [weak self] callback in
                self?.signIn { result in
                    // Convert [String: Any] to Kotlin-compatible dictionary
                    var kotlinDict: [String: Any?] = [:]
                    kotlinDict["success"] = result["success"]
                    kotlinDict["email"] = result["email"]
                    kotlinDict["displayName"] = result["displayName"]
                    kotlinDict["idToken"] = result["idToken"]
                    kotlinDict["error"] = result["error"]
                    callback(kotlinDict)
                }
            },
            signOut: { [weak self] in
                self?.signOut()
            },
            isAvailable: { [weak self] in
                return KotlinBoolean(bool: self?.isAvailable() ?? false)
            }
        )
    }

    /// Sign in with Google and authenticate with Firebase
    /// - Parameter completion: Callback with result dictionary
    public func signIn(completion: @escaping ([String: Any]) -> Void) {
        // Get the client ID from GoogleService-Info.plist
        guard let clientID = FirebaseApp.app()?.options.clientID else {
            completion([
                "success": false,
                "error": "Firebase configuration error: missing client ID"
            ])
            return
        }

        // Get the root view controller
        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
              let rootViewController = windowScene.windows.first?.rootViewController else {
            completion([
                "success": false,
                "error": "Cannot find root view controller"
            ])
            return
        }

        // Find the topmost presented view controller
        var topController = rootViewController
        while let presentedController = topController.presentedViewController {
            topController = presentedController
        }

        // Configure Google Sign-In with iOS Client ID and Server (Web) Client ID
        // The serverClientID is needed so that the ID token is valid for Firebase Auth
        let config = GIDConfiguration(
            clientID: clientID,
            serverClientID: webClientID
        )
        GIDSignIn.sharedInstance.configuration = config

        // Perform sign-in
        GIDSignIn.sharedInstance.signIn(withPresenting: topController) { [weak self] result, error in
            if let error = error {
                let nsError = error as NSError
                // Check if user cancelled
                if nsError.code == GIDSignInError.canceled.rawValue {
                    completion([
                        "success": false,
                        "error": "Sign-in cancelled"
                    ])
                } else {
                    completion([
                        "success": false,
                        "error": "Google Sign-In failed: \(error.localizedDescription)"
                    ])
                }
                return
            }

            guard let user = result?.user,
                  let idToken = user.idToken?.tokenString else {
                completion([
                    "success": false,
                    "error": "Failed to get ID token from Google"
                ])
                return
            }

            // Authenticate with Firebase
            let credential = GoogleAuthProvider.credential(
                withIDToken: idToken,
                accessToken: user.accessToken.tokenString
            )

            Auth.auth().signIn(with: credential) { authResult, authError in
                if let authError = authError {
                    completion([
                        "success": false,
                        "error": "Firebase authentication failed: \(authError.localizedDescription)"
                    ])
                    return
                }

                guard let firebaseUser = authResult?.user else {
                    completion([
                        "success": false,
                        "error": "Firebase authentication returned no user"
                    ])
                    return
                }

                completion([
                    "success": true,
                    "email": firebaseUser.email ?? user.profile?.email ?? "",
                    "displayName": firebaseUser.displayName ?? user.profile?.name ?? "",
                    "idToken": idToken
                ])
            }
        }
    }

    /// Sign out from Google and Firebase
    public func signOut() {
        GIDSignIn.sharedInstance.signOut()
        try? Auth.auth().signOut()
    }

    /// Check if Google Sign-In is available (Firebase is configured)
    public func isAvailable() -> Bool {
        return FirebaseApp.app()?.options.clientID != nil
    }

    /// Handle URL callback from Google Sign-In
    public func handleURL(_ url: URL) -> Bool {
        return GIDSignIn.sharedInstance.handle(url)
    }

    /// Restore previous sign-in session if available
    public func restorePreviousSignIn(completion: @escaping ([String: Any]) -> Void) {
        GIDSignIn.sharedInstance.restorePreviousSignIn { user, error in
            if let error = error {
                completion([
                    "success": false,
                    "error": "Restore failed: \(error.localizedDescription)"
                ])
                return
            }

            guard let user = user,
                  let idToken = user.idToken?.tokenString else {
                completion([
                    "success": false,
                    "error": "No previous session found"
                ])
                return
            }

            // Verify Firebase auth state
            if let firebaseUser = Auth.auth().currentUser {
                completion([
                    "success": true,
                    "email": firebaseUser.email ?? user.profile?.email ?? "",
                    "displayName": firebaseUser.displayName ?? user.profile?.name ?? "",
                    "idToken": idToken
                ])
            } else {
                // Need to re-authenticate with Firebase
                let credential = GoogleAuthProvider.credential(
                    withIDToken: idToken,
                    accessToken: user.accessToken.tokenString
                )

                Auth.auth().signIn(with: credential) { authResult, authError in
                    if let authError = authError {
                        completion([
                            "success": false,
                            "error": "Firebase re-auth failed: \(authError.localizedDescription)"
                        ])
                        return
                    }

                    guard let firebaseUser = authResult?.user else {
                        completion([
                            "success": false,
                            "error": "Firebase re-auth returned no user"
                        ])
                        return
                    }

                    completion([
                        "success": true,
                        "email": firebaseUser.email ?? "",
                        "displayName": firebaseUser.displayName ?? "",
                        "idToken": idToken
                    ])
                }
            }
        }
    }
}

