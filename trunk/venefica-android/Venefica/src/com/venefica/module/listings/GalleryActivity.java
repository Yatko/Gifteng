package com.venefica.module.listings;

import java.util.ArrayList;
import java.util.List;

import com.venefica.module.main.R;
import com.venefica.module.main.R.layout;
import com.venefica.module.main.R.menu;

import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class GalleryActivity extends Activity implements ViewFactory {
	/**
	 * Gallery to images
	 */
	private Gallery gallery;

    private int selectedImagePosition = 0;
    /**
     * Images
     */
    private List<Drawable> drawables;
    /**
     * Adapter for gallery
     */
    private GalleryImageAdapter galImageAdapter;
    /**
     * Image switcher
     */
    private ImageSwitcher imgSwitcher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
      //Gallery
        gallery = (Gallery) findViewById(R.id.galleryActGallery);
        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				imgSwitcher.setImageDrawable(drawables.get(position));
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				imgSwitcher.setVisibility(View.GONE);
			}
		});
        drawables = getImages();
        galImageAdapter = new GalleryImageAdapter(this, drawables);
        gallery.setAdapter(galImageAdapter);
        
        imgSwitcher = (ImageSwitcher) findViewById(R.id.switcherActGallery);
        imgSwitcher.setFactory(this);
        imgSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        imgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
    }

    private List<Drawable> getImages() {
		drawables = new ArrayList<Drawable>();
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
		return drawables;
	}
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gallery, menu);
        return true;
    }*/

	public View makeView() {
		ImageView img = new ImageView(this);
		img.setBackgroundColor(0xFF000000);
		img.setScaleType(ImageView.ScaleType.FIT_CENTER);
		img.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return img;
	}
}
