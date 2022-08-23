package com.socialsirius.messenger.ui.chats.chat.message


import com.socialsirius.messenger.models.ui.ItemQuestionAnswer
import com.socialsirius.messenger.repository.models.LocalMessage

import com.sirius.library.agent.aries_rfc.feature_0113_question_answer.messages.QuestionMessage
import com.sirius.library.agent.listener.Event
import com.sirius.library.mobile.helpers.ScenarioHelper
import com.sirius.library.mobile.scenario.EventAction
import com.sirius.library.mobile.scenario.EventActionListener

import org.json.JSONObject
import java.util.*

class QuestionItemMessage : BaseItemMessage {


    constructor(event: Event?) : super(event)
    constructor(localMessage: LocalMessage?) : super(localMessage)

    override fun getType(): MessageType {
        return if (isAccepted) {
            MessageType.QuestionAccepted
        } else {
            MessageType.Question
        }
    }

    var hint: String? = null
    var expiresTime: Date? = null
    var answerList: List<ItemQuestionAnswer>? = null
    var name: String? = null

    fun setupMessage(questionMessage : QuestionMessage?){

        // expiresTime = questionMessage?.expiresTime
        hint = questionMessage?.questionDetail
        name = questionMessage?.questionText
        answerList = questionMessage?.validResponses?.map {
            ItemQuestionAnswer(it)
        }
    }
    override fun setupFromLocalMessage(localMessage: LocalMessage?) {
        super.setupFromLocalMessage(localMessage)
        val questionMessage = localMessage?.message() as? QuestionMessage
        setupMessage(questionMessage)
    }
    override fun setupFromEvent(event: Event?) {
        super.setupFromEvent(event)
        val questionMessage = event?.message() as? QuestionMessage
        setupMessage(questionMessage)
    }


    override fun accept(comment : String?) {
        ScenarioHelper.getInstance().acceptScenario("Question", message?.getId() ?: "",comment, object:
            EventActionListener {
            override fun onActionStart(action: EventAction, id: String, comment: String?) {

            }

            override fun onActionEnd(
                action: EventAction,
                id: String,
                comment: String?,
                success: Boolean,
                error: String?
            ) {

            }

        })
    }

    override fun cancel() {
        ScenarioHelper.getInstance().stopScenario("Question", message?.getId() ?: "", "Canceled By Me", object :
            EventActionListener {
            override fun onActionStart(action: EventAction, id: String, comment: String?) {

            }

            override fun onActionEnd(
                action: EventAction,
                id: String,
                comment: String?,
                success: Boolean,
                error: String?
            ) {

            }

        } )
    }

    override fun getText(): String {
        return "offer sample"
    }

    override fun getTitle(): String {
        return name ?: ""
    }
}