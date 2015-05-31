package com.example.thitruong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.thitruong.ListProduct.JSONChart;
import com.example.thitruong.ListProduct.JSONParser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class Resultsearch extends ActionBarActivity {
	   
    Intent inten2t;

    InputStream is = null;
	JSONObject jObj = null;
	String json = "";
	GridView gridview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultsearch);
		Intent intenttemp = getIntent();
		String key = intenttemp.getStringExtra("key");

		inten2t = new Intent(this, Detail.class);
		TextView txtKey = (TextView)findViewById(R.id.txtKey);
		txtKey.setText("Kết quả: "+ key);
		new JSONParser().execute("http://khachung.com/android/result.php?key="+key); 
		
		long date = System.currentTimeMillis(); 
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a, dd - MM - yyyy");
		String dateString = sdf.format(date);   
		TextView ftext =(TextView) findViewById(R.id.txtText);
		ftext.setText("Hiện tại: " + dateString + "  ");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resultsearch, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//Lớp xử lý đa tiến trình về JSON :
	public class JSONParser extends AsyncTask <String, JSONObject, String>{
		// constructor
		public JSONParser() {
	
		}
		@Override
		protected String doInBackground(String... params) {
		    // Making HTTP request
		    try {
		    	
		        // defaultHttpClient
		        DefaultHttpClient httpClient = new DefaultHttpClient();
		        HttpGet httpPost = new HttpGet(params[0]);
		            HttpResponse getResponse = httpClient.execute(httpPost);
		           final int statusCode = getResponse.getStatusLine().getStatusCode();
		           
		           if (statusCode != HttpStatus.SC_OK) { 
		              Log.d(getClass().getSimpleName(), 
		                  "Error " + statusCode + " for URL " + params[0]); 
		              return null;
		           }
		           Log.d("success: ", "Text");
		           HttpEntity getResponseEntity = getResponse.getEntity();

		        is = getResponseEntity.getContent();            
		        //doc du lieu
                BufferedReader reader=
                              new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb=new StringBuilder();
                String line=null;
                while((line=reader.readLine())!=null)
                {
                       sb.append(line+"\n");
                }
                is.close();
                json=sb.toString(); //doc StringBuilder vao chuoi
                
                
                jObj = new JSONObject(json);
                
                publishProgress(jObj);
		    } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		    } catch (ClientProtocolException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        Log.d("IO", e.getMessage().toString());
		        e.printStackTrace();
	
		    } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    // return JSON String
		    return jObj.toString();
		}

		private  String[] a1;
		private  String[] a2;
		private  String[] id2;
		protected void onProgressUpdate(JSONObject... values) {
			super.onProgressUpdate(values);
			//ta cập nhật giao diện ở đây:
			JSONObject jsonObj=values[0];

			try {
			
				if(jObj.has("PRODUCT")){
					JSONArray arrJson = jObj.getJSONArray("PRODUCT");
					a1 = new String[arrJson.length()];
					a2 = new String[arrJson.length()];
					id2 = new String[arrJson.length()];
						for	(int i=0; i < arrJson.length(); i++){
							JSONObject objProduct= arrJson.getJSONObject(i);
				            if(objProduct.has("name_product")){
				            	id2[i] = objProduct.getString("id_product");
				            	a1[i] = objProduct.getString("name_product");
				            	a2[i] = objProduct.getString("image_product");
				              }
				        }
						
						//Lấy danh sách các sản phẩm
						CustomList adapter = new CustomList(Resultsearch.this, a1, a2);
						gridview = (GridView) findViewById(R.id.gridview);
						
						gridview.setAdapter(adapter);
						
						gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			                @Override
			                public void onItemClick(AdapterView<?> parent, View view,
			                                        int position, long id) {
			                	inten2t.putExtra("IDProduct", id2[+position]);
			                	startActivity(inten2t);
			                }
			            });
					}
			} catch (JSONException e) {
				Toast.makeText(Resultsearch.this, e.toString(), 
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		
		protected void onPostExecute(String page)
		{   
		    //onPostExecute
		}   
	}
	
		
}
