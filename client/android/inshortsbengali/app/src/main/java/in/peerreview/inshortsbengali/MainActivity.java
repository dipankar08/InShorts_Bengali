package in.peerreview.inshortsbengali;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String mCategories = "";
    private String mSource = "";
    private String mData = "";
    private String mType = "";
    private HorizantalViewPagerAdapter mHorizantalViewPagerAdapter;
    private ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(darkTheme ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_main);

        mHorizantalViewPagerAdapter = new HorizantalViewPagerAdapter(this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setCurrentItem(1);
        pager.setAdapter(mHorizantalViewPagerAdapter);
        pager.setCurrentItem(1);
    }

    public  void setTheme (boolean darkTheme){
        //setTheme(darkTheme ? R.style.AppThemeDark : R.style.AppThemeLight);
    }
    public void showToast(final String msg){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,  msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void buttonPressed(View v){
        switch(v.getId()) {
            case R.id.kolkata: mCategories = "kolkata"; break;
            case R.id.state: mCategories = "state"; break;
            case R.id.india: mCategories = "india"; break;
            case R.id.international: mCategories = "international"; break;
            case R.id.lifestyle: mCategories = "lifestyle"; break;
            case R.id.siteseeing: mCategories = "siteseeing"; break;
            case R.id.game: mCategories = "game"; break;
            case R.id.science: mCategories = "science"; break;
            //all sources
            case R.id.pratidin: mSource = "pratidin"; break;
            case R.id.eisamay: mSource = "eisamay"; break;
            case R.id.zeenews: mSource = "zeenews"; break;
            case R.id.ebela: mSource = "ebela"; break;
        }
        String res  ="";
        if(mCategories.length() != 0){
            res+= "tag="+ mCategories +"&";
        }
        if(mSource.length() != 0){
            res+= "tag="+ mCategories +"&";
        }
        if(mData.length() != 0){
            res+= "tag="+ mData +"&";
        }
        mHorizantalViewPagerAdapter.LoadRemoteData(res);
        pager.setCurrentItem(1);

    }
}
