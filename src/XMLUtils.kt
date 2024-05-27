import kotlin.reflect.KClass
import kotlin.reflect.full.*


fun createTag(obj: Any?): Tag {
    if (obj == null)
        throw IllegalArgumentException("Object cannot be null")
    val clazz = obj::class
    val message = checkTextAndTags(clazz)
    if (message != null) throw IllegalArgumentException(message)
    val attributesList = getAttributes(obj)
    val tagsList = getTags(obj)
    val newTag = Tag(
        (clazz.findAnnotation<XMLName>()?.value ?: clazz.simpleName
        ?: throw IllegalArgumentException("Class has no name?")), attributesList = attributesList.toMutableList()
    )
    tagsList.forEach { newTag.addTag(it)}

    return newTag
}

fun checkTextAndTags(clazz: KClass<*>): String? {
    var textCount = 0
    var hasTags = false
    clazz.declaredMemberProperties.forEach {
        if (it.hasAnnotation<XMLText>()) {
            textCount++
        }
        if (it.hasAnnotation<XMLTag>())
            hasTags = true
    }
    if (hasTags && textCount >= 0)
        return "Class given has both tags and text"
    if (textCount > 1)
        return "Class given has more than one text"
    return null
}

fun getTags(obj: Any): List<Tag> {
    return TODO("Implement")
}

fun getAttributes(obj: Any): List<Attribute> {
    return TODO("Implement")
}


fun createAttribute(): Attribute {
    return TODO("Implement")
}

fun createText(): Attribute {
    return TODO("Implement")
}

