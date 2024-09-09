package com.cistus.hunter.android.firebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuth {
    companion object {
        private val auth = Firebase.auth
    }

    fun currentUser(): FirebaseUser? = auth.currentUser
    fun reload(callback: (String?) -> Unit) {
        currentUser()
            ?.reload()
            ?.addOnSuccessListener { callback(null) }
            ?.addOnFailureListener {
                if (it is FirebaseAuthException)
                    callback(it.errorCode)
                else
                    callback(it.localizedMessage)
            }
    }
    fun reAuthenticate(authCredential: AuthCredential, callback: (String?) -> Unit) {
        currentUser()?.let { currentUser ->
            currentUser.reauthenticate(authCredential)
                .addOnSuccessListener { callback(null) }
                .addOnFailureListener {
                    if (it is FirebaseAuthException)
                        callback(it.errorCode)
                    else
                        callback(it.localizedMessage)
                }
        }
    }
    fun isEmailVerified(): Boolean? = currentUser()?.isEmailVerified
    fun createAccount(emailAddress: String, password: String, callback: (String?) -> Unit) {
        auth.createUserWithEmailAndPassword(emailAddress, password)
            .addOnSuccessListener {
                reload { reloadResult ->
                    callback(reloadResult)
                }
            }
            .addOnFailureListener {
                if (it is FirebaseAuthException)
                    callback(it.errorCode)
                else
                    callback(it.localizedMessage)
            }
    }
    fun signIn(emailAddress: String, password: String, callback: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(emailAddress, password)
            .addOnSuccessListener {
                reload { reloadResult ->
                    callback(reloadResult)
                }
            }
            .addOnFailureListener {
                if (it is FirebaseAuthException)
                    callback(it.errorCode)
                else
                    callback(it.localizedMessage)
            }
    }
    fun sendPasswordReset(emailAddress: String, callback: (String?) -> Unit) {
        auth.sendPasswordResetEmail(emailAddress)
            .addOnSuccessListener { callback(null) }
            .addOnFailureListener {
                if (it is FirebaseAuthException)
                    callback(it.errorCode)
                else
                    callback(it.localizedMessage)
            }
    }
    fun sendEmailVerification(callback: (String?) -> Unit) {
        currentUser()
            ?.sendEmailVerification()
            ?.addOnSuccessListener { callback(null) }
            ?.addOnFailureListener {
                if (it is FirebaseAuthException)
                    callback(it.errorCode)
                else
                    callback(it.localizedMessage)
            }
    }
    fun updateEmailAddress(emailAddress: String, callback: (String?) -> Unit) {
        currentUser()
            ?.verifyBeforeUpdateEmail(emailAddress)
            ?.addOnSuccessListener { callback(null) }
            ?.addOnFailureListener {
                if (it is FirebaseAuthException)
                    callback(it.errorCode)
                else
                    callback(it.localizedMessage)
            }
    }
    fun signOut() = auth.signOut()

}