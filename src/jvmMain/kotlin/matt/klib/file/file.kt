package matt.klib.file

import matt.klib.str.lower
import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter
import java.net.URI


fun File.toMFile() = MFile(this)

/*mac file, matt file, whatever*/
/*sadly this is necessary. Java.io.file is an absolute failure because it doesn't respect Mac os's case sensitivity rules
  I'm actually shocked it took me so long to figure this out*/


//class FixedFile(s: String): MFile(s) {
//  init {
//	MFile.separatorChar
//  }
//}


/*TODO: SUBCLASS IS PROBABLAMATIC BEACUASE OF THE BUILTIN KOTLIN `RESOLVES` FUNCTION (can I disable or override it? maybe in unnamed package?) WHICH SECRETLY TURNS THIS BACK INTO A REGULAR FILE*/
/*TODO:  NOT SUBCLASS FILE IS PROBLEMATIC BECAUSE I NEED TONS OF BOILERPLATE SINCE THE FILE CLASS HAS SO MANY METHODS, EXTENSION METHODS, CLASSES, AND LIBRARIES IT WORKS WITH*/
class MFile(val userPath: String): File(userPath) {
  val userFile = File(this.path)

  constructor(file: File): this(file.path)
  constructor(parent: String, child: String): this(File(parent, child))
  constructor(parent: MFile, child: String): this(parent.path, child)
  constructor(uri: URI): this(File(uri))

  companion object {
	val separatorChar = File.separatorChar
	val separator: String = File.separator
	val pathSeparatorChar = File.pathSeparatorChar
	val pathSeparator = File.pathSeparator

	fun listRoots() = File.listRoots().map { MFile(it) }.toTypedArray()
	fun createTempFile(prefix: String, suffix: String?, directory: MFile?) =
	  MFile(File.createTempFile(prefix, suffix, directory))

	fun createTempFile(prefix: String, suffix: String?) = MFile(File.createTempFile(prefix, suffix))
  }

  override fun getParentFile(): MFile? {
	return super.getParentFile()?.toMFile()
  }

  override fun getAbsoluteFile(): MFile {
	return super.getAbsoluteFile().toMFile()
  }

  override fun listFiles(): Array<MFile>? {
	return super.listFiles()?.map { it.toMFile() }?.toTypedArray()
  }

  override fun listFiles(fiilenameFilter: FilenameFilter?): Array<MFile>? {
	return super.listFiles(fiilenameFilter)?.map { it.toMFile() }?.toTypedArray()
  }

  override fun listFiles(fileFilter: FileFilter?): Array<MFile>? {
	return super.listFiles(fileFilter)?.map { it.toMFile() }?.toTypedArray()
  }

  /*must remain lower since in ext.kt i look here for matching with a astring*/
  internal val idFile = File(userPath.lower())

  override operator fun compareTo(other: File?): Int = idFile.compareTo((other as MFile).idFile)
  override fun equals(other: Any?): Boolean {
	return if (other is File) {
	  require(other is MFile)
	  idFile == other.idFile
	} else false
  }

  override fun hashCode() = idFile.hashCode()
}
//
///*I guess its not technically neccesary. But java.if.File is a really bad prefix and "java.io.FileOutputStream" will get get found and I will get yelled at by my validations. Its also a good general reminder that anything having to do with the java filesystem has this huge issue*/
//@Suppress("unused", "DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
//class MFileOutputStream(private val fOut: FileOutputStream): java.io.OutputStream(), Closeable by fOut,
//															 Flushable by fOut {
//  override fun write(b: Int) {
//	fOut.write(b)
//  }
//}
//














