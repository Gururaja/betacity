package com.bcity.betacity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {
	
	//json objects
	static String title;
	static String user_email;
	static String user_name;
	static String desc;
	static Button reportButton;
	static Spinner issuetopic;
	static Spinner issueward;
	static String ISSUEID;
	static String CITYID;
	static String WARD;
	static String WARDID;
	static String WARD_NO;
	static int WARDPOSITION;
	static String LAT;
	static String LON;
	static String ISSUETYPEID;
	
	// JSON Nodes
	private static final String TAG_CITY = "city_id";
	private static final String TAG_TOPIC = "name";
	private static final String TAG_ISSUE_ID = "id";
	private static final String TAG_ISSUE_API = "http://bcity.in:4466/api/v1/topics";

	private static final String TAG_AGENCY_ID = "agency_id";
	private static final String TAG_TOPIC_ID = "topic_id";
	private static final String TAG_ISSUE_TYPE_NAME = "name";
	private static final String TAG_ISSUE_TYPE_ID = "id";
	private static final String TAG_ISSUE_TYPE_API = "http://bcity.in:4466/api/v1/issue_types";
	
	private static final String TAG_WARD_NO = "ward_no";
	private static final String TAG_WARD_NAME = "name";
	private static final String TAG_WARD_ID = "id";
	private static final String TAG_LAT = "lat";
	private static final String TAG_LON = "lon";
	private static final String TAG_WARD_API = "http://bcity.in:4466/api/v1/wards";
	
	//background
	private BackGroundTask bgt_issue;
	private BackGroundTask bgt_ward;
	private BackGroundTask bgt_issue_type;
	
	// Fields
	Spinner issuespinner;
	AutoCompleteTextView wardspinner;
	Spinner issuetypespinner;
	
	//lists
	static ArrayList<String> issueList = new ArrayList<String>();
	static ArrayList<String> issueidList = new ArrayList<String>();
	static ArrayList<String> cityidList = new ArrayList<String>();
	
	static ArrayList<String> wardList = new ArrayList<String>();
	static ArrayList<String> ward_idList = new ArrayList<String>();
	static ArrayList<String> ward_noList = new ArrayList<String>();
	static ArrayList<String> ward_latList = new ArrayList<String>();
	static ArrayList<String> ward_lonList = new ArrayList<String>();
	
	static ArrayList<String> issue_type_idList = new ArrayList<String>();
	static ArrayList<String> agencyidList = new ArrayList<String>();
	static ArrayList<String> topic_idList = new ArrayList<String>();
	static ArrayList<String> issue_typeList = new ArrayList<String>();
	
	int issue_spinner = 0, issue_spinner_counter = 0;
	int issuetype_spinner = 0, issuetype_spinner_counter = 0;
	int ward_spinner = 0, ward_spinner_counter = 0;
	
	//upload image
	private final int CAMERA_CAPTURE = 1;
	private final int GALLERY_PICTURE = 2;
	static Bitmap bitmap;
	static String encodedImage;
	static String imageName;
	static String imageFilePath;
	
	private ImageView userPictureImageView;
	private Intent pictureActionIntent = null;
	
	//views
	private View StatusView;
	private View FormView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  
        
        StatusView = findViewById(R.id.StatusView);
        FormView = findViewById(R.id.FormView);
      	
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent)
        {
        clear_all_List();
        buildIssueDropdown();
      	buildWardDropdown();
      	
      	userPictureImageView = (ImageView) findViewById(R.id.add_image);
      	Button addImage = (Button) findViewById(R.id.imagebutton);
        addImage.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startDialog();
        		}
        });
        } else {
        	showAlertDialog(this,"No Internet Connection",
        			"Please connect to internet.");
        }
        
    }
	

	private void clear_all_List() {
		issueList.clear();
		issueidList.clear();
		cityidList.clear();
		wardList.clear();
		ward_idList.clear();
		ward_noList.clear();
		ward_latList.clear();
		ward_lonList.clear();	
		issue_type_idList.clear();
		agencyidList.clear();
		topic_idList.clear();
		issue_typeList.clear();
	
		 ScrollView view = (ScrollView)findViewById(R.id.FormView);
		    view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		    view.setFocusable(true);
		    view.setFocusableInTouchMode(true);
		    view.setOnTouchListener(new View.OnTouchListener() {
		        @Override
		        public boolean onTouch(View v, MotionEvent event) {
		            v.requestFocusFromTouch();
		            return false;
		        }
		    });
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.show();
	}
	
	public void buildIssueDropdown() 
	{
		//building post parameters, key and value pair
		ArrayList<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
		apiParams.add(new BasicNameValuePair("call", "issueList"));
		
		bgt_issue = new BackGroundTask(TAG_ISSUE_API, "GET", apiParams);
		
		try {
			JSONArray issues = bgt_issue.execute().get();
			
			issueList.add("Select Topic");
			issueidList.add(null);
			cityidList.add(null);
			
			for (int i=0; 	i < issues.length(); i++) 
			{
				
				JSONObject ii = issues.getJSONObject(i);
				
				//storing each json item in variable
				String id = ii.getString(TAG_ISSUE_ID);
				String name = ii.getString(TAG_TOPIC);
				String cityid = ii.getString(TAG_CITY);
			
				issueList.add(name);
				issueidList.add(id);
				cityidList.add(cityid);
			}
			
			//bind adapter to spinner
			issuespinner = (Spinner)findViewById(R.id.issuespinner);
			//define adapter to be used when displaying the issue list
			ArrayAdapter<String> iAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, issueList);
			//bind the adapter to the spinner
			issuespinner.setAdapter(iAdapter);
			issue_spinner = 1;
			//set a listener for the selected items in the spinner
			issuespinner.setOnItemSelectedListener(new OnItemSelectedListener()
			{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{
			
				if (issue_spinner_counter < issue_spinner){
				issue_spinner_counter++;
				} else {
						showToast_i(issueList.get(position) + " was selected!");
				}
				ISSUEID = issueidList.get(position);
				CITYID = cityidList.get(position);
				issue_typeList.clear();
				buildIssueTypeDropdown();
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
				return;
			}
			});
			
		} 	catch (JSONException e) {
			e.printStackTrace();
			} catch (InterruptedException e) {
			e.printStackTrace();
			} catch (ExecutionException e) {
			e.printStackTrace();
			}
	}
			 
			 public void showToast_i(String msg) 
			 {
			  Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
			 }
						 

	public void buildIssueTypeDropdown()
	{
		//building post parameters, key and value pair
		ArrayList<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
		apiParams.add(new BasicNameValuePair("call", "issuetypeList"));
				
		bgt_issue_type = new BackGroundTask(TAG_ISSUE_TYPE_API, "GET", apiParams);
				
	try 
		{
			JSONArray issue_types = bgt_issue_type.execute().get();
			
			issue_typeList.add("Other");
			issue_type_idList.add(null);
			topic_idList.add(null);
			agencyidList.add(null);
			
			for (int i=0; 	i < issue_types.length(); i++) 
			{						
				JSONObject it = issue_types.getJSONObject(i);
				
				//storing each json item in variable
				String agency_id = it.getString(TAG_AGENCY_ID);
				String topic_id = it.getString(TAG_TOPIC_ID);	
				String name = it.getString(TAG_ISSUE_TYPE_NAME);
				String id = it.getString(TAG_ISSUE_TYPE_ID);
				
				if (topic_id.equals(ISSUEID)) {				
				issue_typeList.add(name);
				issue_type_idList.add(id);
				topic_idList.add(topic_id);
				agencyidList.add(agency_id);	
				} 
			}
					
		//bind adapter to spinner
		issuetypespinner = (Spinner)findViewById(R.id.issuetypespinner);
		//define adapter to be used when displaying the ward list
		ArrayAdapter<String> itAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, issue_typeList);
		//bind the adapter to the spinner
		issuetypespinner.setAdapter(itAdapter);
		issuetype_spinner = 1;
		//set a listener for the selected items in the spinner
		issuetypespinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
		{
			if (issuetype_spinner_counter < issuetype_spinner){
				issuetype_spinner_counter++;
			} else {
				//Issue selectedIssue = issueList.get(position);
				showToast(issue_typeList.get(position) + " was selected!");		
			}
			ISSUETYPEID = issue_type_idList.get(position);
			}
				
		@Override
		public void onNothingSelected(AdapterView<?> parent) 
		{
			return;
		}
		});
		}		catch (JSONException e){
				e.printStackTrace();
				} catch (InterruptedException e){
				e.printStackTrace();
				} catch (ExecutionException e){
				e.printStackTrace();
				}				 
					
	}
			 		 
	public void buildWardDropdown() 
		{
		//building post parameters, key and value pair
		ArrayList<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
		apiParams.add(new BasicNameValuePair("call", "wardList"));
				
		bgt_ward = new BackGroundTask(TAG_WARD_API, "GET", apiParams);
				
		try {
			JSONArray wards = bgt_ward.execute().get();
			//Getting Array of Wards
			//looping through all wards
			wardList.add("Select Ward");
			ward_idList.add(null);
			ward_noList.add(null);
			ward_latList.add(null);
			ward_lonList.add(null);
			
			for (int i=0; 	i < wards.length(); i++) 
			{						
				JSONObject ww = wards.getJSONObject(i);
				
				//storing each json item in variable
				String name = ww.getString(TAG_WARD_NAME);
				String id = ww.getString(TAG_WARD_ID);	
				String ward_no = ww.getString(TAG_WARD_NO);
				String lat = ww.getString(TAG_LAT);
				String lon = ww.getString(TAG_LON);
										
				wardList.add(name);
				ward_idList.add(id);
				ward_noList.add(ward_no);
				ward_latList.add(lat);
				ward_lonList.add(lon);
			}
			
			ArrayAdapter<String> wAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, wardList);
			wardspinner = (AutoCompleteTextView) findViewById(R.id.wardspinner);
			wardspinner.setThreshold(1);
			wardspinner.setAdapter(wAdapter);
			
		} 	catch (JSONException e) {
			e.printStackTrace();
			} catch (InterruptedException e) {
			e.printStackTrace();
			} catch (ExecutionException e) {
			e.printStackTrace();
			}
		}
		 
   
	public void showToast(String msg) 
	{
		Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
	}

	
	@Override	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	try {
	super.onActivityResult(requestCode, resultCode, data);
	
	if (requestCode == GALLERY_PICTURE && null != data ) {
			Uri uri = data.getData();
		if (uri != null) {
			//user had pick an image.
			String[] projection = { MediaStore.Images.Media.DATA };
			@SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			
			//Link to the image
			imageFilePath = cursor.getString(column_index);
		if (imageFilePath !=null) {
			bitmap = BitmapFactory.decodeFile(imageFilePath);
			bitmap =Bitmap.createScaledBitmap(bitmap,150, 150, true);
			userPictureImageView.setImageBitmap(bitmap);
			cursor.close();
					
			bitmap = BitmapFactory.decodeFile(imageFilePath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is the bitmap object   
			byte[] b1 = baos.toByteArray(); 
			encodedImage = Base64.encodeToString(b1, Base64.DEFAULT);
		}else {
			Toast toast = Toast.makeText(this, "Cannot upload images from Picasa web-album.", Toast.LENGTH_LONG);
			toast.show();			
		}
		}else {
				Toast toast = Toast.makeText(this, "No Image is selected.", Toast.LENGTH_LONG);
				toast.show();
		} 
		
	}else if (requestCode == CAMERA_CAPTURE && null != data.getExtras()) {

			// here is the image from the camera
			bitmap = (Bitmap) data.getExtras().get("data");
			try {
				saveToFileAndUri();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userPictureImageView.setImageBitmap(bitmap);
			imageName =Images.Media.DISPLAY_NAME;
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is the bitmap object   
			byte[] b = baos.toByteArray(); 
			encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
	}
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	
} 

	private Uri saveToFileAndUri() throws Exception{
        long currentTime = System.currentTimeMillis();
        String fileName = "bcity_"+currentTime+".jpg";
        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsoluteFile()+"/BCITY_DIRECTORY");
        if(!file.exists()){
            if(!file.mkdirs()){
                throw new Exception("Could not create directories, "+file.getAbsolutePath());
            }
        }
        String filePath = file.getAbsolutePath()+"/"+fileName;
        FileOutputStream out = new FileOutputStream(filePath);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);     //control the jpeg quality
        out.flush();
        out.close();

        long size = new File(filePath).length();

        ContentValues values = new ContentValues(6);
        values.put(Images.Media.TITLE, fileName);

        // That filename is what will be handed to Gmail when a user shares a
        // photo. Gmail gets the name of the picture attachment from the
        // "DISPLAY_NAME" field.
        values.put(Images.Media.DISPLAY_NAME, fileName);
        values.put(Images.Media.DATE_ADDED, currentTime);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        values.put(Images.Media.ORIENTATION, 0);
        values.put(Images.Media.DATA, filePath);
        values.put(Images.Media.SIZE, size);

        return MainActivity.this.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);

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

	
	public void report_issue (View view) {
		try {
	    	showProgress(true);

			new URL("http://bcity.in/api/v1/issues");
	    	EditText titleText = (EditText) findViewById(R.id.titleText);
	    	title = titleText.getText() .toString();     
	    	EditText descText = (EditText) findViewById(R.id.descText);
	    	desc = descText.getText() .toString();    	
	    	EditText emailText = (EditText) findViewById(R.id.emailText);
	    	user_email = emailText.getText() .toString();     	
	    	EditText nameText = (EditText) findViewById(R.id.nameText);
	    	user_name= nameText.getText() .toString();
	    	
	    	
	    	WARD = wardspinner.getEditableText().toString();
			WARDPOSITION = wardList.indexOf(WARD);
	    	WARDID = ward_idList.get(WARDPOSITION);
			WARD_NO = ward_noList.get(WARDPOSITION);
			LAT = ward_latList.get(WARDPOSITION);
			LON = ward_lonList.get(WARDPOSITION);

			IssueReport report = new IssueReport();
			report.execute();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Shows the progress UI and hides the issue form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			StatusView.setVisibility(View.VISIBLE);
			StatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							StatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			FormView.setVisibility(View.VISIBLE);
			FormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							FormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			StatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			FormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	public class IssueReport extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String...strings){
			String posted = "Issue has been reported.";
			
			JSONObject object1 = new JSONObject();
			JSONObject issue = new JSONObject();
			JSONObject coordinate_pair_attributes = new JSONObject();
			JSONObject picture = new JSONObject();
			
		    try {
		    	
		    	picture.put("file_name", "image.jpg");
		    	picture.put("data", encodedImage);
		    	
		    	coordinate_pair_attributes.put("lon", LON);
		   		coordinate_pair_attributes.put("lat", LAT);
		   		
		    	issue.put("issue_type_id", ISSUETYPEID);
		  		issue.put("ward_name", WARD);
		  		issue.put("ward_id", WARDID);
		   		issue.put("title", title);
		   		issue.put("user_email", user_email);
		   		issue.put("user_name", user_name);
		   		issue.put("desc", desc);
		   		issue.put("topic_id", ISSUEID);
		   		issue.put("city_id", CITYID);
		   		issue.put("coordinate_pair_attributes", coordinate_pair_attributes);
		   		//sCNMh7Tx2QCw4z96QUgB
		   		//K4qoqAor51UqSRmVgcFy
		   		object1.put("token", "K4qoqAor51UqSRmVgcFy");
		   		object1.put("issue", issue);
		   		object1.put("picture", picture);
		   		
		    	} 
		   	catch (JSONException e) {
		   		    e.printStackTrace();
		   		}
		   		System.out.println(object1);
		
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://bcity.in/api/v1/issues");
			StringEntity se;
			try {
				se = new StringEntity(object1.toString());
			
			HttpResponse response = null;
			
			httppost.setEntity(se);
//			httppost.setHeader("Accept", "application/json");
			httppost.addHeader("content-type", "application/json");
			
			response = httpclient.execute(httppost);
			response.getEntity().consumeContent();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return posted;
			}

		@Override
		@SuppressLint("NewApi")
		protected void onPostExecute(String posted) { 
			showProgress(false);
			showPostedDialog(MainActivity.this,"Done", posted);
					
			}

		@SuppressWarnings("deprecation")
		public void showPostedDialog(Context issueReport, String title, String message) {
			AlertDialog alertDialog = new AlertDialog.Builder(issueReport).create();
			
			alertDialog.setTitle(title);
			alertDialog.setMessage(message);
			//alertDialog.setIcon(R.drawable.fail);
			
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					Context appContext = getBaseContext().getApplicationContext();
					Intent i = appContext .getPackageManager().getLaunchIntentForPackage(appContext.getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					
				}
			});
			alertDialog.show();
		}


	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
    
}

