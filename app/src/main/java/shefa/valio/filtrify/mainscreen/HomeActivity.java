package shefa.valio.filtrify.mainscreen;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.Swipe;
import com.github.pwittchen.swipe.library.SwipeListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import shefa.valio.filtrify.R;

public class HomeActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static final int REQUEST_IMAGE_CAPTURE = 111;

    private TabLayout tabLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private GPUImage gpuImage;
    private GestureDetector gestureDetector;
    private Swipe swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        tabLayout = new TabLayout(this);
        tabLayout.addTab(tabLayout.newTab().setText("Sepia"));
        tabLayout.addTab(tabLayout.newTab().setText("Negative"));
        tabLayout.addTab(tabLayout.newTab().setText("Dick"));
        toolbar.addView(tabLayout);
        setSupportActionBar(toolbar);

        LinearLayout bottomSheetLayout = ButterKnife.findById(this, R.id.bottom_sheet_image_options);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        ButterKnife.bind(this);

        gpuImage = new GPUImage(this);

        GLSurfaceView surface = (GLSurfaceView) findViewById(R.id.surface_view);
        gpuImage.setGLSurfaceView(surface);
        gestureDetector = new GestureDetector(this, this);

        surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    gpuImage.setFilter(new GPUImageSepiaFilter());
                }else {
                    showMessage(tab.getText().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_new_image) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            Uri uri = Uri.fromFile(new File(images.get(0).path));
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            gpuImage.setImage(uri);
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            gpuImage.setImage(imageBitmap);
        }
    }

    @OnClick(R.id.tv_bottom_sheet_peek)
    public void onBottomSheetPeekClick() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @OnClick(R.id.btn_bottom_sheet_camera)
    public void onCameraClick() {
        onCameraRequested();
    }

    @OnClick(R.id.btn_bottom_sheet_images)
    public void onImagePickerClick() {
        showImagePicker();
    }

    private void showImagePicker() {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1);
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else {
                    showMessage("Can't use this option");
                }
                break;
            }
        }
    }

    private void onCameraRequested() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Function requires camera")
                        .setMessage("The camera is needed so that you are able to take a picture")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestCameraPermissions();
                            }
                        }).setCancelable(true);
                builder.create().show();
            } else {
               requestCameraPermissions();
            }
        }else {
            launchCamera();
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            showMessage("No camera available");
        }
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_IMAGE_CAPTURE);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float X, float Y) {
        if(motionEvent1.getX() - motionEvent2.getX() > 50){
            int selectedTabPos = tabLayout.getSelectedTabPosition();
            int nextTabPos = selectedTabPos + 1;

            if (tabLayout.getTabCount() > nextTabPos)
                tabLayout.getTabAt(nextTabPos).select();
            return true;
        }

        if(motionEvent2.getX() - motionEvent1.getX() > 50) {
            int selectedTabPos = tabLayout.getSelectedTabPosition();
            int nextTabPos = selectedTabPos - 1;

            if (nextTabPos >= 0)
                tabLayout.getTabAt(nextTabPos).select();
            return true;
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        // TODO Auto-generated method stub

        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // TODO Auto-generated method stub

        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub

        return false;
    }

}
