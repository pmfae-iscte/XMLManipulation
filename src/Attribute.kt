
/**
 * An object to represent an XML Attribute.
 * @property [name] A `String` corresponding to the Attribute name/identifier in XML.
 * @property [value] A `String` corresponding to the value of the XML Attribute.
 */
data class Attribute(var name: String, val value: String) {

    /**
     * Returns the string representation of the `Attribute`
     */
    override fun toString(): String {
        return "$name=\"$value\""
    }

    /**
     * Function that utilizes the `Visitor Pattern` allowing to Iterate over [Attribute]'s.
     */
    fun accept(v: Visitor) {
        v.visit(this)
    }

}