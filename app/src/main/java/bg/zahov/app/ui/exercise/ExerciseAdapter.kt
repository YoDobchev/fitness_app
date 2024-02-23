package bg.zahov.app.ui.exercise

import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import bg.zahov.app.util.BaseAdapter
import bg.zahov.app.data.model.SelectableExercise
import bg.zahov.fitness.app.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

//issue with livedata not calling the rebind
class ExerciseAdapter(
    private val selectable: Boolean,
    private val replaceable: Boolean
) : BaseAdapter<SelectableExercise>(
    areItemsTheSame = { oldItem, newItem -> oldItem.exercise.name == newItem.exercise.name },
    areContentsTheSame = { oldItem, newItem -> oldItem == newItem },
    layoutResId = R.layout.item_exercise
) {
    var itemClickListener: ItemClickListener<SelectableExercise>? = null
    override fun createViewHolder(view: View): BaseViewHolder<SelectableExercise> =
        ExerciseAdapterViewHolder(view)

    inner class ExerciseAdapterViewHolder(view: View) : BaseViewHolder<SelectableExercise>(view) {
        private val exerciseBackground =
            view.findViewById<ConstraintLayout>(R.id.exercise_background)
        private val exerciseImage = view.findViewById<ShapeableImageView>(R.id.exercise_image)
        private val exerciseTitle = view.findViewById<MaterialTextView>(R.id.exercise_title)
        private val exerciseSubtitle = view.findViewById<MaterialTextView>(R.id.body_part)

        override fun bind(item: SelectableExercise) {
            Log.d("BIND", "${item.exercise.name} is ${item.isSelected}")
            exerciseTitle.text = item.exercise.name
            exerciseSubtitle.text = item.exercise.bodyPart.name
            //TODO(add actual image resources and determine which one for which exercise)
            exerciseImage.setImageResource(if (item.isSelected) R.drawable.ic_closed_lock else R.drawable.ic_check)
            exerciseBackground.setBackgroundResource(if (item.isSelected) R.color.selected else R.color.background)

            itemView.setOnClickListener {
                itemClickListener?.onItemClicked(item)
                when {
                    selectable -> {
                        onClick(item)
                    }

                    replaceable -> {
                        Log.d("REPLACABLE", "REPLACABLE")
                        onClick(item)
                        deselectRemainingExercise(item)
                    }
                }
            }
        }

        private fun onClick(item: SelectableExercise) {
            item.isSelected = !item.isSelected
            exerciseBackground.setBackgroundResource(if (item.isSelected) R.color.selected else R.color.background)
        }
    }

    private fun deselectRemainingExercise(ignoreItem: SelectableExercise) {
        getRecyclerViewItems().forEach {
            if (it != ignoreItem && it.isSelected) {
                it.isSelected = false
            }
        }
        notifyDataSetChanged()
    }

    interface ItemClickListener<T> {
        fun onItemClicked(item: T)
    }
}
