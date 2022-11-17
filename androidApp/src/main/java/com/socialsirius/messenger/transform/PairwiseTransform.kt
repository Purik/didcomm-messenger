package com.socialsirius.messenger.transform

import com.socialsirius.messenger.models.ui.ItemContacts

import com.sirius.library.agent.pairwise.Pairwise
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.repository.models.LocalMessage
import java.util.*

class PairwiseTransform {

    companion object {
        fun pairwiseToItemContacts(pairwise: Pairwise, lastMessage : LocalMessage?) : Chats {
          val chats =   Chats(pairwise.their.did?:"",pairwise.their.label?:"")
            chats.lastMessage = lastMessage
           return chats
        }

    }
}