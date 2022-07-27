package com.socialsirius.messenger.repository.models

enum class MessageStatus {
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
}