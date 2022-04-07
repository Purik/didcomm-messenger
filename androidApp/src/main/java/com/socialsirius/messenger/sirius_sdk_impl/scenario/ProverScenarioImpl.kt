package com.socialsirius.messenger.sirius_sdk_impl.scenario


import com.sirius.library.mobile.scenario.impl.ProverScenario
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository

class ProverScenarioImpl  constructor(val messageRepository: MessageRepository,
                                             val eventRepository: EventRepository): ProverScenario(eventRepository) {


}