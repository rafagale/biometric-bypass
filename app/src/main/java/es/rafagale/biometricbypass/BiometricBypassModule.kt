package es.rafagale.biometricbypass

import android.annotation.SuppressLint
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedInterface.AfterHookCallback
import io.github.libxposed.api.XposedInterface.BeforeHookCallback
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam
import io.github.libxposed.api.annotations.AfterInvocation
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker
import java.lang.reflect.Member
import java.lang.reflect.Method
import kotlin.random.Random

private lateinit var module: ModuleMain

class ModuleMain(base: XposedInterface, param: ModuleLoadedParam) : XposedModule(base, param) {

    init {
        log("ModuleMain at " + param.processName)
        module = this
    }

    @XposedHooker
    class BiometricBypassHooker(private val magic: Int) : XposedInterface.Hooker {
        companion object {
            @JvmStatic
            @BeforeInvocation
            fun beforeInvocation(callback: BeforeHookCallback): BiometricBypassHooker {
                val key = Random.nextInt()
                val member: Member = callback.member
                val thisObject = callback.thisObject
                val args = callback.args

                module.log("beforeInvocation: key = $key")
                module.log("Class: ${member.declaringClass?.name}")
                module.log("Method: ${member.name}")
                module.log("Arguments: ${args.joinToString()}")
                if (thisObject != null) {
                    module.log("This object: $thisObject")
                }
                return BiometricBypassHooker(key)
            }

            @JvmStatic
            @AfterInvocation
            fun afterInvocation(callback: AfterHookCallback, context: BiometricBypassHooker) {
                module.log("afterInvocation: key = ${context.magic}")
                module.log("Result: ${callback.result ?: "No result"}")
            }
        }
    }

    @SuppressLint("DiscouragedPrivateApi")
    override fun onPackageLoaded(param: PackageLoadedParam) {
        super.onPackageLoaded(param)
        log("onPackageLoaded: " + param.packageName)
        log("param classloader is " + param.classLoader)
        log("module apk path: " + this.applicationInfo.sourceDir)
        log("----------")

        if (param.packageName == TARGET_PACKAGE && param.isFirstPackage) {
            try {
                val targetClass = param.classLoader.loadClass(TARGET_CLASS)
                val targetMethod: Method = targetClass.getDeclaredMethod(TARGET_METHOD) // Ensure this is a Method, not a Member
                hook(targetMethod, BiometricBypassHooker::class.java)
                log("Successfully hooked method: $TARGET_METHOD in class: $TARGET_CLASS.")
            } catch (e: ClassNotFoundException) {
                log("Target class not found: $TARGET_CLASS - ${e.message}")
            } catch (e: NoSuchMethodException) {
                log("Target method not found: $TARGET_METHOD - ${e.message}")
            } catch (e: Exception) {
                log("Unexpected error setting up biometric bypass - ${e.message}")
            }
        } else {
            log("Loaded package does not match target package or it's not the first package: ${param.packageName}")
        }
    }

    companion object {
        const val TARGET_PACKAGE = "com.android.systemui"
        const val TARGET_CLASS = "com.android.systemui.biometrics.AuthContainerView"
        const val TARGET_METHOD = "onDialogAnimatedIn"
    }
}
