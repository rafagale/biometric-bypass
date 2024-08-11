package es.rafagale.biometricbypass;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.view.ViewTreeObserver;

import java.util.Optional;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BiometricBypassHook implements IXposedHookLoadPackage {

    private static final String TAG = "BiometricBypass";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (!"com.android.systemui".equals(lpparam.packageName)) {
            return;
        }

        try {
            XposedHelpers.findAndHookMethod(
                    "com.android.systemui.biometrics.AuthContainerView",
                    lpparam.classLoader,
                    "onDialogAnimatedIn",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            setUpConfirmButtonClickListener((View) param.thisObject);
                        }
                    }
            );
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Error setting up biometric bypass - " + e.getMessage());
        }
    }

    private void setUpConfirmButtonClickListener(final View rootView) {
        findConfirmButton(rootView)
                .ifPresentOrElse(
                        button -> waitForButtonAndClick(rootView, button),
                        () -> XposedBridge.log(TAG + ": Confirm button not found")
                );
    }

    private Optional<Button> findConfirmButton(final View rootView) {
        final String confirmButtonIdName = "button_confirm";
        @SuppressLint("ResourceType")
        int confirmButtonId = rootView.getResources().getIdentifier(confirmButtonIdName, "id", rootView.getContext().getPackageName());
        return Optional.ofNullable(rootView.findViewById(confirmButtonId));
    }

    private void waitForButtonAndClick(final View rootView, final Button confirmBtn) {
        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (confirmBtn.isShown()) {
                    confirmBtn.performClick();
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    XposedBridge.log(TAG + ": Confirm button clicked successfully");
                }
            }
        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }
}
