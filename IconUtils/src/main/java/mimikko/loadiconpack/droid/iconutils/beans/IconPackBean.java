package mimikko.loadiconpack.droid.iconutils.beans;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by uchia on 7/5/2017.
 */

public class IconPackBean {
    @Nullable
    public String pkg;

    @Nullable
    public String launcher;

    @NonNull
    public String drawable;

    @NonNull
    public String drawableNoSeq;


    @Override
    public String toString() {
        return "Package:{"+pkg+"}\t"
                +"Launcher:{"+launcher+"}\t"
                +"Drawable:{"+drawable+"}\t"
                +"DrawableNoSeq{"+drawableNoSeq+"}\t";
    }
}
