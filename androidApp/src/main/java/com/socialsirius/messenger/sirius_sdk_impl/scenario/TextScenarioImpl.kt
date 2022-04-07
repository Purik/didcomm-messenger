package com.socialsirius.messenger.sirius_sdk_impl.scenario


import com.sirius.library.mobile.scenario.impl.TextScenario
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository

class TextScenarioImpl  constructor(val messageRepository: MessageRepository,
                                           val eventRepository: EventRepository): TextScenario(eventRepository) {

}