import kotlin.reflect.KProperty
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class XMLName(val value: String)

@Target(AnnotationTarget.PROPERTY)
annotation class XMLAttribute

@Target(AnnotationTarget.PROPERTY)
annotation class XMLText

@Target(AnnotationTarget.PROPERTY)
annotation class XMLTag

@Target(AnnotationTarget.PROPERTY)
annotation class XMLTextTag

@Target(AnnotationTarget.PROPERTY)
annotation class XMLTagList

fun KProperty<*>.hasNoXMLAnotation() =
    !hasAnnotation<XMLTextTag>() && !hasAnnotation<XMLAttribute>() && !hasAnnotation<XMLText>() && !hasAnnotation<XMLTag>() && !hasAnnotation<XMLTagList>()

fun KProperty<*>.findXMLAnnotation(): String {
    return if (hasAnnotation<XMLAttribute>()) "Attribute"
    else if (hasAnnotation<XMLText>()) "Text"
    else if (hasAnnotation<XMLTag>()) "Tag"
    else if (hasAnnotation<XMLTextTag>()) "TextTag"
    else if (hasAnnotation<XMLTagList>()) "TagList"
    else "Exclude"
}