# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified in the
# Android SDK tools/proguard/proguard-android-optimize.txt file.

# Keep Hilt-generated code
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
