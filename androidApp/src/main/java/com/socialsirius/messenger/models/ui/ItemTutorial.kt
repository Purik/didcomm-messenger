package com.socialsirius.messenger.models.ui

class ItemTutorial {
    var text : String? = null
    var title : String? = null
    var drawableRes : Int? = null
    var page : Int? = 0

    constructor()
    constructor(page: Int?,title: String?,text: String?, drawableRes: Int? ) {
        this.text = text
        this.title = title
        this.page = page
        this.drawableRes = drawableRes
    }
}