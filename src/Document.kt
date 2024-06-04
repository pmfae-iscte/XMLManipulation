/**
 * An object to represent the XML File.
 * @property [mainTag] The [Tag] that contains all `Tag`s of the document.
 * @property [version] The `String` corresponding to the version of XML being used.
 * @property [encoding] The `String` with the enconding to be used to decode the document
 */
class Document(private val mainTag: Tag, version: String = "1.0", encoding: String = "UTF-8") {

    private val version: Attribute = Attribute("version", version)
    private val encoding: Attribute = Attribute("encoding", encoding)

    /**
     * Returns the String corresponding to the XML File.
     */
    fun getXML() = "<?xml $version $encoding?>\n${mainTag.prettyPrint}"

    /**
     * Returns a list with the tags in the tag specified by the [xpath].
     *
     * [xpath] is a `String` composed of a sequence of tags names separated by `/`.
     */
    fun getXMLFragment(xpath: String): List<Tag> {
        if (xpath == "")
            return listOf(mainTag)
        val list = mutableListOf<Tag>()
        fun aux(path: List<String>, t: Tag) {
            if (path.isEmpty()) return
            if (path.size == 1 && t.name == path[0])
                list.add(t)
            else if (t.name == path[0])
                t.children.forEach { aux(path.drop(1), it) }
        }
        aux("${mainTag.name}/$xpath".split("/"), mainTag)
        return list
    }

    /**
     * Creates the `Attribute` with the [attributeName] and [attributeValue] given and then adds to the Tag or Tags in the Document with the given [tagName].
     */
    fun addAttribute(
        tagName: String,
        attributeName: String,
        attributeValue: String
    ) {
        val addAtribute = object : Visitor {
            override fun visit(t: Tag): Boolean {
                if (t.name == tagName) {
                    t.addAttribute(Attribute(attributeName, attributeValue))
                    return false
                }
                return true
            }
        }
        mainTag.accept(addAtribute)
    }

    /**
     * Changes the `Tag`'s names where their name equals [oldTagName] to [newTagName].
     */
    fun renameTag(oldTagName: String, newTagName: String) {
        val renameTag = object : Visitor {
            override fun visit(t: Tag): Boolean {
                if (t.name == oldTagName) {
                    t.name = newTagName
                }
                return true
            }
        }
        mainTag.accept(renameTag)
    }

    /**
     * Changes the `Attribute` name of the `Tag`'s identified with the given [tagName]. The `Attribute` changed is the one where the name equals to [oldAttributeName] and the new name will be the [newAttributeName] given.
     */
    fun renameAttribute(tagName: String, oldAttributeName: String, newAttributeName: String) {
        val renameAttribute = object : Visitor {
            val tagStack = mutableListOf<String>()
            override fun visit(t: Tag): Boolean {
                tagStack.add(t.name)
                return true
            }

            override fun endVisit(t: Tag) {
                tagStack.remove(t.name)
            }

            override fun visit(a: Attribute) {
                if (tagStack.last() == tagName && a.name == oldAttributeName) {
                    a.name = newAttributeName
                }

            }
        }
        mainTag.accept(renameAttribute)
    }

    /**
     * Removes the `Tag`'s with the given [tagName] from the [Document].
     */
    fun removeTag(tagName: String) {
        val removeTag = object : Visitor {
            override fun visit(t: Tag): Boolean {
                if (t.children.find { it.name == tagName } == null)
                    return true

                t.children.forEach {
                    if (it.name == tagName)
                        t.removeTag(tagName)
                }
                return false
            }
        }
        mainTag.accept(removeTag)
    }

    /**
     * Removes the `Attribute`'s with the given [attributeName] from the `Tag`'s with the given [tagName] from the [Document].
     */
    fun removeAttribute(tagName: String, attributeName: String) {
        val removeAttribute = object : Visitor {
            override fun visit(t: Tag): Boolean {
                if (t.name == tagName)
                    t.removeAttribute(attributeName)
                return true
            }
        }
        mainTag.accept(removeAttribute)
    }
}