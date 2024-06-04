
/**
 * Interface that utilizes the `Visitor Pattern` allowing to Iterate over the [Tag]'s data structure.
 */
interface Visitor {
    fun visit(t: Tag): Boolean = true
    fun endVisit(t: Tag) {}
    fun visit(a: Attribute) {}
}