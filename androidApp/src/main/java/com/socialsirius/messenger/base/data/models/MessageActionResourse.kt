package com.socialsirius.messenger.base.data.models


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class MessageActionResourse<out T>(val action: MessageAction, val data: T?, var jid: String, var errorCode: Error = Error.UNDEFINED) {
    companion object {
        fun <T> add(data: T?, jid: String): MessageActionResourse<T> {
            return MessageActionResourse(MessageAction.ADD, data, jid)
        }

        fun <T> delete(data: T?, jid: String): MessageActionResourse<T> {
            return MessageActionResourse(MessageAction.DELETE, data, jid)
        }

        fun <T> change(data: T?, jid: String): MessageActionResourse<T> {
            return MessageActionResourse(MessageAction.CHANGE, data, jid)
        }

        fun <T> updateStatus(data: T?, jid: String): MessageActionResourse<T> {
            return MessageActionResourse(MessageAction.UPDATE, data, jid)
        }
        fun <T> wait(): MessageActionResourse<T> {
            return MessageActionResourse(MessageAction.WAIT, null, "")
        }
    }

    override fun toString(): String {
        return "status= $action mesasge=$jid data=$data"
    }
}