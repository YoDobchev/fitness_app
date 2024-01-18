package bg.zahov.app.utils

import bg.zahov.app.util.Adapter
import bg.zahov.app.data.local.Exercise
import io.realm.kotlin.ext.toRealmList
import org.mongodb.kbson.ObjectId

//FIXME replace these adapters with factory method in entity, also - use constants instead of hardcoded strings
class FirestoreExerciseAdapter : Adapter<Map<String, Any>?, Exercise> {
    override fun adapt(t: Map<String, Any>?): Exercise {
        return Exercise().apply {
            t?.let {
                _id = it["_id"] as? ObjectId ?: ObjectId()
                bodyPart = it["bodyPart"] as? String
                category = it["category"] as? String
                exerciseName = it["exerciseName"] as? String
                isTemplate = it["isTemplate"] as? Boolean

                val setsList = it["sets"] as? List<Map<String, Any>?> ?: emptyList()
                sets = setsList.mapNotNull { setsMap ->
                    setsMap?.let {
                        FirestoreSetsAdapter().adapt(setsMap)
                    }
                }.toRealmList()
            }
        }
    }
}
