sealed interface ModType
sealed interface JvmMod: ModType
sealed interface JsMod: ModType {
  val client: Boolean
}
sealed interface JsLibOnly: JsMod {
  override val client get() = false
}
sealed interface JsClient: JsMod {
  override val client get() = true
}
sealed interface NativeMod: ModType

object APP: ModType, JvmMod
object CLAPP: ModType, JvmMod
object APPLIB: ModType, JvmMod
object LIB: ModType, JvmMod
object ABSTRACT: ModType

object JS_LIB: JsLibOnly
object JS_CLIENT: JsClient

sealed interface MultiPlatformMod: ModType /*has common*/

object ALL: MultiPlatformMod, JvmMod, JsLibOnly, NativeMod
object JVM_ONLY: MultiPlatformMod, JvmMod
object JS_ONLY: MultiPlatformMod, JsLibOnly
object NATIVE_ONLY: MultiPlatformMod, NativeMod
object NO_NATIVE: MultiPlatformMod, JvmMod, JsLibOnly
object NO_JS: MultiPlatformMod, JvmMod, NativeMod
object NO_JVM: MultiPlatformMod, JsLibOnly, NativeMod

