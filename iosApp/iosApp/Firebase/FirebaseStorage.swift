import FirebaseStorage
import Foundation

class FirebaseStorage {
    private static let storage = Storage.storage()
    private static let reference = storage.reference()
    
    func getData(_ child: String, _ maxSize: Int64 = 1024 * 1024, callback: @escaping (Data?) -> Void) {
        FirebaseStorage.reference.child(child).getData(maxSize: maxSize) { result, error in
            if let error = error {
                callback(nil)
            }
            else {
                if let result = result {
                    callback(result)
                }
                else {
                    callback(nil)
                }
            }
        }
    }
}
