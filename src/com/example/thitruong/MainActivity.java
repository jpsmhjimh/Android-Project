package com.example.thitruong;

import android.R.integer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.MenuItem;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.view.*;

import java.io.*;

import org.json.*;

import android.content.res.Resources;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;






import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;











import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
 


import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
 
public class MainActivity extends ActionBarActivity {

	TextView txtText;
	String urlImage;

    InputStream is = null;
	JSONObject jObj = null;
	String json = "";
	
    public static String idcate = "0";
    private static String url = "http://khachung.com/android/cates.php";
    private static String url2 = "http://khachung.com/android/chart_home.php";
    private static final String TAG_NAME = "name";
    GridView gridview;
    // contacts JSONArray
    JSONArray sp = null;
    ImageView iv;
    Context context;    
    ArrayList prgmName;    
    Intent intent;
    public static String [] arr;
    
    public static int [] prgmImages;   
	public TextView findTextView(int id)
	{
		return (TextView) findViewById(id);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// execute your xml news feed loader
		// launch your News activity
        Intent intent2 = new Intent(MainActivity.this, Screen.class);
        startActivity(intent2);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		setContentView(R.layout.activity_main);
		
		addControlAndEvents();

		new JSONParser().execute(url);
		new JSONChart().execute(url2);

		long date = System.currentTimeMillis(); 
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a, dd - MM - yyyy");
		String dateString = sdf.format(date);   
		txtText.setText("Hiện tại: " + dateString + "  ");

		ImageButton  btnSearch = (ImageButton ) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new  Intent(MainActivity.this,Resultsearch.class);
				TextView txtSearch = (TextView) findViewById(R.id.txtSearch);
				String key =  txtSearch.getText().toString();
				intent.putExtra("key",key);
				startActivity(intent);
			}
		});
		
	}
	private void addControlAndEvents() {
		txtText = findTextView(R.id.txtText);
		intent = new Intent(this, ListProduct.class);
		//gridview = (GridView) findViewById(R.id.gridview);
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
		        HttpGet httpPost = new HttpGet(url);
		            HttpResponse getResponse = httpClient.execute(httpPost);
		           final int statusCode = getResponse.getStatusLine().getStatusCode();
		           
		           if (statusCode != HttpStatus.SC_OK) { 
		              Log.d(getClass().getSimpleName(), 
		                  "Error " + statusCode + " for URL " + url); 
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
			
				if(jObj.has("CATE")){
					JSONArray arrJson = jObj.getJSONArray("CATE");
					a1 = new String[arrJson.length()];
					a2 = new String[arrJson.length()];
					id2 = new String[arrJson.length()];
						for	(int i=0; i < arrJson.length(); i++){
							JSONObject objProduct= arrJson.getJSONObject(i);
				            if(objProduct.has("name")){
				            	id2[i] = objProduct.getString("id");
				            	a1[i] = objProduct.getString("name");
				            	a2[i] = objProduct.getString("image");
				              }
				        }
					}
				
				CustomList adapter = new CustomList(MainActivity.this, a1, a2);
				gridview = (GridView) findViewById(R.id.gridview);
				
				gridview.setAdapter(adapter);
				
				gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	                @Override
	                public void onItemClick(AdapterView<?> parent, View view,
	                                        int position, long id) {
	                	intent.putExtra("IDCATE", id2[+position]);
	                	intent.putExtra("NameCATE", a1[+position]);
	                	startActivity(intent);
	                }
	            });
			} catch (JSONException e) {
				txtText.setText("23");
				Toast.makeText(MainActivity.this, e.toString(), 
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		
		protected void onPostExecute(String page)
		{   
		    //onPostExecute
		}   
	}
	
	
	
	//JSONPARSER ve chart
	public class JSONChart extends AsyncTask <String, JSONObject, String>{
		// constructor
		public JSONChart() {
	
		}
		@Override
		protected String doInBackground(String... params) {
		    // Making HTTP request
		    try {
		    	
		        // defaultHttpClient
		        DefaultHttpClient httpClient = new DefaultHttpClient();
		        HttpGet httpPost = new HttpGet(url2);
		            HttpResponse getResponse = httpClient.execute(httpPost);
		           final int statusCode = getResponse.getStatusLine().getStatusCode();
		           
		           if (statusCode != HttpStatus.SC_OK) { 
		              Log.d(getClass().getSimpleName(), 
		                  "Error " + statusCode + " for URL " + url2); 
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

		protected void onProgressUpdate(JSONObject... values) {
			super.onProgressUpdate(values);

			//ta cập nhật giao diện ở đây:
			JSONObject jsonObj=values[0];
			
			JSONArray arrJson;
				try {
					Log.e("Name","d0");
					if(jObj.has("List")){
					arrJson = jObj.getJSONArray("List");
					Log.e("Name","d1");
					Sanpham[] sp =  new Sanpham[arrJson.length()];

					Log.e("Name","d2");
					for	(int i=0; i < arrJson.length(); i++){
						//Lấy giá trị JSON được trả về

						Log.e("Name","d3");
						JSONObject objProduct= arrJson.getJSONObject(i);
						JSONObject arrPro = objProduct.getJSONObject("Product");
						JSONArray arrPri = objProduct.getJSONArray("Price");
						
						Double []temp = new Double[arrPri.length()];
						
						for(int j = 0 ; j < arrPri.length(); j++){
							temp[j] =  arrPri.getJSONObject(j).getDouble("p");
						}
						sp[i] = new Sanpham();
						sp[i].setName(arrPro.getString("name_product"));
						sp[i].setPrice(temp);

			            //if(objProduct.has("name")){a1[i] = objProduct.getString("name");}
			        }
					//Khởi tạo biểu đồ
					createChart2(sp);
					
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		protected void onPostExecute(String page)
		{   
		    //onPostExecute
		}   
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*
		long date = System.currentTimeMillis(); 
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a, dd - MM - yyyy");
		String dateString = sdf.format(date);   
		txtText.setText("Hiện tại: " + dateString + "  ");*/
	}
	
	
	private void	createChart2(Sanpham [] sanpham) {
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		// Now we create the renderer
		XYSeriesRenderer[] renderer = new XYSeriesRenderer[sanpham.length];
		XYSeries[] series = new XYSeries[sanpham.length];
		for(int i =0; i <sanpham.length; i++){
			
			 series[i] = new XYSeries(sanpham[i].getName());
			 for(int j=0; j < sanpham[i].getPrice().length; j++){
				 
				 series[i].add(j, sanpham[i].getPrice()[j]);
			 }
			 Random rnd = new Random(); 
			 int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)); 
			 dataset.addSeries(series[i]);
			 renderer[i] = new XYSeriesRenderer();
			 renderer[i].setLineWidth(3);
			 renderer[i].setColor(color);
				// we add point markers
				//renderer.setPointStrokeWidth(2);
			 renderer[i].setPointStyle(PointStyle.SQUARE);
				//renderer.setp;(PointStyle.CIRCLE);
			 renderer[i].setDisplayChartValues(true);//Cho phép hiển thị giá trị	
			 mRenderer.addSeriesRenderer(renderer[i]);
		}
		
		// We want to avoid black border
		mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
		// Disable Pan on two axis
		mRenderer.setPanEnabled(false, false);
		mRenderer.setYAxisMax(35);
		mRenderer.setYAxisMin(0);
		mRenderer.setLabelsTextSize(18);
		mRenderer.setAxisTitleTextSize(22);
		mRenderer.setLegendTextSize(20);
		
		GraphicalView chartView = ChartFactory.getLineChartView(getApplicationContext(), dataset, mRenderer);

		LinearLayout chartLyt = (LinearLayout)findViewById(R.id.chart_container);
		chartLyt.addView(chartView,0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	
	//Check connect internet
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager  info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
		if(info.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) 
		{
			return true;
		}
		return false;
	}

}
