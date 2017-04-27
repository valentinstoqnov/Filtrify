package shefa.valio.filtrify.mainscreen;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oswaldogh89.library.LatestImages;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorMatrixFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import shefa.valio.filtrify.R;

public class HomeActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static final int REQUEST_IMAGE_CAPTURE = 111;

    @BindView(R.id.latest_images) LatestImages latestImages;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.gpu_image_view) GPUImageView gpuImage;

    private BottomSheetBehavior bottomSheetBehavior;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Filtrify");

        final LinearLayout bottomSheetLayout = ButterKnife.findById(this, R.id.bottom_sheet_image_options);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        ButterKnife.bind(this);


        tabLayout.addTab(tabLayout.newTab().setText("Sepia"));
        tabLayout.addTab(tabLayout.newTab().setText("Negative"));
        tabLayout.addTab(tabLayout.newTab().setText("Emboss"));
        tabLayout.addTab(tabLayout.newTab().setText("Grayscale"));
        tabLayout.addTab(tabLayout.newTab().setText("Contrast"));
        tabLayout.addTab(tabLayout.newTab().setText("Posterize"));
        tabLayout.addTab(tabLayout.newTab().setText("Pixelate"));
        tabLayout.addTab(tabLayout.newTab().setText("Blur"));
        tabLayout.addTab(tabLayout.newTab().setText("Color Balance"));
        tabLayout.addTab(tabLayout.newTab().setText("Color Matrix"));
        tabLayout.addTab(tabLayout.newTab().setText("Highlight"));
        tabLayout.addTab(tabLayout.newTab().setText("Painting"));

        gestureDetector = new GestureDetector(this, this);

        gpuImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        gpuImage.setFilter(new GPUImageSepiaFilter());
                        break;
                    case 1:
                        gpuImage.setFilter(new GPUImageColorInvertFilter());
                        break;
                    case 2:
                        gpuImage.setFilter(new GPUImageEmbossFilter());
                        break;
                    case 3:
                        gpuImage.setFilter(new GPUImageGrayscaleFilter());
                        break;
                    case 4:
                        gpuImage.setFilter(new GPUImageContrastFilter());
                        break;
                    case 5:
                        gpuImage.setFilter(new GPUImagePosterizeFilter());
                        break;
                    case 6:
                        gpuImage.setFilter(new GPUImagePixelationFilter());
                        break;
                    case 7:
                        gpuImage.setFilter(new GPUImageBoxBlurFilter());
                        break;
                    case 8:
                        gpuImage.setFilter(new GPUImageColorBalanceFilter());
                        break;
                    case 9:
                        gpuImage.setFilter(new GPUImageColorMatrixFilter());
                        break;
                    case 10:
                        gpuImage.setFilter(new GPUImageHighlightShadowFilter());
                        break;
                    case 11:
                        gpuImage.setFilter(new GPUImageKuwaharaFilter());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        latestImages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showMessage("touch");
                ArrayList<com.oswaldogh89.library.Image> selectedImages = latestImages.getSelectedImages();
                if (selectedImages.size() > 0) {
                    com.oswaldogh89.library.Image image = selectedImages.get(0);
                    gpuImage.setImage(image.getImgPath());
                    image.setSelected(false);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to leave the app ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);

        MenuItem item = menu.findItem(R.id.action_save);
        SpannableStringBuilder builder = new SpannableStringBuilder("* Save");
        builder.setSpan(new ImageSpan(this, R.drawable.ic_save), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);

        MenuItem item1 = menu.findItem(R.id.action_new_image);
        SpannableStringBuilder builder1 = new SpannableStringBuilder("* New image");
        builder1.setSpan(new ImageSpan(this, R.drawable.ic_image_black), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item1.setTitle(builder1);

        MenuItem item2 = menu.findItem(R.id.action_clear);
        SpannableStringBuilder builder2 = new SpannableStringBuilder("* Clear");
        builder2.setSpan(new ImageSpan(this, R.drawable.ic_clear_black), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item2.setTitle(builder2);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_new_image) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            return true;
        }else if (itemId == R.id.action_save) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save image");
            builder.setMessage("Name the image");
            final EditText editText = new EditText(this);
            builder.setView(editText);
            builder.setCancelable(false);
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String fileName = editText.getText().toString();
                    if (fileName.isEmpty()) {
                        showMessage("Image name not filled in...");
                    }else {
                        gpuImage.saveToPictures("Filtrify", fileName, new GPUImageView.OnPictureSavedListener() {
                            @Override
                            public void onPictureSaved(Uri uri) {
                                showMessage("Image successfully saved!");
                            }
                        });
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.create().show();
        }else if (itemId == R.id.action_clear) {
            gpuImage.removeAllViews();
            //gpuImage.setImage(BitmapFactory.decodeResource(getResources(), android.R.color.transparent));
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
