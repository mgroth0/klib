package matt.klib.mod

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
@Serializable sealed interface JvmExecutable: JvmOnlyMod
@Serializable object APP: JvmExecutable
@Serializable object CLAPP: JvmExecutable
@Serializable sealed interface JvmLib: JvmOnlyMod
@Serializable object APPLIB: JvmLib
@Serializable object LIB: JvmLib
@Serializable object ABSTRACT: ModType
@Serializable object JS_LIB: JsLibOnly
@Serializable object JS_CLIENT: JsClient

//object NATIVE_LIB: matt.klib.mod.JsLibOnly
//object NATIVE_MAIN: matt.klib.mod.JsClient
sealed interface MultiPlatformMod: ModType /*has common*/


@Serializable object MULTI_ALL: MultiPlatformMod, JvmMod, JsLibOnly, NativeLib
@Serializable object JVM_ONLY: MultiPlatformMod, JvmMod
@Serializable object JS_ONLY: MultiPlatformMod, JsLibOnly
@Serializable object NATIVE_ONLY: MultiPlatformMod, NativeMain
@Serializable object NO_NATIVE: MultiPlatformMod, JvmMod, JsLibOnly
@Serializable object NO_JS: MultiPlatformMod, JvmMod, NativeLib
@Serializable object NO_JVM: MultiPlatformMod, JsLibOnly, NativeLib


