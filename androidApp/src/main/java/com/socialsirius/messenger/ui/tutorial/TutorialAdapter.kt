package com.socialsirius.messenger.ui.tutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.SimpleBaseRecyclerViewAdapter
import com.socialsirius.messenger.databinding.ItemsTutorialBinding

import com.socialsirius.messenger.models.ui.ItemTutorial


class TutorialAdapter(override val layoutRes: Int = R.layout.items_tutorial) :
    SimpleBaseRecyclerViewAdapter<ItemTutorial, TutorialAdapter.TutorialViewHolder>() {


    override fun onBind(holder: TutorialViewHolder, item: ItemTutorial, position: Int) {
        val item = getItem(position)
        holder?.bind(item)
    }


    override fun getViewHolder(
        parent: ViewGroup?,
        layoutRes: Int,
        viewType: Int
    ): TutorialViewHolder {
        return TutorialViewHolder(getInflatedView(layoutRes,parent))
    }

    class TutorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       var binding: ItemsTutorialBinding? = DataBindingUtil.bind<ItemsTutorialBinding>(itemView)
        fun bind(item: ItemTutorial) {
            binding?.model = item
            item.drawableRes?.let {
                binding?.tutorialImage?.setImageResource(it)
            }
        }
    }
}