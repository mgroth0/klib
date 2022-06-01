package matt.klib.file.ext

import matt.klib.file.MFile
import matt.klib.file.toMFile
import matt.klib.str.lower
import java.io.IOException

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




