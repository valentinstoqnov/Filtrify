package shefa.valio.filtrify.mainscreen;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oswaldogh89.library.LatestImages;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import shefa.valio.filtrify.R;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 12312;

    @BindView(R.id.latest_images) protected LatestImages latestImages;
    @BindView(R.id.img_view_main) protected ImageView imageView;

    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout bottomSheetLayout = ButterKnife.findById(this, R.id.bottom_sheet_image_options);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        ButterKnife.bind(this);

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
            imageView.setImageURI(uri);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    @OnClick(R.id.tv_bottom_sheet_peek)
    public void onBottomSheetPeekClick() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @OnClick(R.id.btn_bottom_sheet_camera)
    public void onCameraClick() {
        takeAPicture();
    }

    @OnClick(R.id.btn_bottom_sheet_image_link)
    public void onImageLinkClick() {
        showLinkDialog();
    }

    @OnClick(R.id.btn_bottom_sheet_images)
    public void onImagePickerClick() {
        showImagePicker();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showImagePicker() {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1);
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
    }

    private void takeAPicture() {
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
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_CONTACTS},
                                        REQUEST_IMAGE_CAPTURE);
                            }
                        }).setCancelable(true);
                builder.create().show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_IMAGE_CAPTURE);
            }
        }else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                showMessage("No camera available");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } else {
                        showMessage("No camera available");
                    }
                } else {
                    showMessage("Can't use this option");
                }
                break;
            }
        }
    }

    private void showLinkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter the link to the image");
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = editText.getText().toString();
                if (!url.isEmpty()) {
                    Picasso.with(MainActivity.this)
                            .load(url)
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    showMessage("Couldn't get the image");
                                }
                            });
                }else {
                    showMessage("Empty link entered");
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        builder.create().show();
    }
}
