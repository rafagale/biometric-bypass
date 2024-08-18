package es.rafagale.biometricbypass

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class BiometricBypassHook : IXposedHookLoadPackage {

    companion object {
        private const val TAG = "BiometricBypassHook"
        private const val TARGET_PACKAGE = "com.android.systemui"
        private const val TARGET_CLASS = "com.android.systemui.biometrics.AuthContainerView"
        private const val TARGET_METHOD = "onDialogAnimatedIn"
        private const val CONFIRM_BUTTON_ID_NAME = "button_confirm"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            if (lpparam.packageName != TARGET_PACKAGE) return

            XposedHelpers.findAndHookMethod(
                TARGET_CLASS,
                lpparam.classLoader,
                TARGET_METHOD,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        setUpConfirmButtonClickListener(param.thisObject as View)
                    }
                }
            )
        } catch (e: Exception) {
            XposedBridge.log("$TAG: Error setting up biometric bypass - ${e.message}")
        }
    }

    private fun setUpConfirmButtonClickListener(rootView: View) {
        findConfirmButton(rootView)?.let { button ->
            waitForButtonAndClick(rootView, button)
        } ?: run {
            XposedBridge.log("$TAG: Confirm button not found")
        }
    }

    @SuppressLint("ResourceType")
    private fun findConfirmButton(rootView: View): Button? {
        val confirmButtonId = rootView.resources.getIdentifier(
            CONFIRM_BUTTON_ID_NAME,
            "id",
            rootView.context.packageName
        )
        return rootView.findViewById(confirmButtonId)
    }

    private fun waitForButtonAndClick(rootView: View, confirmBtn: Button) {
        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (confirmBtn.isShown) {
                    confirmBtn.performClick()
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    XposedBridge.log("$TAG: Confirm button clicked successfully")
                }
            }
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }
}
