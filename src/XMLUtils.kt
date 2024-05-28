import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.*


fun createTag(obj: Any): Tag {
    return createSubTag(obj, obj::class.getName())
}

fun createSubTag(obj: Any, name: String): Tag {
    val clazz = obj::class

    val classAttributes = getAttributePerXMLElement(obj)

    checkTextAndTags(classAttributes)

    if (classAttributes["Text"] != null) return Tag(name, , attributesList = getAttributes(obj))
    val newTag = Tag(name, attributesList = getAttributes(obj))
    getChildrenTags(obj).forEach { newTag.addTag(it) }
    getTextTags(obj).forEach { newTag.addTag(it) }
    return newTag
}

fun getAttributePerXMLElement(obj: Any): Map<String, MutableList<KProperty<*>>> {
    val map = mutableMapOf<String, MutableList<KProperty<*>>>()
    obj::class.declaredMemberProperties.forEach {
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
    findAnnotation<XMLName>()?.value ?: simpleName ?: throw IllegalArgumentException("Class has no name?")

fun KProperty<*>.getName(): String = findAnnotation<XMLName>()?.value ?: name

fun getAttributes(obj: Any): MutableList<Attribute> =
    obj::class.declaredMemberProperties.filter { it.hasAnnotation<XMLAttribute>() }
        .map { Attribute(it.name, it.call(obj).toString()) }.toMutableList()


fun getText(obj: Any): String? =
    obj::class.declaredMemberProperties.findLast { it.hasAnnotation<XMLText>() }?.call(obj)?.toString()


fun getChildrenTags(obj: Any): List<Tag> {


    return obj::class.declaredMemberProperties.filter { it.hasAnnotation<XMLTag>() && it.call(obj) != null }
        .map { createSubTag(it.call(obj)!!, it.getName()) }
}

fun getTextTags(obj: Any): List<Tag> {
    return obj::class.declaredMemberProperties.filter { it.hasNoXMLAnotation() }.map { createTextTag(it, obj) }
}

fun createTextTag(p: KProperty<*>, obj: Any): Tag {
    return Tag(p.name, text = p.call(obj).toString())
}


