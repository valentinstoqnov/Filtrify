package shefa.valio.filtrify;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureDetector implements GestureDetector.OnGestureListener {

    private OnSwipeListener listener;

    public SwipeGestureDetector(OnSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float velocityX, float velocityY) {
        if(motionEvent1.getX() - motionEvent2.getX() > 50){
            listener.onSwipeLeft();
            return true;
        }

        if(motionEvent2.getX() - motionEvent1.getX() > 50) {
            listener.onSwipeRight();
            return true;
        }

        return false;
    }

    public interface OnSwipeListener {

        void onSwipeLeft();

        void onSwipeRight();
    }
}
