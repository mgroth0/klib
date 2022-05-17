package matt.klib.lang

infix fun Boolean.ifTrue(op: () -> Unit) {
    if (this) op()
}


infix fun <R> Boolean.ifTrueOrNull(op: () -> R): R? {
   return  if (this) op() else null
}

infix fun Boolean.ifFalse(op: () -> Unit) {
    if (!this) op()
}


fun <T : Any> T.inList(): List<T> {
    return listOf(this)
}

fun inlined(op: () -> Unit) {
//    Pleasework()
    op()
}
