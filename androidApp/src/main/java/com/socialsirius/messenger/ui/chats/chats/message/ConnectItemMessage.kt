package com.socialsirius.messenger.ui.chats.chats.message


import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.library.agent.listener.Event
import com.sirius.library.mobile.helpers.ScenarioHelper
import com.sirius.library.mobile.scenario.EventAction
import com.sirius.library.mobile.scenario.EventActionListener
import com.socialsirius.messenger.repository.models.LocalMessage


class ConnectItemMessage : BaseItemMessage {

    var name = ""
    constructor(event: Event?) : super(event)
    constructor(localMessage: LocalMessage?) : super(localMessage) {
        val message = localMessage?.message()
        if(message is Invitation){
           val invit =  message as? Invitation
            name = invit?.label()?:""
        }else if(message is ConnRequest){
            val connRequest =  message as? ConnRequest
            name = connRequest?.label ?:""
        }

    }

    override fun getType(): MessageType {
        if (isError) {
            return MessageType.Connected
        }
        return if (isAccepted) {
            MessageType.Connected
        } else {
            MessageType.Connect
        }
    }


    override fun accept(comment: String?) {
        ScenarioHelper.getInstance().acceptScenario("Invitee", message?.getId() ?: "", comment, object :
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
                if(!isError){
                   // messageRepository?.invitationSuccessLiveData?.postValue(id)
                }

            }

        })
    }

    override fun cancel() {
        ScenarioHelper.getInstance()
            .stopScenario("Invitee", message?.getId() ?: "", "Canceled By Me", object :
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
                    if (isAccepted) {
                    //    messageRepository?.invitationSuccessLiveData?.postValue(id)
                    } else if (isError) {
                     /*   messageRepository?.invitationErrorLiveData?.postValue(
                            Pair(
                                isError,
                                errorString
                            )
                        )*/
                    }
                }
            })
    }

    override fun getText(): String {
        return "You are in chat with $name, start first "
    }

    override fun getTitle(): String {
        return name
    }


}