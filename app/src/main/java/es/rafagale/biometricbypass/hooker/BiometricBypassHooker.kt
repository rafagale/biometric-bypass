package es.rafagale.biometricbypass.hooker

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import es.rafagale.biometricbypass.BiometricBypassModule
import es.rafagale.biometricbypass.BiometricBypassModule.Companion.TAG
import es.rafagale.biometricbypass.module
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.annotations.AfterInvocation
import io.github.libxposed.api.annotations.XposedHooker

@XposedHooker
class BiometricBypassHooker : XposedInterface.Hooker {

    companion object {
        @JvmStatic
        @AfterInvocation
        fun afterInvocation(callback: XposedInterface.AfterHookCallback) {
            val authContainerView = callback.thisObject as? View
                ?: run {
                    module.log("$TAG Error: thisObject is not a View or is null")
                    return
                }

            val context = authContainerView.context
                ?: run {
                    module.log("$TAG Error: Context is null, cannot proceed")
                    return
                }

            @SuppressLint("DiscouragedApi")
            val confirmButtonId = context.resources.getIdentifier(
                BiometricBypassModule.BUTTON_CONFIRM_ID,
                "id",
                context.packageName
            )

            val confirmButton = authContainerView.findViewById<Button?>(confirmButtonId)
            if (confirmButton != null) {
                authContainerView.viewTreeObserver
                    .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            if (confirmButton.isShown) {
                                confirmButton.performClick()
                                module.log("$TAG Confirm button clicked successfully")
                                authContainerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                        }
                    })
            }
        }
    }
}
