import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.*


interface StringModifier { //as of now cannot be inner class!!
    fun modify(o: Any?): String = o.toString()
}

interface Adapter {
    fun adapt(t: Tag): Tag
}

fun createTag(obj: Any): Tag {
    return createSubTag(obj, obj::class.getName())
}

fun createSubTag(obj: Any, name: String): Tag {

    val clazz = obj::class
    val classAttributes = getAttributePerXMLElement(clazz)
    checkTextAndTags(classAttributes)

    if (classAttributes["Text"] != null) return Tag(
        name, getText(obj, classAttributes["Text"]), attributesList = getAttributes(obj, classAttributes["Attribute"])
    )
    val newTag = Tag(name, attributesList = getAttributes(obj, classAttributes["Attribute"]))
    getChildrenTags(obj, classAttributes["Tag"]).forEach { newTag.addTag(it) }
    getTextTags(obj, classAttributes["TextTag"]).forEach { newTag.addTag(it) }
    getListTags(obj, classAttributes["ListTag"]).forEach { newTag.addTag(it) }



    return if (clazz.hasAnnotation<XmlAdapter>()) {
        val adapter = clazz.findAnnotation<XmlAdapter>()!!.adapter
        if (adapter.isInner)
            adapter.primaryConstructor!!.call(obj).adapt(newTag)
        else
            adapter.createInstance().adapt(newTag)
    } else newTag
}

fun getAttributePerXMLElement(clazz: KClass<*>): Map<String, MutableList<KProperty<*>>> {
    val map = mutableMapOf<String, MutableList<KProperty<*>>>()
    clazz.declaredMemberProperties.forEach {
        if (map[it.findXMLAnnotation()] == null) map[it.findXMLAnnotation()] = mutableListOf(it)
        else map[it.findXMLAnnotation()]!!.add(it)
    }
    return map.toMap()
}

fun checkTextAndTags(attributes: Map<String, MutableList<KProperty<*>>>) {
    if (attributes["Text"] != null && (attributes["Tag"] != null || attributes["TextTag"] != null || attributes["ListTag"] != null)) throw IllegalArgumentException(
        "Class given has both tags and text"
    )
    if ((attributes["Text"]?.size ?: 0) > 1) throw IllegalArgumentException("Class given has more than one text")
}

fun KClass<*>.getName(): String =
    findAnnotation<XmlName>()?.value ?: simpleName ?: throw IllegalArgumentException("Class has no name?")

fun KProperty<*>.getName(): String = findAnnotation<XmlName>()?.value ?: name

fun getAttributes(obj: Any, kProperties: MutableList<KProperty<*>>?): MutableList<Attribute> = kProperties?.map {
    Attribute(
        it.getName(), if (it.hasAnnotation<XmlString>()) {
            val modifierClass = it.findAnnotation<XmlString>()!!.stringModifier
            if (modifierClass.isInner)
                modifierClass.primaryConstructor!!.call(obj).modify(it.call(obj))
            else
                modifierClass.createInstance().modify(it.call(obj))
        } else it.call(obj).toString()
    )
}?.toMutableList() ?: mutableListOf()


fun getText(obj: Any, kProperties: MutableList<KProperty<*>>?): String = kProperties!![0].call(obj).toString()


fun getChildrenTags(obj: Any, kProperties: MutableList<KProperty<*>>?): List<Tag> {
    return kProperties?.map { createSubTag(it.call(obj)!!, it.getName()) } ?: listOf()
}

fun getTextTags(obj: Any, kProperties: MutableList<KProperty<*>>?): List<Tag> {
    return kProperties?.map { Tag(it.getName(), text = it.call(obj).toString()) } ?: listOf()
}

fun getListTags(obj: Any, kProperties: MutableList<KProperty<*>>?): List<Tag> {
    val tags: MutableList<Tag> = mutableListOf()
    kProperties?.forEach {
        val element = it.call(obj)
        if (element is Collection<*> || element is Array<*>) {
            val t = Tag(it.getName())
            ((if (element is Array<*>) element.toList() else element) as Collection<*>).forEach { item ->
                if (item != null) t.addTag(createTag(item))
            }
            tags.add(t)
        }
    }
    return tags
}


