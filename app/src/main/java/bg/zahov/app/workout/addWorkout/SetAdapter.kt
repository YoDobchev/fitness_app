package bg.zahov.app.workout.addWorkout

import android.view.View
import bg.zahov.app.backend.Sets
import bg.zahov.app.common.BaseAdapter
import bg.zahov.app.common.SwipeGesture
import bg.zahov.app.data.ClickableSet
import bg.zahov.app.utils.equalsTo
import bg.zahov.fitness.app.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class SetAdapter : BaseAdapter<ClickableSet>(
    areItemsTheSame = { oldItem, newItem -> oldItem.set.equalsTo(newItem.set)},
    areContentsTheSame = {oldItem, newItem -> oldItem.set.equalsTo(newItem.set)},
    layoutResId = R.layout.set_item
), SwipeGesture.OnSwipeListener
{
    var itemClickListener: ItemClickListener<ClickableSet>? = null
    override fun createViewHolder(view: View) = SetAdapterViewHolder(view)

    inner class SetAdapterViewHolder(view: View) : BaseViewHolder(view) {
        private val setIndicator = view.findViewById<MaterialTextView>(R.id.set_number)
        private val check = view.findViewById<ShapeableImageView>(R.id.check)

        override fun bind(item: ClickableSet) {
            setIndicator.setOnClickListener {
                itemClickListener?.onSetNumberClicked(item, itemView)
            }
            check.setOnClickListener {
                itemClickListener?.onCheckClicked(item, itemView)
            }
        }
    }

    interface ItemClickListener<T> {
        fun onSetNumberClicked(item: T, clickedView: View)
        fun onCheckClicked(item: T, clickedView: View)
    }

    override fun onSwipe(position: Int) {
        deleteItem(position)
    }
}