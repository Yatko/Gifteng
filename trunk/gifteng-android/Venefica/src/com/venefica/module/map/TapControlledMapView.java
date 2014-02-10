package com.venefica.module.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

/**
 * @author avinash
 * Custom map view to handle tap events 
 */
public class TapControlledMapView extends MapView implements OnGestureListener {

    private GestureDetector gd;    
    private OnSingleTapListener singleTapListener;

	/** Constructor
	 * @param context
	 * @param attrs
	 */
	public TapControlledMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupGestures();
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     * @param defStyle
     */
    public TapControlledMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupGestures();
    }

    /**
     * Constructor
     * @param context
     * @param apiKey
     */
    public TapControlledMapView(Context context, String apiKey) {
        super(context, apiKey);
        setupGestures();
    }
    
    /**
     * Method to handle events according to gesture
     */
    private void setupGestures() {
    	gd = new GestureDetector(this);  
        
        //set the on Double tap listener  
        gd.setOnDoubleTapListener(new OnDoubleTapListener() {

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if (singleTapListener != null) {
					return singleTapListener.onSingleTap(e);
				} else {
					return false;
				}
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				TapControlledMapView.this.getController().zoomInFixing((int) e.getX(), (int) e.getY());
				return false;
			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				return false;
			}
        	
        });
    }
    
	/* (non-Javadoc)
	 * @see com.google.android.maps.MapView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (this.gd.onTouchEvent(ev)) {
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
	}
	
	/**
	 * Set SingleTapListener for map
	 * @param singleTapListener
	 */
	public void setOnSingleTapListener(OnSingleTapListener singleTapListener) {
		this.singleTapListener = singleTapListener;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onShowPress(android.view.MotionEvent)
	 */
	@Override
	public void onShowPress(MotionEvent e) {}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public void onLongPress(MotionEvent e) {}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
    
}


