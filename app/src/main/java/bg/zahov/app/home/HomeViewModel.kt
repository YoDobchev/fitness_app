package bg.zahov.app.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bg.zahov.app.repository.UserRepository
import bg.zahov.app.realm_db.Workout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _userName = MutableLiveData<String>()
    private val repo = UserRepository.getInstance()
    val userName: LiveData<String> get() = _userName
    private val _numberOfWorkouts = MutableLiveData<Int>()
    val numberOfWorkouts: LiveData<Int> get() = _numberOfWorkouts
    private val _userWorkouts = MutableLiveData<List<Workout>>()
    val userWorkouts: LiveData<List<Workout>> get() = _userWorkouts


    //There are multiple issues to be fixed her
    //TODO(After login if there wasn't a realm file created we never have the username)
    //TODO(If the user is opted out of sync and this error isn't fixed we need to log him out however logging back in crashes)
    init {
        viewModelScope.launch {
                repo.getUserHomeInfo().let { result ->
                    result.first?.let{
                        _userName.postValue(it)
                        _numberOfWorkouts.postValue(result.second!!)
                        _userWorkouts.postValue(result.third!!)
                    } ?: auth.signOut()
                }
        }
    }
}


