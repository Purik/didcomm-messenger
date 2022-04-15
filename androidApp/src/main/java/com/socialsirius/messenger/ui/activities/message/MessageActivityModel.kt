package com.socialsirius.messenger.ui.activities.message;


import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseActivityModel
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.models.ui.ItemContacts
import javax.inject.Inject


class MessageActivityModel @Inject constructor(
    resourceProvider: ResourcesProvider
    //messageListenerUseCase: MessageListenerUseCase
) : BaseActivityModel( ) {

    var chat: Chats? = null




}