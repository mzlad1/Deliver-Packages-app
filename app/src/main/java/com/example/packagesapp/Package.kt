package com.example.packagesapp

data class Package(val id : String? ,val receiver: String, val sender: String, val receiverPhone: String, val price: String){
    constructor(receiver: String, sender: String, receiverPhone: String, price: String) : this(null, receiver, sender, receiverPhone, price)
}

