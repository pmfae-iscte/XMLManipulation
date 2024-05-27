interface Visitor {
    fun visit(t: Tag): Boolean = true
    fun endVisit(t: Tag) {}
    fun visit(a: Attribute) {}
}