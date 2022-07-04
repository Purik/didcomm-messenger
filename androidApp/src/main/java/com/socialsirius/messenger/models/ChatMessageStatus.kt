package com.socialsirius.messenger.models

/**
 *  Статусы сообщений.
 */
enum class ChatMessageStatus {
    /**
     * Отправляется
     */
    default,
    /**
     * Отправлено, но не доставлено
     */
    sent,
    /**
     * Отправлено, и доставлено
     */
    received,
    /**
     * Отправлено, доставлено и прочитано
     */
    acknowlege,
    /**
     * Ошибка
     */
    error,



    //NEW
    // "sent"
    //"acknowlege"
    //"received"

    //OLD
    /* val ELEMENT_MARKABLE = "markable"
     val ELEMENT_ACKNOWLEDGED = "acknowledged"
     val ELEMENT_RECEIVED = "received"
     val ELEMENT_ERROR = "error"*/
}