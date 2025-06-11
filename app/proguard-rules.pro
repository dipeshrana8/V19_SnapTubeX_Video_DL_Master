
-keep public class com.onesignal.** {
    public *;
}
-dontwarn com.onesignal.**

# --- Room (keep auto-generated classes + annotation processors) ---
-keep class androidx.room.RoomDatabase { *; }

#=======================
#  2) OBfuscATE everything else
#=======================

# By default, R8 already obfuscates any class not matched above. You do NOT need a separate “-obfuscate” flag.

#=======================
#  3) OPTIONAL: strip Log calls (release only)
#=======================
-keep class com.vidswift.downloader.allvideosaver.model.** { *; }
-keep class com.vidswift.downloader.allvideosaver.myAds.** { *; }
-keep class com.vidswift.downloader.allvideosaver.download.** { *; }
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
    public static *** w(...);
    public static *** e(...);
}
