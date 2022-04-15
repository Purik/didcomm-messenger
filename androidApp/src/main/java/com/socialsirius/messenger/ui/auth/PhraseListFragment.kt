package com.socialsirius.messenger.ui.auth


import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cash.z.ecc.android.bip39.Mnemonics
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.databinding.FragmentCreatePhraseSecondBinding
import com.socialsirius.messenger.models.ui.PassPhraseItem
import com.socialsirius.messenger.ui.auth.createPhrase.PassPhraseAdapter
import java.nio.charset.Charset


class PhraseListFragment : BottomSheetDialogFragment() {
    var dataBinding: FragmentCreatePhraseSecondBinding?  = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_create_phrase_second, container, false)
        dataBinding = DataBindingUtil.bind<FragmentCreatePhraseSecondBinding>(v)
        setupViews()
        val list = createList()
        updateAdapter(list)
        return v
    }
    val userRepository = App.getInstance().appComponent.provideUserRepository()


    fun updateAdapter(list : List<PassPhraseItem>){
        adapter?.dataList = list.toMutableList()
        adapter?.notifyDataSetChanged()
    }

    fun createList(): List<PassPhraseItem> {
        val mnemonicCode: Mnemonics.MnemonicCode = Mnemonics.MnemonicCode(userRepository?.myUser?.pass!!)
        val list: MutableList<PassPhraseItem> = mutableListOf()
        mnemonicCode.words.forEachIndexed { index, chars ->
            val int =  index+1
            println(" chars.concatToString()="+ chars.concatToString())
            list.add(PassPhraseItem(int.toString(), chars.concatToString()))
        }
        return list
    }

    var adapter: PassPhraseAdapter? = null
     fun setupViews() {

        val chipsLayoutManager =
            ChipsLayoutManager.newBuilder(requireContext()) //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.TOP) //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(true) //set maximum views count in a particular row

                .setGravityResolver { Gravity.CENTER } //you are able to break row due to your conditions. Row breaker should return true for that views

                .setOrientation(ChipsLayoutManager.HORIZONTAL) // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT) // whether strategy is applied to last row. FALSE by default

                .build()

        dataBinding?.passPhraseList?.layoutManager = chipsLayoutManager
        dataBinding?.passPhraseList?.adapter = adapter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PassPhraseAdapter()
    }
}