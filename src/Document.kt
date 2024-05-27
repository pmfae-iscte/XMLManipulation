class Document(private val mainTag: Tag, version: String = "1.0", encoding: String = "UTF-8") {

    private val version: Attribute = Attribute("version", version)
    private val encoding: Attribute = Attribute("encoding", encoding)

    fun getXML() = "<?xml $version $encoding?>\n${mainTag.prettyPrint}"


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

    fun addAttribute(
        tagName: String,
        attributeName: String,
        attributeValue: String
    ) { //Não vê os filhos da tag com o nome dado
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