package com.CreaLeParole;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.CreaLeParole.library.UserFunctions;
import com.learn2crack.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PasswordReset extends Activity {

	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";

	EditText email;
	TextView alert;
	Button resetpass;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  
        setContentView(R.layout.passwordreset);

        email = (EditText) findViewById(R.id.forpas);
        alert = (TextView) findViewById(R.id.alert);
        resetpass = (Button) findViewById(R.id.respass);
        resetpass.setOnClickListener(new View.OnClickListener() {
        
        	@Override
        	public void onClick(View view) {
        		NetAsync(view);
        	}
        });
    }

    private class NetCheck extends AsyncTask<String,String,Boolean>{
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(PasswordReset.this);
            nDialog.setMessage("Caricamento..");
            nDialog.setTitle("Verifico la rete");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        
        @Override
        protected void onPostExecute(Boolean th){
            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                alert.setText("Errore di connessione");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {
    	private ProgressDialog pDialog;
        String forgotpassword;
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            forgotpassword = email.getText().toString();

            pDialog = new ProgressDialog(PasswordReset.this);
            pDialog.setTitle("Contatto il server");
            pDialog.setMessage("Ricevo i dati..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.forPass(forgotpassword);
            return json;


        }


        @Override
        protected void onPostExecute(JSONObject json) {
        	/**
        	 * Checks if the Password Change Process is sucesss
        	 **/
        	try {
                if (json.getString(KEY_SUCCESS) != null) {
                    alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);

                    if(Integer.parseInt(res) == 1){
                       pDialog.dismiss();
                       alert.setText("Un'email ti è stata inviata. Controlla la posta in arrivo");
                    }
                    else if (Integer.parseInt(red) == 2){    
                    	pDialog.dismiss();
                        alert.setText("La tua email non esiste nel nostro database");
                    }
                    else {
                        pDialog.dismiss();
                        alert.setText("Si è verificato un errore");
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
                
        /**
         * Individua l'Activity nella quale andare quando si preme il pulsante "back"
         */
        @Override
    	public void onBackPressed() {
    	    Intent backIntent = new Intent(this, Login.class);
    	    startActivity(backIntent);
    	    super.onBackPressed();
    	}
                
	    public void NetAsync(View view){
	        new NetCheck().execute();
	    }
    }





