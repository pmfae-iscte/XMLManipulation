data class Tag(
    var name: String,
    private var text: String = "",
    private var parent: Tag? = null,
    private val attributesList: MutableList<Attribute> = mutableListOf(),
) {
    init {
        parent?.addTag(this)
    }

    constructor(name: String, parent: Tag) : this(name) {
        parent.addTag(this)
    }

    private val childrenList: MutableList<Tag> = mutableListOf()
    val attributes get() = attributesList.toMutableList()
    val children get() = childrenList.toMutableList()
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

    fun getParent() = parent

    fun setText(text: String) {
        if (childrenList.isNotEmpty()) throw IllegalStateException("The tag is a parent tag, so it can't have text")
        this.text = text
    }

    fun getText() = text

    fun addAttribute(attribute: Attribute) {
        if (attributesList.find { it.name == attribute.name } != null) throw IllegalStateException("Attribute \"${attribute.name}\" already exists")
        attributesList.add(attribute)
    }


    fun setAttribute(attributeName: String, attributeValue: String) {
        if (attributesList.find { it.name == attributeName } == null) throw NoSuchElementException("No such attribute named \"$attributeName\" found")
        attributesList[attributesList.indexOfFirst { it.name == attributeName }] =
            Attribute(attributeName, attributeValue)
    }

    fun setAttribute(oldAttribute: Attribute, newAttribute: Attribute) {
        if (attributesList.find { it == oldAttribute } == null) throw NoSuchElementException("No such attribute \"${oldAttribute.name}\" found")
        attributesList[attributesList.indexOfFirst { it.name == oldAttribute.name }] =newAttribute
    }

    fun setAttribute(oldAttributeName: String, newAttributeName: String, attributeValue: String) {
        if (attributesList.find { it.name == oldAttributeName } == null) throw NoSuchElementException("No such attribute named \"$oldAttributeName\" found")
        attributesList[attributesList.indexOfFirst { it.name == oldAttributeName }] =
            Attribute(newAttributeName, attributeValue)
    }

    fun removeAttribute(attribute: Attribute) {
        attributesList.remove(attribute)
    }

    fun removeAttribute(attributeName: String) {
        attributesList.removeIf { it.name == attributeName }
    }

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


    fun removeTag(tag: Tag?) {
        tag?.parent = null
        childrenList.remove(tag)
    }

    fun removeTag(tagName: String) {
        val tag = childrenList.find { it.name == tagName }
        tag?.parent = null
        childrenList.remove(tag)
    }

    fun removeTag(tagName: String, nth: Int) {
        val tags = childrenList.filter { it.name == tagName }
        if (nth > tags.size) throw IllegalArgumentException("Theres only ${tags.size} and was asked to remove the $nth")
        else {
            tags[nth - 1].parent = null
            childrenList.remove(tags[nth - 1])
        }
    }

    fun removeLastTag(tagName: String) {
        val tag = childrenList.findLast { it.name == tagName }
        tag?.parent = null
        childrenList.remove(tag)
    }

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