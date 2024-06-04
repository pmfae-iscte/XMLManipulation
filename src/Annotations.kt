import kotlin.reflect.KClass

/*
 * These annotations are used to define how the object class and it's attributes are translated into the Tag to be created.
 */

/**
 * This annotation determines the name of the [Tag] or [Attribute].
 * @property [value] The name of the [Tag] or [Attribute] derived from the `class` or `attribute` it is assigned to.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class XmlName(val value: String)

/**
 * This annotation determines that the class's `attribute` will be a [Attribute] of the XML [Tag] of the `class`.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlAttribute

/**
 * This annotation determines that the class's `attribute` will be the `text` of the XML [Tag] of the `class`.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlText

/**
 * This annotation determines that the class's `attribute` will be a common [Tag] of the XML [Tag] of the `class`.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlTag

/**
 * This annotation determines that the class's `attribute` will be a [Tag] of the XML [Tag] of the `class` with its data turned into the [Tag]'s text.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlTextTag

/**
 * This annotation determines that the class's `attribute` (that should be either [Iterable] or an [Array]) will be a [Tag] of the XML [Tag] of the `class` with its elements being translated into the [Tag]'s childreen.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlTagList

/**
 * This annotation allows a transformation of the `String` to be the [Attribute.value]. It should be used with [XmlAttribute].
 * @property [stringModifier] [KClass] of [StringModifier] interface that has a function [StringModifier.modify] the return the transformation of the class's attribute data.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val stringModifier: KClass<out StringModifier>)

/**
 * This annotation allows a personalization of the [Tag] created from a `class`.
 */
@Target(AnnotationTarget.CLASS)
annotation class XmlAdapter(val adapter: KClass<out Adapter>)
