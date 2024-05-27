import org.junit.jupiter.api.Test

class ClassXMLMappingTests {

    @Test
    fun createTagTest() {
        @XMLName("componente")
        class ComponenteAvaliacao(val nome: String, val peso: Int)
        val c = ComponenteAvaliacao("Quizzes", 20)
        print(createTag(c).prettyPrint)
    }


}