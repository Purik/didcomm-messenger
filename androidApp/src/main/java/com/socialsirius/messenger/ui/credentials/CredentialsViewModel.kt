package com.socialsirius.messenger.ui.credentials

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.library.mobile.helpers.PairwiseHelper
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.models.ui.ItemCredentials
import com.socialsirius.messenger.models.ui.ItemCredentialsDetails
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.transform.PairwiseTransform
import com.socialsirius.messenger.ui.chats.chat.message.OfferItemMessage
import java.util.*
import javax.inject.Inject

class CredentialsViewModel @Inject constructor(val messageRepository: MessageRepository) :
    BaseViewModel() {

    val scanQrLiveData = MutableLiveData<Boolean>()
    val adapterListLiveData: MutableLiveData<List<ItemCredentials>?> = MutableLiveData(null)


    override fun setupViews() {
        super.setupViews()
        val list = createList()
        adapterListLiveData.postValue(list)
    }

    fun onScanQrClick(v: View) {
        scanQrLiveData.postValue(true)
    }

    private fun createList(): List<ItemCredentials> {

        val credentilas = PairwiseHelper.getInstance().getAllCredentials()
        val list = credentilas.map {
            var nameUser = ""
            var textComment = ""
            it.schema_id?.let {
                val list = messageRepository.getMessagesWithMessageLike(it)
                val firstMessage = list.firstOrNull()
                val message = LocalMessageTransform.toBaseItemMessage(firstMessage)
                textComment =  message.getText()
                val did = firstMessage?.pairwiseDid

                did?.let {
                    val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = did)
                    nameUser = pairwise?.their?.label?:""

                }
             //   val message = LocalMessageTransform.toBaseItemMessage(list.firstOrNull()
              //  message?.getTitle()
                Log.d("mylog", "list=" + list)
            }


            val splittes = it.schema_id?.split(":") ?: listOf("", "", "", "")

            val name = splittes[2]
            val item = ItemCredentials(name, Date(), false, it)

            item.isAccepted = true
            item.textComment = textComment
            item.userName = nameUser
            item.detailList = it.getAttributes().map {
                ItemCredentialsDetails(it.name ?: "", it.value ?: "", it.mimeType ?: "")
            }

            /*       var offerAttaches = offerMessage?.messageObj?.getJSONArray("~attach")
                   if (offerAttaches != null) {
                       //  val att = offerAttaches.optJSONObject(0)
                       for (attach in offerAttaches) {
                           val att = attach as? com.sirius.library.utils.JSONObject
                           if (att != null) {
                               val type = att.optString("@type") ?: ""
                               for (attach in offerAttaches) {
                                   if (type.endsWith("/credential-translation")) {
                                       val dataObject = att.optJSONObject("data")
                                       val jsonArray = dataObject?.optJSONArray("json") ?: JSONArray()
                                       for (jsonObject in jsonArray) {
                                           val att = jsonObject as? com.sirius.library.utils.JSONObject
                                           val name = att?.optString("attrib_name")
                                           val translation = att?.optString("translation")
                                           val attrib = ProposedAttrib(name, translation)
                                           val filtered =   item.detailList?.firstOrNull() {
                                               attrib.name == it.name
                                           }
                                           filtered?.name = translation
                                       }
                                       //     name = jsonObject?.optString("name")
                                   }
                               }
                           }
                       }

       */


            item
        }
        return list

    }
}