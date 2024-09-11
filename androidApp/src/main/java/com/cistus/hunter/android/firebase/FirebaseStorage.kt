package com.cistus.hunter.android.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseStorage {
    companion object {
        private val storage = Firebase.storage
        private val reference = storage.reference
    }

    fun getData(child: String, maxSize: Long = 1024 * 1024, callback: (ByteArray?) -> Unit) {
        reference.child(child).getBytes(maxSize)
            .addOnSuccessListener {
                if (it.isEmpty())
                    callback(null)
                else
                    callback(it)
            }
            .addOnFailureListener { callback(null) }
    }
}