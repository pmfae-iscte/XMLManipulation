import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ClassXMLMappingTests {

    @Test
    fun `check if cant have both text and tags and if can't have more than one text`() {
        class TestClass1(@XmlText val text: String, @XmlTag val tag: String)
        assertEquals(
            "Class given has both tags and text",
            assertThrows<IllegalArgumentException> { createTag(TestClass1("", "")) }.message
        )
        class TestClass2(@XmlText val text1: String, @XmlText val text2: String)
        assertEquals(
            "Class given has more than one text",
            assertThrows<IllegalArgumentException> { createTag(TestClass2("", "")) }.message
        )
    }

    @Test
    fun createTagTest() {
        @XmlName("componente")
        class ComponenteAvaliacao(@XmlAttribute val nome: String, @XmlAttribute val peso: Int)

        val c = ComponenteAvaliacao("Quizzes", 20)
        assertEquals("<componente nome=\"Quizzes\" peso=\"20\"/>", createTag(c).prettyPrint)
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
        assertEquals(
            "<fuc code=\"M4310\">\n" + "\t<ects>6.0</ects>\n" + "\t<nome>Programação Avançada</nome>\n" + "\t<avaliacao>\n" + "\t\t<componente nome=\"Quizzes\" peso=\"20\"/>\n" + "\t\t<componente nome=\"Projeto\" peso=\"80\"/>\n" + "\t</avaliacao>\n" + "</fuc>",
            createTag(f).prettyPrint
        )
    }

    @Test
    fun createTagWithStringTransformation_asInnerClassAndNotInnerClass() {

        class UpperCase : StringModifier {
            override fun modify(o: Any?): String {
                return o.toString().uppercase()
            }
        }

        class ComponenteAvaliacao(
            @XmlString(UpperCase::class) @XmlAttribute val nome: String,
            @XmlString(AddPercentage::class) @XmlAttribute val peso: Int
        ) {
            inner class AddPercentage : StringModifier {
                override fun modify(o: Any?): String {
                    return o.toString() + "%"
                }
            }
        }

        val c = ComponenteAvaliacao("Quizzes", 20)

        assertEquals("<ComponenteAvaliacao nome=\"QUIZZES\" peso=\"20%\"/>", createTag(c).prettyPrint)
    }

    @Test
    fun createTagUsingAdapter() {

        class UpperCase : StringModifier {
            override fun modify(o: Any?): String {
                return o.toString().uppercase()
            }
        }

        @XmlName("componente")
        class ComponenteAvaliacao(
            @XmlString(UpperCase::class) @XmlAttribute val nome: String,
            @XmlString(AddPercentage::class) @XmlAttribute val peso: Int
        ) {
            inner class AddPercentage : StringModifier {
                override fun modify(o: Any?): String {
                    return o.toString() + "%"
                }
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
            @XmlAttribute val codigo: String,
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
        assertEquals(
            "<fuc codigo=\"M4310\">\n" +
                    "\t<nome>Programação Avançada</nome>\n" +
                    "\t<ects>6.0</ects>\n" +
                    "\t<componente nome=\"QUIZZES\" peso=\"20%\"/>\n" +
                    "\t<componente nome=\"PROJETO\" peso=\"80%\"/>\n" +
                    "</fuc>",
            createTag(f).prettyPrint
        )
    }
}
