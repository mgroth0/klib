package matt.klib.reflect

import matt.file.MFile
import matt.file.recursiveChildren
import matt.klib.lang.inlined



fun jumpToKotlinSourceString(
  rootProject: MFile,
  s: String,
  packageFilter: String?
): Pair<MFile, Int>? {
  println("matt.kjlib.jumpToKotlinSourceString:${s}:${packageFilter}")
  val packFolder = packageFilter?.replace(".", "/")
  var pair: Pair<MFile, Int>? = null
  inlined {
	rootProject["settings.gradle.kts"]
	  .readLines()
	  .asSequence()
	  .filterNot { it.isBlank() }
	  .map { it.trim() }
	  .filterNot { it.startsWith("//") }
	  .map { it.replace("include(\"", "").replace("\")", "") }
	  .map { it.replace(":", "/") }
	  .map { rootProject[it]["src"] }
	  .toList().forEach search@{ src ->
		println("searching source folder: $src")
		src.recursiveChildren()
		  .filter {
			(packageFilter == null || packFolder!! in it.absolutePath)
		  }
		  .filter { maybekt ->
			maybekt.extension == "kt"
		  }
		  .forEach kt@{ kt ->
			print("searching ${kt}... ")
			var linenum = 0 // I guess ide_open uses indices??
			kt.bufferedReader().lines().use { lines ->
			  for (line in lines) {
				if (s in line) {
				  println("found!")

				  pair = kt to linenum
				  return@inlined
				}
				linenum += 1

			  }
			}
			println("not here.")
		  }
	  }
  }
  println("matt.kjlib.jumpToKotlinSourceString: dur:${System.currentTimeMillis()}ms worked?: ${pair != null}")
  return pair
}