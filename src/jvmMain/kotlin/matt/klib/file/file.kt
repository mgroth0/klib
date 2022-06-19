package matt.klib.file

import matt.klib.str.lower
import matt.klib.stream.search
import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter
import java.net.URI
import java.nio.file.Files


fun File.toMFile() = MFile(this)

/*mac file, matt file, whatever*/
/*sadly this is necessary. Java.io.file is an absolute failure because it doesn't respect Mac OSX's case sensitivity rules
  I'm actually shocked it took me so long to figure this out*/

/*TODO: SUBCLASS IS PROBABLAMATIC BEACUASE OF THE BUILTIN KOTLIN `RESOLVES` FUNCTION (can I disable or override it? maybe in unnamed package?) WHICH SECRETLY TURNS THIS BACK INTO A REGULAR FILE*/
/*TODO:  NOT SUBCLASSING JAVA.FILE IS PROBLEMATIC BECAUSE I NEED TONS OF BOILERPLATE SINCE THE FILE CLASS HAS SO MANY METHODS, EXTENSION METHODS, CLASSES, AND LIBRARIES IT WORKS WITH*/
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


  operator fun contains(other: MFile): Boolean {
	return other != this && other.search({
	  takeIf {
		it == this@MFile
	  }
	}, { parentFile?.toMFile() }) != null
  }



  /*MUST KEEP THESE METHODS HERE AND NOT AS EXTENSIONS IN ORDER TO ROBUSTLY OVERRIDE KOTLIN.STDLIB'S DEFAULT FILE EXTENSIONS. OTHERWISE, I'D HAVE TO MICROMANAGE MY IMPORTS TO MAKE SURE I'M IMPORTING THE CORRECT EXTENSIONS*/


  fun relativeTo(base: MFile): MFile = idFile.relativeTo(base.idFile).toMFile()




  fun startsWith(other: MFile): Boolean = idFile.startsWith(other.idFile)
  fun startsWith(other: String): Boolean = idFile.startsWith(other.lower())
  fun endsWith(other: MFile) = idFile.endsWith(other.idFile)
  fun endsWith(other: String): Boolean = idFile.endsWith(other.lower())


  fun resolve(relative: MFile): MFile = userFile.resolve(relative).toMFile()
  fun resolve(relative: String): MFile = userFile.resolve(relative).toMFile()


  fun resolveSibling(relative: MFile) = userFile.resolveSibling(relative).toMFile()


  fun resolveSibling(relative: String): MFile = userFile.resolveSibling(relative).toMFile()

  fun mkparents() = parentFile!!.mkdirs()



  var text
	get() = readText()
	set(v) {
	  mkparents()
	  writeText(v)
	}


  fun isBlank() = bufferedReader().run {
	val r = read() == -1
	close()
	r
  }

  fun isImage() = extension in listOf("png", "jpg", "jpeg")

  fun append(s: String, mkdirs: Boolean = true) {
	if (mkdirs) mkparents()
	appendText(s)
  }

  fun write(s: String, mkparents: Boolean = true) {
	if (mkparents) mkparents()
	writeText(s)
  }

  @Suppress("unused")
  val fname: String
	get() = name
  val abspath: String
	get() = absolutePath


  fun getNextAndClearWhenMoreThan(n: Int, extraExt: String = "itr"): MFile {
	val backupFolder = parentFile
	val allPreviousBackupsOfThis = backupFolder!!.listFiles()!!.filter {
	  it.name.startsWith(this@MFile.name + ".${extraExt}")
	}.associateBy { it.name.substringAfterLast(".${extraExt}").toInt() }


	val myBackupI = (allPreviousBackupsOfThis.keys.maxOrNull() ?: 0) + 1


	allPreviousBackupsOfThis
	  .filterKeys { it < (myBackupI - n) }
	  .forEach { it.value.delete() }

	return backupFolder.resolve("${this.name}.${extraExt}${myBackupI}").toMFile()

  }

  fun resRepExt(newExt: String) =
	MFile(parentFile!!.absolutePath + separator + nameWithoutExtension + "." + newExt)

  fun deleteIfExists() {
	if (exists()) {
	  if (isDirectory) {
		deleteRecursively()
	  } else {
		delete()
	  }
	}
  }


  val doesNotExist get() = !exists()


  infix fun withExtension(ext: String): MFile {
	return when (this.extension) {
	  ext  -> this
	  ""   -> MFile(this.path + "." + ext)
	  else -> MFile(this.path.replace("." + this.extension, ".$ext"))
	}
  }

  fun appendln(line: String) {
	append(line + "\n")
  }

  val unixNlink get() = Files.getAttribute(this.toPath(), "unix:nlink").toString().toInt()
  val hardLinkCount get() = unixNlink


}








