package se.johan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 1;
    private Bitmap _bitmap;
    private ImageView _imageView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        _imageView = (ImageView) findViewById(R.id.picture_view);
    }

    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setAction(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //you need to handle NullPionterException here.
        Object savedImage  = savedInstanceState.getParcelable("image");
        if(savedImage != null) {
            _bitmap = (Bitmap)savedImage;
            _imageView.setImageBitmap(_bitmap);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
             outState.putParcelable("image", _bitmap);
             super.onSaveInstanceState(outState);
    }

    
    
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            handlePictureReceived(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handlePictureReceived(Intent data) {
        if (_bitmap != null) {
            _imageView.setImageBitmap(null);
            _bitmap.recycle();
        }
        Bundle extras = data.getExtras();
        _bitmap = (Bitmap) extras.get("data");
        _imageView.setImageBitmap(_bitmap);
    }
}
