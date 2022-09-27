package com.socialsirius.messenger.ui.activities.auth




import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseActivityModel
import com.socialsirius.messenger.repository.MessageRepository
import javax.inject.Inject

class AuthActivityModel @Inject constructor(
    resourceProvider: ResourcesProvider,messageRepository: MessageRepository

) : BaseActivityModel(messageRepository) {



}