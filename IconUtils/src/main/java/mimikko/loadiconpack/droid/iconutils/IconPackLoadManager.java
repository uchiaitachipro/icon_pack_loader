package mimikko.loadiconpack.droid.iconutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mimikko.loadiconpack.droid.iconutils.beans.IconPackBean;
import mimikko.loadiconpack.droid.iconutils.utils.AppFilterReader;
import mimikko.loadiconpack.droid.iconutils.utils.IconPackFilterReader;
import mimikko.loadiconpack.droid.iconutils.utils.PkgUtils;

/**
 * Created by uchia on 7/4/2017.
 */

public class IconPackLoadManager {

    protected  PackageManager mPackageManager;
    protected  Resources mResources;
    protected AppFilterReader mAppFilterReader;
    protected IconPackFilterReader mIconPackFilterReader;

    public IconPackLoadManager(PackageManager pm,Resources r){
        mPackageManager = pm;
        mResources = r;
    }

    protected Drawable getIconDrawable(Resources r,int resId){
        try {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1){
                return  r.getDrawable(resId);
            }else{
                return ResourcesCompat.getDrawable(r, resId, null);
            }
        }catch (Exception e){
            return null;
        }
    }

    public String getThemeApkPath(String packageName) {
        try {
            return mPackageManager.getApplicationInfo(packageName, 0).sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    //这个方法把我们主题apk里的resource 加入到我们自己的主apk里的resource里
    //这个dexPath就是 我们theme.apk在 我们主apk 的存放路径
    public Resources addOtherResourcesToMain(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //反射调用addAssetPath这个方法 就可以
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            //把themeapk里的资源 通过addAssetPath
            // 这个方法增加到本apk自己的path里面以后 就可以重新构建出resource对象了
            Resources r = new Resources(assetManager,
                    mResources.getDisplayMetrics(),
                    mResources.getConfiguration());
            return r;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    public Resources loadThemeResToCurrentApk(String packageName) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            String dexPath = getThemeApkPath(packageName);
             return addOtherResourcesToMain(dexPath);
        } else {
            try {
                Resources r1 = mPackageManager.getResourcesForApplication(packageName);
                return r1;
            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        }
    }

    public Map<String,IconPackBean> buildAppFilter(Resources r,String packageName){
        if(mAppFilterReader == null){
            mAppFilterReader = AppFilterReader.getInstance(r,packageName);
        }
        return mAppFilterReader.getmAppFilterConfigMap();
    }

    public List<Intent> buildIconpackFilter(Resources resources){

        if(mIconPackFilterReader == null){
            mIconPackFilterReader = IconPackFilterReader.getInstance(resources);
        }

        return mIconPackFilterReader.getFilters();
    }

    public Map<String,Drawable> mapToFinalResults(Context context,Resources r, String packageName){

        Map<String,Drawable> map = new HashMap<>();
        List<String> installedApp =  PkgUtils.getInstalledPkgActivities(context);
        Map<String,IconPackBean> beanMap =  buildAppFilter(r,packageName);
        for (String app : installedApp){
            if(beanMap.containsKey(app)){
                int resID = r.getIdentifier(beanMap.get(app).drawable,IconPackConfig.ICONSOURCE,packageName);
                Drawable drawable = getIconDrawable(r,resID);
                if(drawable != null){
                    map.put(app,drawable);
                }
            }
        }

        return map;
    }

    public Map<String,Drawable> getIconPackList(Context context){

        Map<String,Drawable> map = new HashMap<>();
        Set<String> iconPackList = FindIconPack(context);
        for (String packageName : iconPackList){
            Drawable d = PkgUtils.getIcon(mPackageManager,packageName);
            if(d != null){

                map.put(packageName,d);

            }
        }
        return map;

    }



    public Set<String> FindIconPack(Context context){

        List<Intent> i = buildIconpackFilter(mResources);
        return PkgUtils.getInstalledIconPackPkgs(context,i);
    }

    public void Recycle(){
        mAppFilterReader.setmAppFilterConfigMap(null);
        mIconPackFilterReader.setmFilters(null);
        mPackageManager = null;
        mResources = null;
    }

}
