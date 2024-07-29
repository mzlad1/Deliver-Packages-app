package com.example.packagesapp

data class Package(val id : String? ,val receiver: String, val sender: String, val receiverPhone: String, val price: Double){
    constructor(receiver: String, sender: String, receiverPhone: String, price: Double) : this(null, receiver, sender, receiverPhone, price)
}

