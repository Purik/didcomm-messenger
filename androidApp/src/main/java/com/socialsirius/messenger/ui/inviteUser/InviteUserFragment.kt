package com.socialsirius.messenger.ui.inviteUser

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.RGB_565
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.google.zxing.BarcodeFormat.QR_CODE
import com.google.zxing.EncodeHintType
import com.google.zxing.EncodeHintType.CHARACTER_SET
import com.google.zxing.EncodeHintType.ERROR_CORRECTION
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentInviteUserBinding
import com.socialsirius.messenger.ui.activities.message.MessageActivity

import java.util.EnumMap


class InviteUserFragment : BaseFragment<FragmentInviteUserBinding, InviteUserViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() = InviteUserFragment()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_invite_user

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    var dialog: AlertDialog? = null
    override fun subscribe() {
        model.onBackClickLiveData.observe(this, Observer {
            baseActivity.popPage()
        })
        model.qrCodeLiveData.observe(this, Observer {
            updateQrCode(it)
        })
        model.shareButtonAction.observe(this, Observer {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, it)
            startActivity(Intent.createChooser(intent, getString(R.string.send_share_via)))
        })

  /*      model.repositoryCreatedLiveData.observe(this, Observer {
         //   Log.d("mylog20890", "createdChat = " + it)
            it?.let {
                if (it is SecretChats) {
                    Log.d("mylog2080", "goToNewSecretChatLiveData=" + it);


                    val messageIntent = Intent(context, MessageActivity::class.java).apply {
                        putExtra(StaticFields.BUNDLE_CHAT, it)
                    }
                    startActivity(messageIntent)
                    (baseActivity as? MainActivity)?.showChats()
                    (baseActivity as? InviteActivity)?.finish()
                }
                model.repositoryCreatedLiveData.value = null
            }
        })*/

       /* model.invitationStartLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationStartLiveData.value = null
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Connecting...")
                builder.setMessage("Please wait,secure connection is being established")
                builder.setCancelable(false)
                dialog = builder.show()
            }
        })*/

        model.invitationErrorLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationErrorLiveData.value = null
                dialog?.cancel()
                model.onShowToastLiveData.postValue(it.second)
            }

        })

     /*   model.invitationSuccessLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationSuccessLiveData.value = null
                val item = model.getMessage(it)
                dialog?.cancel()
                baseActivity.finish()
                MessageActivity.newInstance(requireContext(), item)
                //   popPage(ChatsFragment.newInstance(item))
            }
        })*/

        model.invitationPolicemanSuccessLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationPolicemanSuccessLiveData.value = null
                val item = model.getMessage(it)
                dialog?.cancel()
                baseActivity.finish()
                MessageActivity.newInstance(requireContext(),item)
                //  popPage(DocumentShareFragment.newInstance(item))
            }
        })
    }

    private fun updateQrCode(qrCode: String) {
        val hintsMap: MutableMap<EncodeHintType, Any?> = EnumMap(com.google.zxing.EncodeHintType::class.java)
        hintsMap[CHARACTER_SET] = "utf-8"
        hintsMap[ERROR_CORRECTION] = ErrorCorrectionLevel.M

        val mWidth = 256
        val mHeight = 256

        try {
            val bitMatrix = QRCodeWriter().encode(qrCode, QR_CODE, mWidth, mHeight, hintsMap)
            val pixels = IntArray(mWidth * mHeight)
            for (i in 0 until mHeight) {
                for (j in 0 until mWidth) {
                    if (bitMatrix[j, i]) {
                        pixels[i * mWidth + j] = 0x00000000
                    } else {
                        pixels[i * mWidth + j] = -0x1
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(pixels, 0, mWidth, mWidth, mHeight, RGB_565)
            dataBinding.qrCodeImageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}