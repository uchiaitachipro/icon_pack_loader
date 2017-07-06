package mimikko.loadiconpack.droid.iconutils.utils;

import android.os.Build;

/**
 * Created by uchia on 7/5/2017.
 */

public class C {
    public static final int SDK = Build.VERSION.SDK_INT;

    public static final String LOG_TAG = "NANO_ICON_PACK";

    public static final String APP_CODE_COMPONENT = "<item component=\"ComponentInfo{%1$s/%2$s}\" drawable=\"%3$s\" />";
    public static final String APP_CODE_LABEL = "<!-- %1$s / %2$s -->";
    public static final String APP_CODE_BUILD = "<!-- Build: %1$s / %2$s -->";

    public static final String URL_NANO_SERVER = "http://by-syk.com:8081/nanoiconpack/";
    //    public static final String URL_NANO_SERVER = "http://192.168.43.76:8082/nanoiconpack/";
    public static final String URL_COOLAPK_API = "https://api.coolapk.com/v6/";

    //    public static final String REQ_REDRAW_PREFIX = "\uD83C\uDE38 ";
//    public static final String REQ_REDRAW_PREFIX = "\uD83D\uDE4F ";
//    public static final String REQ_REDRAW_PREFIX = "\uD83D\uDCCE ";
//    public static final String REQ_REDRAW_PREFIX = "\uD83D\uDC65 ";
    public static final String REQ_REDRAW_PREFIX = "\uD83D\uDC64 ";
    //    public static final String ICON_ONE_SUFFIX = " ◎";
    public static final String ICON_ONE_SUFFIX = " ·";
}
