package bg.zahov.app.mediators

import bg.zahov.app.realm_db.Exercise
import bg.zahov.app.realm_db.RealmManager
import bg.zahov.app.realm_db.Workout

class UserRepository private constructor(){
    companion object {
        @Volatile
        private var repoInstance: UserRepository? = null
        fun getInstance() = repoInstance ?: synchronized(this) {
            repoInstance ?: UserRepository().also { repoInstance = it }
        }
    }

    private val realmInstance = RealmManager.getInstance()
    suspend fun getUserHomeInfo(userId: String): Triple<String?, Int, List<Workout>> {
        return realmInstance.getUserInformationForProfileFragment(userId)
    }
    suspend fun getUsername(userId: String): String? {
        return realmInstance.getUsername(userId)
    }
    suspend fun changeUserName(userId:String, newUserName: String){
        realmInstance.changeUserName(userId, newUserName)
    }
    suspend fun addExercise(userId: String, newExercise: Exercise){
        realmInstance.addExercise(userId, newExercise)
    }
    suspend fun getUserExercises(userId: String) = realmInstance.getUserExercises(userId)

}