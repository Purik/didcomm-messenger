package com.socialsirius.messenger.base.data.models


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, var message: String?,var  errorCode : Error? = Error.UNDEFINED) {
    companion object {
        fun <T> success(data: T?,message: String? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, message)
        }

        fun <T> error(msg: String?, errorCode: Error? = Error.UNDEFINED,data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg,errorCode)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

    override fun toString(): String {
        return "status= $status mesasge=$message data=$data"
    }
}