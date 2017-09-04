package in.peerreview.External;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import in.peerreview.inshortsbengali.MainActivity;

/**
 Exaple:'
 ShareScreen.share();
 */
public class ShareScreen {
    private static Context mContext;
    public static void setup(Context cx){
        mContext = cx;
    }
    public static void share(){
       View view = MainActivity.Get().getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap b = getScreenShot(view);
        File  f = store(b, "share.jpg");
        shareImage(f,"Share news..");
    }

    private static File store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        if (file.exists ()) file.delete ();

        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }


    private static void shareImage(File file,String titile){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey, I am sending this news to you. For more news like this please visit InShorts Bengali.");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "The interesting news only for you..");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            MainActivity.Get().startActivity(Intent.createChooser(intent, titile));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.Get(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

}
