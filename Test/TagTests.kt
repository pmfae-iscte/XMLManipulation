import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TagTests {
    @Test
    fun testAttributeToString() {
        assertEquals("at1=\"nice\"", Attribute("at1", "nice").toString())
    }

    @Test
    fun testAddAttribute() {//TODO pode ser tudo list of attributes nos testes em vez de dar jointostring...................
        val tag = Tag("e1")
        tag.addAttribute(Attribute("at1", "nice"))
        assertEquals("at1=\"nice\"", tag.attributes.joinToString(" "))
        assertEquals(
            "Attribute \"at1\" already exists",
            assertThrows<IllegalStateException> { tag.addAttribute(Attribute("at1", "nicer")) }.message
        )
    }

    @Test
    fun testChangeAttribute() {
        val tag = Tag("e1")
        tag.addAttribute(Attribute("at1", "nice"))

        assertEquals(
            "No such attribute \"at2\" found",
            assertThrows<NoSuchElementException> {
                tag.setAttribute(
                    Attribute("at2", "nice"),
                    Attribute("at2", "nicer")
                )
            }.message
        )
        tag.setAttribute(Attribute("at1", "nice"), Attribute("at1", "nice2"))
        assertEquals("at1=\"nice2\"", tag.attributes.joinToString(" "))

        assertEquals(
            "No such attribute named \"at2\" found",
            assertThrows<NoSuchElementException> { tag.setAttribute("at2", "niceres") }.message
        )
        tag.setAttribute("at1", "nice3")
        assertEquals("at1=\"nice3\"", tag.attributes.joinToString(" "))

        assertEquals(
            "No such attribute named \"at2\" found",
            assertThrows<NoSuchElementException> { tag.setAttribute("at2", "at1", "niceres") }.message
        )
        tag.setAttribute("at1", "at2", "nice4")
        assertEquals("at2=\"nice4\"", tag.attributes.joinToString(" "))

    }

    @Test
    fun testRemoveAttribute() {
        val tag = Tag("e1")
        val attribute = Attribute("at1", "nice")
        tag.addAttribute(attribute)
        assertEquals("at1=\"nice\"", tag.attributes.joinToString(" "))
        tag.removeAttribute(attribute)
        assertEquals("", tag.attributes.joinToString(" "))

        tag.addAttribute(Attribute("at1", "nice"))
        assertEquals("at1=\"nice\"", tag.attributes.joinToString(" "))
        tag.removeAttribute("at1")
        assertEquals("", tag.attributes.joinToString(" "))
    }

    @Test
    fun testPrettyPrint() {
        val tag = Tag("e1")
        val tag2 = Tag("e2")
        tag.addTag(tag2)
        assertEquals(
            "<e1>\n" +
                    "\t<e2/>\n" +
                    "</e1>", tag.prettyPrint
        )
        val tag3 = Tag("e3", "is this text?")
        tag.addTag(tag3)
        assertEquals(
            "<e1>\n" +
                    "\t<e2/>\n" +
                    "\t<e3>is this text?</e3>\n" +
                    "</e1>", tag.prettyPrint
        )
    }

    @Test
    fun testAddTag() {
        val tag = Tag("e1")
        val tag2 = Tag("e2")
        tag.addTag(tag2)
        assertEquals(
            "<e1>\n" +
                    "\t<e2/>\n" +
                    "</e1>", tag.prettyPrint
        )
        assertEquals(null, tag.getParent())
        assertEquals(tag, tag2.getParent())

    }

    @Test
    fun testRemoveTag() {
        val tag = Tag("e1")
        val tag2 = Tag("e2")
        val tag2text = Tag("e2", "text")
        tag.addTag(tag2)

        assertEquals(
            "The tag is a text tag, so it can't have children tags",
            assertThrows<IllegalStateException> { tag2text.addTag(Tag("wtv")) }.message
        )

        assertEquals(
            "<e1>\n" +
                    "\t<e2/>\n" +
                    "</e1>", tag.prettyPrint
        )
        tag.removeTag(tag2)
        assertEquals("<e1/>", tag.prettyPrint)

        tag.addTag(tag2)

        tag.addTag(tag2text)
        assertEquals("<e1>\n" + "\t<e2/>\n" + "\t<e2>text</e2>\n" + "</e1>", tag.prettyPrint)
        tag.removeTag("e2")
        assertEquals("<e1>\n" + "\t<e2>text</e2>\n" + "</e1>", tag.prettyPrint)

        tag.addTag(tag2)
        assertEquals("<e1>\n" + "\t<e2>text</e2>\n" + "\t<e2/>\n" + "</e1>", tag.prettyPrint)
        tag.removeLastTag("e2")
        assertEquals(
            "<e1>\n" +
                    "\t<e2>text</e2>\n" +
                    "</e1>", tag.prettyPrint
        )

        tag.addTag(tag2)
        assertEquals("<e1>\n" + "\t<e2>text</e2>\n" + "\t<e2/>\n" + "</e1>", tag.prettyPrint)
        tag.removeTag("e2", 2)
        assertEquals(
            "<e1>\n" +
                    "\t<e2>text</e2>\n" +
                    "</e1>", tag.prettyPrint
        )
    }
}