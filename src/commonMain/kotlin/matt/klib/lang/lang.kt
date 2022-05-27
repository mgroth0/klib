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


/*kinda how JetBrains wants us to do it*/
fun String.cap() =
  replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
/*if I go back to 1.4: this.capitalize()*/


/*kinda how JetBrains wants us to do it*/
fun String.decap() =
  replaceFirstChar { it.lowercase() }
/*if I go back to 1.4: this.decapitalize()*/


val Any.void get() = Unit