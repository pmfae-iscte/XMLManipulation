
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class XMLName(val value:String)

@Target(AnnotationTarget.PROPERTY)
annotation class XMLExclude

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class XMLTextTag

@Target(AnnotationTarget.PROPERTY)
annotation class XMLTag

