/**
 * An object to represent an XML Tag.
 * @property [name] A `String` corresponding to the Tag name in XML.
 * @property [text] A `String` corresponding to the text of the XML Tag. If it has [text] it will not be possible to add `Tag`s.
 * @constructor Automatically adds this [Tag] to the [parent] if given.
 */
data class Tag(
    var name: String,
    private var text: String = "",
    private var parent: Tag? = null,
    private val attributesList: MutableList<Attribute> = mutableListOf(),
) {
    init {
        parent?.addTag(this)
    }

    /**
     * A constructor that allows the creation of a `Tag` with only the [name] and [parent].
     */
    constructor(name: String, parent: Tag) : this(name) {
        parent.addTag(this)
    }

    private val childrenList: MutableList<Tag> = mutableListOf()

    /**
     * Returns a read-only `List` of the attributes.
     */
    val attributes get() = attributesList.toList()

    /**
     * Returns a read-only `List` of the children tags.
     */
    val children get() = childrenList.toList()

    /**
     * Returns the [Tag] in the XML pretty print format.
     */
    val prettyPrint: String
        get() {
            val stringBuilder = StringBuilder()
            fun aux(e: Tag, depth: Int = 0) {
                if (e.text != "") stringBuilder.append(
                    "\t".repeat(depth) + "<${e.name}${
                        if (e.attributesList.isNotEmpty()) " ${e.attributes.joinToString(" ")}" else ""
                    }>" + e.text + "</${e.name}>\n"
                )
                else if (e.childrenList.isNotEmpty()) {
                    stringBuilder.append(
                        "\t".repeat(depth) + "<${e.name}${
                            if (e.attributesList.isNotEmpty()) " ${e.attributes.joinToString(" ")}" else ""
                        }>\n"
                    )
                    e.childrenList.forEach { aux(it, depth + 1) }
                    stringBuilder.append("\t".repeat(depth) + "</${e.name}>\n")

                } else stringBuilder.append(
                    "\t".repeat(depth) + "<${e.name}" + "${
                        if (e.attributesList.isNotEmpty()) " ${e.attributes.joinToString(" ")}" else ""
                    }/>\n"
                )
            }
            aux(this)
            return stringBuilder.toString().dropLast(1)
        }

    /**
     * Returns the parent reference if it exists or null if it doesn't.
     */
    fun getParent() = parent

    /**
     * Changes the [text] of the [Tag].
     * @throws IllegalStateException if the [Tag] has children tags.
     */
    fun setText(text: String) {
        if (childrenList.isNotEmpty()) throw IllegalStateException("The tag is a parent tag, so it can't have text")
        this.text = text
    }

    /**
     * Returns the [text] of the [Tag].
     */
    fun getText() = text

    /**
     * Adds the specified [attribute] to the [Tag].
     * @throws IllegalStateException if the [Tag] already has an `Attribute` with the same name.
     */
    fun addAttribute(attribute: Attribute) {
        if (attributesList.find { it.name == attribute.name } != null) throw IllegalStateException("Attribute \"${attribute.name}\" already exists")
        attributesList.add(attribute)
    }

    /**
     * Replaces the [oldAttribute] of the [Tag] with the [newAttribute] given.
     * @throws NoSuchElementException if the [Tag] does not contain the specified [oldAttribute].
     */
    fun setAttribute(oldAttribute: Attribute, newAttribute: Attribute) {
        if (attributesList.find { it == oldAttribute } == null) throw NoSuchElementException("No such attribute \"${oldAttribute.name}\" found")
        attributesList[attributesList.indexOfFirst { it.name == oldAttribute.name }] = newAttribute
    }

    /**
     * Changes the value of the attribute with the [attributeName] given to the [attributeValue] given.
     * @throws NoSuchElementException if the [Tag] does not contain an `Attribute` with the [attributeName] given.
     */
    fun setAttribute(attributeName: String, attributeValue: String) {
        if (attributesList.find { it.name == attributeName } == null) throw NoSuchElementException("No such attribute named \"$attributeName\" found")
        attributesList[attributesList.indexOfFirst { it.name == attributeName }] =
            Attribute(attributeName, attributeValue)
    }

    /**
     * Replaces the attribute with the [oldAttributeName] given with a new one created with the [newAttributeName] and [attributeValue] given.
     * @throws NoSuchElementException if the [Tag] does not contain an `Attribute` with the [oldAttributeName] given.
     */
    fun setAttribute(oldAttributeName: String, newAttributeName: String, attributeValue: String) {
        if (attributesList.find { it.name == oldAttributeName } == null) throw NoSuchElementException("No such attribute named \"$oldAttributeName\" found")
        attributesList[attributesList.indexOfFirst { it.name == oldAttributeName }] =
            Attribute(newAttributeName, attributeValue)
    }

    /**
     * Removes the specified [attribute] from the [Tag].
     */
    fun removeAttribute(attribute: Attribute) {
        attributesList.remove(attribute)
    }

    /**
     * Removes the `Attribute` with the [attributeName] given from the [Tag].
     */
    fun removeAttribute(attributeName: String) {
        attributesList.removeIf { it.name == attributeName }
    }

    /**
     * Adds the specified [tag] as child of this [Tag], setting this [Tag] as the `parent` of the specified [Tag].
     * @throws IllegalStateException if the [text] of the [Tag] is not empty.
     */
    fun addTag(tag: Tag) {
        if (text != "") throw IllegalStateException("The tag is a text tag, so it can't have children tags")
        if (childrenList.contains(tag)) {
            val copiedTag = tag.copy()
            childrenList.add(copiedTag)
            copiedTag.parent = this
        } else {
            childrenList.add(tag)
            tag.parent = this
        }
    }

    /**
     * Removes the specified [tag] from the children of this [Tag] and this [Tag] stops being the [tag]'s parent.
     * @return The specified [tag].
     */
    fun removeTag(tag: Tag?): Tag? {
        tag?.parent = null
        childrenList.remove(tag)
        return tag
    }

    /**
     * Removes the `Tag` that has the specified [tagName] from the children of this [Tag] and this [Tag] stops being the removed `Tag` parent.
     * @return The removed `Tag` if one found, or `null` if none found.
     */
    fun removeTag(tagName: String): Tag? {
        val tag = childrenList.find { it.name == tagName }
        tag?.parent = null
        childrenList.remove(tag)
        return tag
    }

    /**
     * Removes the [nth] `Tag` with the specified [tagName] from the children of this [Tag] and this [Tag] stops being removed `Tag` parent.
     * @return The removed `Tag`.
     * @throws IllegalArgumentException if there are less `Tag`'s with the given [tagName] than the [nth].
     */
    fun removeTag(tagName: String, nth: Int): Tag {
        val tags = childrenList.filter { it.name == tagName }
        val tag: Tag
        if (nth > tags.size) throw IllegalArgumentException("Theres only ${tags.size} and was asked to remove the $nth")
        else {
            tag = tags[nth - 1]
            tags[nth - 1].parent = null
            childrenList.remove(tags[nth - 1])
        }
        return tag
    }

    /**
     * Removes the last `Tag` that has the specified [tagName] from the children of this [Tag] and this [Tag] stops being the removed `Tag` parent.
     * @return The removed `Tag` if one found, or `null` if none found.
     */
    fun removeLastTag(tagName: String): Tag? {
        val tag = childrenList.findLast { it.name == tagName }
        tag?.parent = null
        childrenList.remove(tag)
        return tag
    }

    /**
     * Function that utilizes the `Visitor Pattern` allowing to iterate over the [Tag]'s data structure.
     */
    fun accept(v: Visitor) {
        if (v.visit(this)) {
            attributesList.forEach {
                it.accept(v)
            }
            childrenList.forEach {
                it.accept(v)
            }
        }
        v.endVisit(this)
    }
}