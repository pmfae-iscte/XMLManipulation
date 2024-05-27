import org.junit.jupiter.api.Test

class ClassXMLMappingTests {

    @Test
    fun createTagTest() {
        @XMLName("componente")
        class ComponenteAvaliacao(val nome: String, val peso: Int)
        val c = ComponenteAvaliacao("Quizzes", 20)
        println(createTag(c).prettyPrint)
        class FUC(
            val codigo: String,
            val nome: String,
            val ects: Double,
            val observacoes: String,
            val avaliacao: List<ComponenteAvaliacao>
        )
        val f = FUC("M4310", "Programação Avançada", 6.0, "la la...",
            listOf(
                ComponenteAvaliacao("Quizzes", 20),
                ComponenteAvaliacao("Projeto", 80)
            )
        )
        println(createTag(f).prettyPrint)
    }


}