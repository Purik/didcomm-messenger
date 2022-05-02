package com.socialsirius.messenger.models

class FileAttach {

    enum class FileType{
        Image,
        Audio,
        Doc,
        Video
    }
    var fileType : FileType?= null
    var fileName : String? = null
    var id : String? = null
    var fileBase64 : String? = null
    var fileBase64Bytes : ByteArray? = null
    var chunkedAllNumber : Int = 0
    var chunkedOrder : Int = 0
    var messageText : String? = null
}