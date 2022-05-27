package matt.klib.depinfo

import kotlinx.serialization.Serializable

@Serializable data class DepInfo(
  val subprojects: MutableList<SubProject> = mutableListOf()
)

@Serializable data class SubProject(
  val folder: String,
  val configurations: MutableList<Configuration> = mutableListOf()
)

@Serializable data class Configuration(
  val name: String,
  val dependencies: MutableList<Dependency> = mutableListOf()
)

@Serializable data class Dependency(
  val group: String,
  val name: String,
//  val file: String,
  val configurations: MutableList<Configuration> = mutableListOf()
)