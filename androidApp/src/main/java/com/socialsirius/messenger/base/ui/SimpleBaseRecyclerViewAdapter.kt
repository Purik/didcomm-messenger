package com.socialsirius.messenger.base.ui

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class SimpleBaseRecyclerViewAdapter<T, H : RecyclerView.ViewHolder?> :
    BaseRecyclerViewAdapter<T, H>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        return getViewHolder(parent, layoutRes, viewType)
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        val  item = getItem(position)
        onBind(holder, item, position)
    }

    fun setOnAdapterClick(){

    }


    fun getInflatedView(@LayoutRes layoutRes: Int, parent: ViewGroup? = null, attach: Boolean = false): View {
        return inflater!!.inflate(layoutRes, parent, attach)
    }

    @get:LayoutRes
    abstract val layoutRes: Int

    abstract fun getViewHolder(parent: ViewGroup?, @LayoutRes layoutRes: Int, viewType: Int): H

   open  fun onBind(holder: H,item : T, position: Int) {
        holder?.itemView?.setOnClickListener {
            onAdapterItemClick?.onItemClick(item)
        }
    }
    var parentFragment: BaseFragment<*, *>? = null


    abstract class SimpleViewHolder<VB : ViewDataBinding, T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding  : VB? = DataBindingUtil.bind<VB>(itemView)

        abstract fun bind(item : T)
    }
}
