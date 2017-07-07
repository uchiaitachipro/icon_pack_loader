package loadiconpack.mimikko.droid;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mimikko.loadiconpack.droid.iconutils.IconPackLoadManager;

public class MainActivity extends AppCompatActivity {

    //kh.android.funnyiconpack
    //com.laihz.gradualiconpack.gamma
    //com.by_syk.nanoiconpack.sample
    //loadiconpack.mimikko.droid
    //com.natewren.linesfree
    static final String sPackage = "com.natewren.linesfree";
    IconPackLoadManager iconPackLoadManager;
    ListView mListView;
    Button mBtn1;
    Button mBtn2;
    Button mBtn3;
    Button mBtn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview);
        mBtn1 = (Button)findViewById(R.id.pixel);
        mBtn2 = (Button)findViewById(R.id.line);
        mBtn3 = (Button)findViewById(R.id.funny);
        mBtn4 = (Button)findViewById(R.id.icon_pack);

        mBtn1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showAllIconPack("com.themezilla.pixelui");
            }
        });

        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllIconPack("com.natewren.linesfree");
            }
        });

        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllIconPack("kh.android.funnyiconpack");
            }
        });

        mBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Map.Entry<String, Drawable>> listData = new ArrayList<>();
                iconPackLoadManager = new IconPackLoadManager(getPackageManager(),
                        getResources());
                for (Map.Entry<String, Drawable> entry : iconPackLoadManager
                        .getIconPackList(MainActivity.this).entrySet()) {

                    listData.add(entry);
                }

                mListView.setAdapter(new IconPackAdapter(listData, MainActivity.this));
            }
        });



    }

    protected void showAllIconPack(String packageName) {

        List<Map.Entry<String, Drawable>> listData = new ArrayList<>();
        iconPackLoadManager = new IconPackLoadManager(getPackageManager(),
                getResources());
        Resources r = iconPackLoadManager.loadThemeResToCurrentApk(packageName);
        if (r != null) {
            Map<String, Drawable> data = iconPackLoadManager
                    .mapToFinalResults(this, r, packageName);
            for (Map.Entry<String, Drawable> entry : data.entrySet()) {

                Log.d("buildAppFilter", "key = " + entry.getKey());
                listData.add(entry);
            }
        }
        Log.d("InstalledIconPack", packageName);
        mListView.setAdapter(new IconPackAdapter(listData, this));
    }
}
