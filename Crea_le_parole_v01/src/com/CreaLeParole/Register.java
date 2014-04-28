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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.CreaLeParole.library.DatabaseHandler;
import com.CreaLeParole.library.UserFunctions;
import com.learn2crack.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register extends Activity {


    /**
     *  JSON risposta ai nomi dei nodi
     **/
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_USERNAME = "uname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";

    /**
     * Definisco gli oggetti del layout
     **/
    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    Button btnRegister;
    TextView registerErrorMsg;

    /**
     * E' chiamato quando l'activity viene creata per la prima volta
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Con questi 2 imposto l'activity a schermo intero (FULL SCREEN)
 		requestWindowFeature(Window.FEATURE_NO_TITLE);
 		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
 		
 		setContentView(R.layout.register);

    /**
     * Definisco gli oggetti del layout
     **/
        inputFirstName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.lname);
        inputUsername = (EditText) findViewById(R.id.uname);
        inputEmail = (EditText) findViewById(R.id.loginUsername);
        inputPassword = (EditText) findViewById(R.id.pword);
        btnRegister = (Button) findViewById(R.id.register);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);



	/**
	 * Button which Switches back to the login screen on clicked
	 **/
/*
        Button login = (Button) findViewById(R.id.bktologin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });*/

        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) && ( !inputEmail.getText().toString().equals("")) ){
                    if ( inputUsername.getText().toString().length() > 2 ){
                    	NetAsync(view);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"L'username deve avere minimo 3 caratteri", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Uno o più campi sono vuoti", Toast.LENGTH_SHORT).show();
                }
            }
        });
       }
    /**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>{
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Register.this);
            nDialog.setMessage("Caricamento..");
            nDialog.setTitle("Verifico la rete..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
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
                registerErrorMsg.setText("Errore di connessione. Riprova");
            }
        }
    }





    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

		/**
		 * Definisco un processo di dialogo
		 **/
        private ProgressDialog pDialog;
        private String email,password,fname,lname,uname;
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputUsername = (EditText) findViewById(R.id.uname);
            inputPassword = (EditText) findViewById(R.id.pword);
               /*fname = inputFirstName.getText().toString();
           		lname = inputLastName.getText().toString();*/
            	uname= inputUsername.getText().toString();
            	email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setTitle("Sto contattando il server..");
            pDialog.setMessage("Registrazione");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
        	UserFunctions userFunction = new UserFunctions();
        	JSONObject json = userFunction.registerUser(fname, lname, email, uname, password);
            return json;
        }
        
        
        /**
         * Questo metodo si applica quando viene cliccato il pulsante della registrazione
         */
        @Override
        protected void onPostExecute(JSONObject json) {
    	/**
        * Checks for success message.
        **/
        try {
        	if (json.getString(KEY_SUCCESS) != null) {
                registerErrorMsg.setText("");
                String res = json.getString(KEY_SUCCESS);

                String red = json.getString(KEY_ERROR);

                //Se entro qui dentro significa che la registrazione è avvenuta con successo
                if(Integer.parseInt(res) == 1){
                	pDialog.setTitle("Ottengo i dati");
                    pDialog.setMessage("Carico le impostazioni");
                    registerErrorMsg.setText("Registrazione avvenuta con successo");

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    JSONObject json_user = json.getJSONObject("user");
                    
                    /**
                     * Inserisce i dati di registrazione nel database SQlite
                     * Launch Registered screen
                     **/
                    UserFunctions logout = new UserFunctions();
                    logout.logoutUser(getApplicationContext());
                    db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                    
                    /**
                     * Lancia lo screen Registered
                     **/
                    Intent registered = new Intent(getApplicationContext(), Registered.class);

                    /**
                     * Chiude tutte le view prima di lanciare Registered
                    **/
                    registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pDialog.dismiss();
                    startActivity(registered);
                    finish();
                }
                else if (Integer.parseInt(red) ==2){
                    pDialog.dismiss();
                    registerErrorMsg.setText("Questo nome utente già esiste");
                }
                else if (Integer.parseInt(red) ==3){
                    pDialog.dismiss();
                    registerErrorMsg.setText("Email non valida");
                }
            }
            else{
            	pDialog.dismiss();
                registerErrorMsg.setText("Errore nella registrazione");
            }
        } catch (JSONException e) {
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