package me.gmcardoso.listpad.model

class List(
    var id: Int? = null,
    var name: String,
    var description: String,
    var urgent: Int,
    var categoryId: Int?
)