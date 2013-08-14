package com.bcity.betacity;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadImage extends Activity {

	private final int CAMERA_CAPTURE = 1;
	private final int GALLERY_PICTURE = 2;
	private Bitmap bitmap;
	public static String encodedImage;
	
	private ImageView userPictureImageView;
	private Intent pictureActionIntent = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		userPictureImageView = (ImageView) findViewById(R.id.add_image);
		startDialog();
		
	}
	@Override	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	
	if (requestCode == GALLERY_PICTURE && null != data ) {
			Uri uri = data.getData();
		if (uri != null) {
			//user had pick an image.
			Cursor cursor = getContentResolver().query(uri, new String[] 
					{ android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
			cursor.moveToFirst();
			//Link to the image
			final String imageFilePath = cursor.getString(0);
			Bitmap b = BitmapFactory.decodeFile(imageFilePath);
			b =Bitmap.createScaledBitmap(b,150, 150, true);
			userPictureImageView.setImageBitmap(b);
			cursor.close();
			bitmap = BitmapFactory.decodeFile(imageFilePath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
			byte[] b1 = baos.toByteArray(); 
			encodedImage = Base64.encodeToString(b1, Base64.DEFAULT);

		}
		else {
				Toast toast = Toast.makeText(this, "No Image is selected.", Toast.LENGTH_LONG);
				toast.show();
		} 
		
	}
	else if (requestCode == CAMERA_CAPTURE && null != data.getExtras()) {
			// here is the image from the camera
			Bitmap bitmap = (Bitmap) data.getExtras().get("data");
			userPictureImageView.setImageBitmap(bitmap);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
			byte[] b = baos.toByteArray(); 
			encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
	} 
	
} 

	private void startDialog() {
		AlertDialog.Builder myalertDialog = new AlertDialog.Builder(this);
		myalertDialog.setTitle("Upload Picture Options");
		myalertDialog.setMessage("How do you want to set your picture");
	
		myalertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
				pictureActionIntent.setType("image/*");
				pictureActionIntent.addCategory(Intent.CATEGORY_OPENABLE);
				pictureActionIntent.putExtra("return-data", true);
				startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
			}	
		});
	
		myalertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(pictureActionIntent, CAMERA_CAPTURE);
			}
		});
		myalertDialog.show();
	}

}
