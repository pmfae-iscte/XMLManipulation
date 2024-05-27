data class Attribute(var name: String, val value: String) {

    override fun toString(): String {
        return "$name=\"$value\""
    }

    fun accept(v: Visitor) {
        v.visit(this)
    }

}