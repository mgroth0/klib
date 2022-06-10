sealed interface ModType
sealed interface JvmMod: ModType
sealed interface JsMod: ModType
sealed interface NativeMod: ModType

object APP: ModType, JvmMod
object CLAPP: ModType, JvmMod
object APPLIB: ModType, JvmMod
object LIB: ModType, JvmMod
object ABSTRACT: ModType

object JS: JsMod

sealed interface MultiPlatformMod: ModType /*has common*/

object ALL: MultiPlatformMod, JvmMod, JsMod, NativeMod
object JVM_ONLY: MultiPlatformMod, JvmMod
object JS_ONLY: MultiPlatformMod, JsMod
object NATIVE_ONLY: MultiPlatformMod, NativeMod
object NO_NATIVE: MultiPlatformMod, JvmMod, JsMod
object NO_JS: MultiPlatformMod, JvmMod, NativeMod
object NO_JVM: MultiPlatformMod, JsMod, NativeMod

