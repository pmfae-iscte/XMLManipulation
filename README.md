# XML Manipulation 🤖

[![Use this template](https://img.shields.io/badge/from-XML--Manipulation-brightgreen?logo=dropbox)](https://github.com/cortinico/kotlin-android-template/generate) ![Language](https://img.shields.io/github/languages/top/cortinico/kotlin-android-template?color=blue&logo=kotlin)

A simple library for XML generation and manipulation.

It has features to represent Documents, Tags, Tag inner text, Inner Tags and attributes like in the following example:

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





## Index 

##### Table of Contents  
**[XML Manipulation 🤖]**(#xml-manipulation-)  
[Emphasis](#emphasis)  
...snip...    
<a name="headers"/>
## Headers

[TOC]



## Features 🎨

- Create XML Attributes, Tags and Documents.
- XML Tags:
  - Add Inner text.
  - Add/Change/Remove Attributes.
  - Add/Remove  Inner Tags.
  - Iteration of data structure with a Visitor Pattern.
  - XML pretty print.
  - Access a copy of the Attributes.
  - Access a copy of the inner Tags.
- XML Documents:
  - Get the XML text.
  - Get List of Tags according to a xpath given.
  - Add/Rename/Remove Attributes of specified Tags from document.
  - Rename/Remove Tags from document.
- Creation of Tags from Kotlin Objects using Annotations to define how the Tags are created.



## How to use 👣

#### Example to create a Attribute with class instantiation

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



#### Examples to create a Document with class instantiation

- ```kotlin
  Document(tagXPTO)
  ```

- ```kotlin
  Document(tagXPTO, "1.0")
  ```

- ```kotlin
  Document(tagXPTO, "1.0", "UTF-8")
  ```



#### Examples of Tags features: 

After creation of Tag:

- Add Inner text.

  ```kotlin
  tag.setText("text of the tag")
  tag.setText("")//equals to removing the text and allows for adding of tags
  ```

- Add/Change/Remove Attributes.

  ```kotlin
  val a1 = Attribute("name", "value1")
  val a2 = Attribute("name", "value2")
  tag.addAttribute(a1) //tag will contain Attribute("name", "value1")
  ```

  ```kotlin
  tag.setAttribute(a1, a2) //tag will contain Attribute("name", "value2") // names of attributes should be the same
  tag.setAttribute("name", "value3") //tag will contain Attribute("name", "value3") 
  tag.setAttribute("name", "name2", "value4") //tag will not contain Attribute("name", "value3") but contain Attribute("name2", "value4")
  ```

  Assuming some attributes where added, including a1 again:

  ```kotlin
  tag.removeAttribute(a1)
  tag.removeAttribute("attributeWithBigName")
  ```

- Add/Remove Inner Tags.

  ```kotlin
  val t = Tag("XPTO")
  tag.addTag(t)
  ```

  ```kotlin
  tag.removeTag(t)
  tag.removeTag("TagWithThisName")//removes the first one with the name equal to the String given
  tag.removeTag("TagWithThisName", 3)//removes the 3rd Tag with the name equal to the String given
  tag.removeLastTag("TagWithThisName")
  ```

- Iteration of data structure with a Visitor Pattern.

  ```kotlin
  val renameAttribute = object : Visitor {
      val tagStack = mutableListOf<String>()
      override fun visit(t: Tag): Boolean {
          tagStack.add(t.name)
          return true
      }
  
      override fun endVisit(t: Tag) {
          tagStack.remove(t.name)
      }
  
      override fun visit(a: Attribute) {
          if (tagStack.last() == "TagWithThisName" && a.name == "AttributeWithThisName") {
              a.name = "NewAttributeName"
          }
      }
  }
  mainTag.accept(renameAttribute)
  ```

- XML pretty print.

  ```kotlin
  tag.prettyPrint
  ```

- Access a copy of the Attributes.

  ```kotlin
  tag.attributes//its a list
  ```

- Access a copy of the inner Tags.

  ```kotlin
  tag.children//its a list
  ```

#### Examples of Documents features: 

- Get the XML text.

  ```kotlin
  doc.getXML()
  ```

- Get List of Tags according to a xpath given.

  ```kotlin
  doc.getXMLFragment("tag1/tag2/tag3")
  ```

- Add/Rename/Remove Attributes of specified Tags from document.

  ```kotlin
  doc.addAttribute("tagName", "attributeName", "attributeValue")//adds to all tags with given name
  ```

  ```kotlin
  doc.renameAttribute("tagName", "oldAttributeName", "newAttributeName")//rename in all tags with the given name
  ```

  ```kotlin
  doc.removeAttribute("tagName", "attributeName")//removes from all tags with the given name
  ```

- Rename/Remove Tags from document.

  ```kotlin
  doc.renameTag("oldTagName", "newTagName")//rename all tags with the given name
  ```

  ```kotlin
  doc.removeTag("tagName")//removes all tags with the given name
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

#### Create Tags from Objects - Examples

1. Example with mandatory annotations:

   ```kotlin
   class ComponenteAvaliacao(@XmlAttribute val nome: String, @XmlAttribute val peso: Int)
   class FUC(
               @XmlAttribute val codigo: String,
               @XmlTextTag val nome: String,
               @XmlTextTag val ects: Double,
               val observacoes: String,
               @XmlTagList val avaliacao: List<ComponenteAvaliacao>
           )
   val c = ComponenteAvaliacao("Quizzes", 20)
   val f = FUC(
               "M4310", "Programação Avançada", 6.0, "la la...", listOf(
                   c, ComponenteAvaliacao("Projeto", 80)
               )
           )
   
   println("${createTag(c).prettyPrint}\n")
   println(createTag(f).prettyPrint)
   ```

   Output result:

   ```
   <ComponenteAvaliacao nome="Quizzes" peso="20"/>
   
   <FUC codigo="M4310">
   	<ects>6.0</ects>
   	<nome>Programação Avançada</nome>
   	<avaliacao>
   		<ComponenteAvaliacao nome="Quizzes" peso="20"/>
   		<ComponenteAvaliacao nome="Projeto" peso="80"/>
   	</avaliacao>
   </FUC>
   ```

2. Example with annotations for customization:

   ```kotlin
   class UpperCase : StringModifier {
           override fun modify(o: Any?): String = o.toString().uppercase()
       }
   
       @XmlName("componente")
       class ComponenteAvaliacao(
           @XmlString(UpperCase::class) @XmlAttribute val nome: String,
           @XmlString(AddPercentage::class) @XmlAttribute val peso: Int
       ) {
           inner class AddPercentage : StringModifier {
               override fun modify(o: Any?): String = o.toString() + "%"
           }
       }
   
       class FUCAdapter : Adapter {
           override fun adapt(t: Tag): Tag {
               val ects = t.removeTag("ects")
               t.addTag(ects!!)
               val avaliacao = t.removeTag("avaliacao")
               avaliacao!!.children.forEach { t.addTag(it) }
               return t
           }
       }
   
       @XmlAdapter(FUCAdapter::class)
       @XmlName("fuc")
       class FUC(
           @XmlName("code") @XmlAttribute val codigo: String,
           @XmlTextTag val nome: String,
           @XmlTextTag val ects: Double,
           val observacoes: String,
           @XmlTagList val avaliacao: List<ComponenteAvaliacao>
       )
   
       val f = FUC(
           "M4310", "Programação Avançada", 6.0, "la la...", listOf(
               ComponenteAvaliacao("Quizzes", 20), ComponenteAvaliacao("Projeto", 80)
           )
       )
       println(createTag(f).prettyPrint)
   ```

   Output result:

   ```
   <fuc code="M4310">
   	<nome>Programação Avançada</nome>
   	<ects>6.0</ects>
   	<componente nome="QUIZZES" peso="20%"/>
   	<componente nome="PROJETO" peso="80%"/>
   </fuc>
   ```

   

## Observations 🔍

- A Tag can't contain both a non empty text property and inner tags.
- All removal of tags returns the tag that was removed or `null` if non were removed.
- Can only remove the `nth` tag if there are `nth` inner tags or more.
- When creating Tags from Objects, can't have two tags annotated with the text identifier, or have both annotations for tags and text.
