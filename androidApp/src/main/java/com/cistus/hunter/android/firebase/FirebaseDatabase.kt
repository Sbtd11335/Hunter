package com.cistus.hunter.android.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

open class FirebaseDatabase {
    companion object {
        private val database = Firebase.database
        private val reference = database.reference
    }

    fun getData(child: String, callback: (Any?) -> Unit) {
        reference.child(child).get()
            .addOnSuccessListener {
                if (it.exists())
                    callback(it.getValue(true))
                else
                    callback(null)
            }
            .addOnFailureListener { callback(null) }
    }
    fun getDataRealtime(child: String, callback: (Any?) -> Unit) {
        reference.child(child).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                    callback(snapshot.getValue(true))
                else
                    callback(null)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }
    fun setData(child: String, data: Map<String, Any>, callback: (String?) -> Unit) {
        reference.child(child).updateChildren(data)
            .addOnSuccessListener { callback(null) }
            .addOnFailureListener { callback(it.localizedMessage) }
    }
    fun deleteData(child: String, callback: (String?) -> Unit) {
        reference.child(child).removeValue()
            .addOnSuccessListener { callback(null) }
            .addOnFailureListener { callback(it.localizedMessage) }
    }
}