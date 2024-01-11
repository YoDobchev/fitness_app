package bg.zahov.app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bg.zahov.app.repository.UserRepository
import bg.zahov.app.utils.isAValidEmail
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var repo: UserRepository? = null

    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            callback(false, "Fields cannot be empty!")
            return
        }
        if (!email.isAValidEmail()) {
            callback(false, "Please ensure your email is valid!")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    auth.currentUser?.uid?.let {
                        if (repo == null) {
                            repo = UserRepository.getInstance(it)
                        } else {
                            repo!!.updateUser(it)
                        }
                    }

                    repo?.let {
                        if (it.isSyncRequired()) {
                            viewModelScope.launch {
                                it.syncFromFirestore()
                            }
                        }
                        callback(true, null)
                    } ?: callback(false, "Failed to log in :(")

                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun sendPasswordResetEmail(email: String, callback: (String) -> Unit) {
        if (email.isEmpty()) {
            callback("Email field cannot be empty!")
            return
        }
        if (!email.isAValidEmail()) {
            callback("Please ensure your email is valid!")
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                callback(
                    if (task.isSuccessful) "Password link sent!" else task.exception?.message
                        ?: "Failed to send email reset link"
                )
            }

    }
}
