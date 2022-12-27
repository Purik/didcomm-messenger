package  com.socialsirius.messenger.ui.chats.allChats


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sirius.library.mobile.helpers.PairwiseHelper
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.base.ui.OnAdapterItemClick
import com.socialsirius.messenger.base.ui.OnCustomBtnClick
import com.socialsirius.messenger.databinding.FragmentAllChatsBinding
import com.socialsirius.messenger.databinding.ViewContactInfoBinding
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.ui.activities.invitations.InvitationsActivity
import com.socialsirius.messenger.ui.activities.invite.InviteActivity
import com.socialsirius.messenger.ui.activities.message.MessageActivity
import com.socialsirius.messenger.ui.activities.scan.ScanActivity
import com.socialsirius.messenger.ui.chats.invitations.InvitationsListFragment
import com.socialsirius.messenger.ui.inviteUser.InviteUserFragment
import com.socialsirius.messenger.ui.scan.MenuScanQrFragment
import com.socialsirius.messenger.utils.extensions.observeUntilDestroy


class AllChatsFragment : BaseFragment<FragmentAllChatsBinding, AllChatsViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() = AllChatsFragment()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_all_chats

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    private var adapter: ChatsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatsAdapter()
        adapter!!.onAdapterItemClick = object : OnAdapterItemClick<Chats>{
            override fun onItemClick(item: Chats) {
                model.onSelectChat(item)
            }
        }
        adapter!!.onCustomBtnClick = object : OnCustomBtnClick<Chats>{
            override fun onBtnClick(btnId: Int, item: Chats?, position: Int) {
                if(btnId == 100) {
                    item?.let {      showContactAlert(item)
                    }

                }
            }

            override fun onLongBtnClick(btnId: Int, item: Chats?, position: Int) {

            }


        }
        dataBinding.chatsRecyclerView.adapter = adapter
    }

    fun showContactAlert(chat : Chats){
       val builder =  AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.view_contact_info,null, false)
       val binding =  DataBindingUtil.bind<ViewContactInfoBinding>(view)
        binding?.let {
            it.avatarImageView.update(chat)
            it.nameTextView.text = chat.title
            it.didTextView.text = chat.id
           val pairwise =  PairwiseHelper.getPairwise(theirDid = chat.id)

            it.verkeyTextView.text =   pairwise?.their?.verkey
            it.metaTextView.text  =  pairwise?.their?.endpointAddress
        }

        builder.setView(view)
        builder.show()
    }
    override fun subscribe() {
        model.emptyStateLiveData.observe(this, Observer {
           dataBinding.emptyStateView.visibility = if (it) View.VISIBLE else View.GONE
        })
        model.updateMessageLiveData.observe(this, Observer { idMess->
           model.updateStatusOfChat(idMess)
        })

       // model.chatsLiveData.observe(this, Observer {

       /*     if (it.status != Status.LOADING) {
                val items = it.data ?: listOf()
                //model.chatsListLiveData.postValue(items)
                model.originalList = items
                model.emptyStateLiveData.value = items.isEmpty()
                model.filterWith(model.filterLiveData.value)
                model.updateLastActivity()
            }*/
   //     })
        model.chatsListLiveData.observe(this, Observer {
            adapter?.dataList = it.toMutableList()
            adapter?.notifyDataSetChanged()
        })
        model.chatsSelectLiveData.observe(this, Observer { chat ->
            if (   chat!=null ){
                model.chatsSelectLiveData.value = null
                if (chat.id  == "invitation"){

                    InvitationsActivity.newInstance(baseActivity)
                }else{
                    MessageActivity.newInstance(requireContext(),chat)
                }

            }

        })

        model.eventStoreLiveData.observe(this, Observer {
            model.getChatList()
        })

   /*     model.messagesLiveData.observe(this, Observer {
            model.refreshAllChats()
        })*/

        model.updateOneChatsLiveData.observe(this, Observer {
          /*  if (it is SecretChats) {
                if (it.isHidden) {
                    return@Observer
                }
            }*/
         //   adapter?.updateItem(it)
        })

  /*      model.lastActivityAllLiveData.observe(this, Observer {
            model.updateLastActivity()
        })*/

    //    model.activityStatusLiveData.observe(this, Observer {
          //  val statusMap = it?.map { it.uid to it.isOnline }?.toMap().orEmpty()
        //    adapter?.updateActivityStatus(statusMap)

    //    })
/*
        model.messagesInDBForceRefreshLiveData.observe(this, Observer {
            model.refreshAllChats()
        })

        model.filterLiveData.observe(this, Observer {
            model.filterWith(it)
        })*/

        model.inviteUserLiveData.observe(this, Observer {
            if(it){
                model.inviteUserLiveData.value = false
                InviteActivity.newInstance(requireContext())
            }
        })

        model.scanQrLiveData.observe(this, Observer {
            if(it){
                model.scanQrLiveData.value = false
                ScanActivity.newInstance(requireContext())
            }
        })
    }

}