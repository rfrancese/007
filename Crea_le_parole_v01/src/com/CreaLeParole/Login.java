package com.CreaLeParole;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Activity {

    Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputUsername;
    EditText inputPassword;
    private TextView loginErrorMsg;
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    /**
     * E' chiamato quando l'activity viene creata per la prima volta
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Con questi 2 imposto l'activity a schermo intero (FULL SCREEN)
 		requestWindowFeature(Window.FEATURE_NO_TITLE);
 		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.loginUsername);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (Button)findViewById(R.id.passres);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
        
        /**
         * Bottone per resettare la password. Richiama la classe PasswordReset
         */
        passreset.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
		        Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
		        startActivityForResult(myIntent, 0);
		        finish();
	        }
    	});

        /**
         * Bottone per registrarsi. Richiama la classe Register
         */
        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
                finish();
             }});

        /**
         * Bottone per il login. Segnala un errore se i campi sono vuoti
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) ){
                    NetAsync(view);
                }
                else if ( ( !inputUsername.getText().toString().equals("")) ){
                    Toast.makeText(getApplicationContext(),"Il campo Password è vuoto", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) ){
                    Toast.makeText(getApplicationContext(),"Il campo Username è vuoto", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"I campi Username e Password sono vuoti", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


	/**
	 * Async Task per vedere se la connessione ad internet funziona
	 **/
    private class NetCheck extends AsyncTask<String,String,Boolean>{
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Login.this);
            nDialog.setTitle("Verifico la rete");
            nDialog.setMessage("Caricamento..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        
        /**
         * Cerca di collegarsi a Google.com attraverso il dispositivo per verificare che la connessione funzioni
        **/
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
        @
        Override
        protected void onPostExecute(Boolean th){
        	
        	/**
        	 * Se il valore è true prosegue con il login (c'è la connessione)
        	 */
            if(th == true){
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                loginErrorMsg.setText("Errore di connessione. Riprova");
            }
        }
    }

    /**
     * Async Task riceve ed invia dati al database MySql attraverso JSON.
     **/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String username,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            inputUsername = (EditText) findViewById(R.id.loginUsername);
            inputPassword = (EditText) findViewById(R.id.pword);
            username = inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("Contatto il server");
            pDialog.setMessage("Login in corso..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(username, password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
            	if (json.getString(KEY_SUCCESS) != null) {
            		String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Login nel pannello utente");
                        pDialog.setTitle("Ricevo i dati");
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                       /**
                        *Se le informazioni di JSON sono contenute in SQlite questo lancia il menù principale (la classe main)
                        **/
                        Intent upanel = new Intent(getApplicationContext(), menu_principale.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(upanel);
                        /**
                         * Chiude la schermata di Login
                         **/
                        finish();
                    }else{
                        pDialog.dismiss();
                        loginErrorMsg.setText("Username/password errati");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }
}