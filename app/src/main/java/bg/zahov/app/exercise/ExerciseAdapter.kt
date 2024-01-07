package bg.zahov.app.exercise

import android.view.View
import bg.zahov.app.common.BaseAdapter
import bg.zahov.app.data.SelectableExercise
import bg.zahov.app.utils.applySelectAnimation
import bg.zahov.app.utils.equalsTo
import bg.zahov.fitness.app.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class ExerciseAdapter(
    private val selectable: Boolean,
) : BaseAdapter<SelectableExercise>(
    areItemsTheSame = { oldItem, newItem -> oldItem.exercise._id.toHexString() == newItem.exercise._id.toHexString() },
    areContentsTheSame = { oldItem, newItem -> oldItem.exercise.equalsTo(newItem.exercise) },
    layoutResId = R.layout.exercise_item
) {
    var itemClickListener: ItemClickListener<SelectableExercise>? = null
    override fun createViewHolder(view: View): BaseViewHolder = ExerciseAdapterViewHolder(view)

    inner class ExerciseAdapterViewHolder(view: View) : BaseViewHolder(view) {
        private val exerciseImage = view.findViewById<ShapeableImageView>(R.id.exercise_image)
        private val exerciseTitle = view.findViewById<MaterialTextView>(R.id.exercise_title)
        private val exerciseSubtitle = view.findViewById<MaterialTextView>(R.id.body_part)

        override fun bind(item: SelectableExercise) {
            exerciseTitle.text = item.exercise.exerciseName
            exerciseSubtitle.text = item.exercise.bodyPart
            itemView.setBackgroundResource(if (item.isSelected) R.color.selected else R.color.background)

            when (selectable) {
                true -> {
                    itemView.setOnClickListener {
                        exerciseImage.setImageResource(if (item.isSelected) R.drawable.ic_closed_lock else R.drawable.ic_check)
                        it.applySelectAnimation(
                            !item.isSelected,
                            R.color.selected,
                            R.color.background
                        )
                        it.setBackgroundResource(if (item.isSelected) R.color.selected else R.color.background)
                        item.isSelected = !item.isSelected
                        itemClickListener?.onItemClicked(item, adapterPosition, itemView)
                    }
                }

                false -> {
                    itemView.setOnClickListener {
                        itemClickListener?.onItemClicked(item, adapterPosition, itemView)
                    }
                }
            }
        }
    }

    interface ItemClickListener<T> {
        fun onItemClicked(item: T, itemPosition: Int, clickedView: View)
    }

    fun getSelected() = getItems().filter { it.isSelected }

}
