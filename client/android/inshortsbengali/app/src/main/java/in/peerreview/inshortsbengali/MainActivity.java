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
            case R.id.state:
                mHorizantalViewPagerAdapter.LoadRemoteData("tag=state");
                pager.setCurrentItem(1);
                break;
            case R.id.kolkata:
                mHorizantalViewPagerAdapter.LoadRemoteData("tag=kolkata");
                pager.setCurrentItem(1);
                break;
        }
    }
}
