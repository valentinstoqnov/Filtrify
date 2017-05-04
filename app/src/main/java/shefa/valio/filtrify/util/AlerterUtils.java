package shefa.valio.filtrify.util;

import android.app.Activity;
import android.view.View;

import com.tapadoo.alerter.Alerter;

import shefa.valio.filtrify.R;

public class AlerterUtils {

    public static void showMessage(Activity activity, String message) {
        Alerter.create(activity)
                .setIcon(R.drawable.ic_message)
                .setText(message)
                .enableIconPulse(true)
                .setDuration(1900)
                .setBackgroundColor(R.color.deep_orange)
                .show();
    }

    public static void showSaved(Activity activity) {
        Alerter.create(activity)
                .setIcon(R.drawable.ic_happy)
                .setTitle("Saved!")
                .setText("Image successfully saved")
                .setBackgroundColor(R.color.green)
                .setDuration(1100)
                .enableIconPulse(true)
                .show();
    }

    public static void showError(Activity activity) {
        Alerter.create(activity)
                .setTitle("Error")
                .setText("Something went wrong")
                .setDuration(1300)
                .setBackgroundColor(R.color.red)
                .setIcon(R.drawable.ic_sad)
                .enableIconPulse(true)
                .show();
    }
}
