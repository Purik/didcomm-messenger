package com.socialsirius.messenger.models.ui

class ItemTags {

    constructor() {

    }

    constructor(title: String?, id: Int, color: Int) {
        this.title = title
        this.id = id
        this.color = color
    }

    var title: String? = null
    var id: Int = 0
    var color: Int = 0
}