package loadiconpack.mimikko.droid;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by uchia on 7/5/2017.
 */

public class IconPackAdapter extends BaseAdapter {

    private List<Map.Entry<String,Drawable>> mData;
    private Context mContext;


    public IconPackAdapter(List<Map.Entry<String,Drawable>> data,Context context){
        mData = data;
        mContext = context;
    }



    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if(view == null){
            TextView tv = new TextView(mContext);
            view  = tv;
            holder = new ViewHolder(tv);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();

        }

        Map.Entry<String,Drawable> data = mData.get(i);
        holder.title.setText(data.getKey());
        Drawable d = data.getValue();
        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
        holder.title.setCompoundDrawables(d,null,null,null);
        return view;
    }


    private class ViewHolder{

        public TextView title;

        public ViewHolder(TextView tv){
            title = tv;
        }

    }


}
