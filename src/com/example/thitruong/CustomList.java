package com.example.thitruong;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.widget.ArrayAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
 

public class CustomList extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] web;
	private final String[] imageId;
	ImageView imageView ;
	public CustomList(Activity context,String[] web, String[] imageId) {
		super(context, R.layout.list, web);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.web = web;
		this.imageId = imageId;
	}
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		
		View rowView= inflater.inflate(R.layout.list, null, true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txtName);
		 
		imageView = (ImageView) rowView.findViewById(R.id.img);
		
		new LoadImageFromURL().execute(imageId[position]);
		
		txtTitle.setText(web[position]);
		
		return rowView;
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
	
}
