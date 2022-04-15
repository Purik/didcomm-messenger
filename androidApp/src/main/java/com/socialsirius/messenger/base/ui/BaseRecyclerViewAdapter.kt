package com.socialsirius.messenger.base.ui

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.socialsirius.messenger.base.App

/**
 * Created by IGOR on 15.03.2017.
 */
abstract class BaseRecyclerViewAdapter<T, H : RecyclerView.ViewHolder?> : RecyclerView.Adapter<H> {
    var inflater: LayoutInflater? = null
    var onAdapterItemClick: OnAdapterItemClick<T>? = null
    var onCustomBtnClick: OnCustomBtnClick<T>? = null

    protected constructor() {
        inflater =
            App.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    protected constructor(context: Context?) {
        setContextInside(context)
    }


    fun addToDataList(dataList: List<T>?) {
        if (this.dataList != null) {
            this.dataList!!.addAll(dataList!!)
        }
    }

    fun addToDataList(position: Int, dataList: List<T>?) {
        this.dataList!!.addAll(position, dataList!!)
    }

    fun removeFromDataItem(data: T) {
        dataList!!.remove(data)
    }

    fun removeFromDataList(dataList: List<T>?) {
        this.dataList!!.removeAll(dataList!!)
    }

    fun addToDataItem(data: T) {
        dataList!!.add(data)
    }

    fun addToSelectedDataList(dataList: List<T>?) {
        selectedDataList.addAll(dataList!!)
    }

    fun addToSelectedDataItem(data: T) {
        selectedDataList.add(data)
    }

    fun removeFromSelectedDataItem(data: T) {
        selectedDataList.remove(data)
    }

    fun removeFromSelectedDataList(dataList: List<T>?) {
        selectedDataList.removeAll(dataList!!)
    }

    fun clearSelectedDataList() {
        selectedDataList.clear()
    }

    fun isItemSelected(data: T): Boolean {
        return selectedDataList.contains(data)
    }

    fun clearSelectedAndAddItem(data: T) {
        clearSelectedDataList()
        addToSelectedDataItem(data)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    open var dataList: MutableList<T>? = null

    var selectedDataList: MutableList<T> = ArrayList()


    fun setContextInside(context: Context?) {
        this.context = context
        inflater =
            App.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (context != null) {
            if (context is Activity) {
                activity = context
            }
        }
    }

    var context: Context? = null
    val baseActivity: BaseActivity<*, *>?
        get() = if (activity is BaseActivity<*, *>) {
            activity as BaseActivity<*, *>?
        } else null
    var activity: Activity? = null

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H
    abstract override fun onBindViewHolder(holder: H, position: Int)
    override fun getItemCount(): Int {
        return if (dataList == null) {
            0
        } else {
            dataList!!.size
        }
    }

    fun getItem(position: Int): T {
        return dataList!![position]
    }
}
