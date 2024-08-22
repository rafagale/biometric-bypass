# Biometric Bypass Module

## Overview

The Biometric Bypass Module streamlines the face authentication process on Android devices by automatically bypassing the confirmation step that typically follows a successful face unlock. This results in a seamless, hands-free user experience. The module is optimized for Android 14 and has been rigorously tested on the Pixel 8 Pro (husky) running the August build (ap2a.240805.005).

## How It Works

In accordance with Android's official [biometric authentication documentation](https://developer.android.com/training/sign-in/biometric-auth#no-explicit-user-action), a manual confirmation is generally required even after a successful face authentication. While this provides an extra layer of security, it can also lead to unnecessary friction in user workflows.

The Biometric Bypass Module enhances usability by automating the confirmation step for apps utilizing the BiometricPrompt API. This ensures that after a successful face authentication, no further user action is required, allowing for a more fluid interaction with your device.

## Visual Example

Below are visual examples comparing the experience with the Biometric Bypass Module enabled versus the default Android behavior:

<p align="center">
    <img src="media/module_enabled.gif" width="200" alt="Module Enabled">
    <br/>
    <strong>Module Enabled: Face unlock with automatic confirmation bypass.</strong>
    <br/><br/>
    <img src="media/module_disabled.gif" width="200" alt="Default Behavior">
    <br/>
    <strong>Default Behavior: Face unlock with manual confirmation required.</strong>
</p>

## Installation Instructions

1. Ensure that [LSPosed](https://github.com/mywalkb/LSPosed_mod/releases) is installed on your Android device.
2. Download and install the Biometric Bypass Module APK.
3. Activate the module via your LSPosed.
4. Restart the System UI to apply changes.

## Compatibility

- **Android Version:** 14
- **Device:** Pixel 8 Pro (husky)
- **Build:** ap2a.240805.005

## Additional Flavors

This module is available in multiple flavors to support different Xposed API implementations:

- **Modern Xposed API (Kotlin):** The main branch of this module leverages the latest Xposed API, offering modern Kotlin-based implementation for enhanced performance and maintainability.
- **Legacy Xposed API (Java):** A version using Rovo89's original Xposed API with a Java-based implementation.
- **Legacy Xposed API (Kotlin):** Another version using Rovo89's original Xposed API, but implemented in Kotlin for developers preferring this language.

These additional flavors ensure compatibility with a wide range of Xposed setups, making the module accessible to users and developers across various environments.

## Risks and Warnings

By automatically bypassing the confirmation step post-face authentication, this module may reduce the security of your device. The confirmation step serves as a safeguard to ensure the user's intent to proceed with the authenticated action. Skipping this step could potentially lead to unintended actions being performed on your device.

**Potential Risks Include:**
- Unintended payments or purchases within apps that require biometric authentication.
- Accidental authorization of sensitive actions within apps without explicit user confirmation.

We strongly recommend weighing these risks carefully before deciding to use this module.

## Contributions

As a developer new to reverse engineering Android code, I highly encourage contributions and suggestions for improvement. If you have ideas for enhancements or encounter any issues, please feel free to submit a pull request or open an issue on the repository.

## Disclaimer

This module alters system behavior, which could impact your device's security model. Use this module at your own risk. The developers are not responsible for any unintended consequences resulting from its use.
