import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.*


fun KClass<*>.createTag(obj: Any): Tag {
//    checkTextAndTags()
    val textTags = getTextTags(obj)
//    if (text != null) return Tag(getName(), text = text, attributesList = getAttributes(obj))

    val newTag = Tag(obj::class.getName(), attributesList = getAttributes(obj))
    getChildrenTags(obj).forEach { newTag.addTag(it) }
    textTags.forEach { newTag.addTag(it) }
    return newTag
}

fun KClass<*>.checkTextAndTags() {
    var textCount = 0
    var hasTags = false
    declaredMemberProperties.forEach {
        if (it.hasAnnotation<XMLTextTag>()) textCount++
        if (it.hasAnnotation<XMLTag>()) hasTags = true
    }
    if (hasTags && textCount >= 0) throw IllegalArgumentException("Class given has both tags and text")
    if (textCount > 1) throw IllegalArgumentException("Class given has more than one text")
}

fun KClass<*>.getName(): String =
    (findAnnotation<XMLName>()?.value ?: simpleName ?: throw IllegalArgumentException("Class has no name?"))

fun KClass<*>.getAttributes(obj: Any): MutableList<Attribute> =
    declaredMemberProperties.filter { !it.hasAnnotation<XMLTag>() && !it.hasAnnotation<XMLExclude>() && !it.hasAnnotation<XMLTextTag>() }
        .map { Attribute(it.name, it.call(obj).toString()) }.toMutableList()

fun KClass<*>.getChildrenTags(obj: Any): List<Tag> {
    println(simpleName)
    return declaredMemberProperties.filter { it.hasAnnotation<XMLTag>() }.map { createTag(it.call(obj)) }}

fun KClass<*>.getTextTags(obj: Any): List<Tag> =

declaredMemberProperties.filter { it.hasAnnotation<XMLTextTag>() }.map { createTextTag(it, obj) }

fun KClass<*>.createTextTag(p: KProperty<*>, obj: Any): Tag {
    return Tag(p.name, text = p.call(obj).toString())
}


