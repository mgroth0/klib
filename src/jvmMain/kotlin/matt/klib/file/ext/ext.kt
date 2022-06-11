package matt.klib.file.ext

import matt.klib.commons.plus
import matt.klib.file.MFile
import matt.klib.file.toMFile
import matt.klib.str.lower
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path

@Suppress("DEPRECATION") fun createTempDir(prefix: String = "tmp", suffix: String? = null, directory: MFile? = null) =
  kotlin.io.createTempDir(prefix, suffix, directory).toMFile()


@Suppress("DEPRECATION") fun createTempFile(prefix: String = "tmp", suffix: String? = null, directory: MFile? = null) =
  kotlin.io.createTempFile(prefix, suffix, directory).toMFile()

fun MFile.relativeTo(base: MFile): MFile = userFile.relativeTo(base.userFile).toMFile()
fun MFile.relativeToOrSelf(base: MFile): MFile = userFile.relativeToOrSelf(base.userFile).toMFile()
fun MFile.relativeToOrNull(base: MFile): MFile? = userFile.relativeToOrNull(base.userFile)?.toMFile()
fun MFile.copyTo(target: MFile, overwrite: Boolean = false, bufferSize: Int = DEFAULT_BUFFER_SIZE): MFile =
  userFile.copyTo(target, overwrite, bufferSize).toMFile()

/*fun MFile.copyRecursively(
  target: MFile,
  overwrite: Boolean = false,
  onError: (MFile, IOException)->OnErrorAction = { _, exception -> throw exception }
): Boolean = userFile.copyRecursively(target.userFile, overwrite) { f, e ->
  onError(MFile(f), e)
}*/


fun MFile.startsWith(other: MFile): Boolean = idFile.startsWith(other.idFile)
fun MFile.startsWith(other: String): Boolean = idFile.startsWith(other.lower())
fun MFile.endsWith(other: MFile) = idFile.endsWith(other.idFile)
fun MFile.endsWith(other: String): Boolean = idFile.endsWith(other.lower())


fun MFile.resolve(relative: MFile): MFile = userFile.resolve(relative).toMFile()
fun MFile.resolve(relative: String): MFile = userFile.resolve(relative).toMFile()


fun MFile.resolveSibling(relative: MFile) = userFile.resolveSibling(relative).toMFile()


fun MFile.resolveSibling(relative: String): MFile = userFile.resolveSibling(relative).toMFile()

fun MFile.mkparents() = parentFile!!.mkdirs()

var MFile.text
  get() = readText()
  set(v) {
	mkparents()
	writeText(v)
  }

fun String.writeToFile(f: MFile, mkdirs: Boolean = true) {
  if (mkdirs) {
	f.parentFile!!.mkdirs()
  }
  f.writeText(this)
}


fun MFile.isBlank() = bufferedReader().run {
  val r = read() == -1
  close()
  r
}

fun MFile.isImage() = extension in listOf("png", "jpg", "jpeg")

fun MFile.append(s: String, mkdirs: Boolean = true) {
  if (mkdirs) mkparents()
  appendText(s)
}

fun MFile.write(s: String, mkparents: Boolean = true) {
  if (mkparents) mkparents()
  writeText(s)
}

@Suppress("unused")
val MFile.fname: String
  get() = name
val MFile.abspath: String
  get() = absolutePath


fun MFile.getNextAndClearWhenMoreThan(n: Int, extraExt: String = "itr"): MFile {
  val backupFolder = parentFile
  val allPreviousBackupsOfThis = backupFolder!!.listFiles()!!.filter {
	it.name.startsWith(this@getNextAndClearWhenMoreThan.name + ".${extraExt}")
  }.associateBy { it.name.substringAfterLast(".${extraExt}").toInt() }


  val myBackupI = (allPreviousBackupsOfThis.keys.maxOrNull() ?: 0) + 1


  allPreviousBackupsOfThis
	.filterKeys { it < (myBackupI - n) }
	.forEach { it.value.delete() }

  return backupFolder.resolve("${this.name}.${extraExt}${myBackupI}").toMFile()

}

fun MFile.resRepExt(newExt: String) =
  MFile(parentFile!!.absolutePath + MFile.separator + nameWithoutExtension + "." + newExt)

fun MFile.deleteIfExists() {
  if (exists()) {
	if (isDirectory) {
	  deleteRecursively()
	} else {
	  delete()
	}
  }
}


@Suppress("unused")
fun Iterable<MFile>.filterHasExtension(ext: String) = filter { it.extension == ext }

@Suppress("unused")
fun Sequence<MFile>.filterHasExtension(ext: String) = filter { it.extension == ext }


@Suppress("unused")
fun Path.startsWithAny(atLeastOne: Path, vararg more: Path): Boolean {
  if (startsWith(atLeastOne)) return true
  more.forEach { if (startsWith(it)) return true }
  return false
}

fun Path.startsWithAny(atLeastOne: MFile, vararg more: MFile): Boolean {
  if (startsWith(atLeastOne.toPath())) return true
  more.forEach { if (startsWith(it.toPath())) return true }
  return false
}

fun String.toPath(): Path = FileSystems.getDefault().getPath(this.trim())
val MFile.doesNotExist get() = !exists()


infix fun MFile.withExtension(ext: String): MFile {
  return when (this.extension) {
	ext  -> this
	""   -> MFile(this.path + "." + ext)
	else -> MFile(this.path.replace("." + this.extension, ".$ext"))
  }
}

fun MFile.appendln(line: String) {
  append(line + "\n")
}

val MFile.unixNlink get() = Files.getAttribute(this.toPath(), "unix:nlink").toString().toInt()
val MFile.hardLinkCount get() = unixNlink