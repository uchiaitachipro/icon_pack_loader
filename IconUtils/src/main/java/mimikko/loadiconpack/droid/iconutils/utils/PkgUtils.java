package mimikko.loadiconpack.droid.iconutils.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.params.InputConfiguration;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import mimikko.loadiconpack.droid.iconutils.IconPackConfig;

/**
 * Created by uchia on 7/5/2017.
 */

public class PkgUtils {
    public static boolean isPkgInstalled(Context context, String pkgName) {
        if (context == null || pkgName == null) {
            return false;
        }

        try {
            context.getPackageManager().getPackageInfo(pkgName, 0);
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return false;
    }

    public static boolean isPkgInstalledAndEnabled(Context context, String pkgName) {
        return getLauncherActivity(context, pkgName) != null;
    }

    public static List<String> getInstalledPkgs(Context context) {
        List<String> pkgNameList = new ArrayList<>();
        if (context == null) {
            return pkgNameList;
        }

        try {
            List<PackageInfo> pkgList = context.getPackageManager().getInstalledPackages(0);
            if (pkgList != null) {
                for (PackageInfo packageInfo : pkgList) {
                    pkgNameList.add(packageInfo.packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pkgNameList;
    }

    public static List<String> getInstalledPkgsWithLauncherActivity(Context context) {
        List<String> pkgNameList = new ArrayList<>();
        if (context == null) {
            return pkgNameList;
        }

        try {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);
            for (ResolveInfo resolveInfo : list) {
                pkgNameList.add(resolveInfo.activityInfo.packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pkgNameList;
    }

    public static Set<String> getInstalledIconPackPkgs(Context context, List<Intent> filter) {
        Set<String> pkgNameList = new HashSet<>();
        if (context == null) {
            return pkgNameList;
        }

            for (Intent i :filter)
            {
                if(i == null) continue;
                List<ResolveInfo> list = null;
                try {
                    list = context.getPackageManager().queryIntentActivities(i,0);
                } finally {
                    for (ResolveInfo r : list){
                        pkgNameList.add(r.activityInfo.packageName);
                    }
                }

            }

        return pkgNameList;
    }

    public static List<String> getInstalledPkgActivities(Context context) {
        List<String> pkgLauncherList = new ArrayList<>();
        if (context == null) {
            return pkgLauncherList;
        }

        try {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);
            for (ResolveInfo resolveInfo : list) {
                pkgLauncherList.add(resolveInfo.activityInfo.name);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return pkgLauncherList;
    }

    public static String getLauncherActivity(Context context, String pkgName) {
        if (context == null || pkgName == null) {
            return null;
        }

        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            if (intent != null) {
                return intent.getComponent().getClassName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCurLauncher(Context context) {
        if (context == null) {
            return null;
        }

        try {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_HOME);
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(mainIntent, 0);
            if (resolveInfo != null) {
                return resolveInfo.activityInfo.packageName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @TargetApi(17)
    public static String getAppLabelEn(Context context, String pkgName, String def) {
        if (context == null || TextUtils.isEmpty(pkgName)) {
            return def;
        }

        String result = def;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getPackageInfo(pkgName, 0).applicationInfo;

            Configuration configuration = new Configuration();
            // It's better, I think, to use Locale.ENGLISH
            // instead of Locale.ROOT (although I want to do).
            if (C.SDK >= 17) {
                configuration.setLocale(Locale.ENGLISH);
            } else {
                configuration.locale = Locale.ENGLISH;
            }
            // The result is a value in disorder maybe if using:
            //     packageManager.getResourcesForApplication(PACKAGE_NAME)
            Resources resources = packageManager.getResourcesForApplication(applicationInfo);
            resources.updateConfiguration(configuration,
                    context.getResources().getDisplayMetrics());
            int labelResId = applicationInfo.labelRes;
            if (labelResId != 0) {
                // If the localized label is not added, the default is returned.
                // NOTICE!!!If the default were empty, Resources$NotFoundException would be called.
                result = resources.getString(labelResId);
            }

            /*
             * NOTICE!!!
             * We have to restore the locale.
             * On the one hand,
             * it will influence the label of Activity, etc..
             * On the other hand,
             * the got "resources" equals the one "this.getResources()" if the current .apk file
             * happens to be this APK Checker(com.by_syk.apkchecker).
             * We need to restore the locale, or the language of APK Checker will change to English.
             */
            if (C.SDK >= 17) {
                configuration.setLocale(Locale.getDefault());
            } else {
                configuration.locale = Locale.getDefault();
            }
            resources.updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isSysApp(Context context, String pkgName) {
        if (context == null || TextUtils.isEmpty(pkgName)) {
            return false;
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getPackageInfo(pkgName, 0).applicationInfo;
//            return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM;
            return ((ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)
                    & applicationInfo.flags) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getAppVer(Context context, String format) {
        if (context == null || TextUtils.isEmpty(format)) {
            return "";
        }

        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return String.format(Locale.US, format, packageInfo.versionName, packageInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Get launcher icon
     */
    public static Drawable getIcon(PackageManager pkgManager, String pkgName) {
        if (pkgManager  == null || TextUtils.isEmpty(pkgName)) {
            return null;
        }

        try {
            PackageInfo packageInfo = pkgManager.getPackageInfo(pkgName, 0);
            return packageInfo.applicationInfo.loadIcon(pkgManager);
        } catch (Exception e) {
            Log.d(C.LOG_TAG, pkgName + " is not installed.");
        }

        return null;
    }

    /**
     * Get Activity icon
     */
    public static Drawable getIcon(PackageManager pkgManager, String pkgName, String activity) {
        if (pkgManager  == null || TextUtils.isEmpty(pkgName) || TextUtils.isEmpty(activity)) {
            return null;
        }

        Intent intent = new Intent();
        intent.setClassName(pkgName, activity);
        ResolveInfo resolveInfo = pkgManager.resolveActivity(intent, 0);
        if (resolveInfo != null) {
            return resolveInfo.loadIcon(pkgManager);
        }
        return null;
    }

    @NonNull
    public static String concatComponent(@NonNull String pkgName, String launcherActivity) {
        String component = pkgName;
        if (!TextUtils.isEmpty(launcherActivity)) {
            if (launcherActivity.startsWith(pkgName)) {
                component += "/" + launcherActivity.substring(pkgName.length());
            } else {
                component += "/" + launcherActivity;
            }
        }
        return component;
    }
}
