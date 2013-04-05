package com.venefica.module.listings.post;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.listings.GalleryImageAdapter;
import com.venefica.module.main.R;
import com.venefica.services.AdDto;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Fragment class to preview listing before posting
 */
public class PostPreviewFragment extends SherlockFragment implements OnClickListener, ViewFactory {
	
	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnPostPreivewListener{
		/**
		 * Get captured images for preview
		 * @return ArrayList<Bitmap>
		 */
		public ArrayList<Bitmap> getImages();
		/**
		 * Get captured image list for preview
		 * @return ArrayList<String>
		 */
		public ArrayList<String> getImageList();
		/**
		 * get Listing data
		 * @return AdDto
		 */
		public AdDto getListing();
		/**
		 * method to Post listing after preview
		 */
		public void onPostButtonClick();
		/**
		 * set true if preview shown
		 * @param isPreviewVisible
		 */
		public void setPreviewVisible(boolean isPreviewVisible);
		/**
		 * get cover image position
		 * @return int 
		 */
		public int getCoverImagePosition();
	}
	private OnPostPreivewListener listener;
	/**
	 * ImageSwitcher for preview of images in step 2
	 */
	private ImageSwitcher imgSwitcher;
	/**
	 * Gallery view
	 */
	private Gallery gallery;
	/**
	 * Images taken
	 */
	private ArrayList<Bitmap> images;
	/**
	 * image list
	 */
	private ArrayList<String> imageList;
	/**
	 * Adapter for gallery
	 */
	private GalleryImageAdapter galImageAdapter;	
	/**
	 * listing to post
	 */
	private AdDto listing;
	/**
	 * text views
	 */
	private TextView txtTitle, txtDescription, txtCategory, txtCurrentvalue;
	private Button btnPost;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//images
		images = listener.getImages();
		listing = listener.getListing();
		imageList = listener.getImageList();
		galImageAdapter = new GalleryImageAdapter(getActivity(), null, images, true, true, false);		
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// set layout for fragment
		View view = inflater.inflate(R.layout.view_post_listing_preview,
				container, false);
		imgSwitcher = (ImageSwitcher) view.findViewById(R.id.imgSwitcherActPostPreview);
		imgSwitcher.setFactory(this);
		gallery = (Gallery) view.findViewById(R.id.galleryActPostPreview);
		gallery.setAdapter(galImageAdapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				if (images != null && images.size() > 0) {					
					if (imageList != null) {
						imgSwitcher.setImageDrawable(new BitmapDrawable(getImageFromCache(imageList.get(position))));
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
//		imgSwitcher.setImageDrawable(new BitmapDrawable(images.get(0)));
		txtTitle = (TextView) view.findViewById(R.id.txtActPostPreviewTitle);
		txtTitle.setText(listing.getTitle());
		txtDescription = (TextView) view.findViewById(R.id.txtActPostPreviewDescription);
		txtDescription.setText(listing.getDescription());
		txtCategory = (TextView) view.findViewById(R.id.txtActPostPreviewCategory);
		txtCategory.append(":  "+Html.fromHtml("<b>"+listing.getCategory()+"</b>"));
		txtCurrentvalue = (TextView) view.findViewById(R.id.txtActPostPreviewCurrValue);
		txtCurrentvalue.append(":  "+Html.fromHtml("<b>$"+listing.getPrice()+"</b>"));
		btnPost = (Button) view.findViewById(R.id.btnActPostPreviewPost);
		btnPost.setOnClickListener(this);
		return view;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	listener = (OnPostPreivewListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPostPreivewListener");
        }
    }
	@Override
	public void onResume() {
		super.onResume();
		listener.setPreviewVisible(true);
		galImageAdapter.setCoverImagePosition(listener.getCoverImagePosition());
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		images = null;
	}
	/* (non-Javadoc)
	 * @see android.widget.ViewSwitcher.ViewFactory#makeView()
	 */
	@Override
	public View makeView() {
		ImageView img = new ImageView(getActivity());
		img.setBackgroundColor(0xFF000000);
		img.setScaleType(ImageView.ScaleType.CENTER_CROP);
		img.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return img;
	}
	/**
	 * get images from cache
	 * @param fileName
	 * @return Bitmap
	 */
	private Bitmap getImageFromCache(String fileName){
		return ((VeneficaApplication)getActivity().getApplication()).getImgManager().getBitmapFromCache(fileName);
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActPostPreviewPost) {
			listener.onPostButtonClick();
		}
	}
}
