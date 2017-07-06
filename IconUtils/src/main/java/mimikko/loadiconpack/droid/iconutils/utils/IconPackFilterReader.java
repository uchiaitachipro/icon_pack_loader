package mimikko.loadiconpack.droid.iconutils.utils;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mimikko.loadiconpack.droid.iconutils.IconPackConfig;
import mimikko.loadiconpack.droid.iconutils.R;
import mimikko.loadiconpack.droid.iconutils.beans.IconPackBean;

/**
 * Created by uchia on 7/5/2017.
 */

public class IconPackFilterReader {

    private boolean mDone = false;

    private List<Intent>  mFilter ;

    private static IconPackFilterReader mReader;

    private IconPackFilterReader(Resources resources){
        mFilter = new LinkedList<>();
        init(resources);
    }


    private boolean init(Resources resources){
        if (isReadDone()) {
            return true;
        }
        if (resources == null) {
            return false;
        }
        int resId = getFilterConfigId();
        XmlResourceParser parser = resources.getXml(resId);
        try {
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    if (!IconPackConfig.ICONPACKLABEL.equals(parser.getName())) {
                        event = parser.next();
                        continue;
                    }
                    Intent filter = new Intent();
                    String action = parser.getAttributeValue(null,
                            IconPackConfig.ICONPACKACTIONELEMENT);
                    if (TextUtils.isEmpty(action)) {
                        event = parser.next();
                        continue;
                    }
                    filter.setAction(action);
                    String category = parser.getAttributeValue(null,
                            IconPackConfig.ICONPACKCATEGORYELEMENT);
                    if (TextUtils.isEmpty(category)) {
                        event = parser.next();
                        continue;
                    }
                    filter.addCategory(category);

                    mFilter.add(filter);
                }
                event = parser.next();
            }
            mDone = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected int getFilterConfigId(){
        return getFilterConfigId(R.xml.icon_pack_filter);
    }

    protected int getFilterConfigId(int resId){
        return resId;
    }

    public List<Intent> getFilters() {
        return mFilter;
    }

    public void setmFilters(List<Intent> mFilter) {
        this.mFilter = mFilter;
    }

    public boolean isReadDone(){
        return  mDone;
    }
    public static IconPackFilterReader getInstance(Resources resources) {
        if (mReader == null) {
            synchronized (AppFilterReader.class) {
                if (mReader == null) {
                    mReader = new IconPackFilterReader(resources);
                }
            }
        }
        return mReader;
    }

}
