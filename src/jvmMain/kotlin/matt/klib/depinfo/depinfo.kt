package matt.klib.depinfo

import kotlinx.serialization.Serializable

@Serializable data class DepInfo(
  val subprojects: MutableList<SubProject> = mutableListOf()
)

@Serializable data class SubProject(
  val folderAbsPath: String,
  val configurations: MutableList<Configuration> = mutableListOf()
)

@Serializable data class Configuration(
  val name: String,
  val dependencies: MutableList<Dependency> = mutableListOf()
)

@Serializable
sealed interface Dependency {
  val group: String
  val name: String
  val configurations: MutableList<Configuration>
}

@Serializable data class LibDependency(
  override val group: String,
  override val name: String,
): Dependency {
  override val configurations: MutableList<Configuration> = mutableListOf()
}

@Serializable data class ProjectDependency(
  override val group: String,
  override val name: String,
  val projectDirAbsPath: String
): Dependency {
  override val configurations: MutableList<Configuration> = mutableListOf()
}