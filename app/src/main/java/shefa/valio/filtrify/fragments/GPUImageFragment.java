package shefa.valio.filtrify.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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
import shefa.valio.filtrify.SwipeGestureDetector;

public class GPUImageFragment extends Fragment implements SwipeGestureDetector.OnSwipeListener{

    public static final String TAG = "GPUImageFragment";

    @BindView(R.id.gpu_image_view) GPUImageView gpuImageView;
    private GestureDetector gestureDetector;
    private TabLayout tabLayout;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        unbinder = ButterKnife.bind(this, view);
        gestureDetector = new GestureDetector(getContext(), new SwipeGestureDetector(this));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.VISIBLE);
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
                    case 8:
                        gpuImageView.setFilter(new GPUImageColorBalanceFilter());
                        break;
                    case 9:
                        gpuImageView.setFilter(new GPUImageColorMatrixFilter());
                        break;
                    case 10:
                        gpuImageView.setFilter(new GPUImageHighlightShadowFilter());
                        break;
                    case 11:
                        gpuImageView.setFilter(new GPUImageKuwaharaFilter());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
