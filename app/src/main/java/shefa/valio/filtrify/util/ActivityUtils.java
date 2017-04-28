package shefa.valio.filtrify.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by valio_stoyanov on 28.04.17.
 */

public class ActivityUtils {

    public static void addFragmentToActivityWithTag (FragmentManager fragmentManager, Fragment fragment, int frameId, String tag) {
        fragmentManager.beginTransaction().add(frameId, fragment, tag).commit();
    }

    public static void hideFragments(FragmentManager fm, String... tags) {
        for (String tag : tags) {
            Fragment fragment = fm.findFragmentByTag(tag);

            if (fragment != null && !fragment.isHidden()) {
                fm.beginTransaction()
                        .hide(fragment)
                        .commit();
            }
        }
    }

    public static void showFragment(FragmentManager fm, Fragment fragment) {
        if (!fragment.isVisible()) {
            fm.beginTransaction()
                    .show(fragment)
                    .commit();
        }
    }
}
