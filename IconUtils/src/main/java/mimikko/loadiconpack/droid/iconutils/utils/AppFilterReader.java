package mimikko.loadiconpack.droid.iconutils.utils;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mimikko.loadiconpack.droid.iconutils.IconPackConfig;
import mimikko.loadiconpack.droid.iconutils.beans.IconPackBean;

/**
 * Created by uchia on 7/5/2017.
 */

public class AppFilterReader {

    private final  String mPackageName;
    private static AppFilterReader reader;
    private boolean isReadDone = false;
    private Map<String,IconPackBean> mAppFilterConfigMap;

    public Map<String, IconPackBean> getmAppFilterConfigMap() {
        return mAppFilterConfigMap;
    }

    public void setmAppFilterConfigMap(Map<String, IconPackBean> mAppFilterConfigMap) {
        this.mAppFilterConfigMap = mAppFilterConfigMap;
    }

    private AppFilterReader(Resources resources, String packageName) {
        mPackageName = packageName;
        mAppFilterConfigMap = new HashMap<>();
        init(resources);
    }

    private boolean init(Resources resources) {
        if (isReadDone()) {
            return true;
        }
        if (resources == null) {
            return false;
        }

        if(mPackageName == null){
            return false;
        }

        int resId = resources.getIdentifier(IconPackConfig.APPFIlTER,
                IconPackConfig.APPFILTERLOCATION,mPackageName);


        XmlResourceParser parser = resources.getXml(resId);
        try {
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    if (!IconPackConfig.APPFILTERXMLLABEL.equals(parser.getName())) {
                        event = parser.next();
                        continue;
                    }
                    IconPackBean bean = new IconPackBean();
                    bean.drawable = parser.getAttributeValue(null, IconPackConfig.APPFILTERDRAWABLEELEMENT);
                    if (TextUtils.isEmpty(bean.drawable)) {
                        event = parser.next();
                        continue;
                    }
                    if (bean.drawable.matches(".+?_\\d+")) {
                        bean.drawableNoSeq = bean.drawable.substring(0, bean.drawable.lastIndexOf('_'));
                    } else {
                        bean.drawableNoSeq = bean.drawable;
                    }
                    String component = parser.getAttributeValue(null,IconPackConfig.APPFILTERCOMPONENTELEMENT);
                    if (component == null) {
                        event = parser.next();
                        continue;
                    }
                    Matcher matcher = Pattern.compile("ComponentInfo\\{([^/]+?)/(.+?)\\}").matcher(component);
                    if (matcher.matches()) {
                        bean.pkg = matcher.group(1);
                        bean.launcher = matcher.group(2);
                    }

                    if(!TextUtils.isEmpty(bean.launcher)){
                        mAppFilterConfigMap.put(bean.launcher,bean);
                    }
                }
                event = parser.next();
            }
            isReadDone = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isReadDone() {
        return isReadDone;
    }

    public static AppFilterReader getInstance(Resources resources,String packageName) {
        if (reader == null) {
            synchronized (AppFilterReader.class) {
                if (reader == null) {
                    reader = new AppFilterReader(resources,packageName);
                }
            }
        }

        return reader;
    }

}
