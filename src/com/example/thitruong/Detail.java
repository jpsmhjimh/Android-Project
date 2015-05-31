package com.example.thitruong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.thitruong.CustomList.LoadImageFromURL;
import com.example.thitruong.ListProduct.JSONChart;
import com.example.thitruong.ListProduct.JSONParser;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Detail extends ActionBarActivity {

	TextView txtText;
	String urlImage;
	String strName;
	TextView txtTenSp;
	
    InputStream is = null;
	JSONObject jObj = null;
	String json = "";
	ListView listview;
	ImageView imageView ;
	TextView txtName,txtUnit,txtAmount,txtPrice,txtDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		setContentView(R.layout.activity_detail);
		
		
		
		Intent intenttemp = getIntent();
		String message = intenttemp.getStringExtra("IDProduct");

		txtTenSp =(TextView) findViewById(R.id.txtTensp);
		txtName =(TextView) findViewById(R.id.txtName);
		txtUnit =(TextView) findViewById(R.id.txtUnit);
		txtAmount =(TextView) findViewById(R.id.txtAmount);
		txtPrice =(TextView) findViewById(R.id.txtPrice);
		txtDate =(TextView) findViewById(R.id.txtDate);
		imageView = (ImageView) findViewById(R.id.imgProduct);
		listview = (ListView) findViewById(R.id.listView1);
				
		new JSONParser().execute("http://khachung.com/android/detail.php?pro="+message);
		new JSONChart().execute("http://khachung.com/android/chart_pro.php?pro="+message);
		
		ImageButton  btnSearch = (ImageButton ) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new  Intent(Detail.this,Resultsearch.class);
				TextView txtSearch = (TextView) findViewById(R.id.txtSearch);
				String key =  txtSearch.getText().toString();
				intent.putExtra("key",key);
				startActivity(intent);
			}
		});

		long date = System.currentTimeMillis(); 
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a, dd - MM - yyyy");
		String dateString = sdf.format(date);   
		TextView ftext =(TextView) findViewById(R.id.txtText);
		ftext.setText("Hiện tại: " + dateString + "  ");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
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
						JSONObject objProduct= arrJson.getJSONObject(0);//txtName,txtUnit,txtAmount,txtPrice,txtDate;
						txtTenSp.setText("Tên: "+ objProduct.getString("name_product"));
						txtName.setText("Tên: "+ objProduct.getString("name_product"));
						strName = objProduct.getString("name_product");
						txtUnit.setText("Đơn vị: "+ objProduct.getString("unit_product"));
						txtAmount.setText("Số lượng: "+ objProduct.getString("amount_product"));
						txtPrice.setText("Giá: "+ objProduct.getString("price_product"));
						txtDate.setText("Ngày cập nhật: "+ objProduct.getString("create_date"));
						new LoadImageFromURL().execute(objProduct.getString("image_product"));
							//Lấy danh sách các sản phẩm
							//CustomList adapter = new CustomList(ListProduct.this, a1, a2);
					}
					if(jObj.has("PRICE")){
						
						JSONArray arrPrice = jObj.getJSONArray("PRICE");
						//listview
						String[] arr = new String[arrPrice.length()];
						for(int i=0; i<arrPrice.length(); i++){
							arr[i] ="- " + arrPrice.getJSONObject(i).getString("create_time") +" : " + arrPrice.getJSONObject(i).getString("price") + " vnđ";
						}
						ArrayAdapter<String>adapter=new ArrayAdapter<String>(Detail.this, android.R.layout.simple_list_item_1, arr);
						//4. Đưa Data source vào ListView
						listview.setAdapter(adapter);
					}
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), e.toString(), 
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
			
			protected void onPostExecute(String page)
			{   
			    //onPostExecute
			}   
		}

		
		public class LoadImageFromURL extends AsyncTask<String, Void, Bitmap>{
			@Override
			 protected Bitmap doInBackground(String... params) {
				// TODO Auto-generated method stub
				 try {
					 Bitmap bitMap = BitmapFactory.decodeStream((InputStream)new URL(params[0]).getContent());
					 return bitMap;
					 
				 } catch (MalformedURLException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
				 } catch (IOException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
				 }
				 return null;
			 }
			 
			 @Override
			 protected void onPostExecute(Bitmap result) {
			 // TODO Auto-generated method stub
			 super.onPostExecute(result);
			 	imageView.setImageBitmap(result);
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

			protected void onProgressUpdate(JSONObject... values) {
				super.onProgressUpdate(values);

				//ta cập nhật giao diện ở đây:
				JSONObject jsonObj=values[0];
				
				JSONArray arrJson;
					try {
						if(jObj.has("Price")){

						Sanpham sp =  new Sanpham();
						JSONArray arrPri = jObj.getJSONArray("Price");
						
						Double []temp = new Double[arrPri.length()];
						
						for(int j = 0 ; j < arrPri.length(); j++){
							temp[j] =  arrPri.getJSONObject(j).getDouble("p");
						}
						sp = new Sanpham();
						sp.setName(strName);
						sp.setPrice(temp);

				            //if(objProduct.has("name")){a1[i] = objProduct.getString("name");}
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
		
		private void	createChart2(Sanpham sanpham) {
			
			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
			// Now we create the renderer
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			XYSeries series = new XYSeries(sanpham.getName());
			 for(int j=0; j < sanpham.getPrice().length; j++){
				 
				 series.add(j, sanpham.getPrice()[j]);
			 }
			 Random rnd = new Random(); 
			 int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)); 
			 dataset.addSeries(series);
			 renderer = new XYSeriesRenderer();
			 renderer.setLineWidth(3);
			 renderer.setColor(color);
				// we add point markers
				//renderer.setPointStrokeWidth(2);
			 renderer.setPointStyle(PointStyle.SQUARE);
				//renderer.setp;(PointStyle.CIRCLE);
			 renderer.setDisplayChartValues(true);//Cho phép hiển thị giá trị	
			 mRenderer.addSeriesRenderer(renderer);
			
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
		

}
