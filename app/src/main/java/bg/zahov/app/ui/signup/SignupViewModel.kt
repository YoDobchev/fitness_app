package bg.zahov.app.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bg.zahov.app.data.exception.AuthenticationException
import bg.zahov.app.data.remote.AuthenticationImpl
import bg.zahov.app.util.isEmail
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val auth = AuthenticationImpl.getInstance()

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state



    fun signUp(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String,
    ) {
        if (areFieldsEmpty(userName, email, password)) {
            _state.value = State.FailedAuthentication("Do not leave empty fields")
            return
        }

        if (!email.isEmail()) {
            _state.value = State.FailedAuthentication("Email is not valid")
            return
        }

        if (password != confirmPassword || password.length < 6) {
            _state.value =
                State.FailedAuthentication("Make sure the passwords are matching and at least 6 characters long")
            return
        }

        viewModelScope.launch {
            try {
                auth.signup(userName, email, password)
                _state.postValue(State.Authenticated(true))
            } catch (e: AuthenticationException) {
                _state.postValue(State.FailedAuthentication(e.message))
            }
        }

    }

    private fun areFieldsEmpty(userName: String?, email: String?, pass: String?) =
        listOf(userName, email, pass).any { it.isNullOrEmpty() }

    sealed interface State {
        object Default : State

        data class Authenticated(val proceed: Boolean) : State
        data class FailedAuthentication(val message: String?) : State
    }
}

