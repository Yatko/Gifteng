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
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.os.Bundle;
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
import android.widget.ViewSwitcher.ViewFactory;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.listings.GalleryImageAdapter;
import com.venefica.module.listings.post.PostAct.PostListingTask;
import com.venefica.module.main.R;
import com.venefica.module.utils.CameraPreview;
import com.venefica.module.utils.Utility;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Fragment class to capture from camera and pick from gallery
 */
public class PostImagesFragment extends SherlockFragment implements OnClickListener, ViewFactory{

	
	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnPostImagesListener{
		public void onPickFromGallery();
		public void onCameraPick();
		public void onSetCoverImage();
		public void onCameraError(int errorCode);
		public void onNextButtonClick();
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
	/**
	 * Adapter for gallery
	 */
	private GalleryImageAdapter galImageAdapter;
	/**
	 * buttons for step one
	 */
	private ImageButton imgBtnPickGallery, imgBtnPickCamera, imgBtnNextToStep2;

	protected boolean isFirstImage = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//images
		images = new ArrayList<Bitmap>();
		images.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_camera));
		galImageAdapter = new GalleryImageAdapter(getActivity(), null, images, true, true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//set layout for fragment
		View view = inflater.inflate(R.layout.view_camera_post_images,
				container, false);
		imgBtnPickGallery = (ImageButton) view.findViewById(R.id.imgBtnPostListingPickGallery);
		imgBtnPickGallery.setOnClickListener(this);
		imgBtnPickCamera = (ImageButton) view.findViewById(R.id.imgBtnPostListingPickCamera);
		imgBtnPickCamera.setOnClickListener(this);
		imgBtnNextToStep2 = (ImageButton) view.findViewById(R.id.imgBtnPostListingNextToStep2);
		imgBtnNextToStep2.setOnClickListener(this);
		
		cameraPreview = (FrameLayout) view.findViewById(R.id.layViewPostListingCameraPreview);
		imgSwitcher = (ImageSwitcher) view.findViewById(R.id.imgSwitcherActPostListing);
		imgSwitcher.setFactory(this);
		gallery = (Gallery) view.findViewById(R.id.galleryActPostListing);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				if (images != null && images.size() > 0) {
					/*seletedImagePosition = position;
					if (seletedImagePosition == coverImagePosition) {
						txtSetAsCoverImg.setVisibility(View.VISIBLE);
					} else {
						txtSetAsCoverImg.setVisibility(View.GONE);
					}*/
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
		camPreview = new CameraPreview(getActivity());
		camPreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		cameraPreview.addView(camPreview);
		
		//set adapter
		gallery.setAdapter(galImageAdapter);
		
		return view;
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
			putImageInCache(Bitmap.createScaledBitmap(image, Constants.IMAGE_MAX_SIZE_X, Constants.IMAGE_MAX_SIZE_Y, false));
			if (isFirstImage) {
				images.clear();
			}
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
    public void onResume() {
    	super.onResume();
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

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imgBtnPostListingPickGallery) {
			pickImageFromGallery();
		} else if (view.getId() == R.id.imgBtnPostListingPickCamera){
			camera.takePicture(null, null, pictureCallback);
		} else if (view.getId() == R.id.imgBtnPostListingNextToStep2){
			if (!isFirstImage) {
				listener.onNextButtonClick();
				/*gallery.setSelection(0);
				if ( images != null && images.size() > 0) {
					imgSwitcher.setImageDrawable(new BitmapDrawable(images.get(0)));
				}
				if (coverImagePosition == -1) {
					showCoverImgInstructions(getResources().getString(R.string.msg_postlisting_sel_cover_image_instruction));
				}				
				if (camera != null) {
					camera.stopPreview();
				}*/
			}else {
				Utility.showLongToast(getActivity(), getResources().getString(R.string.msg_postlisting_sel_image));
			}			
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
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
					if (isFirstImage) {
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
