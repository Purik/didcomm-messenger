package com.socialsirius.messenger.ui.chats.chat.message



import com.socialsirius.messenger.models.ui.ItemCredentialsDetails
import com.socialsirius.messenger.repository.models.LocalMessage
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.ProposedAttrib
import com.sirius.library.agent.aries_rfc.feature_0037_present_proof.messages.RequestPresentationMessage
import com.sirius.library.agent.listener.Event
import com.sirius.library.mobile.SiriusSDK
import com.sirius.library.mobile.helpers.ScenarioHelper
import com.sirius.library.mobile.scenario.EventAction
import com.sirius.library.mobile.scenario.EventActionListener


import org.json.JSONObject
import java.util.*

class ProverItemMessage : BaseItemMessage {


    constructor(event: Event) : super(event)
    constructor(localMessage: LocalMessage) : super(localMessage)

    override fun getType(): MessageType {
        if (isError) {
            return MessageType.ProverError
        }
        return if (isAccepted) {
            MessageType.ProverAccepted
        } else {
            MessageType.Prover
        }
    }


    var hint: String? = null
    var expiresTime: Date? = null
    var detailList: List<ItemCredentialsDetails>? = null
    var name: String? = null

    fun setupMessage(requestPresentationMessage: RequestPresentationMessage?) {

        requestPresentationMessage?.expiresTime()?.let {
            expiresTime = Date(requestPresentationMessage.expiresTime()!!.time)
        }

        val proofRequest = requestPresentationMessage?.proofRequest().toString()
        val message = JSONObject(requestPresentationMessage?.serialize())
        val attches = message.optJSONArray("~attach")
        val list = mutableListOf<ProposedAttrib>()

        if(attches!=null){
            for (i in 0 until attches.length()) {

                val attachObject = attches.getJSONObject(i)
                val type = attachObject.optString("@type")

                if (type.contains("credential-translation")) {
                    val langObject = attachObject.optJSONObject("~l10n")
                    val dataObj = attachObject.optJSONObject("data")
                    val dataJson = dataObj.optJSONArray("json")
                    for (z in 0 until dataJson.length()) {
                        val attrObj = dataJson.getJSONObject(z)
                        val attrName = attrObj.optString("attrib_name")
                        val translation = attrObj.optString("translation")
                        list.add(ProposedAttrib(attrName, translation))
                    }
                }
            }
        }
        hint = requestPresentationMessage?.comment
        val reqObject = JSONObject(proofRequest)
        name = reqObject.optString("name")
        val names = mutableMapOf<String, Boolean>()
        val reqAttr = reqObject.optJSONObject("requested_attributes")
        val iteraror = reqAttr.keys()
        while (iteraror.hasNext()) {
            val key = iteraror.next()
            val attrObject = reqAttr.optJSONObject(key)
            val name = attrObject?.optString("name")
            name?.let {
                var existTranslation = false
                list.forEach {
                    if (it.name == name) {
                        if (it.value?.isNotEmpty()==true) {
                            names.put(it.value?:"", false)
                            existTranslation = true
                        }
                    }
                }
                if (!existTranslation) {
                    names.put(name, false)
                }
            }
        }

        try{
            val string2 = SiriusSDK.getInstance().context.anonCreds.
            proverSearchCredentialsForProofReq(requestPresentationMessage?.proofRequest(), limitReferents = 1)


            val objecte1 =  com.sirius.library.utils.JSONObject(string2)
            val objecte = objecte1.optJSONObject("requested_attributes")
            objecte?.let {
                for (objectsd in objecte.keySet()){
                    val array =  objecte.optJSONArray(objectsd)
                    val credInfo = array?.optJSONObject(0)?.optJSONObject("cred_info")?.optJSONObject("attrs")
                    credInfo?.let {
                        for (credKey in  credInfo.keySet()){
                            if(names.contains(credKey)){
                                names.put(credKey, true)
                            }
                        }
                    }
                }
            }

        }catch (e :Exception ){
            e.printStackTrace()
        }


        detailList = names?.map {
            var name  =it.key
            if(name == "first_name"){
                name = "First Name"
            }
            if(name == "last_name"){
                name = "Last Name"
            }
            if(name == "categories"){
                name = "Categories"
            }
            if(name == "birthday"){
                name = "Birthday"
            }
            if(name == "place_of_birth"){
                name = "Place of birth"
            }
            var value = "not available"
            if(it.value){
                 value = "available"
            }
            ItemCredentialsDetails(name, value,"")
        }
    }

    override fun setupFromLocalMessage(localMessage: LocalMessage?) {
        super.setupFromLocalMessage(localMessage)
        val requestPresentationMessage = localMessage?.message() as? RequestPresentationMessage
        setupMessage(requestPresentationMessage)
    }

    override fun setupFromEvent(event: Event?) {
        super.setupFromEvent(event)
        val requestPresentationMessage = event?.message() as? RequestPresentationMessage
        setupMessage(requestPresentationMessage)
    }


    override fun accept(comment: String?) {
        ScenarioHelper.getInstance().acceptScenario("Prover", message?.getId() ?: "", comment, object :
            EventActionListener {
            override fun onActionStart(action: EventAction, id: String, comment: String?) {
                startLoading(id)
            }

            override fun onActionEnd(
                action: EventAction,
                id: String,
                comment: String?,
                success: Boolean,
                error: String?
            ) {
                isError = !success
                isAccepted = success
                errorString = error
                commentString = error
                stopLoading(id)
            }

        })
    }

    override fun cancel() {
        ScenarioHelper.getInstance().stopScenario("Prover", message?.getId() ?: "", "Canceled By Me",
            object : EventActionListener {
                override fun onActionStart(action: EventAction, id: String, comment: String?) {
                    startLoading(id)
                }

                override fun onActionEnd(
                    action: EventAction,
                    id: String,
                    comment: String?,
                    success: Boolean,
                    error: String?
                ) {
                    isError = !success
                    isAccepted = success
                    errorString = error
                    commentString = error
                    stopLoading(id)
                }
            })
    }

    override fun getText(): String {
        return hint ?: ""
    }

    override fun getTitle(): String {
        return name ?: ""
    }
}