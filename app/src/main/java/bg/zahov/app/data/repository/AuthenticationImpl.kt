package bg.zahov.app.data.repository

import android.util.Log
import bg.zahov.app.data.exception.AuthenticationException
import bg.zahov.app.data.interfaces.Authentication
import bg.zahov.app.data.remote.FirebaseAuthentication

class AuthenticationImpl : Authentication {
    companion object {
        @Volatile
        private var instance: AuthenticationImpl? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: AuthenticationImpl().also { instance = it }
            }
    }

    private val auth = FirebaseAuthentication.getInstance()

    override suspend fun signup(username: String, email: String, password: String) {
        auth.signup(username, email, password)
    }

    override suspend fun login(email: String, password: String) {
        Log.d("login", "calling from impl")
        auth.login(email, password)
    }

    override suspend fun logout() {
        auth.logout()
    }

    override suspend fun deleteAccount() {
        auth.deleteAccount()
    }

    override suspend fun passwordResetByEmail(email: String) = auth.passwordResetByEmail(email)


    override suspend fun passwordResetForLoggedUser() = auth.passwordResetForLoggedUser()

    override fun isAuthenticated() = auth.isAuthenticated()

    override suspend fun initDataSources() {
        auth.init()
    }

    override suspend fun updatePassword(newPassword: String) {
        auth.updatePassword(newPassword)
    }

    override suspend fun updateEmail(newEmail: String) {
        auth.updateEmail(newEmail)
    }

    override suspend fun reauthenticate(password: String) = auth.reauthenticate(password)
}