# Personal Message Assistant (starter)

Starter Android Studio project for Android 8+; intended for testing on an iQOO Z6 Lite running Android 14.

## Current features
- Requests notification-listener access only through Android's Settings screen.
- Shows up to 30 recent notification previews exposed to the app while its process is available.
- Indicates whether a notification appears to expose a RemoteInput reply action.
- Does **not** send messages, save notification text permanently, upload data, or contact an AI service.

## Important limitations
- Notification content depends on what the source app and Android expose. Sensitive or hidden previews may be absent.
- The reply-action indicator is only a detection hint. This starter does not invoke reply actions.
- Automatic reply behavior must be implemented carefully, with explicit user settings, app-specific testing, and safeguards. Not all WhatsApp or Instagram notifications support it.
- Android may stop the process or clear in-memory data. This starter is not a production-ready background service.
- Notification access is sensitive. Enable it only for software you trust.

## Install/build with Android Studio
1. Install Android Studio from https://developer.android.com/studio.
2. Open this project folder in Android Studio.
3. Allow Android Studio to download the Gradle and Android SDK components it requests.
4. If prompted to install Android SDK Platform 35, accept.
5. Connect your phone by USB. On the phone, enable Developer options and USB debugging, then approve the computer's RSA prompt.
6. In Android Studio, choose your phone as the run target and press Run.
7. On the phone, tap "Open notification access settings" and enable access for Personal Message Assistant only if you consent.
8. Send a test message to yourself from another account/device and check the preview.

## If you prefer an APK file
In Android Studio choose Build > Build Bundle(s) / APK(s) > Build APK(s). The debug APK is usually under:
`app/build/outputs/apk/debug/app-debug.apk`

This source project is a starter and may need Gradle/SDK downloads on first open. Test before relying on it.
