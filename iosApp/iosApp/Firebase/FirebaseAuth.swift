import FirebaseAuth

final class FirebaseAuth {
    private static let auth = Auth.auth()
    
    func currentUser() -> User? { return FirebaseAuth.auth.currentUser }
    func reload(callback: @escaping (AuthErrorCode?) -> Void) {
        currentUser()?.reload { error in
            if let error = error as? NSError {
                callback(AuthErrorCode(rawValue: error.code))
            }
            else {
                callback(nil)
            }
        }
    }
    func isEmailVerified() -> Bool? { currentUser()?.isEmailVerified }
    func createAccount(_ emailAddress: String, _ password: String, callback: @escaping (AuthErrorCode?) -> Void) {
        FirebaseAuth.auth.createUser(withEmail: emailAddress, password: password) { result, error in
            if let error = error as? NSError {
                callback(AuthErrorCode(rawValue: error.code))
            }
            else {
                self.reload { reloadError in
                    callback(reloadError)
                }
            }
        }
    }
    func signIn(_ emailAddress: String, _ password: String, callback: @escaping (AuthErrorCode?) -> Void) {
        FirebaseAuth.auth.signIn(withEmail: emailAddress, password: password) { result, error in
            if let error = error as? NSError {
                callback(AuthErrorCode(rawValue: error.code))
            }
            else {
                self.reload { reloadResult in
                    callback(reloadResult)
                }
            }
        }
    }
    func sendPasswordReset(_ emailAddress: String, callback: @escaping (AuthErrorCode?) -> Void) {
        FirebaseAuth.auth.sendPasswordReset(withEmail: emailAddress) { error in
            if let error = error as? NSError {
                callback(AuthErrorCode(rawValue: error.code))
            }
            else {
                callback(nil)
            }
        }
    }
    func sendEmailVerification(callback: @escaping (AuthErrorCode?) -> Void) {
        currentUser()?.sendEmailVerification { error in
            if let error = error as? NSError {
                callback(AuthErrorCode(rawValue: error.code))
            }
            else {
                callback(nil)
            }
        }
    }
    func updateEmailAddress(_ emailAddress: String, callback: @escaping (AuthErrorCode?) -> Void) {
        currentUser()?.sendEmailVerification(beforeUpdatingEmail: emailAddress) { error in
            if let error = error as? NSError {
                callback(AuthErrorCode(rawValue: error.code))
            }
            else {
                callback(nil)
            }
        }
    }
    func signOut() -> Bool {
        do {
            try FirebaseAuth.auth.signOut()
            return true
        }
        catch {
            return false
        }
    }
}
