import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class XmlName(val value: String)

@Target(AnnotationTarget.PROPERTY)
annotation class XmlAttribute

@Target(AnnotationTarget.PROPERTY)
annotation class XmlText

@Target(AnnotationTarget.PROPERTY)
annotation class XmlTag

@Target(AnnotationTarget.PROPERTY)
annotation class XmlTextTag

@Target(AnnotationTarget.PROPERTY)
annotation class XmlTagList

@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val stringModifier: KClass<out StringModifier>)
//fun KProperty<*>.hasNoXMLAnotation() =
//    !hasAnnotation<XMLTextTag>() && !hasAnnotation<XMLAttribute>() && !hasAnnotation<XMLText>() && !hasAnnotation<XMLTag>() && !hasAnnotation<XMLTagList>()

fun KProperty<*>.findXMLAnnotation(): String {
    return if (hasAnnotation<XmlAttribute>()) "Attribute"
    else if (hasAnnotation<XmlText>()) "Text"
    else if (hasAnnotation<XmlTag>()) "Tag"
    else if (hasAnnotation<XmlTextTag>()) "TextTag"
    else if (hasAnnotation<XmlTagList>()) "ListTag"
    else "Exclude"
}