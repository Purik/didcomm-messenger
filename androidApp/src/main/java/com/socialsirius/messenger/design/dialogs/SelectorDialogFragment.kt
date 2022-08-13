package com.socialsirius.messenger.design.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialsirius.messenger.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_selector.view.textView
import kotlinx.android.synthetic.main.layout_selector.recyclerView


class SelectorDialogFragment(
    private val items: List<String>,
    private val onItemSelect: (Pair<Int, String>) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_selector, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = SelectorAdapter()
    }

    inner class SelectorAdapter() : Adapter<SelectorAdapter.SelectorViewHolder>() {

        override fun getItemCount() = items.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selector, parent, false)
            return SelectorViewHolder(view)
        }

        override fun onBindViewHolder(holder: SelectorViewHolder, position: Int) {
            holder.bind(items[position])
        }

        inner class SelectorViewHolder(itemView: View) : ViewHolder(itemView), LayoutContainer {

            override val containerView: View?
                get() = itemView

            fun bind(item: String) {
                itemView.textView.text = item
                itemView.setOnClickListener {
                    onItemSelect.invoke(Pair(adapterPosition, item))
                    this@SelectorDialogFragment.dismiss()
                }
            }
        }
    }
}