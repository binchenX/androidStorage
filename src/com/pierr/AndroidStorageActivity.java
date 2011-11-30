package com.pierr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.FileEntity;

import com.pierr.R;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

public class AndroidStorageActivity extends Activity {
    /** Called when the activity is first created. */
	
	
	TextView mTextPrivateStroage = null;
	TextView mTextPublicStorage  = null;
	TextView mTextExternalDirs = null;
	
	private static String TAG = "AndroidStorage";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //write to private storage
        
        //read it back
   
        //
        setContentView(R.layout.main);
   
        
        mTextPrivateStroage = (TextView)findViewById(R.id.privateStorage);
        mTextPublicStorage = (TextView)findViewById(R.id.externalPublicStorage);
        mTextExternalDirs = (TextView)findViewById(R.id.externalFileDir);
        
        writeAndReadPrivateStorage();
        
        writeExternalFilesDir();
        
        
        createExternalStoragePublicPicture();
        
        
    
    }
    
    private void  writeExternalFilesDir()
    {
    	
    	File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    	if(dir.exists())
    	{
    		boolean ret = dir.mkdir();
    		{
    			Log.i(TAG, "Fail to create  " + dir.getAbsolutePath());
    		}
    		
    		
    	}
    	
    	mTextExternalDirs.setText(dir.getAbsolutePath());
    	
    	//go a heard to create pic there
    	
    	try {
    	    File file = new File(dir, "DemoPicture.jpg");

	        // Very simple code to copy a picture from the application's
	        // resource into the external file.  Note that this code does
	        // no error checking, and assumes the picture is small (does not
	        // try to copy it in chunks).  Note that if external storage is
	        // not currently mounted this will silently fail.
	        InputStream is = getResources().openRawResource(R.drawable.icon);
	        OutputStream os = new FileOutputStream(file);
	        byte[] data = new byte[is.available()];
	        is.read(data);
	        os.write(data);
	        is.close();
	        os.close();
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	
    	
    	
    }
    
    private void writeAndReadPrivateStorage() 
    {
    	
    	
    	//write it 
    	String FILENAME = "hello_file";
    	String string = "hello world!";
    	
    	File fileDir = getFilesDir();
    	
    	String path = fileDir.getAbsolutePath();
    	
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("create file " + FILENAME)
    	  .append("\n under dir " + path);
    	

    	FileOutputStream fos = null;
    	try {
    	
    	fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
    	fos.write(string.getBytes());
    	fos.close();
    	
    	
    	
    	//read it out
    	FileInputStream fis = openFileInput(FILENAME);
    	byte[] buffer = new byte[40];
    	
    	fis.read(buffer);
    	
    	sb.append("  \n read back content of " + FILENAME + " is \n")
    	  .append(new String(buffer));
    	
    	mTextPrivateStroage.setText(sb.toString());
    	
    	
    	}catch (FileNotFoundException ex)
    	{
    		ex.printStackTrace();
    	}catch (Exception ex){
    		ex.printStackTrace();
    		
    	}
    	
    	
    	
    }
    
    /**
     * 
     * access external public storage 
     * 
     */
    	void createExternalStoragePublicPicture() {
    	    // Create a path where we will place our picture in the user's
    	    // public pictures directory.  Note that you should be careful about
    	    // what you place here, since the user often manages these files.  For
    	    // pictures and other media owned by the application, consider
    	    // Context.getExternalMediaDir().
    	    File path = Environment.getExternalStoragePublicDirectory(
    	            Environment.DIRECTORY_PICTURES);
    	    File file = null;
    	    
    	    mTextPublicStorage.setText(path.getAbsolutePath());

    	    try {
    	        // Make sure the Pictures directory exists.
    	    	
    	    	if(!path.exists())
    	    	{
    	    		Log.i(TAG, "create dir");
    	    		boolean ret = path.mkdirs();
    	    		
    	    		if(ret == false)
    	    		{
    	    			Log.i(TAG, "fail to create " + path.getAbsolutePath());
    	    		}
    	    	}
    	    	
    	         file = new File(path, "DemoPicture.jpg");

    	        // Very simple code to copy a picture from the application's
    	        // resource into the external file.  Note that this code does
    	        // no error checking, and assumes the picture is small (does not
    	        // try to copy it in chunks).  Note that if external storage is
    	        // not currently mounted this will silently fail.
    	        InputStream is = getResources().openRawResource(R.drawable.icon);
    	        OutputStream os = new FileOutputStream(file);
    	        byte[] data = new byte[is.available()];
    	        is.read(data);
    	        os.write(data);
    	        is.close();
    	        os.close();

    	        // Tell the media scanner about the new file so that it is
    	        // immediately available to the user.
    	        MediaScannerConnection.scanFile(this,
    	                new String[] { file.toString() }, 
    	                null,
    	                new MediaScannerConnection.OnScanCompletedListener() {
    	        	
    	            @Override
							public void onScanCompleted(String path, Uri uri) {
								// TODO Auto-generated method stub
    	            	 Log.i("ExternalStorage", "Scanned " + path + ":");
     	                Log.i("ExternalStorage", "-> uri=" + uri);
								
							}


    	        });
    	    } catch (IOException e) {
    	        // Unable to create file, likely because external storage is
    	        // not currently mounted.
    	        Log.w("ExternalStorage", "Error writing " + file, e);
    	    }
    	}

    	void deleteExternalStoragePublicPicture() {
    	    // Create a path where we will place our picture in the user's
    	    // public pictures directory and delete the file.  If external
    	    // storage is not currently mounted this will fail.
    	    File path = Environment.getExternalStoragePublicDirectory(
    	            Environment.DIRECTORY_PICTURES);
    	    File file = new File(path, "DemoPicture.jpg");
    	    file.delete();
    	}

    	boolean hasExternalStoragePublicPicture() {
    	    // Create a path where we will place our picture in the user's
    	    // public pictures directory and check if the file exists.  If
    	    // external storage is not currently mounted this will think the
    	    // picture doesn't exist.
    	    File path = Environment.getExternalStoragePublicDirectory(
    	            Environment.DIRECTORY_PICTURES);
    	    File file = new File(path, "DemoPicture.jpg");
    	    return file.exists();
    	}
    
    	
    private void writeAndreadPublicStorage()
    {
    	
    	//File dir = getE
    }
}