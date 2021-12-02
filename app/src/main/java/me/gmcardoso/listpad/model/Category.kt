package me.gmcardoso.listpad.model

class Category(
    var id: Int? = null,
    var name: String,
    var default: Int,
    var color: String
) {
    override fun toString(): String {
        return name
    }
}