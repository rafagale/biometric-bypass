import es.rafagale.biometricbypass.hookers.BiometricServiceHooker
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam

private lateinit var module: MainModule

class MainModule(base: XposedInterface, param: ModuleLoadedParam) : XposedModule(base, param) {
    init {
        module = this
    }

    override fun onPackageLoaded(param: PackageLoadedParam) {
        super.onPackageLoaded(param)

        if (param.packageName == TARGET_PACKAGE) {
            try {
                module.log("Hooking System UI for biometric bypass.")
                BiometricServiceHooker.hook(param, module)
            } catch (e: Exception) {
                module.log("Error setting up biometric bypass - ${e.message}")
            }
        }
    }

    companion object {
        const val TARGET_PACKAGE = "com.android.systemui"
    }
}
