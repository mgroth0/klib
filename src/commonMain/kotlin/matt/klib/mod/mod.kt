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

/*these all have to have no properties or constructors for the time being since I'm regularly replacing instances with "createNewIsntance"s of the same class*/

@Serializable class APP: JvmExecutable
@Serializable class CLAPP: JvmExecutable
@Serializable sealed interface JvmLib: JvmOnlyMod
@Serializable class APPLIB: JvmLib
@Serializable class LIB: JvmLib
@Serializable class ABSTRACT: ModType
@Serializable class JS_LIB: JsLibOnly
@Serializable class JS_CLIENT: JsClient

//object NATIVE_LIB: matt.klib.mod.JsLibOnly
//object NATIVE_MAIN: matt.klib.mod.JsClient
sealed interface MultiPlatformMod: ModType /*has common*/


@Serializable class MULTI_ALL: MultiPlatformMod, JvmMod, JsLibOnly, NativeLib
@Serializable class JVM_ONLY: MultiPlatformMod, JvmMod
@Serializable class JS_ONLY: MultiPlatformMod, JsLibOnly
@Serializable class NATIVE_ONLY: MultiPlatformMod, NativeMain
@Serializable class NO_NATIVE: MultiPlatformMod, JvmMod, JsLibOnly
@Serializable class NO_JS: MultiPlatformMod, JvmMod, NativeLib
@Serializable class NO_JVM: MultiPlatformMod, JsLibOnly, NativeLib


