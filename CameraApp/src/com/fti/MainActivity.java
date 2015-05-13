package com.fti;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	final static int CAMERA_RESULT = 0;
	ImageView imv;
	String imageFilePath;
	boolean isImageFitToScreen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageFilePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/myfavoritepicture.jpg";
		File imageFile = new File(imageFilePath);
		Uri imageFileUri = Uri.fromFile(imageFile);

		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(i, CAMERA_RESULT);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			// Get a reference to the ImageView
			imv = (ImageView) findViewById(R.id.ReturnedImageView);
			Display currentDisplay = getWindowManager().getDefaultDisplay();
			int dw = currentDisplay.getWidth();
			int dh = currentDisplay.getHeight();
			// Load up the image's dimensions not the image itself
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeFile(imageFilePath,
					bmpFactoryOptions);
			int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
					/ (float) dh);
			int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
					/ (float) dw);

			if (heightRatio > 1 && widthRatio > 1) {
				if (heightRatio > widthRatio) {
					// Height ratio is larger, scale according to it
					bmpFactoryOptions.inSampleSize = heightRatio;
				} else {
					// Width ratio is larger, scale according to it
					bmpFactoryOptions.inSampleSize = widthRatio;
				}
			}
			// Decode it for real
			bmpFactoryOptions.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
			// Display it
			imv.setImageBitmap(bmp);
			
			
			
			imv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isImageFitToScreen){
						isImageFitToScreen = false;
						imv.setLayoutParams(new LinearLayout
								.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
										LinearLayout.LayoutParams.WRAP_CONTENT));
						imv.setAdjustViewBounds(true);
					}else{
						isImageFitToScreen=true;
						imv.setLayoutParams(new LinearLayout
								.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
										LinearLayout.LayoutParams.MATCH_PARENT));
						imv.setScaleType(ImageView.ScaleType.FIT_XY);
					}
				}
			});
		}
	}
}
