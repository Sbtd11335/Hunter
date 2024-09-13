import FirebaseDatabase

final class FirebaseDatabase {
    private static let database = Database.database()
    private static let reference = database.reference()
    private static var initialized = false
    
    init() {
        if (!FirebaseDatabase.initialized) {
            FirebaseDatabase.database.isPersistenceEnabled = true
            FirebaseDatabase.reference.keepSynced(true)
            FirebaseDatabase.initialized = true
        }
    }
    
    
    func getData(_ child: String, callback: @escaping (Any?) -> Void) {
        FirebaseDatabase.reference.child(child).getData { error, result in
            if error != nil {
                callback(nil)
            }
            else {
                guard let result = result else {
                    callback(nil)
                    return
                }
                callback(result.valueInExportFormat())
            }
        }
    }
    func setData(_ child: String, data: [AnyHashable : Any], callback: @escaping (String?) -> Void) {
        FirebaseDatabase.reference.child(child).updateChildValues(data) { error, _ in
            if let error = error {
                callback(error.localizedDescription)
            }
            else {
                callback(nil)
            }
        }
    }
    func deleteData(_ child: String, callback: @escaping (String?) -> Void) {
        FirebaseDatabase.reference.child(child).removeValue { error, _ in
            if let error = error {
                callback(error.localizedDescription)
            }
            else {
                callback(nil)
            }
        }
    }
}
