/**
 * 
 */
package com.venefica.module.listings.post;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.venefica.module.listings.GalleryImageAdapter;
import com.venefica.module.listings.GalleryImageAdapter.OnActionModeListener;
import com.venefica.module.main.R;
import com.venefica.module.utils.CameraPreview;
import com.venefica.module.utils.Utility;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Fragment class to capture from camera and pick from gallery
 */
public class PostImagesFragment extends SherlockFragment implements OnClickListener, ViewFactory, OnActionModeListener{

	
	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnPostImagesListener{
		/**
		 * called on camera start error
		 * @param errorCode
		 */
		public void onCameraError(int errorCode);
		/**
		 * Send images data to activity on next button click
		 * @param imageList
		 * @param images
		 * @param coverImagePosition
		 */
		public void onNextButtonClick(ArrayList<String> imageList, ArrayList<Bitmap> images
				, int coverImagePosition, ArrayList<Long> imagesTodeleteFromServer);
		/**
		 * @return true if user returns/back from preview fragment
		 */
		public boolean isBackFromPreview();
		/** 
		 * Get images to update/delete in update listing mode
		 * @return ArrayList<ImageDto>
		 */
		public List<ImageDto> getImagesToUpdate();
		
		/** get image ids to delete from server
		 * @return ArrayList<Long>
		 */
		public ArrayList<Long> getImagesTodeleteFromServer();
	}
	private static final int REQ_GET_IMAGE = 1002;
	private OnPostImagesListener listener;
	
	/**
	 * surface view for camera in step 1
	 */
	private FrameLayout cameraPreview;
	private Camera camera;
	private CameraPreview camPreview;
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
	private ArrayList<Long> imagesTodeleteFromServer;
	/**
	 * Adapter for gallery
	 */
	private GalleryImageAdapter galImageAdapter;
	/**
	 * buttons for step one
	 */
	private ImageButton imgBtnPickGallery, imgBtnPickCamera, imgBtnNextToStep2;
	/**
	 * true if first photo
	 */
	protected boolean isFirstImage = true;
	/**
	 * ActionMode
	 */
	private ActionMode actionMode;
	/**
	 * action mode visible flag
	 */
	private boolean isActionModeActive = false;
	/**
	 * cover image position
	 */
	private int coverImagePosition = -1;
	/**
	 * root layout
	 */
	private View rootView;
	
	/**
	 * images to update
	 */
	private List<ImageDto> imageDtos;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//images
		imageDtos = new ArrayList<ImageDto>();
		images = new ArrayList<Bitmap>();
		imageList = new ArrayList<String>();
		if (PostListingActivity.getCURRENT_MODE() == PostListingActivity.ACT_MODE_POST_LISTING) {
			images.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_camera));			
		}
		galImageAdapter = new GalleryImageAdapter(getActivity(), imageDtos, images, true, true, true);
		galImageAdapter.setActionModeListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//set layout for fragment
		rootView = inflater.inflate(R.layout.view_camera_post_images,
				container, false);
		imgBtnPickGallery = (ImageButton) rootView.findViewById(R.id.imgBtnPostListingPickGallery);
		imgBtnPickGallery.setOnClickListener(this);
		imgBtnPickCamera = (ImageButton) rootView.findViewById(R.id.imgBtnPostListingPickCamera);
		imgBtnPickCamera.setOnClickListener(this);
		imgBtnNextToStep2 = (ImageButton) rootView.findViewById(R.id.imgBtnPostListingNextToStep2);
		imgBtnNextToStep2.setOnClickListener(this);
		
		cameraPreview = (FrameLayout) rootView.findViewById(R.id.layViewPostListingCameraPreview);
		imgSwitcher = (ImageSwitcher) rootView.findViewById(R.id.imgSwitcherActPostListing);
		imgSwitcher.setFactory(this);
		
		gallery = (Gallery) rootView.findViewById(R.id.galleryActPostListing);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				/*if (PostListingActivity.getCURRENT_MODE() == PostListingActivity.ACT_MODE_UPDATE_LISTING) {
					if (position >= imageDtos.size()) {
						imgSwitcher.setImageDrawable(new BitmapDrawable(getImageFromCache(imageList.get(position - imageDtos.size()))));						
					} else {
						imgSwitcher.setImageDrawable(new BitmapDrawable(getImageFromCache(Constants.PHOTO_URL_PREFIX + imageDtos.get(position).getUrl())));
					}
				}else if (PostListingActivity.getCURRENT_MODE() == PostListingActivity.ACT_MODE_POST_LISTING 
						&& images != null && images.size() > 0) {					
					if (imageList != null) {
						imgSwitcher.setImageDrawable(new BitmapDrawable(getImageFromCache(imageList.get(position))));
					}
				}*/
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		camPreview = new CameraPreview(getActivity());
		camPreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		cameraPreview.addView(camPreview);
		
		//set adapter
		gallery.setAdapter(galImageAdapter);
		
		return rootView;
	}

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	listener = (OnPostImagesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPostImagesListener");
        }
    }
	
	/**
	 * Handle picture callback for camera
	 */
	private PictureCallback pictureCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			imgBtnPickCamera.setEnabled(false);
			Bitmap image = null;
			FileOutputStream outStream = null;
			String path;
			ExifInterface exif = null;
			try {
				path = String.format(Locale.US,
						"/sdcard/img.jpg", System.currentTimeMillis());
				outStream = new FileOutputStream(path);
				outStream.write(data);
				outStream.close();
				//get Exif data
				exif=new ExifInterface(path);
				InputStream is = new FileInputStream(path);
				Rect rect = new Rect(0, 0, Constants.IMAGE_MAX_SIZE_X, Constants.IMAGE_MAX_SIZE_Y);
        	    BitmapFactory.Options opts = new BitmapFactory.Options();
        	    opts.inInputShareable = false;
        	    opts.inSampleSize = 2;
                image = BitmapFactory.decodeStream(is, rect, opts);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){

			    image = rotate(image, 90);
			}else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
				image= rotate(image, 270);
			}else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
				image= rotate(image, 180);
			}
			if (isFirstImage || imageList.size() == 0) {
				images.clear();
			}
			putImageInCache(Bitmap.createScaledBitmap(image, Constants.IMAGE_MAX_SIZE_X, Constants.IMAGE_MAX_SIZE_Y, false));
			
			images.add(Bitmap.createScaledBitmap(image, Constants.IMAGE_THUMBNAILS_WIDTH, Constants.IMAGE_THUMBNAILS_HEIGHT, false));
			galImageAdapter.notifyDataSetChanged();
			image.recycle();			
			isFirstImage = false;
			gallery.setSelection(images.size()-1);
			try {
				camera.startPreview();
			} catch (Exception e) {
			}
			imgBtnPickCamera.setEnabled(true);
		}
	};
	
	/**
	 * Method to rotate given bitmap with specified angle
	 * @param bitmap
	 * @param degree
	 * @return bitmap rotated with specified angle
	 */
	private Bitmap rotate(Bitmap bitmap, int degree) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix mtx = new Matrix();
		mtx.postRotate(degree);

		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}
	/**
	 * Start camera
	 */
	private void startCamera() {
		try {
			if (Utility.checkCameraHardware(getActivity())) {
				camera = Utility.getCameraInstance();
				camPreview.setCamera(camera);
			}
		} catch (Exception e) {
			e.printStackTrace();
			listener.onCameraError(Constants.ERROR_START_CAMERA);
		}
	}
	/**
	 * release the camera for other applications
	 */
	private void releaseCamera(){
        if (camera != null){
            camera.release();
            camera = null;
        }
    }
	/**
	 * get images from cache
	 * @param fileName
	 * @return Bitmap
	 */
	private Bitmap getImageFromCache(String fileName){
		return ((VeneficaApplication)getActivity().getApplication()).getImgManager().getBitmapFromCache(fileName);
	}
	
	/**
	 * Store images in cache
	 * @param bitmap
	 */
	private void putImageInCache(Bitmap bitmap){
		String fileName = "IMG" + System.currentTimeMillis();
		if (imageList == null) {
			imageList = new ArrayList<String>();
		}
		((VeneficaApplication)getActivity().getApplication()).getImgManager().putBitmapInCache(fileName, bitmap);
		imageList.add(fileName);
		bitmap.recycle();
	}
	
	/**
     * Get image
     */
    private void pickImageFromGallery() {
        // Gallery.
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQ_GET_IMAGE);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	if (PostListingActivity.getCURRENT_MODE() == PostListingActivity.ACT_MODE_UPDATE_LISTING) {
    		imagesTodeleteFromServer = listener.getImagesTodeleteFromServer();
    		if (imagesTodeleteFromServer != null) {
    			galImageAdapter.setDeletePositions(imagesTodeleteFromServer);
			}
			imageDtos.clear();
			imageDtos.addAll(listener.getImagesToUpdate());
			galImageAdapter.notifyDataSetChanged();
    	}
    }
    @Override
    public void onResume() {
    	super.onResume();
    	if (listener.isBackFromPreview()) {
			listener.onNextButtonClick(imageList, images, coverImagePosition, imagesTodeleteFromServer);
		}
    	startCamera();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	releaseCamera();
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
	 * @author avinash
	 * Class to handle action modes
	 */
	private final class AnActionModeOfPostImage implements ActionMode.Callback{

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			getSherlockActivity().getSupportMenuInflater().inflate(R.menu.activity_postlisting_gallery_action_modes, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			if (galImageAdapter.getSelectedPositions().size() == 1) {
				menu.findItem(R.id.menu_post_gallery_set_cover_image).setVisible(true);
			} else {
				menu.findItem(R.id.menu_post_gallery_set_cover_image).setVisible(false);
			}			
			isActionModeActive = true;
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			int id = item.getItemId();
			if (id == R.id.menu_post_gallery_delete) {
				if (PostListingActivity.getCURRENT_MODE() == PostListingActivity.ACT_MODE_POST_LISTING) {
					//Delete selected images
					ArrayList<String> listToDelete = new ArrayList<String>();
					ArrayList<Bitmap> imgsToDelete = new ArrayList<Bitmap>();
					for (Integer position : galImageAdapter
							.getSelectedPositions()) {
						listToDelete.add(imageList.get((int) position));
						imgsToDelete.add(images.get((int) position));
					}
					imageList.removeAll(listToDelete);
					images.removeAll(imgsToDelete);
					imageList.trimToSize();
					images.trimToSize();
					listToDelete.clear();
					imgsToDelete.clear();
				} else if (PostListingActivity.getCURRENT_MODE() == PostListingActivity.ACT_MODE_UPDATE_LISTING) {
					for (Integer position : galImageAdapter
							.getSelectedPositions()) {
						int index = 0;
						if ((int)position >= imageDtos.size()) {
							index = ((int) position) - imageDtos.size();
							imageList.remove(index);
							images.remove(index);
						} else {
							if (imagesTodeleteFromServer == null) {
								imagesTodeleteFromServer = new ArrayList<Long>();
							}
							if (!imagesTodeleteFromServer.contains(imageDtos.get(
										(int) position).getId())) {
								imagesTodeleteFromServer.add(imageDtos.get(
										(int) position).getId());
							}
							galImageAdapter.setDeletePositions(imagesTodeleteFromServer);
						
						}
					}
					imageList.trimToSize();
					images.trimToSize();
				}
				if (imageDtos.size() == 0 && imageList.size() == 0 && images.size() == 0 ) {
					images.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_camera));
				}
				galImageAdapter.notifyDataSetChanged();
				gallery.invalidate();
				galImageAdapter.clearSelectedPositions();
			} else if (id == R.id.menu_post_gallery_set_cover_image) {				
				//set cover image in post new mode
				coverImagePosition = galImageAdapter.getSelectedPositions().get(0);
	        	galImageAdapter.setCoverImagePosition(coverImagePosition);
	        	galImageAdapter.notifyDataSetChanged();
	        	gallery.setSelection(coverImagePosition);
				galImageAdapter.clearSelectedPositions();
			}
			if (actionMode != null) {
				actionMode.finish();
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			isActionModeActive = false;
		}		
	}
	/**
     * Helper method to show action modes
     * @param show boolean
     */
    public void showActionMode(boolean show){
    	if (show) {
    		if (!isActionModeActive) {    			
    			actionMode = getSherlockActivity().startActionMode(new AnActionModeOfPostImage());
			} else {
				actionMode.invalidate();
			}  		
		} else if (actionMode != null) {
			actionMode.finish();
		}    	
    }
    
    /**
     * Method to set action mode title
     * @param title CharSequence
     */
    @Override
	public void setActionModeTitle(String title) {
    	if (actionMode != null) {
    		actionMode.setTitle(title);
		}
	}
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imgBtnPostListingPickGallery) {
			pickImageFromGallery();
		} else if (view.getId() == R.id.imgBtnPostListingPickCamera){
			camera.takePicture(null, null, pictureCallback);
		} else if (view.getId() == R.id.imgBtnPostListingNextToStep2){
			if ((!isFirstImage  || PostListingActivity.getCURRENT_MODE() == PostListingActivity.ACT_MODE_UPDATE_LISTING)
					&& (imageDtos.size() > 0 || (imageList.size() > 0 && images.size() > 0))) {
				if (actionMode != null) {
					actionMode.finish();
				}
												
				if (PostListingActivity.getCURRENT_MODE() == PostListingActivity.ACT_MODE_POST_LISTING
						&& coverImagePosition == -1) {
					showCoverImgInstructions(getResources().getString(R.string.msg_postlisting_sel_cover_image_instruction));
				}else {
					listener.onNextButtonClick(imageList, images, coverImagePosition, imagesTodeleteFromServer);
				}				
			}else {
				Utility.showLongToast(getActivity(), getResources().getString(R.string.msg_postlisting_sel_image));
			}			
		}
	}

	/**
	 * Method to show hint message to set cover image
	 * @param message
	 */
	private void showCoverImgInstructions(String message){
		int [] location = new int[2];
		rootView.getLocationOnScreen(location);
		
		LayoutInflater inflater = getSherlockActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.view_set_cover_image,
		                               (ViewGroup) getSherlockActivity().findViewById(R.id.toastPostLayoutRootCoverImg));
		
		TextView text = (TextView) layout.findViewById(R.id.txtPostListingSetCoverMsg);
		text.setText(message);
		Toast toast = new Toast(getSherlockActivity().getApplicationContext());
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 
				location[1] + (gallery.getHeight()/3));
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			//get image from gallery
			if (requestCode == REQ_GET_IMAGE && data != null) {
				try {
					InputStream stream = getActivity().getContentResolver()
							.openInputStream(data.getData());
					Rect rect = new Rect(0, 0, Constants.IMAGE_MAX_SIZE_X,
							Constants.IMAGE_MAX_SIZE_Y);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inInputShareable = false;
					opts.inSampleSize = 2;
					Bitmap bitmap = BitmapFactory.decodeStream(stream, rect,
							opts);
					if (isFirstImage || imageList.size() == 0) {
						images.clear();
					}
					putImageInCache(Bitmap.createScaledBitmap(bitmap,
							Constants.IMAGE_MAX_SIZE_X,
							Constants.IMAGE_MAX_SIZE_Y, false));
					images.add(Bitmap.createScaledBitmap(bitmap,
							Constants.IMAGE_THUMBNAILS_WIDTH,
							Constants.IMAGE_THUMBNAILS_HEIGHT, false));
					galImageAdapter.notifyDataSetChanged();
					bitmap.recycle();
					isFirstImage = false;
					gallery.setSelection(images.size() - 1);
					stream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}	
}
