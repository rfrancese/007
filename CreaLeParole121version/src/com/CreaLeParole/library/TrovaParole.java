package com.CreaLeParole.library;

import java.util.*; 
import java.io.*;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.internal.ca;
import com.google.android.gms.internal.in;
import com.skynet.clp.R;

public class TrovaParole extends Activity{
	
//	//a handle to the application's resources  
//    private Resources resources;  
//    //a string to output the contents of the files to LogCat  
//    private String output;
	
	public TrovaParole(){
		
	}
	
//	/** Called when the activity is first created. */  
//    @Override  
//    public void onCreate(Bundle savedInstanceState)  
//    {  
//        super.onCreate(savedInstanceState);  
//        setContentView(R.layout.main);  
//  
//        //get the application's resources  
//        resources = getResources();  
//  
//        try  
//        {  
//            //Load the file from assets folder - don't forget to INCLUDE the extension  
//            output = LoadFile("parole", false);  
//            //output to LogCat  
//            Log.i("test", output);  
//        }  
//        catch (IOException e)  
//        {  
//            //display an error toast message  
//            Toast toast = Toast.makeText(this, "File: not found!", Toast.LENGTH_LONG);  
//            toast.show();  
//        }  
//    }  
//    
//    //load file from apps res/raw folder or Assets folder  
//    public String LoadFile(String fileName, boolean loadFromRawFolder) throws IOException  
//    {  
//        //Create a InputStream to read the file into  
//        InputStream iS;  
//  
//        iS = resources.getAssets().open(fileName);  
//  
//        //create a buffer that has the same size as the InputStream  
//        byte[] buffer = new byte[iS.available()];  
//        //read the text file as a stream, into the buffer  
//        iS.read(buffer);  
//        //create a output stream to write the buffer into  
//        ByteArrayOutputStream oS = new ByteArrayOutputStream();  
//        //write this buffer to the output stream  
//        oS.write(buffer);  
//        //Close the Input and Output streams  
//        oS.close();  
//        iS.close();  
//  
//        //return the output stream as a String  
//        return oS.toString();  
//    }  

    public boolean trovaParola(String parola) throws Exception {
    	File file = new File("parole"); 
		boolean trovata = false; 
		if(file.exists() && parola.length()>1){
			
		    System.out.println("Il file è stato trovato!"); 
		    
		    FileInputStream input = new FileInputStream(file); 
		    InputStreamReader isr = new InputStreamReader(input); 
		    BufferedReader br = new BufferedReader(isr);
		    
		    FileInputStream input2 = new FileInputStream(file); 
		    InputStreamReader isr2 = new InputStreamReader(input2); 
		    BufferedReader br2 = new BufferedReader(isr2);
		    
		    String linea="";
		    linea=br.readLine();
		    System.out.println("parola inserita -> "+parola);
		    do{
		    	if(parola.equalsIgnoreCase(linea)){
		    		System.out.println("----- si");
        			trovata = true;
        			System.out.println("----> "+linea);
        			break;
		    	}
		    	else
		    		System.out.println("no");
		    	linea=br.readLine();
		    }while(linea!=null);
		    System.out.println("--> "+trovata);
		    linea="";
		    linea=br2.readLine();
		    if(!trovata){
		    	System.out.println("bukkn");
			    do{
	        		if (linea.contains(parola)) {
	        			System.out.println("si");
	        			trovata = true;
	        			System.out.println("----> "+linea);
	        			break;
	        		} else {
	        			trovata = false;
	        			System.out.println("no");
	        		}
			        linea=br2.readLine(); 
				}while(linea!=null); 
		    }
		} 
	    else 
	        System.out.println("Il file non esiste!"); 
	    if(!trovata) 
	    	trovata = false;
	    return trovata;
    }
    
}