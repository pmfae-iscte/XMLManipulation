import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ClassXMLMappingTests {

    @Test
    fun `check if cant have both text and tags and if can't have more than one text`() {
        class TestClass1(@XMLText val text: String, @XMLTag val tag: String)
        assertEquals(
            "Class given has both tags and text",
            assertThrows<IllegalArgumentException> { createTag(TestClass1("", "")) }.message
        )
        class TestClass2(@XMLText val text1: String, @XMLText val text2: String)
        assertEquals(
            "Class given has more than one text",
            assertThrows<IllegalArgumentException> { createTag(TestClass2("", "")) }.message
        )
    }

    @Test
    fun createTagTest() {
        @XMLName("componente")
        class ComponenteAvaliacao(@XMLAttribute val nome: String, @XMLAttribute val peso: Int)

        val c = ComponenteAvaliacao("Quizzes", 20)

        //        println(createTag(c).prettyPrint)
        @XMLName("fuc")
        class FUC(
            @XMLName("code")
            @XMLAttribute
            val codigo: String,
            @XMLTextTag
            val nome: String,
            @XMLTextTag
            val ects: Double,
            val observacoes: String,
            @XMLTagList
            val avaliacao: List<ComponenteAvaliacao>
        )

        val f = FUC(
            "M4310", "Programação Avançada", 6.0, "la la...",
            listOf(
                ComponenteAvaliacao("Quizzes", 20),
                ComponenteAvaliacao("Projeto", 80)
            )
        )
        println(createTag(f).prettyPrint)
    }


}
