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
		Bundle intent = getIntent().getExtras();
					
		try {
			URL path = new URL("http://bcity.in:4466/api/v1/issues");
			
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
		
	
	
/*
	public static String getStringContent(String uri) throws Exception {

	    try {
	        HttpClient client = new DefaultHttpClient();
	        HttpGet request = new HttpGet();
	        request.setURI(new URI(uri));
	        HttpResponse response = client.execute(request);
	        InputStream ips  = response.getEntity().getContent();
	        BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));

	        StringBuilder sb = new StringBuilder();
	        String s;
	        while(true )
	        {
	            s = buf.readLine();
	            if(s==null || s.length()==0)
	                break;
	            sb.append(s);

	        }
	        buf.close();
	        ips.close();
	        String viewer = new String(sb.toString());
	        return sb.toString();
 	            
	        
	        } 
	    
	    finally {
	
	            }
	           
	} 
	
	public void View(String viewer){
		String se =  new String(viewer.toString());
   		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(se);
		
		setContentView(textView);
	}

*/
	
	
	/*public JSONObject writeJSON() {
		JSONObject object = new JSONObject();
		    try {
		    	object.put("issue_type_id", "76");
		  		object.put("location", "Koramangala" );
		   		object.put("title", "Issue title" );
		   		object.put("user_email", "useremail@gmail.com");
		   		object.put("user_name", "User name");
		   		object.put("desc", "Issue description");
		   		object.put("topic_id", "13");
		   		object.put("city_id", "1");
		   		} 
		   	catch (JSONException e) {
		   		    e.printStackTrace();
		   		}
		   		System.out.println(object);
		return object;
	}*/
	

	/*AsyncHttpClient client = new AsyncHttpClient();
	client.get("http://www.google.com", new AsyncHttpResponseHandler() {
	    @Override
	    public void onSuccess(String response) {
	
	    	
	    	
	    	System.out.println(response);
	    }
	});
*/

	/*		protected void makeRequest(URL path, JSONObject object) throws Exception {
	
	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost("http://bcity.in:4466/api/v1/issues");
	StringEntity  se =  new StringEntity(object.toString());
	HttpResponse response = null;
	
	httppost.setEntity(se);
//	httppost.setHeader("Accept", "application/json");
	httppost.addHeader("content-type", "application/json");
	
	response = httpclient.execute(httppost);
	response.getEntity().consumeContent();
	}
*/			

	
	private class PostReport extends  AsyncTask<String, Void, String> {
		
	
		
		@Override
		protected String doInBackground(String...strings){
			String posted = "Issue has been reported";
			
			JSONObject object1 = new JSONObject();
		    try {
		    	object1.put("issue_type_id", "76");
		  		object1.put("location", "Koramangala" );
		   		object1.put("title", "Issue title" );
		   		object1.put("user_email", "useremail@gmail.com");
		   		object1.put("user_name", "User name");
		   		object1.put("desc", "Issue description");
		   		object1.put("topic_id", "13");
		   		object1.put("city_id", "1");
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
			
			
			
			/*Notification noti = new Notification.Builder(getBaseContext())
	         .setContentTitle("Thank you")
	         .setContentText(posted)
	         .build();*/

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
