

interface StringModifier { //as of now cannot be inner class!!
    fun modify(o: Any?): String = o.toString()
}

interface Adapter {
    fun adapt(t: Tag): Tag
}