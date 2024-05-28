import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ClassXMLMappingTests {

    @Test
    fun check_if_cant_have_both_text_and_tags_and_if_cant_have_more_than_one_text(){
        class TestClass1(@XMLTextTag val text:String, @XMLTag val tag: String)
        assertEquals(
            "Class given has both tags and text",
        assertThrows<IllegalArgumentException> { TestClass1::class.createTag(TestClass1("","")) }.message)
        class TestClass2(@XMLTextTag val text1:String, @XMLTextTag val text2: String)
        assertEquals(
            "Class given has more than one text",
            assertThrows<IllegalArgumentException> { TestClass2::class.createTag(TestClass2("","")) }.message)
    }

    @Test
    fun createTagTest() {
        @XMLName("componente")
        class ComponenteAvaliacao(val nome: String, val peso: Int)
        val c = ComponenteAvaliacao("Quizzes", 20)
        println(ComponenteAvaliacao::class.createTag(c).prettyPrint)
        class FUC(
            val codigo: String,
            @XMLTextTag
            val nome: String,
            @XMLTextTag
            val ects: Double,
            @XMLExclude
            val observacoes: String,
            @XMLTag
            val avaliacao: List<ComponenteAvaliacao>
        )
        val f = FUC("M4310", "Programação Avançada", 6.0, "la la...",
            listOf(
                ComponenteAvaliacao("Quizzes", 20),
                ComponenteAvaliacao("Projeto", 80)
            )
        )
        println(FUC::class.createTag(f).prettyPrint)
    }


}