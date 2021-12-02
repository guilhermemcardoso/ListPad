package me.gmcardoso.listpad.model

class Color(
    var id: Int,
    var name: String,
    var hex: String
) {
    override fun toString(): String {
        return name
    }
}