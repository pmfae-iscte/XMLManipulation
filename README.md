# XML Manipulation 🤖

[![Use this template](https://img.shields.io/badge/from-XML--Manipulation-brightgreen?logo=dropbox)](https://github.com/cortinico/kotlin-android-template/generate) ![Language](https://img.shields.io/github/languages/top/cortinico/kotlin-android-template?color=blue&logo=kotlin)

A simple library for XML generation and manipulation.

It has features to represent Documents, Tags, Tag inner text, Inner Tags and attributes like the following example:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<plano>
	<curso>Mestrado em Engenharia Informática</curso>
		<fuc codigo="M4310">
			<nome>Programação Avançada</nome>
			<ects>6.0</ects>
				<avaliacao>
					<componente nome="Quizzes" peso="20%"/>
					<componente nome="Projeto" peso="80%"/>
			</avaliacao>
		</fuc>
		<fuc codigo="03782">
			<nome>Dissertação</nome>
			<ects>42.0</ects>
			<avaliacao>
				<componente nome="Dissertação" peso="60%"/>
				<componente nome="Apresentação" peso="20%"/>
				<componente nome="Discussão" peso="20%"/>
			</avaliacao>
		</fuc>
</plano>
```



## Features 🎨

- Create XML Attributes, Tags and Documents.
- XML Tags:
  - Add Inner text.
  - Add Attributes.
  - Remove Attributes.
  - Add/Remove  Inner Tags.
  - Iteration of data structure with a Visitor Pattern.
  - XML pretty print
- XML Documents:
  - Get XML text.
  - Get Tags according to a xpath given.
  - Add Attributes to Tags
  - Rename Tags
  - Rename Attributes
  - Remove Tags
  - Remove Attributes
- Creation of Tags from Kotlin Objects using Annotations to define how the Tags are created.



## How to use 👣

#### Example to create a Attribute with instantiation

- ```kotlin
  Attribute("AttributeName", "AttributeValue")
  ```

  

#### Examples to create a Tag with class instantiation

- ```kotlin
  Tag("Example1")
  ```

- ```kotlin
  Tag("Example2", "InnerTagText")
  ```

- ```kotlin
  Tag("Example3", "InnerTagText", Tag("ParentTag"))
  ```

- ```kotlin
  Tag("Example4", "InnerTagText", Tag("ParentTag"), mutableListOf(Attribute("AttributeOne", "AttOneVal")))
  ```

- ```kotlin
  Tag("Example5", Tag("ParentTag"))
  ```


  

#### Create Tags from Objects

The class should have all of it's attributes assigned with the proper annotations:

- `@XmlAttribute` - For XML Attributes.
- `@XmlText` - For the inner Tag text.
- **Tags**:
  - `@XmlTag`- For common inner Tags.
  - `@XmlTextTag` - To create a inner Tag with the attribute data as the inner text of that tag.
  - `@XmlListTag` - To create a inner Tag with the List Elements as children tags. (Can be used with any Kotlin Collection and with Kotlin Array)

For optional customization the following annotations can be used:

- `@XmlName` - To set the Tag or Attribute name
- `@XmlString(val stringModifier: KClass<out StringModifier>)` - To modify the value of the Attribute to create. Receives a class of the`StringModifier` interface, example bellow.
- `@XmlAdapter(val adapter: KClass<out Adapter>)` - To freely personalize the Tag created. Receives a class of the `Adapter` interface, example bellow.

##### Examples

