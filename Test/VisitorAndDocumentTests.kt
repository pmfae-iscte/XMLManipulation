import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VisitorAndDocumentTests {

    private val plano = Tag("plano")
    private val componente11: Tag
    private val componente12: Tag
    private val componente21: Tag
    private val componente22: Tag
    private val componente23: Tag
    private val doc = Document(plano)

    init {
        Tag("curso", "Mestrado em Engenharia Informática", plano)
        val fuc1 = Tag("fuc", plano)
        fuc1.addAttribute(Attribute("codigo", "M4310"))
        Tag("nome", "Programação Avançada", fuc1)
        Tag("ects", "6.0", fuc1)
        val avaliacao1 = Tag("avaliacao", fuc1)
        componente11 = Tag(
            "componente",
            parent = avaliacao1,
            attributesList = mutableListOf(Attribute("nome", "Quizzes"), Attribute("peso", "20%")),
        )
        componente12 = Tag(
            "componente",
            parent = avaliacao1,
            attributesList = mutableListOf(Attribute("nome", "Projeto"), Attribute("peso", "80%"))
        )
        val fuc2 = Tag("fuc", plano)
        fuc2.addAttribute(Attribute("codigo", "03782"))
        Tag("nome", "Dissertação", fuc2)
        Tag("ects", "42.0", fuc2)
        val avaliacao2 = Tag("avaliacao", fuc2)
        componente21 = Tag(
            "componente", parent = avaliacao2,
            attributesList = mutableListOf(Attribute("nome", "Dissertação"), Attribute("peso", "60%"))
        )
        componente22 = Tag(
            "componente", parent = avaliacao2,
            attributesList = mutableListOf(Attribute("nome", "Apresentação"), Attribute("peso", "20%"))
        )
        componente23 = Tag(
            "componente", parent = avaliacao2,
            attributesList = mutableListOf(Attribute("nome", "Discussão"), Attribute("peso", "20%"))
        )


    }

    @Test
    fun visitorCountTest() {
        val tag = Tag("e1")
        val tag2 = Tag("e2")
        val tag3 = Tag("e3")
        val tag4 = Tag("e4", "is this text?")
        tag.addTag(tag2)
        tag.addTag(tag3)
        tag3.addTag(tag4)

        val countTags = object : Visitor {
            var count = 0

            override fun visit(t: Tag): Boolean {
                count++
                return true
            }
        }
        tag.accept(countTags)
        assertEquals(4, countTags.count)

        val countAttribute = object : Visitor {
            var count = 0

            override fun visit(a: Attribute) {
                count++
            }
        }
        tag.accept(countAttribute)
        assertEquals(0, countAttribute.count)
        tag2.addAttribute(Attribute("at1", "test"))
        tag4.addAttribute(Attribute("at2", "test3"))

        tag.accept(countAttribute)
        assertEquals(2, countAttribute.count)
    }

    @Test
    fun testAddAttribute() {
        val tag1 = Tag("e1")
        val tag2 = Tag("e2")
        val tag3 = Tag("e3")
        val tag4 = Tag("e4")
        val tag5 = Tag("e4")
        val tag6 = Tag("e4")
        tag1.addTag(tag2)
        tag1.addTag(tag3)
        tag2.addTag(tag6)
        tag3.addTag(tag4)
        tag3.addTag(tag5)
        tag6.addAttribute(Attribute("new", "test"))
        val doc = Document(tag1, "2.0")
        doc.addAttribute("e4", "attritest", "true")
        assertEquals(
            "<?xml version=\"2.0\" encoding=\"UTF-8\"?>\n" +
                    "<e1>\n" +
                    "\t<e2>\n" +
                    "\t\t<e4 new=\"test\" attritest=\"true\"/>\n" +
                    "\t</e2>\n" +
                    "\t<e3>\n" +
                    "\t\t<e4 attritest=\"true\"/>\n" +
                    "\t\t<e4 attritest=\"true\"/>\n" +
                    "\t</e3>\n" +
                    "</e1>", doc.getXML()
        )
    }

    @Test
    fun testRenameTag() {
        doc.renameTag("curso", "course")
        doc.renameTag("componente", "component")
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<plano>\n" +
                    "\t<course>Mestrado em Engenharia Informática</course>\n" +
                    "\t<fuc codigo=\"M4310\">\n" +
                    "\t\t<nome>Programação Avançada</nome>\n" +
                    "\t\t<ects>6.0</ects>\n" +
                    "\t\t<avaliacao>\n" +
                    "\t\t\t<component nome=\"Quizzes\" peso=\"20%\"/>\n" +
                    "\t\t\t<component nome=\"Projeto\" peso=\"80%\"/>\n" +
                    "\t\t</avaliacao>\n" +
                    "\t</fuc>\n" +
                    "\t<fuc codigo=\"03782\">\n" +
                    "\t\t<nome>Dissertação</nome>\n" +
                    "\t\t<ects>42.0</ects>\n" +
                    "\t\t<avaliacao>\n" +
                    "\t\t\t<component nome=\"Dissertação\" peso=\"60%\"/>\n" +
                    "\t\t\t<component nome=\"Apresentação\" peso=\"20%\"/>\n" +
                    "\t\t\t<component nome=\"Discussão\" peso=\"20%\"/>\n" +
                    "\t\t</avaliacao>\n" +
                    "\t</fuc>\n" +
                    "</plano>", doc.getXML()
        )
    }

    @Test
    fun testRenameAttribute() {
        doc.renameAttribute("componente", "nome", "name")
        doc.renameAttribute("fuc", "codigo", "ID")
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<plano>\n" +
                    "\t<curso>Mestrado em Engenharia Informática</curso>\n" +
                    "\t<fuc ID=\"M4310\">\n" +
                    "\t\t<nome>Programação Avançada</nome>\n" +
                    "\t\t<ects>6.0</ects>\n" +
                    "\t\t<avaliacao>\n" +
                    "\t\t\t<componente name=\"Quizzes\" peso=\"20%\"/>\n" +
                    "\t\t\t<componente name=\"Projeto\" peso=\"80%\"/>\n" +
                    "\t\t</avaliacao>\n" +
                    "\t</fuc>\n" +
                    "\t<fuc ID=\"03782\">\n" +
                    "\t\t<nome>Dissertação</nome>\n" +
                    "\t\t<ects>42.0</ects>\n" +
                    "\t\t<avaliacao>\n" +
                    "\t\t\t<componente name=\"Dissertação\" peso=\"60%\"/>\n" +
                    "\t\t\t<componente name=\"Apresentação\" peso=\"20%\"/>\n" +
                    "\t\t\t<componente name=\"Discussão\" peso=\"20%\"/>\n" +
                    "\t\t</avaliacao>\n" +
                    "\t</fuc>\n" +
                    "</plano>", doc.getXML()
        )
    }

    @Test
    fun testRemoveTag() {
        doc.removeTag("taginexistente")
        doc.removeTag("componente")
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<plano>\n" +
                    "\t<curso>Mestrado em Engenharia Informática</curso>\n" +
                    "\t<fuc codigo=\"M4310\">\n" +
                    "\t\t<nome>Programação Avançada</nome>\n" +
                    "\t\t<ects>6.0</ects>\n" +
                    "\t\t<avaliacao/>\n" +
                    "\t</fuc>\n" +
                    "\t<fuc codigo=\"03782\">\n" +
                    "\t\t<nome>Dissertação</nome>\n" +
                    "\t\t<ects>42.0</ects>\n" +
                    "\t\t<avaliacao/>\n" +
                    "\t</fuc>\n" +
                    "</plano>", doc.getXML()
        )
        doc.removeTag("fuc")
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<plano>\n" +
                    "\t<curso>Mestrado em Engenharia Informática</curso>\n" +
                    "</plano>", doc.getXML()
        )
    }

    @Test
    fun testRemoveAttribute() {
        doc.removeAttribute("componente", "nada")
        doc.removeAttribute("componente", "")
        doc.removeAttribute("taginexistente", "d")
        doc.removeAttribute("fuc", "codigo")
        doc.removeAttribute("componente", "peso")
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<plano>\n" +
                    "\t<curso>Mestrado em Engenharia Informática</curso>\n" +
                    "\t<fuc>\n" +
                    "\t\t<nome>Programação Avançada</nome>\n" +
                    "\t\t<ects>6.0</ects>\n" +
                    "\t\t<avaliacao>\n" +
                    "\t\t\t<componente nome=\"Quizzes\"/>\n" +
                    "\t\t\t<componente nome=\"Projeto\"/>\n" +
                    "\t\t</avaliacao>\n" +
                    "\t</fuc>\n" +
                    "\t<fuc>\n" +
                    "\t\t<nome>Dissertação</nome>\n" +
                    "\t\t<ects>42.0</ects>\n" +
                    "\t\t<avaliacao>\n" +
                    "\t\t\t<componente nome=\"Dissertação\"/>\n" +
                    "\t\t\t<componente nome=\"Apresentação\"/>\n" +
                    "\t\t\t<componente nome=\"Discussão\"/>\n" +
                    "\t\t</avaliacao>\n" +
                    "\t</fuc>\n" +
                    "</plano>", doc.getXML()
        )
    }

    @Test
    fun testMicroXPath() {
        assertEquals(
            listOf(componente11, componente12, componente21, componente22, componente23),
            doc.getXMLFragment("fuc/avaliacao/componente")
        )
        println("Finished")
    }

}