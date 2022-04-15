package com.socialsirius.messenger.ui.auth.createPhrase

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.SimpleBaseRecyclerViewAdapter
import com.socialsirius.messenger.databinding.ItemPassphraseBinding
import com.socialsirius.messenger.models.ui.PassPhraseItem

class PassPhraseAdapter(override val layoutRes: Int = R.layout.item_passphrase) :
    SimpleBaseRecyclerViewAdapter<PassPhraseItem, PassPhraseAdapter.PassPhraseViewHolder>() {


    class PassPhraseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ItemPassphraseBinding>(itemView)
        fun bind(item: PassPhraseItem) {
            binding?.model = item
        }
    }



    override fun getViewHolder(
        parent: ViewGroup?,
        layoutRes: Int,
        viewType: Int
    ): PassPhraseViewHolder {
        return PassPhraseViewHolder(getInflatedView(layoutRes,parent))
    }

    override fun onBind(holder: PassPhraseViewHolder, item: PassPhraseItem, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}