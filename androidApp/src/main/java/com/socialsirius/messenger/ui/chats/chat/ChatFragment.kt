package  com.socialsirius.messenger.ui.chats.chat

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.PopupMenu
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.base.ui.OnAdapterItemClick
import com.socialsirius.messenger.base.ui.OnCustomBtnClick
import com.socialsirius.messenger.databinding.FragmentChatBinding
import com.socialsirius.messenger.design.AvatarView
import com.socialsirius.messenger.design.chat.ChatPanelView
import com.socialsirius.messenger.design.decorators.Decorator
import com.socialsirius.messenger.design.decorators.StickyHeaderDecor
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.ui.chats.chat.message.BaseItemMessage
import com.socialsirius.messenger.ui.chats.chat.message.MessageType
import com.socialsirius.messenger.ui.chats.chat.message.OfferItemMessage
import com.socialsirius.messenger.ui.chats.chat.message.ProverItemMessage
import com.socialsirius.messenger.ui.connections.connectionCard.ConnectionCardFragment


import com.socialsirius.messenger.utils.FileUtils
import com.socialsirius.messenger.utils.FileUtils.generateFileName
import com.socialsirius.messenger.utils.FileUtils.generateFileVideoName
import com.socialsirius.messenger.utils.PermissionHelper
import com.socialsirius.messenger.utils.Utils
import com.socialsirius.messenger.utils.extensions.observeUntilDestroy
import java.io.File

private const val CHAT_ITEM = "CHAT_ITEM"
private const val CHATS_ITEM = "CHATS_ITEM"
private const val CAMERA_PERMISSION_CODE = 1080
private const val SOUND_PERMISSION_CODE = 1070

class ChatFragment() : BaseFragment<FragmentChatBinding, ChatViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance(chat: Chats?) = ChatFragment().apply {
            arguments = Bundle().apply {
                putSerializable(CHAT_ITEM, chat)
            }
        }
    }

    private var generatedFileName: String? = null
    private var cameraType = 0
    private var adapter: MessagesAdapter? = null

    override fun getLayoutRes(): Int = R.layout.fragment_chat

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    val decorator by lazy<RecyclerView.ItemDecoration> {
        Decorator.Builder()
            .overlay(StickyHeaderDecor())
            .build()
    }

    override fun setupViews() {
        model.setChat(arguments?.getSerializable(CHAT_ITEM) as? Chats)
        super.setupViews()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        dataBinding.chatPanelView.onSendClick = (object : ChatPanelView.OnSendClick {
            override fun onMessageSend(message: String) {
                model.sendMessageText(message)
            }

            override fun onSoundSend(time: Long, type: ChatPanelView.SendType) {
                // model.sendSound(time, type)
            }
        })

        adapter = MessagesAdapter()
        adapter!!.onCustomBtnClick = object : OnCustomBtnClick<BaseItemMessage> {
            override fun onBtnClick(btnId: Int, item: BaseItemMessage?, position: Int) {
                if (btnId == MessagesAdapter.readActionId) {
                    model.readUnread(item?.getMessageId(), false)
                }
                if (btnId == MessagesAdapter.localReadActionId) {
                    model.readUnread(item?.getMessageId(), true)
                }
                if (btnId == R.id.mainPanelView) {
                    model.onMessageShortClick(item)
                }
            }

            override fun onLongBtnClick(btnId: Int, item: BaseItemMessage?, position: Int) {
                if (btnId == R.id.mainPanelView) {
                    //   model.onMessageLongClick(item)
                }
            }

        }
        adapter!!.onAdapterItemClick = object : OnAdapterItemClick<BaseItemMessage> {
            override fun onItemClick(item: BaseItemMessage) {

                if (item is OfferItemMessage || item is ProverItemMessage) {
                    val title = model.chatLiveData.value?.title
                    baseActivity.pushPage(ConnectionCardFragment.newInstance(item, title))
                }
            }

        }

        //     dataBinding.chatRecyclerView.addItemDecoration(decorator)


        dataBinding.chatRecyclerView.adapter = adapter
        dataBinding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
        }

        dataBinding.chatRecyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position =
                        (dataBinding.chatRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    //   if (position <= 3) model.rangeLoadMessages()
                }
            }
        })
        /*

           */



        dataBinding.chatPanelView.onSoundListener = (object : ChatPanelView.OnSoundListener {
            override fun onSoundStart(): Boolean {
                return PermissionHelper.checkPermissionsForAudio(
                    baseActivity,
                    SOUND_PERMISSION_CODE
                )
            }

            override fun onSoundStartRecord() {
                ConstraintSet().let {
                    it.clone(dataBinding.mainView)
                    it.connect(
                        R.id.chatRecyclerView,
                        ConstraintSet.BOTTOM,
                        R.id.chatPanelSpace,
                        ConstraintSet.TOP
                    )
                    it.applyTo(dataBinding.mainView)
                }
                //   model.startSoundRecord()
            }

            override fun onSoundCancel() {
                //    model.deleteCurrentFileFromFilename()
            }

            override fun onSoundStopRecord(
                isSend: Boolean,
                time: Long,
                type: ChatPanelView.SendType
            ) {
                ConstraintSet().let {
                    it.clone(dataBinding.mainView)
                    it.connect(
                        R.id.chatRecyclerView,
                        ConstraintSet.BOTTOM,
                        R.id.chatPanelView,
                        ConstraintSet.TOP
                    )
                    it.applyTo(dataBinding.mainView)
                }
                //     model.stopSoundRecord()
                if (isSend) {
                    //        model.sendSound(time, type)
                }
            }
        })


/*
         addToContactsListYesButton.setOnClickListener {
             model.removeUser()
         }*/
        dataBinding.chatPanelView.setOnAttachmentClickListener(View.OnClickListener {
            val checkPermissionsForCamera =
                PermissionHelper.checkPermissionsForCamera(baseActivity, CAMERA_PERMISSION_CODE)
            if (checkPermissionsForCamera) {
                openAttachmentDialog()
            }
        })
        // botButtonRecycler.layoutManager = GridLayoutManager(context,)
    }

    private fun updateAdapter(data: List<BaseItemMessage>) {
        adapter?.dataList = data.toMutableList()
        adapter?.notifyDataSetChanged()
    }


    override fun subscribe() {
        model.adapterListLiveData.observe(this, Observer {
            updateAdapter(it)
            dataBinding.chatRecyclerView.postDelayed({
                dataBinding.chatRecyclerView.scrollToPosition((adapter?.itemCount ?: 1) - 1)
            }, 200)
        })
        model.chatLiveData.observe(this, Observer {
            //NotificationsUtils.removeMessageNotify(activity, model.chatLiveData.value?.id ?: "")
            it?.let {
                dataBinding.topBarView.text = it.title
                dataBinding.avatarView.update(it)
            }
            //   model.updateLastActivity()
            // model.showHideAcceptedBtn()
            //  model.loadMessagesIfEMpty()
        })

        model.clearTextLiveData.observe(this, Observer {
            if (it) {
                model.clearTextLiveData.value = false
                dataBinding.chatPanelView.setMessage("")
            }
        })
        model.eventStoreLiveData.observe(this, Observer {
            model.updateList()

        })

        model.moreActionLiveData.observe(this, Observer { options ->
            options?.let {
                PopupMenu(requireContext(), dataBinding.moreButton).apply {
                    options.forEach { menu.add(it) }
                    setOnMenuItemClickListener {
                        when (it.title) {
                            /*  getString(R.string.menu_fragment_messages_chat_add_to_favorite) -> {
                                  model.addToFavorite()
                              }
                              getString(R.string.menu_fragment_messages_chat_del_from_favorite) -> {
                                  model.deleteFromFavorite()
                              }
                              getString(R.string.menu_fragment_messages_create_secret_chat) -> {
                                  model.inviteToSecret()
                              }*/
                            getString(R.string.menu_fragment_messages_chat_clear) -> {
                                model.deleteChatRequest()
                            }
                            /*   getString(R.string.title_settings) -> {
                                   baseActivity.pushPage(GroupEditFragment.newInstance(model.getCurrentChat()
                                       ?: Chats()))
                               }
                               getString(R.string.leave_chat) -> {
                                   model.leaveGroupChat()
                               }
                               getString(R.string.menu_fragment_messages_chat_add_users) -> {
                                   baseActivity.pushPage(GroupAddFragment.newInstance(RoomsResponse(model.getCurrentChat()
                                       ?: Chats())))
                               }
                               getString(R.string.add_user) -> {
                                   model.addUserToContacts()
                               }
                               getString(R.string.menu_fragment_messages_send_propose) -> {
                                   model.sendTestProposeCredential()
                               }*/

                        }
                        return@setOnMenuItemClickListener true
                    }
                    show()
                }
                model.moreActionLiveData.value = null
            }
        })

        model.lastActivityLiveData.observe(this, Observer {
            dataBinding.detailsBarView.text = it
            if (it.isNullOrEmpty()) {
                dataBinding.detailsBarView.visibility = View.GONE
            } else {
                dataBinding.detailsBarView.visibility = View.VISIBLE
            }
        })

        /*    model.lastActivityAllLiveData.observe(this, Observer {
                model.updateLastActivity()
            })*/

        /*    model.activityStatusLiveData.observe(this, Observer {
                view?.findViewWithTag<AvatarView>(it.first)?.updateStatus(it.second)
                adapter?.updateActivityStatus(it)
            })*/

        model.messageActionLiveData.observe(this, Observer {
            MessageActionDialogFragment(it).show(
                parentFragmentManager,
                MessageActionDialogFragment::class.java.simpleName
            )
        })
        model.updateMessageLiveData.observe(this, Observer {
            model.updateMessageStatus(it)
        })

/*
        model.onBackClickLiveData.observe(this, Observer { onBackPressed() })
        model.chatLiveData.observe(this, Observer {
            NotificationsUtils.removeMessageNotify(activity, model.chatLiveData.value?.id ?: "")
            topBarView.text = it.title
            avatarView.update(it)
            model.updateLastActivity()
            model.showHideAcceptedBtn()
            model.loadMessagesIfEMpty()
        })

        model.messagesAddLiveData.observe(this, Observer {
            adapter?.addRange(it.first, it.second)
            val last = (dataBinding.chatRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            val lastCount = (adapter?.itemCount ?: 1) - 1
            val end = lastCount - last
            val isScrollToEnd = it.third || (end < 15)
            if (isScrollToEnd) {
                chatRecyclerView.scrollToPosition((adapter?.itemCount ?: 1) - 1)
            }
        })*/

        /*  val first =  (dataBinding.chatRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
          val last =  (dataBinding.chatRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
          val lastCount =  adapter?.itemCount ?: 1 - 1
          val end =  lastCount - last
          val isScrollToEnd = it.third || (end <15)
          if (isScrollToEnd ) {
              dataBinding.chatRecyclerView.scrollToPosition(lastCount)
          }
          */
        /*  model.messagesAddRangeLiveData.observe(this, Observer {
              adapter?.addRange(it.first, it.second)
          })

          model.messagesSetLiveData.observe(this, Observer {
              adapter?.setItems()
          })

          model.messagesUpdateLiveData.observe(this, Observer {
              //обновляет ТОЛЬКО статус сообщения и уголочки
              adapter?.update(it)
          })



          model.updateOneChatLiveData.observe(this, Observer {
              Log.d("mylog2080", "updateOneChatLiveData=" + it);
              if (it != null) {
                  if (it.id == model.getCurrentChat()?.id) {
                      val secretchats = model.getCurrentChat() as? SecretChats
                      if (it is SecretChats) {
                          it.isTempNotAccepted = secretchats?.isTempNotAccepted ?: false
                          it.tempInviteMessage = secretchats?.tempInviteMessage
                      }
                      model.chatLiveData.postValue(it)
                  }
              }
          })


          model.messageActionLiveData.observe(this, Observer {
              MessageActionDialogFragment(it).show(parentFragmentManager, MessageActionDialogFragment::class.java.simpleName)
          })

          model.showAcceptInviteButtonLiveData.observe(this, Observer {
              acceptInviteButton.visibility = if (it.first) View.VISIBLE else View.GONE
              acceptInviteButton.isEnabled = !it.second
              Log.d("mylog20809", "it.second=" + it.second);
              chatPanelView.isEnabled = it.second
          })

          model.goToNewSecretChatLiveData.observe(this, Observer {
              Log.d("mylog2080", "goToNewSecretChatLiveData=" + it);
              activity?.finish()
              val messageIntent = Intent(context, MessageActivity::class.java).apply {
                  putExtra(StaticFields.BUNDLE_CHAT, it)
              }
              startActivity(messageIntent)
          })

          model.goToNewSecretFromChatLiveData.observe(this, Observer {
              it?.let {
                  if (it.id == model.getCurrentChat()?.id) {
                      model.chatLiveData.postValue(it)
                      model.showHideAcceptedBtn()
                  } else {
                      baseActivity.showPage(newInstance(it))
                  }
                  model.goToNewSecretFromChatLiveData.postValue(null)
              }
          })

          model.goToConnectionLiveData.observe(this, Observer {
              it?.let {
                  baseActivity.pushPageAdd(ConnectionCardFragment.newInstance(it))
                  model.goToConnectionLiveData.value = null
              }
          })



          model.messageTextLiveData.observe(this, Observer {
              chatPanelView.setMessage(it ?: "")
          })

          model.bottomBotButtonList.observe(this, Observer {
              if (it == null) {
                  botButtonLayout.visibility = View.GONE
                  return@Observer
              }
              if (it.isEmpty()) {
                  botButtonLayout.visibility = View.GONE
              } else {
                  botButtonLayout.visibility = View.VISIBLE
              }
              val botButtonAdapter = BotButtonAdapter()
              botButtonAdapter.buttonList = it
              botButtonAdapter.botOnClickListener = object : BotOnClickListener {
                  override fun onBotMenuClick(botButton: BotButton) {
                      model.botButtonOnclick(botButton)
                  }
              }
              var spanCount = 1
              if (it.size > 1) {
                  spanCount = 2
              }
              botButtonRecycler.layoutManager = GridLayoutManager(context, spanCount)
              botButtonRecycler.adapter = botButtonAdapter
          })
  */

        model.isOnlineLiveData.observe(this, Observer {
            dataBinding.avatarView.updateStatus(it)
        })
    }

    private fun openAttachmentDialog() {
        val alertDialog = AlertDialog.Builder(baseActivity)

        val chooseList: MutableList<String> = ArrayList()
        chooseList.add(getString(R.string.alert_dialog_create_photo))
        chooseList.add(getString(R.string.alert_dialog_create_video))
        chooseList.add(getString(R.string.alert_dialog_from_gallery))
        chooseList.add(getString(R.string.alert_dialog_from_file))

        val array = chooseList.toTypedArray()
        alertDialog.setItems(array) { dialog, position ->
            when (position) {
                0 -> {
                    startCamera()
                }
                1 -> {
                    startVideoCamera()
                }
                2 -> {
                    openGallery()
                }
                3 -> {
                    openFileChooser()
                }
            }
        }

        alertDialog.show()
    }

    private fun openGallery() {

        val galerryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galerryIntent.type = "*/*"
        galerryIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
        startActivityForResult(galerryIntent, 1020)

    }


    fun getIntentForFile(filePath: String, context: Context): Intent {
        val intent = Intent()
        val uri = Uri.fromFile(File(filePath))
        /*  val uri = FileProvider.getUriForFile(
                  context,
                  context.applicationContext.packageName + ".fileprovider",
                  File(filePath)
          )*/
        intent.action = Intent.ACTION_VIEW
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, getFileContentType(filePath))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent
    }

    fun getFileContentType(filePath: String): String? {
        val file = File(filePath)
        val map = MimeTypeMap.getSingleton()
        val ext = MimeTypeMap.getFileExtensionFromUrl(file.name)
        var type = map.getMimeTypeFromExtension(ext)
        if (type == null) type = "text/plain"
        return type
    }

    private fun openFileChooser() {
        /*      val i2 = Intent(activity, FileChooser::class.java)
              i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal)
              startActivityForResult(i2, Utils.RC_PROFILE_IMAGE_FIELD_FILE)*/
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val image = FileUtils.getOutputMediaFile(baseActivity, "upload", generateFileName())
        model.fileName = image.absolutePath
        val uriSavedImage = Uri.fromFile(image)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage)
        startActivityForResult(intent, Utils.RC_PROFILE_IMAGE_FIELD_CAMERA)
    }

    private fun startVideoCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        val image = FileUtils.getOutputMediaFile(baseActivity, "upload", generateFileVideoName())
        model.fileName = image.absolutePath
        val uriSavedImage = Uri.fromFile(image)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage)
        startActivityForResult(intent, Utils.RC_PROFILE_IMAGE_FIELD_CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(
            CAMERA_PERMISSION_CODE,
            requestCode,
            permissions,
            grantResults,
            object : PermissionHelper.OnRequestPermissionListener {
                override fun onRequestFail() {
                    Toast.makeText(
                        baseActivity,
                        R.string.check_file_permission_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onRequestSuccess() {
                    openAttachmentDialog()
                }

            })
        PermissionHelper.onRequestPermissionsResult(
            SOUND_PERMISSION_CODE,
            requestCode,
            permissions,
            grantResults,
            object : PermissionHelper.OnRequestPermissionListener {
                override fun onRequestFail() {
                    Toast.makeText(
                        baseActivity,
                        R.string.check_audio_permission_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onRequestSuccess() {

                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
                Utils.RC_PROFILE_IMAGE_FIELD_CAMERA -> {
                    //  model.sendImageVideo()
                }
                Utils.RC_PROFILE_IMAGE_FIELD_GALLERY -> {
                    if (data != null) {
                        val uri = data.data
                        val imageFilePath = FileUtils.getPath(App.getContext(), uri)
                        if (imageFilePath != null) {
                            model.fileName = imageFilePath
                            //      model.sendImageVideo()
                        }
                    }
                }
                Utils.RC_PROFILE_IMAGE_FIELD_FILE -> if (data != null) {
                    val file = data.data
                    if (file != null) {
                        val filePath = file.path
                        if (filePath != null && "" != filePath) {
                            model.fileName = filePath
                            //   model.sendImageVideo()
                        }
                    }
                }

            }
        }
    }

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
                1010 -> {
                    //  model.sendImageVideo()
                }
                1020 -> {
                    if (data != null) {
                        val uri = data.data
                       // val imageFilePath = FileUtils.getPath(App.getContext(), uri)
                        if (uri != null) {
                            val fileAttach = model.generateFileAttach(uri)
                            model.sendMessageWithAttach(fileAttach)
                        }
                    }
                }
                1030 -> if (data != null) {
                    */
/*  val file = data.data
                      if (file != null) {
                          val filePath = file.path
                          if (filePath != null && "" != filePath) {
                              model.fileName = filePath
                              model.sendImageVideo()
                          }
                      }*//*

                }

            }
        }
    }
*/

    /*  private fun onImageClick(message: ChatMessageItem) {
          if (message.messageType == ContentType.image) {
              val intent = Intent(requireContext(), ChatImageActivity::class.java)
              intent.putExtra(ChatImageActivity.ATTACHMENT, message.url)
              requireContext().startActivity(intent)
          } else if (message.messageType == ContentType.video) {
              val intent = Intent(requireContext(), VideoPlayerActivity::class.java)
              intent.putExtra(ChatImageActivity.ATTACHMENT, message.url)
              requireContext().startActivity(intent)
          }
      }
  */

    /*   private fun onDocumentClick(message: ChatMessageItem) {
           val downloadedPath = model.getDownloadedPath(message.url)
           val file = File(downloadedPath)
           if (!file.exists()) {
               val builder = AlertDialog.Builder(requireContext())
               builder.setTitle(requireContext().getString(R.string.add_from_fs) + "?")
               builder.setMessage(App.getContext().getString(R.string.alert_download_file) + " " + model.getFilename(message.url, message.text) + "?")
               builder.setPositiveButton(requireContext().getString(R.string.yes)) { dialog, which ->
                   model.downloadRepository.startDownloadAfterRefresh(listOfNotNull(DaoUtilsMessagesNew.readMessage(message.id)), true)
               }
               builder.setNegativeButton(requireContext().getString(R.string.no), null)
               builder.show()
           } else {
               if (message.downloadPercent in 1..99 && !message.isMine) {
                   model.showToast(R.string.upload_time)
                   return
               }
               try {
                   val intent = getIntentForFile(downloadedPath, requireContext())
                   requireContext().startActivity(intent)
               }catch (e : Exception){
                   e.printStackTrace()
                   model.showToast(R.string.unknown_mime_type)
               }

           }

       }*/

/*    fun getIntentForFile(filePath: String, context: Context): Intent {
        val intent = Intent()
        val uri = Uri.fromFile(File(filePath))

        intent.action = Intent.ACTION_VIEW
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, getFileContentType(filePath))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent
    }*/

    /* fun getFileContentType(filePath: String): String? {
         val file = File(filePath)
         val map = MimeTypeMap.getSingleton()
         val ext = MimeTypeMap.getFileExtensionFromUrl(file.name)
         var type = map.getMimeTypeFromExtension(ext)
         if (type == null) type = "text/plain"
         return type
     }

     private fun openFileChooser() {
         val i2 = Intent(activity, FileChooser::class.java)
         i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal)
         startActivityForResult(i2, Utils.RC_PROFILE_IMAGE_FIELD_FILE)
     }

     private fun startCamera() {
         val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
         val image = FileUtils.getOutputMediaFile(baseActivity, "upload", generateFileName())
         model.fileName = image.absolutePath
         val uriSavedImage = Uri.fromFile(image)
         intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage)
         startActivityForResult(intent, Utils.RC_PROFILE_IMAGE_FIELD_CAMERA)
     }

     private fun startVideoCamera() {
         val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
         val image = FileUtils.getOutputMediaFile(baseActivity, "upload", generateFileVideoName())
         model.fileName = image.absolutePath
         val uriSavedImage = Uri.fromFile(image)
         intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage)
         startActivityForResult(intent, Utils.RC_PROFILE_IMAGE_FIELD_CAMERA)
     }

     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         PermissionHelper.onRequestPermissionsResult(CAMERA_PERMISSION_CODE, requestCode, permissions, grantResults, object : PermissionHelper.OnRequestPermissionListener {
             override fun onRequestFail() {
                 Toast.makeText(baseActivity, R.string.check_file_permission_error, Toast.LENGTH_SHORT).show()
             }

             override fun onRequestSuccess() {
                 openAttachmentDialog()
             }

         })
         PermissionHelper.onRequestPermissionsResult(SOUND_PERMISSION_CODE, requestCode, permissions, grantResults, object : PermissionHelper.OnRequestPermissionListener {
             override fun onRequestFail() {
                 Toast.makeText(baseActivity, R.string.check_audio_permission_error, Toast.LENGTH_SHORT).show()
             }

             override fun onRequestSuccess() {

             }
         })
     }



     override fun onDestroyView() {
         super.onDestroyView()
         model.updateUncompleteMessage(chatPanelView.getMessage())
     }

     override fun onPause() {
         super.onPause()
         adapter?.releaseAllMediaPlayers()
         dataBinding.chatPanelView.hideKeyboard(activity)
     }
 */
/*

    private fun downloadAttach(message: Messages) {
        val messages: MutableList<Messages> = java.util.ArrayList<Messages>()
        messages.add(message)
        startDownloadAfterRefresh(messages, true)
        *//*   Uri uri = Uri.parse(attachment.getFull_url());
        DownloadManager.Request r = new DownloadManager.Request(uri);
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, attachment.getFileName());
        r.allowScanningByMediaScanner();
        r.addRequestHeader("AUTHORIZATION", "Token " + AppPref.getServerInfo().getSession());
        r.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager dm = (DownloadManager) context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(r);*//*
    }*/

/*    fun showClickItemPopupMenu(v: View?, position: Int) {
        val message: Messages? = adapter!!.getItem(position)
        if (message.getType() === Messages.MESSAGE_TYPE_INDY_TRANSPORT_MESAGE) {
            val messageText: String = message.getText().replace(StaticFields.INDY_TRANSPORT_START_TAG, "")
            val bundle = Bundle()
            bundle.putInt("type", 3)
            bundle.putString("message", messageText)
            bundle.putBoolean("isAccepted", message.isAccepted())
            bundle.putString("userJid", message.getUser().getJid())
            val wallet: Wallet = IndyWallet.getOrOpenMyWallet(this, bundle)
            message.setAccepted(true)
            DaoUtils.writeMessage(message)
            if (wallet != null) {
                handleWalletByType(3, messageText, message.getUser().getJid(), bundle)
            }
            return
        }
        if (message.getType() !== Messages.MESSAGE_TYPE_DATE_DIVIDER *//*&& !user.getJid().contains(MAIN_JID_CONFERENCE_DOMAIN_WITH_AT)*//*
                && message.getDocument() == null) {
            val url = StringUtils.separatedText(message.getMessageText())
            if (StringUtils.isExtractRoot(url) && (url.substring(0, 4) == "www." || url.substring(0, 4) == "http")) {
                try {
                    val text = StringUtils.separatedText(message.getMessageText())
                    var finalText: String? = ""
                    finalText = if (text.length <= 50 && text.length > 4) if (url.substring(0, 4) == "http") text else "https://$text" else text
                    val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(finalText))
                    startActivity(myIntent)
                    return
                } catch (e: Exception) {
                    e.printStackTrace()
                    showClickPopup(v, message)
                    return
                }
            } else {
                showClickPopup(v, message)
                return
            }
        }
        val attachment: Attachment = message.getDocument()
        if (attachment != null) {
            if (attachment.isImage()) {
                val intent = Intent(context, ChatImageActivity::class.java)
                intent.putExtra(ChatImageActivity.ATTACHMENT, attachment)
                context!!.startActivity(intent)
            } else if (attachment.isVideo()) {

                //DOWNLOAD FIRST
                if (FileUtils.isFileExist(attachment.getDirByType(), attachment.getFull_url())) {
                    val intent = Intent(context, VideoPlayerActivity::class.java)
                    intent.putExtra(ChatImageActivity.ATTACHMENT, attachment)
                    context!!.startActivity(intent)
                } else {
                    showSaveDialog(message)
                }
            } else if (attachment.isOtherType()) {
                //DOWNLOAD FIRST
                if (FileUtils.isFileExist(attachment.getDirByType(), attachment.getFull_url())) {
                    val uri = Uri.parse(attachment.getFull_url())
                    val filename = uri.lastPathSegment
                    val myMime = MimeTypeMap.getSingleton()
                    val newIntent = Intent(Intent.ACTION_VIEW)
                    val pos = filename!!.lastIndexOf(".")
                    if (pos > 0) {
                        val ext = filename.substring(pos + 1, filename.length)
                        val mimeType = myMime.getMimeTypeFromExtension(ext)
                        newIntent.setDataAndType(Uri.fromFile(FileUtils.getOutputMediaFile(attachment.getDirByType(), filename)), mimeType)
                        newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        try {
                            context!!.startActivity(newIntent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, context!!.getString(R.string.unknown_mime_type), Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(context, context!!.getString(R.string.unknown_mime_type), Toast.LENGTH_LONG).show()
                    }
                } else {
                    showSaveDialog(message)
                }
            }
        }
    }*/


/*    private fun showClickPopup(v: View, message: Messages) {
        val popupMenu = PopupMenu(v.context, v, Gravity.TOP)
        if (message.getDocument() != null) {
            popupMenu.inflate(R.menu.menu_fragment_messages_doc_item)
        } else if (user.isRoom()) {
            popupMenu.inflate(R.menu.menu_fragment_messages_group_item)
        } else {
            popupMenu.inflate(R.menu.menu_fragment_messages_item)
        }
        val sended = popupMenu.menu.findItem(R.id.action_sended)
        var sended_title = ""
        sended_title = if (message.getUser().getJid().equals(AppPref.getUserJid())) {
            java.lang.String.format(App.getContext().getString(R.string.menu_fragment_messages_item_sended),
                    DateFormatter.dateToHHmm(message.getDate()))
        } else {
            java.lang.String.format(App.getContext().getString(R.string.menu_fragment_messages_item_received),
                    DateFormatter.dateToHHmm(message.getDate()))
        }
        val s = SpannableString(sended_title)
        s.setSpan(ForegroundColorSpan(resources.getColor(R.color.menu_light_text)), 0, s.length, 0)
        sended.title = s
        val actionCopyText = popupMenu.menu.findItem(R.id.action_copy_text)
        val action_resend = popupMenu.menu.findItem(R.id.action_resend)
        val action_make_task = popupMenu.menu.findItem(R.id.action_make_task)
        val action_reply = popupMenu.menu.findItem(R.id.action_reply)
        if (action_reply != null) {
            action_reply.isVisible = false
        }
        if (actionCopyText != null) {
            if (!Jid.isJidPeerJid(user.getJid())) {
                actionCopyText.isVisible = true
            } else {
                actionCopyText.isVisible = false
            }
        }
        if (action_resend != null) {
            if (!Jid.isJidPeerJid(user.getJid())) {
                action_resend.isVisible = true
            } else {
                action_resend.isVisible = false
            }
        }
        if (action_make_task != null) {
            if (message.getType() !== BaseMessages.MESSAGE_TYPE_INCOME && message.getType() !== BaseMessages.MESSAGE_TYPE_OUTCOME) {
                action_make_task.isVisible = false
            } else {
                action_make_task.isVisible = true
            }
        }
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_reply -> {
                    //TODO make reply
                    Toast.makeText(context, context!!.getString(R.string.menu_fragment_messages_item_reply), Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_resend -> {
                    //TODO make resend
                    val intent = Intent(context, SendShareDataActivity::class.java)
                    intent.action = Intent.ACTION_SEND
                    intent.type = MimeTypes.BASE_TYPE_TEXT
                    intent.putExtra(StaticFields.BUNDLE_ONE_MESSAGE, message)
                    context!!.startActivity(intent)
                    true
                }
                R.id.action_save -> {
                    //TODO make save
                    if (Utils.checkFileExists(message.getDocument().getFileName())) {
                        Toast.makeText(context, App.getContext().getString(R.string.alert_download_file_already), Toast.LENGTH_SHORT).show()
                        *//*  String filename = message.getDocument().getFileName();
                            MimeTypeMap myMime = MimeTypeMap.getSingleton();
                            Intent newIntent = new Intent(Intent.ACTION_VIEW);
                            int pos = filename.lastIndexOf(".");
                            if (pos > 0) {
                                String ext = filename.substring(pos + 1, filename.length());
                                String mimeType = myMime.getMimeTypeFromExtension(ext);
                                newIntent.setDataAndType(Uri.fromFile(new File(Utils.buildPath(filename))), mimeType);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                try {
                                    context.startActivity(newIntent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, context.getString(R.string.unknown_mime_type), Toast.LENGTH_LONG).show();
                                }
                            }*//*
                    } else {
                        showSaveDialog(message)
                    }
                    // Toast.makeText(context, context.getString(R.string.menu_fragment_messages_item_save), Toast.LENGTH_SHORT).show();
                    true
                }
                R.id.action_make_task -> {
                    //Toast.makeText(context, context.getString(R.string.menu_fragment_messages_item_make_task), Toast.LENGTH_SHORT).show();
                    createTask(message)
                    true
                }
                R.id.action_copy_text -> {
                    ClipboardUtils.copyToClipboard(context, message.getText())
                    Toast.makeText(context, context!!.getString(R.string.menu_fragment_messages_item_copy_text_toast), Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_delete_all -> {
                    Toast.makeText(context, context!!.getString(R.string.menu_fragment_messages_item_delete_all), Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_delete -> {
                    message.setRemoved(true)
                    DaoUtils.writeMessage(message)
                    //                            adapter.notifyListChanges(message, MessagesListAdapter.Method.delete);
                    adapter!!.notifyDataSetChanged()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }*/
}