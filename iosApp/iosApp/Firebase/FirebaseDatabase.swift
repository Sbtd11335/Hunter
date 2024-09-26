import FirebaseDatabase

final class FirebaseDatabase {
    private static let database = Database.database()
    private static let reference = database.reference()

    func goOffline() {
        FirebaseDatabase.database.goOffline()
    }
    func goOnline() {
        FirebaseDatabase.database.goOnline()
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
    func getDataRealtime(_ child: String, callback: @escaping (Any?) -> Void) {
        FirebaseDatabase.reference.child(child).observe(.value) { result in
            if (!result.exists()) {
                callback(nil)
            }
            else {
                callback(result.valueInExportFormat())
            }
        }
    }
    func getLastData(_ child: String, toLast: UInt = 1, callback: @escaping (Any?) -> Void) {
        FirebaseDatabase.reference.child(child).queryLimited(toLast: toLast).getData { error, result in
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
    func getLastDataRealtime(_ child: String, toLast: UInt = 1, callback: @escaping (Any?) -> Void) {
        FirebaseDatabase.reference.child(child).queryLimited(toLast: toLast).observe(.value) { result in
            if (!result.exists()) {
                callback(nil)
            }
            else {
                callback(result.valueInExportFormat())
            }
        }
    }
    func getLastData(_ child: String, atValue: String?, callback: @escaping (Any?) -> Void) {
        if (atValue == nil) {
            getData(child, callback: {
                callback($0)
            })
        }
        FirebaseDatabase.reference.child(child).queryOrderedByKey().queryStarting(atValue: atValue).getData { error, result in
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
    func getLastDataRealtime(_ child: String, atValue: String?, callback: @escaping (Any?) -> Void) {
        if (atValue == nil) {
            getDataRealtime(child, callback: {
                callback($0)
            })
        }
        else {
            FirebaseDatabase.reference.child(child).queryOrderedByKey().queryStarting(atValue: atValue).observe(.value) { result in
                if (!result.exists()) {
                    callback(nil)
                }
                else {
                    callback(result.valueInExportFormat())
                }
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
