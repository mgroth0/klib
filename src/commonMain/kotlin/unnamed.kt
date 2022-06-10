sealed interface ModType
object APP: ModType
object CLAPP: ModType
object APPLIB: ModType
object LIB: ModType
object ABSTRACT: ModType

sealed interface MultiPlatformMod: ModType /*has common*/
sealed interface JvmMod: ModType
sealed interface JsMod: ModType
sealed interface NativeMod: ModType

object ALL: MultiPlatformMod, JvmMod, JsMod, NativeMod
object JVM_ONLY: MultiPlatformMod, JvmMod
object JS_ONLY: MultiPlatformMod, JsMod
object NATIVE_ONLY: MultiPlatformMod, NativeMod
object NO_NATIVE: MultiPlatformMod, JvmMod, JsMod
object NO_JS: MultiPlatformMod, JvmMod, NativeMod
object NO_JVM: MultiPlatformMod, JsMod, NativeMod