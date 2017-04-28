package shefa.valio.filtrify.mainscreen;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import shefa.valio.filtrify.R;
import shefa.valio.filtrify.fragments.AboutFragment;
import shefa.valio.filtrify.fragments.AddImageFragment;
import shefa.valio.filtrify.fragments.GPUImageFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
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

        MenuItem item2 = menu.findItem(R.id.action_clear);
        SpannableStringBuilder builder2 = new SpannableStringBuilder("* Clear");
        builder2.setSpan(new ImageSpan(this, R.drawable.ic_clear_black), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item2.setTitle(builder2);

        MenuItem item3 = menu.findItem(R.id.action_about);
        SpannableStringBuilder builder3 = new SpannableStringBuilder("* About");
        builder3.setSpan(new ImageSpan(this, R.drawable.ic_info_outline), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item3.setTitle(builder3);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_save) {
         /*   AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            builder.create().show();*/
        }else if (itemId == R.id.action_clear) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            GPUImageFragment gpuImageFragment = (GPUImageFragment) fragmentManager.findFragmentByTag(GPUImageFragment.TAG);
            if (gpuImageFragment != null) {
                fragmentManager.beginTransaction()
                        .remove(gpuImageFragment)
                        .add(new AddImageFragment(), AddImageFragment.TAG)
                        .commit();
            }
        }else if (itemId == R.id.action_about) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            AboutFragment aboutFragment = (AboutFragment) fragmentManager.findFragmentByTag(AboutFragment.TAG);

            if (aboutFragment == null) {
                fragmentManager.beginTransaction()
                        .add(new AboutFragment(), AboutFragment.TAG)
                        .commit();
            }else {
                fragmentManager.beginTransaction()
                        .show(aboutFragment)
                        .commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
