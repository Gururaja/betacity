package com.bcity.betacity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends Activity {
 
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
	
	private BackGroundTask bgt_issue;
	private BackGroundTask bgt_ward;
	private BackGroundTask bgt_issue_type;
	
	// Fields
	Spinner issuespinner;
	Spinner wardspinner;
	Spinner issuetypespinner;
	
	//ArrayList<Issue> issueList = new ArrayList<Issue>();
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
		
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  
      	buildIssueDropdown();
      	buildWardDropdown();
      	
      	Button addImage = (Button) findViewById(R.id.imagebutton);
      	addImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), UploadImage.class);
				startActivity(intent);
			}
		});
	
      	
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
			
			
			
			for (int i=0; 	i < issue_types.length(); i++) 
			{						
				JSONObject it = issue_types.getJSONObject(i);
				
				//storing each json item in variable
				String agency_id = it.getString(TAG_AGENCY_ID);
				String topic_id = it.getString(TAG_TOPIC_ID);	
				String name = it.getString(TAG_ISSUE_TYPE_NAME);
				String id = it.getString(TAG_ISSUE_TYPE_ID);
				
				if (topic_id.equals(ISSUEID)) {
					
				issue_typeList.add("Other");
				issue_type_idList.add(null);
				topic_idList.add(null);
				agencyidList.add(null);
				
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
						
				//bind adapter to spinner
				wardspinner = (Spinner)findViewById(R.id.wardspinner);
				//define adapter to be used when displaying the ward list
				ArrayAdapter<String> wAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wardList);
				//bind the adapter to the spinner
				wardspinner.setAdapter(wAdapter);
				ward_spinner = 1;
				//set a listener for the selected items in the spinner
				wardspinner.setOnItemSelectedListener(new OnItemSelectedListener()
				{
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
				{
					if (ward_spinner_counter < ward_spinner){
						ward_spinner_counter++;
					} else {
						//Issue selectedIssue = issueList.get(position);
						showToast(wardList.get(position) + " was selected!");
					}
					
					WARDID = ward_idList.get(position);
					WARD_NO = ward_noList.get(position);
					WARD = wardList.get(position);
					LAT = ward_latList.get(position);
					LON = ward_lonList.get(position);
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
			 

   
	public void showToast(String msg) 
	{
		Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
	}

	public void report_issue (View view) {
    	
    	Intent intent = new Intent(this, ReportIssueActivity.class);
    	
    	EditText titleText = (EditText) findViewById(R.id.titleText);
    	title = titleText.getText() .toString();     
    	EditText descText = (EditText) findViewById(R.id.descText);
    	desc = descText.getText() .toString();    	
    	EditText emailText = (EditText) findViewById(R.id.emailText);
    	user_email = emailText.getText() .toString();     	
    	EditText nameText = (EditText) findViewById(R.id.nameText);
    	user_name= nameText.getText() .toString();
    	
    	intent.putExtras(intent);
  	    	
    	startActivity(intent);	
    	
    }
    			  
   
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

