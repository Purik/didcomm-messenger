package com.socialsirius.messenger.models.ui

import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import java.io.Serializable

class ItemActions : Serializable{


    enum class ActionType{
        Request,
        Connect,
        Question,
        Offer,

    }
    constructor() {

    }

    constructor(id : String, type : ActionType?, hint: String) {
        this.type = type
        this.hint = hint
        this.id = id
        setupFromType()
    }


    private fun setupFromType(){
        if(type == null){
            backColor = App.getContext().resources.getColor(R.color.red)
            title = "Unknown"
        }
        if(type == ActionType.Request){
            backColor = App.getContext().resources.getColor(R.color.red)
            title = "REQUEST!"
        }
        if(type == ActionType.Connect){
            backColor = App.getContext().resources.getColor(R.color.yellow)
            title = "CONNECT?"
        }
        if(type == ActionType.Question){
            backColor = App.getContext().resources.getColor(R.color.red)
            title = "QUESTION?"
        }
        if(type == ActionType.Offer){
            backColor = App.getContext().resources.getColor(R.color.red)
            title = "Offer!"
        }
    }
    var type: ActionType? = null
    var id: String? = null
    var title: String? = null
    var hint: String? = null
    var backColor: Int? = App.getContext().resources.getColor(R.color.red)


}