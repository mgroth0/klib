package matt.klib.log

import java.io.ByteArrayOutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.io.PrintStream
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

fun mAssert(b: Boolean) {
  if (!b) {
	throw RuntimeException("Bad!")
  }
}

fun massert(b: Boolean) = mAssert(b)


@Suppress("unused")
fun log(s: String?) = println(s)


fun <T> logInvokation(vararg withstuff: Any, f: ()->T): T {
  val withstr = if (withstuff.isEmpty()) "" else " with $withstuff"
  println("running $f $withstr")
  val rrr = f()
  println("finished running $f")
  return rrr
}

interface Prints {
  fun println(a: Any)
  fun print(a: Any)
}

class Printer(private val pw: PrintWriter): Prints {
  override fun println(a: Any) = pw.println(a)
  override fun print(a: Any) = pw.print(a)
}


fun Exception.printStackTraceToString(): String {
  val baos = ByteArrayOutputStream()
  val utf8: String = StandardCharsets.UTF_8.name()
  printStackTrace(PrintStream(baos, true, utf8))
  val data = baos.toString(utf8)
  return data
}


fun redirectOut(duplicate: Boolean = true, op: (String)->Unit) {
  val old = System.out
  val re = if (duplicate) redirect2Core {
    op(it)
    old.println(it)
  } else redirect2Core(op)
  System.setOut(re)
}

fun redirectErr(duplicate: Boolean = true, op: (String)->Unit) {
  val old = System.err
  val re = if (duplicate) redirect2Core {
    op(it)
    old.println(it)
  } else redirect2Core(op)
  System.setErr(re)
}



fun redirect2Core(op: (String)->Unit): PrintStream {
  return PrintStream(object: ByteArrayOutputStream() {
    override fun flush() {
      val message = toString()
      if (message.isEmpty()) return
      op(message)
      reset()
    }
  }, true)
}


fun pipedPrintStream(): Pair<PrintStream, PipedInputStream> {
  val PIPE_BUFFER = 2048

  //     -> console
  val inPipe = PipedInputStream(PIPE_BUFFER)
  val outPipe = PipedOutputStream(inPipe)
  val ps = PrintStream(outPipe, true)
  //       <- stdout

  return ps to inPipe
}