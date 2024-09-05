import Firebase
import FirebaseCore
import FirebaseAuth

final class FirebaseAuth {
    private static let auth = Auth.auth()
    static func getCurrentUser() -> User? { auth.currentUser }
    static func isEmailVerified(callback: @escaping (Bool?) -> Void) {
        guard let currentUser = getCurrentUser() else {
            callback(nil)
            return
        }
        FirebaseAuth.reload { result in
            if result != nil {
                callback(nil)
                return
            }
            callback(currentUser.isEmailVerified)
        }
    }
    static func updateUser(_ user: User?, callback: @escaping (String?) -> Void) {
        auth.updateCurrentUser(user) { error in
            if let error = error {
                callback(error.localizedDescription)
                return
            }
            callback(nil)
        }
    }
    static func reload(callback: @escaping (String?) -> Void) {
        auth.currentUser?.reload() { error in
            if let error = error {
                callback(error.localizedDescription)
                return
            }
            callback(nil)
        }
    }
    static func createUserAccount(_ emailAddress: String, _ password: String, callback: @escaping (String?) -> Void) {
        auth.createUser(withEmail: emailAddress, password: password) { result, error in
            if let error = error {
                callback(error.localizedDescription)
                return
            }
            updateUser(result?.user) { updateResult in
                reload() { reloadError in
                    callback(reloadError)
                    return
                }
            }
        }
    }
    static func sendEmailVerification(callback: @escaping (String?) -> Void) {
        guard let currentUser = getCurrentUser() else {
            callback("User account not found.")
            return
        }
        currentUser.sendEmailVerification() { error in
            if let error = error {
                callback(error.localizedDescription)
                return
            }
            callback(nil)
        }
    }
    static func sendPasswordReset(_ emailAddress: String, callback: @escaping (String?) -> Void) {
        auth.sendPasswordReset(withEmail: emailAddress) { error in
            if let error = error {
                callback(error.localizedDescription)
                return
            }
            callback(nil)
        }
    }
    static func updateEmailAddress(_ emailAddress: String, _ callback: @escaping (String?) -> Void) {
        guard let currentUser = getCurrentUser() else {
            callback("User account not found.")
            return
        }
        currentUser.sendEmailVerification(beforeUpdatingEmail: emailAddress) { error in
            if let error = error {
                callback(error.localizedDescription)
                return
            }
            callback(nil)
        }
    }
    static func signIn(_ emailAddress: String, _ password: String, callback: @escaping (String?) -> Void) {
        auth.signIn(withEmail: emailAddress, password: password) { result, error in
            if let error = error {
                callback(error.localizedDescription)
                return
            }
            updateUser(result?.user) { updateResult in
                reload() { reloadError in
                    callback(reloadError)
                    return
                }
            }
            callback(nil)
        }
    }
    static func signOut(callback: @escaping (String?) -> Void) {
        do {
            try auth.signOut()
            callback(nil)
        }
        catch let error {
            callback(error.localizedDescription)
        }
    }
}
