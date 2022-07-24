import kotlinx.serialization.Serializable

/*
https://github.com/Kotlin/kotlinx.serialization/pull/1958
*/

@Serializable sealed interface ModType

@Serializable sealed interface JvmMod: ModType
@Serializable sealed interface JvmOnlyMod: JvmMod

@Serializable sealed interface JsMod: ModType {
  val client: Boolean
}

@Serializable sealed interface JsLibOnly: JsMod {
  override val client get() = false
}

@Serializable sealed interface JsClient: JsMod {
  override val client get() = true
}

@Serializable sealed interface NativeMod: ModType
@Serializable sealed interface NativeMain: NativeMod
@Serializable sealed interface NativeLib: NativeMod
@Serializable object APP: ModType, JvmOnlyMod
@Serializable object CLAPP: ModType, JvmOnlyMod
@Serializable object APPLIB: ModType, JvmOnlyMod
@Serializable object LIB: ModType, JvmOnlyMod
@Serializable object ABSTRACT: ModType
@Serializable object JS_LIB: JsLibOnly
@Serializable object JS_CLIENT: JsClient

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


val JvmMod.isExecutable get() = this in setOf(APP, CLAPP)
val JvmMod.isAnyLib get() = this in setOf(LIB, APPLIB)