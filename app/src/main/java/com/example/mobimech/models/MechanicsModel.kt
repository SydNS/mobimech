package com.example.mobimech.models

import com.google.firebase.database.Exclude

class MechanicsModel {
    var name: String? = null
    var imageUrl: String? = null

    @get:Exclude
    @set:Exclude
    var key: String? = null
    var description: String? = null
    private var position = 0

    constructor() {
        //empty constructor needed
    }

    constructor(position: Int) {
        this.position = position
    }

    constructor(name: String, imageUrl: String?, Des: String?) {
        var name = name
        if (name.trim { it <= ' ' } == "") {
            name = "No Name"
        }
        this.name = name
        this.imageUrl = imageUrl
        description = Des
    }
}