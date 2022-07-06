import kotlinx.serialization.Serializable

@Serializable
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

sealed interface NativeMain: NativeMod

sealed interface NativeLib: NativeMod

@Serializable
object APP: ModType, JvmMod

@Serializable
object CLAPP: ModType, JvmMod

@Serializable
object APPLIB: ModType, JvmMod

@Serializable
object LIB: ModType, JvmMod

@Serializable
object ABSTRACT: ModType

@Serializable
object JS_LIB: JsLibOnly

@Serializable
object JS_CLIENT: JsClient

//object NATIVE_LIB: JsLibOnly
//object NATIVE_MAIN: JsClient
sealed interface MultiPlatformMod: ModType /*has common*/


@Serializable object ALL: MultiPlatformMod, JvmMod, JsLibOnly, NativeLib
@Serializable object JVM_ONLY: MultiPlatformMod, JvmMod
@Serializable object JS_ONLY: MultiPlatformMod, JsLibOnly
@Serializable object NATIVE_ONLY: MultiPlatformMod, NativeMain
@Serializable object NO_NATIVE: MultiPlatformMod, JvmMod, JsLibOnly
@Serializable object NO_JS: MultiPlatformMod, JvmMod, NativeLib
@Serializable object NO_JVM: MultiPlatformMod, JsLibOnly, NativeLib

