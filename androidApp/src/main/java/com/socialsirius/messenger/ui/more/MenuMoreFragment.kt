package  com.socialsirius.messenger.ui.more

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentMenuMoreBinding

import java.io.File

class MenuMoreFragment : BaseFragment<FragmentMenuMoreBinding, MenuMoreViewModel>() {
    private val GALLERY_IMAGE = 2
    private val PHOTO_IMAGE = 1

    companion object {
        @JvmStatic
        fun newInstance() = MenuMoreFragment()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_menu_more

    override fun setModel() {
      //  dataBinding!!.viewModel = model
    }

    override fun initDagger() {
       // App.getInstance().appComponent.inject(this)
    }

 //   private var adapter: ConnectionsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

   /*     adapter = ConnectionsAdapter(model::onConnectionClick)
        lastConnectionsContainer.adapter = adapter*/
    }

    override fun subscribe() {
      /*  model.connectionsLiveData.observe(this, Observer {
            adapter?.setItems(it)
            if (it.isEmpty()) {
                lastConnectionsPlaceholder.visibility = View.VISIBLE
            } else {
                lastConnectionsPlaceholder.visibility = View.GONE
            }
        })
        model.nameLiveData.observe(this, Observer {
            nameView.text = it
        })
        model.phoneLiveData.observe(this, Observer {
            phoneView.text = it
        })
        model.nicknameLiveData.observe(this, Observer {
            nicknameView.text = it
        })
        model.avatarLiveData.observe(this, Observer {
            avatarView.update(it)
        })

        model.connectionClickLiveData.observe(this, Observer {
            it?.let {
                startActivity(Intent(context, ConnectionsRequestDetailActivity::class.java).apply {
                    putExtra(StaticFields.BUNDLE_CONNECTION_CARD, it.connectionWrapper)
                })
                model.connectionClickLiveData.value = null
            }
        })

        model.settingsLiveData.observe(this, Observer {
            startActivity(Intent(context, OrganizerActivity::class.java).apply {
                putExtra(StaticFields.BUNDLE_USER, AppPref.getMyselfUser())
            })
        })

        //update avatar replace to where it belong
        model.avatarOnClickLiveData.observe(this, Observer {
            it?.let {
                openCroper()
                model.avatarOnClickLiveData.value = null
            }
        })*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      /*  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                val file: File = FileUtils.getFile(App.getContext(), resultUri)
                iconView.setImageURI(resultUri)
                model.uploadIconAndSend(file.absolutePath)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }*/
    }

    private fun openCroper() {
    /*    CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setFixAspectRatio(true)
            .setAllowRotation(true)
            .setAllowFlipping(true)
            .setActivityMenuIconColor(resources.getColor(R.color.white))
            .start(requireActivity(), this)*/
    }
}