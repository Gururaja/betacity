package com.bcity.betacity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;



public class ReportIssueActivity extends Activity {
	
	@SuppressLint("New Api")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_issue);
		getIntent().getExtras();
					
		try {
			new URL("http://bcity.in:4466/api/v1/issues");
			
			PostReport report = new PostReport();
			report.execute();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		// Show the Up button in the action bar.
		setupActionBar();
	
	}	
		
	
	private class PostReport extends  AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String...strings){
			String posted = "Issue has been reported";
			
			JSONObject object1 = new JSONObject();
			JSONObject issue = new JSONObject();
			JSONObject coordinate_pair_attributes = new JSONObject();
			JSONObject picture = new JSONObject();
			
		    try {
		    	
		    	picture.put("filename", "");
		    	picture.put("data", "");
		    	
		    	coordinate_pair_attributes.put("lon", MainActivity.LON);
		   		coordinate_pair_attributes.put("lat", MainActivity.LAT);
		   		
		    	issue.put("issue_type_id", MainActivity.ISSUETYPEID);
		  		issue.put("ward_name", MainActivity.WARD);
		  		issue.put("ward_id", MainActivity.WARDID);
		   		issue.put("title", MainActivity.title);
		   		issue.put("user_email", MainActivity.user_email);
		   		issue.put("user_name", MainActivity.user_name);
		   		issue.put("desc", MainActivity.desc);
		   		issue.put("topic_id", MainActivity.ISSUEID);
		   		issue.put("city_id", MainActivity.CITYID);
		   		issue.put("coordinate_pair_attributes", coordinate_pair_attributes);
		   		
		   		object1.put("token", "sCNMh7Tx2QCw4z96QUgB");
		   		object1.put("issue", issue);
		   		object1.put("picture", picture);
		   		
		    	} 
		   	catch (JSONException e) {
		   		    e.printStackTrace();
		   		}
		   		System.out.println(object1);
		
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://bcity.in:4466/api/v1/issues");
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
			
			TextView txt = (TextView) findViewById(R.id.posted);
	        txt.setText(posted);
			
			}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}
*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
