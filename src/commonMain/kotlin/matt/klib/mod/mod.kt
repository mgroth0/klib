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

@Serializable class APP: JvmExecutable {
  private companion object /*temporarily for migration*/
}

@Serializable class CLAPP: JvmExecutable {
  private companion object /*temporarily for migration*/
}

@Serializable sealed interface JvmLib: JvmOnlyMod
@Serializable class APPLIB: JvmLib {
  private companion object /*temporarily for migration*/
}

@Serializable class LIB: JvmLib {
  private companion object /*temporarily for migration*/
}

@Serializable class ABSTRACT: ModType {
  private companion object /*temporarily for migration*/
}

@Serializable class JS_LIB: JsLibOnly {
  private companion object /*temporarily for migration*/
}

@Serializable class JS_CLIENT: JsClient {
  private companion object /*temporarily for migration*/
}

//object NATIVE_LIB: matt.klib.mod.JsLibOnly
//object NATIVE_MAIN: matt.klib.mod.JsClient
sealed interface MultiPlatformMod: ModType /*has common*/


@Serializable class MULTI_ALL: MultiPlatformMod, JvmMod, JsLibOnly, NativeLib {
  private companion object /*temporarily for migration*/
}

@Serializable class JVM_ONLY: MultiPlatformMod, JvmMod {
  private companion object /*temporarily for migration*/
}

@Serializable class JS_ONLY: MultiPlatformMod, JsLibOnly {
  private companion object /*temporarily for migration*/
}

@Serializable class NATIVE_ONLY: MultiPlatformMod, NativeMain {
  private companion object /*temporarily for migration*/
}

@Serializable class NO_NATIVE: MultiPlatformMod, JvmMod, JsLibOnly {
  private companion object /*temporarily for migration*/
}

@Serializable class NO_JS: MultiPlatformMod, JvmMod, NativeLib {
  private companion object /*temporarily for migration*/
}

@Serializable class NO_JVM: MultiPlatformMod, JsLibOnly, NativeLib {
  private companion object /*temporarily for migration*/
}


