package com.bcity.betacity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {
 
	// variables required are
	static EditText title;
	static EditText user_email;
	static EditText user_name;
	static EditText desc;
	static Button reportButton;
	static Spinner issuetopic;
	static Spinner issueward;
	


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        title = (EditText) findViewById(R.id.titleText);
        user_email = (EditText) findViewById(R.id.emailText);
        user_name = (EditText) findViewById(R.id.nameText);
        desc = (EditText) findViewById(R.id.descText);
        issuetopic = (Spinner) findViewById(R.id.issuespinner);
        issueward = (Spinner) findViewById(R.id.wardspinner);
        

    }
    
/*
	public class SpinnerActivity extends Activity implements OnItemSelectedListener {
	
	public void onItemSelected(AdapterView<?> parent, View view,
			int pos, long id) {
		Spinner spinner = (Spinner) findViewById(R.id.issuespinner);
		// spinner.setOnItemSelectedListener(this);
		String topic_id = parent.getItemAtPosition(pos) .toString();
		Spinner spinner1 = (Spinner) findViewById(R.id.wardspinner);
		// spinner.setOnItemSelectedListener(this);
		String location = parent.getItemAtPosition(pos) .toString();
			
	}

	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	}
*/    

    public void report_issue (View view) {
    	
    	Intent intent = new Intent(this, ReportIssueActivity.class);
    	intent.putExtras(intent);
  	
    	
    	startActivity(intent);	
    	
    }
/*  	
    	EditText editText = (EditText) findViewById(R.id.titleText);
    	String title = editText.getText() .toString();    	
    	EditText editText1 = (EditText) findViewById(R.id.descText);
    	String desc = editText1.getText() .toString();    	
    	EditText editText2 = (EditText) findViewById(R.id.emailText);
    	String user_email = editText2.getText() .toString();     	
    	EditText editText3 = (EditText) findViewById(R.id.nameText);
    	String name = editText3.getText() .toString();

    
    //public void writeJSON() {
    	JSONObject object = new JSONObject();
    	try {
    		object.put("issue_type_id", " ");
    		object.put("location", issueward );
    		object.put("title", title );
    		object.put("user_email", user_email );
    		object.put("user_name", user_name );
    		object.put("desc", desc );
    		object.put("topic_id", issuetopic );
    		object.put("city_id", "1");
    		} 
    	catch (JSONException e) {
    		    e.printStackTrace();
    		}
    		System.out.println(object);
    		//json.put("url","http://bcity.in:4466/api/issues");

    }
*/   
/*    	HttpClient httpClient = new DefaultHttpClient();

    	try {
            HttpPost request = new HttpPost("http://bcity.in:4466/api/issues");
            StringEntity params =new StringEntity("details={\"issue_type_id\":\" \",\"location\":\"location\",\"title\":\"title\",\"user_email\":\"user_email\",\"desc\":\"desc\",\"topic_id\":\"topic_id\",\"city_id\":\"1\"} ");
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            // handle response here...
        	}
    	catch (Exception ex) {
            // handle exception here
        	} 
    	finally {
            httpClient.getConnectionManager().shutdown();
        	}
*/    
    			  
        		
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

