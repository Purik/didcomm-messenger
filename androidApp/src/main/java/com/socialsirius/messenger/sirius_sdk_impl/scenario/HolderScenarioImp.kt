package com.socialsirius.messenger.sirius_sdk_impl.scenario


import com.sirius.library.mobile.scenario.impl.HolderScenario
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.repository.models.LocalMessage


class HolderScenarioImp  constructor(val messageRepository: MessageRepository,
                                            val eventRepository: EventRepository) : HolderScenario(eventRepository) {




}