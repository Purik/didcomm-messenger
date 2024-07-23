package com.socialsirius.messenger.ui.auth.createPhrase

import android.R.attr.label
import android.R.attr.text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentCreatePhraseSecondBinding
import com.socialsirius.messenger.models.ui.PassPhraseItem


class CreatePhraseSecondFragment :
    BaseFragment<FragmentCreatePhraseSecondBinding, CreatePhraseSecondViewModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_create_phrase_second
    }



    override fun subscribe() {
        model.passPhraseListLiveData.observe(this, Observer {
            updateAdapter(it)
        })

        model.startClickLiveData.observe(this, Observer {
            if(it){
                val passphraseString = model.passPhraseListLiveData.value?.joinToString(separator = ",") { it.title }
                model.startClickLiveData.value = false
                val clipboard: ClipboardManager? =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                val clip = ClipData.newPlainText("Passphrase", passphraseString)
                clipboard?.setPrimaryClip(clip)
                model.onShowToastLiveData.postValue("Your passphrase copied to clipboard")
            }
        })
    }

    fun updateAdapter(list : List<PassPhraseItem>){
        adapter?.dataList = list.toMutableList()
        adapter?.notifyDataSetChanged()
    }
    var adapter: PassPhraseAdapter? = null
    override fun setupViews() {
        super.setupViews()
        val chipsLayoutManager =
            ChipsLayoutManager.newBuilder(requireContext()) //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.TOP) //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(true) //set maximum views count in a particular row

                .setGravityResolver { Gravity.CENTER } //you are able to break row due to your conditions. Row breaker should return true for that views

                .setOrientation(ChipsLayoutManager.HORIZONTAL) // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT) // whether strategy is applied to last row. FALSE by default

                .build()

        dataBinding.passPhraseList.layoutManager = chipsLayoutManager
        dataBinding.passPhraseList.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PassPhraseAdapter()
    }

    override fun setModel() {
        dataBinding.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }
}