package es.rafagale.biometricbypass

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import es.rafagale.biometricbypass.BiometricBypassModule.Companion.BUTTON_CONFIRM_ID
import es.rafagale.biometricbypass.BiometricBypassModule.Companion.TAG
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.annotations.AfterInvocation
import io.github.libxposed.api.annotations.XposedHooker

@XposedHooker
class BiometricBypassHooker(private val magic: Int) : XposedInterface.Hooker {
    companion object {
        @SuppressLint("DiscouragedApi")
        @JvmStatic
        @AfterInvocation
        fun afterInvocation(
            callback: XposedInterface.AfterHookCallback,
            context: BiometricBypassHooker
        ) {
            module.log("$TAG afterInvocation: key = ${context.magic}")
            module.log("$TAG Result: ${callback.result ?: "No result"}")

            // Access the 'this' object from the hooked method, which should be the AuthContainerView
            val authContainerView = callback.thisObject as? View ?: return

            // Try to find the Confirm button using the constant BUTTON_CONFIRM_ID
            val confirmButton = authContainerView.findViewById<Button>(
                authContainerView.resources.getIdentifier(
                    BUTTON_CONFIRM_ID,
                    "id",
                    authContainerView.context.packageName
                )
            )

            if (confirmButton != null) {
                module.log("$TAG Confirm button found: $confirmButton")

                // Set up an OnGlobalLayoutListener to wait for the button to be visible
                val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (confirmButton.isShown) {
                            confirmButton.performClick()
                            module.log("$TAG Confirm button clicked")
                            authContainerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    }
                }
                authContainerView.viewTreeObserver.addOnGlobalLayoutListener(listener)
            } else {
                module.log("$TAG Confirm button not found")
            }
        }
    }
}
