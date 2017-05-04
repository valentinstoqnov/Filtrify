package shefa.valio.filtrify.mainscreen;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import shefa.valio.filtrify.R;
import shefa.valio.filtrify.SwipeGestureDetector;
import shefa.valio.filtrify.util.AlerterUtils;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements SwipeGestureDetector.OnSwipeListener {

    private static final int REQUEST_CODE_PICKER = 123;
    private static final int REQUEST_IMAGE_CAPTURE = 321;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.gpu_image_view) GPUImageView gpuImageView;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.bottom_sheet_new_image) LinearLayout llBottomSheet;
    @BindView(R.id.rl_image_filter) RelativeLayout rlEditImage;
    private BottomSheetBehavior bottomSheetBehavior;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        tabLayout.addTab(tabLayout.newTab().setText("Sepia"));
        tabLayout.addTab(tabLayout.newTab().setText("Negative"));
        tabLayout.addTab(tabLayout.newTab().setText("Emboss"));
        tabLayout.addTab(tabLayout.newTab().setText("Grayscale"));
        tabLayout.addTab(tabLayout.newTab().setText("Contrast"));
        tabLayout.addTab(tabLayout.newTab().setText("Posterize"));
        tabLayout.addTab(tabLayout.newTab().setText("Pixelate"));
        tabLayout.addTab(tabLayout.newTab().setText("Blur"));

        gestureDetector = new GestureDetector(this, new SwipeGestureDetector(this));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        gpuImageView.setFilter(new GPUImageSepiaFilter());
                        break;
                    case 1:
                        gpuImageView.setFilter(new GPUImageColorInvertFilter());
                        break;
                    case 2:
                        gpuImageView.setFilter(new GPUImageEmbossFilter());
                        break;
                    case 3:
                        gpuImageView.setFilter(new GPUImageGrayscaleFilter());
                        break;
                    case 4:
                        gpuImageView.setFilter(new GPUImageContrastFilter());
                        break;
                    case 5:
                        gpuImageView.setFilter(new GPUImagePosterizeFilter());
                        break;
                    case 6:
                        gpuImageView.setFilter(new GPUImagePixelationFilter());
                        break;
                    case 7:
                        gpuImageView.setFilter(new GPUImageBoxBlurFilter());
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

        gpuImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @OnClick(R.id.tv_select_image)
    public void onSelectImageClick() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @OnClick({R.id.btn_new_image_camera, R.id.btn_new_image_link, R.id.btn_new_image_gallery})
    public void onCameraClick(Button button) {
        switch (button.getId()) {
            case R.id.btn_new_image_link:
                showLinkPickerUi();
                break;
            case R.id.btn_new_image_gallery:
                showImagePickerUi();
                break;
            case R.id.btn_new_image_camera:
                showCameraUi();
                break;
        }
    }

    @OnClick(R.id.btn_image_remove)
    public void onRemoveImageClick() {
        showNewImageUi();
    }

    @OnClick(R.id.btn_image_save)
    public void onSaveImageClick() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Enter file name:")
                .setView(editText)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editText.getText().toString();
                        if (!name.isEmpty()) {
                            name += ".jpg";
                            gpuImageView.saveToPictures(Environment.DIRECTORY_DCIM, name, new GPUImageView.OnPictureSavedListener() {
                                @Override
                                public void onPictureSaved(Uri uri) {
                                    AlerterUtils.showSaved(MainActivity.this);
                                    showNewImageUi();
                                }
                            });
                        }else {
                            AlerterUtils.showMessage(MainActivity.this, "Invalid image name");
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle("About Filtrify aka the best app ever")
                    .setMessage("This app is school project. I don't know what to say but I just put some random text here in order to see how the dialog will look like. \n SAMO LEVSKI ! SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !SAMO LEVSKI !")
                    .setPositiveButton("OK", null)
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICKER && data != null) {
                ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                if (images.size() > 0) {
                    setGpuImageView(BitmapFactory.decodeFile(images.get(0).getPath()));
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                setGpuImageView(imageBitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else {
                    AlerterUtils.showMessage(this, "Can't use this option");
                }
                break;
            }
        }
    }

    private void setGpuImageView(Bitmap bitmap) {
        showEditImageUi();
        gpuImageView.setImage(bitmap);
    }

    private void loadImage(String path) {
        progressBar.setVisibility(View.VISIBLE);
        System.out.println(path);
        Picasso.with(this)
                .load(path)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        progressBar.setVisibility(GONE);
                        setGpuImageView(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        progressBar.setVisibility(GONE);
                        AlerterUtils.showError(MainActivity.this);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    private void showLinkPickerUi() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Enter image link")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String link = editText.getText().toString();
                        if (link.isEmpty()) {
                            AlerterUtils.showMessage(MainActivity.this, "Invalid link");
                        }else {
                            loadImage(link);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showImagePickerUi() {
        Intent intent = new Intent(this, ImagePickerActivity.class);

        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_MODE, true);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_MODE, ImagePickerActivity.MODE_SINGLE);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_LIMIT, 1);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SHOW_CAMERA, false);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_TITLE, "Album");
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_TITLE, "Tap to select images");
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_DIRECTORY, "Camera");

        startActivityForResult(intent, REQUEST_CODE_PICKER);
    }

    private void showCameraUi() {
        if (hasCameraPermissions()) {
            launchCamera();
        } else {
            askForCameraPermissions();
        }
    }

    private void showNewImageUi() {
        tabLayout.setVisibility(GONE);
        rlEditImage.setVisibility(View.INVISIBLE);
        llBottomSheet.setVisibility(View.VISIBLE);
    }

    private void showEditImageUi() {
        rlEditImage.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        llBottomSheet.setVisibility(View.INVISIBLE);
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            AlerterUtils.showMessage(this, "No camera available");
        }
    }

    private boolean hasCameraPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void askForCameraPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Function requires camera")
                    .setMessage("The camera is needed so that you can take a picture")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestCameraPermissions();
                        }
                    })
                    .setCancelable(true)
                    .create()
                    .show();
        } else {
            requestCameraPermissions();
        }
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return rlEditImage.getVisibility() == View.VISIBLE ? gestureDetector.onTouchEvent(event) : super.onTouchEvent(event);
    }

    @Override
    public void onSwipeLeft() {
        int selectedTabPos = tabLayout.getSelectedTabPosition();
        int nextTabPos = selectedTabPos + 1;

        if (tabLayout.getTabCount() > nextTabPos) {
            tabLayout.getTabAt(nextTabPos).select();
        }
    }

    @Override
    public void onSwipeRight() {
        int selectedTabPos = tabLayout.getSelectedTabPosition();
        int nextTabPos = selectedTabPos - 1;

        if (nextTabPos >= 0) {
            tabLayout.getTabAt(nextTabPos).select();
        }
    }
}
