package in.peerreview.inshortsbengali;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String mCategories = "";
    private String mSource = "";
    private String mData = "";
    private String mType = "";
    private HorizantalViewPagerAdapter mHorizantalViewPagerAdapter;
    private ViewPager pager;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sMainActivity = this;
        //mark it false when the build is ready
        Telemetry.setup("http://52.89.112.230/api/inshortsbengalistat", true);
        //setTheme(darkTheme ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_main);

        mHorizantalViewPagerAdapter = new HorizantalViewPagerAdapter(this);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setCurrentItem(1);
        pager.setAdapter(mHorizantalViewPagerAdapter);
        pager.setCurrentItem(1);
    }

    public void showLoading() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
            }
        });

    }

    public void hideLoading() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
            }
        });

    }


    private static MainActivity sMainActivity;

    public static MainActivity Get() {
        return sMainActivity;
    }

    public void showToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void buttonPressed(final View v) {
        switch (v.getId()) {
            case R.id.all:
                mCategories = "";
                mSource = "";
                refetch();
                break;
            case R.id.trending:
                mCategories = "kolkata";
                break;
            case R.id.saved:
                break;
            case R.id.kolkata:
                mCategories = "kolkata";
                refetch();
                break;
            case R.id.state:
                mCategories = "state";
                refetch();
                break;
            case R.id.india:
                mCategories = "india";
                refetch();
                break;
            case R.id.international:
                mCategories = "international";
                refetch();
                break;
            case R.id.lifestyle:
                mCategories = "lifestyle";
                refetch();
                break;
            case R.id.siteseeing:
                mCategories = "siteseeing";
                refetch();
                break;
            case R.id.game:
                mCategories = "game";
                refetch();
                break;
            case R.id.science:
                mCategories = "science";
                refetch();
                break;
            //all sources
            case R.id.pratidin:
                mSource = "pratidin";
                refetch();
                break;
            case R.id.eisamay:
                mSource = "eisamay";
                refetch();
                break;
            case R.id.anandabazar:
                mSource = "anandabazar";
                refetch();
                break;
            case R.id.bartaman:
                mSource = "bartamanpatrika";
                refetch();
                break;
            case R.id.zeenews:
                mSource = "zeenews";
                refetch();
                break;
            case R.id.ebela:
                mSource = "ebela";
                refetch();
                break;
            case R.id.songbaad:
                mSource = "songbaad";
                refetch();
                break;

        }
        Telemetry.sendTelemetry("menu_btn_click", new HashMap<String, String>() {{
            put("btn_id", getResources().getResourceName(v.getId()));
        }});

    }

    public void refetch() {
        String res = "";
        if (mCategories.length() != 0) {
            res += "tag=" + mCategories + "&";
        }
        if (mSource.length() != 0) {
            res += "tag=" + mSource + "&";
        }
        if (mData.length() != 0) {
            res += "tag=" + mData + "&";
        }
        mHorizantalViewPagerAdapter.LoadRemoteData(res);
        pager.setCurrentItem(1);
    }

    public void moveToTop() {
        mHorizantalViewPagerAdapter.verticalViewPager.setCurrentItem(0);
    }
}
