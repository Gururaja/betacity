package com.bcity.betacity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class ReportIssueActivity extends Activity {
	
	@SuppressLint("New Api")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_issue);
		Bundle intent = getIntent().getExtras();
		
		String url = "http://bcity.in:4466/api/v1/issues";
		
		
		
	/*	try {
			URL path = new URL("http://bcity.in:4466/api/v1/issues");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/		
		// Show the Up button in the action bar.
		setupActionBar();
	
	}	
	
		// get json through httpget
	public static JSONArray getJSONfromURL(String url){
		InputStream is = null;
		String result = "";
		JSONArray jArray = null;
		
		//http get method
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpGet httpget = new HttpGet(url);
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();

	    }catch(Exception e){
	            Log.e("log_tag", "Error in http connection "+e.toString());
	    }
	    
	  //convert response to string
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e("log_tag", "Error converting result "+e.toString());
	    }
	    
	    try{
	    	
            jArray = new JSONArray(result);            
            String viewer = jArray.toString();
	    }catch(JSONException e){
	            Log.e("log_tag", "Error parsing data "+e.toString());
	    }
    
	    return jArray;
	    
	
	}
	
	public void View(String viewer){
		String se =  new String(viewer.toString());
   		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(se);
		
		setContentView(textView);
	}
	
/*	public static String getStringContent(String uri) throws Exception {

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
	               // any cleanup code...
	            }
	           
	} 
	
	public void View(String viewer){
		String se =  new String(viewer.toString());
   		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(se);
		
		setContentView(textView);
	}
	
/*	public void writeJSON() {
		JSONObject object = new JSONObject();
		    try {
		    	object.put("issue_type_id", " ");
		  		object.put("location", MainActivity.issueward );
		   		object.put("title", MainActivity.title );
		   		object.put("user_email", MainActivity.user_email);
		   		object.put("user_name", MainActivity.user_name);
		   		object.put("desc", MainActivity.desc);
		   		object.put("topic_id", MainActivity.issuetopic);
		   		object.put("city_id", "1");
		   		} 
		   	catch (JSONException e) {
		   		    e.printStackTrace();
		   		}
		   		System.out.println(object);
		   		
/*		   		String se =  new String(object.toString());
		   		TextView textView = new TextView(this);
				textView.setTextSize(40);
				textView.setText(se);
				
				setContentView(textView);
		   		// json.put("url",http://bcity.in:4466/api/issues);
*/

/*	
	public static  HttpResponse makeRequest(URL path, JSONObject object) throws Exception {
			
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(path.toURI());
			StringEntity  se =  new StringEntity(object.toString());
			
			httppost.setEntity(se);
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			
			ResponseHandler responsehandler = new BasicResponseHandler();
			return httpclient.execute(httppost, responsehandler); 
	}
*/	
	
/*	public void postData(URL path, JSONObject object){
		// Create a new HttpClient and Post Header

	    HttpParams myParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(myParams, 10000);
	    HttpConnectionParams.setSoTimeout(myParams, 10000);
	    HttpClient httpclient = new DefaultHttpClient(myParams);
	    String json=object.toString();

	    try {

	        HttpPost httppost = new HttpPost(path.toString());
	        httppost.setHeader("Content-type", "application/json");

	        StringEntity se = new StringEntity(object.toString()); 
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        httppost.setEntity(se); 

	        HttpResponse response = httpclient.execute(httppost);
	        String temp = EntityUtils.toString(response.getEntity());
	        Log.i("tag", temp);


	    } catch (ClientProtocolException e) {

	    } catch (IOException e) {
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