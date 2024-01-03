package bg.zahov.app.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bg.zahov.app.realm_db.Exercise
import bg.zahov.app.utils.equalsTo
import bg.zahov.fitness.app.R
import com.google.android.material.textview.MaterialTextView

class ExerciseAdapter : RecyclerView.Adapter<ExerciseAdapter.ExerciseAdapterViewHolder>() {

    private val items = ArrayList<Exercise>()
    var itemClickListener: ItemClickListener<Exercise>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAdapterViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        return ExerciseAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newExercises: List<Exercise>) {
        val oldList: List<Exercise> = ArrayList(items)
        items.clear()
        items.addAll(newExercises)
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newExercises.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition]._id.toHexString() == newExercises[newItemPosition]._id.toHexString()
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].equalsTo(newExercises[newItemPosition])
            }
        }).dispatchUpdatesTo(this)
    }

    inner class ExerciseAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val exerciseTitle = view.findViewById<MaterialTextView>(R.id.exercise_title)
        private val exerciseSubtitle = view.findViewById<MaterialTextView>(R.id.body_part)

        //TODO(on click) :)
        fun bind(exercise: Exercise) {
            exerciseTitle.text = exercise.exerciseName
            exerciseSubtitle.text = exercise.bodyPart
        }
    }

    interface ItemClickListener<T> {
        fun onItemClicked(item: T, itemPosition: Int, clickedView: View)
    }
}