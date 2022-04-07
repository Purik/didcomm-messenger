package com.socialsirius.messenger.sirius_sdk_impl.scenario


import com.sirius.library.mobile.scenario.impl.QuestionAnswerScenario
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository

class QuestionAnswerScenarioImp  constructor(val messageRepository: MessageRepository,
                                                    val eventRepository: EventRepository) : QuestionAnswerScenario(eventRepository) {


}