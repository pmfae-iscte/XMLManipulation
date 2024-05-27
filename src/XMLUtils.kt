import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation


fun createTag(obj: Any?): Tag {
    if (obj == null)
        throw IllegalArgumentException("Object cannot be null")
    val clazz = obj::class
    return Tag((clazz.findAnnotation<XMLName>()?.value ?: clazz.simpleName)!!,  attributesList =
    clazz.declaredMemberProperties.map { Attribute(it.name, it.call(obj).toString()) }.toMutableList())
}

