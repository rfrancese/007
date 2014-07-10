//package com.CreaLeParole;
//
//
//import android.content.ContentValues;
//import android.content.Intent;
//import android.content.IntentSender.SendIntentException;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import com.CreaLeParole.library.MioDbHelper;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.CommonStatusCodes;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.games.Games;
//import com.google.android.gms.plus.Plus;
//import com.google.android.gms.plus.model.people.Person;
//import com.google.android.gms.plus.model.people.Person.Image;
//import com.google.android.gms.plus.model.people.PersonBuffer;
//import com.google.example.games.basegameutils.BaseGameActivity;
//import com.skynet.clp.R;
//
//public class Login extends BaseGameActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<com.google.android.gms.plus.People.LoadPeopleResult> {
//
////    Button Btnregister;
////    Button passreset;
////    EditText inputUsername;
////    EditText inputPassword;
//    GoogleApiClient mGoogleApiClient;
//    final static String TAG = "CreaLeParole";
//    Person currentPerson;
//    private String personName;
//    private String personID;
//    private Image personPhoto;
//    private String personGooglePlusProfile;
//    private String personemail;
//    private String personPhotoUrlTemp;
//    private String personPhotoUrl;
//    //Variabile per la gestione del database
//  	private MioDbHelper mMioDbHelper = null;
//
//    /* Request code used to invoke sign in user interactions. */
//    private static final int RC_SIGN_IN = 0;
//
//    /* A flag indicating that a PendingIntent is in progress and prevents
//     * us from starting further intents.
//     */
//    private boolean mIntentInProgress;
//    
//    public GoogleApiClient getGoogleApiClient(){
//        return mGoogleApiClient;
//    }
//    
//    @Override
//    public void onStart(){
//        super.onStart();
//        if(mGoogleApiClient != null) {
//        	System.out.println("1 connessione......");
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        if(mGoogleApiClient != null) {
//            if (mGoogleApiClient.isConnected()) {
//                mGoogleApiClient.disconnect();
//            }
//        }
//    }
//    
//    /**
//     * E' chiamato quando l'activity viene creata per la prima volta
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//    	// Con questi 2 imposto l'activity a schermo intero (FULL SCREEN)
// 		requestWindowFeature(Window.FEATURE_NO_TITLE);
//        
// 		super.onCreate(savedInstanceState);
//        
// 		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
// 		//Inizializiamo la variabile per poter gestire il database
//        mMioDbHelper = new MioDbHelper(getApplicationContext());
//        
//        setContentView(R.id.screen_login);
//        
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        
//        findViewById(R.id.sign_in_button).setOnClickListener(this);
//        findViewById(R.id.sign_out_button).setOnClickListener(this);
//        
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//        .addApi(Plus.API)
//        .addApi(Games.API)
//        .addScope(Plus.SCOPE_PLUS_LOGIN)
//        .addConnectionCallbacks(this)
//        .addOnConnectionFailedListener(this)
//        .build();
//    }
//        
////        /**
////         * Bottone per resettare la password. Richiama la classe PasswordReset
////         */
//////        passreset.setOnClickListener(new View.OnClickListener() {
////	    	public void onClick(View view) {
////		        Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
////		        startActivityForResult(myIntent, 0);
////		        finish();
////	        }
////    	});
////
////        /**
////         * Bottone per registrarsi. Richiama la classe Register
////         */
////        Btnregister.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View view) {
////                Intent myIntent = new Intent(view.getContext(), Register.class);
////                startActivityForResult(myIntent, 0);
////                finish();
////             }});
//
////        /**
////         * Bottone per il login. Segnala un errore se i campi sono vuoti
////         */
////        btnLogin.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View view) {
////                if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) ){
////                    NetAsync(view);
////                }
////                else if ( ( !inputUsername.getText().toString().equals("")) ){
////                    Toast.makeText(getApplicationContext(),"Il campo Password è vuoto", Toast.LENGTH_SHORT).show();
////                }
////                else if ( ( !inputPassword.getText().toString().equals("")) ){
////                    Toast.makeText(getApplicationContext(),"Il campo Username è vuoto", Toast.LENGTH_SHORT).show();
////                }
////                else{
////                    Toast.makeText(getApplicationContext(),"I campi Username e Password sono vuoti", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
////    }
////
////
////	/**
////	 * Async Task per vedere se la connessione ad internet funziona
////	 **/
////    private class NetCheck extends AsyncTask<String,String,Boolean>{
////        private ProgressDialog nDialog;
////
////        @Override
////        protected void onPreExecute(){
////            super.onPreExecute();
////            nDialog = new ProgressDialog(Login.this);
////            nDialog.setTitle("Verifico la rete");
////            nDialog.setMessage("Caricamento..");
////            nDialog.setIndeterminate(false);
////            nDialog.setCancelable(false);
////            nDialog.show();
////        }
//        
////        /**
////         * Cerca di collegarsi a Google.com attraverso il dispositivo per verificare che la connessione funzioni
////        **/
////        @Override
////        protected Boolean doInBackground(String... args){
////            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
////            NetworkInfo netInfo = cm.getActiveNetworkInfo();
////            if (netInfo != null && netInfo.isConnected()) {
////                try {
////                    URL url = new URL("http://www.google.com");
////                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
////                    urlc.setConnectTimeout(3000);
////                    urlc.connect();
////                    if (urlc.getResponseCode() == 200) {
////                        return true;
////                    }
////                } catch (MalformedURLException e1) {
////                    e1.printStackTrace();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////            return false;
////
////        }
////        @
////        Override
////        protected void onPostExecute(Boolean th){
////        	
////        	/**
////        	 * Se il valore è true prosegue con il login (c'è la connessione)
////        	 */
////            if(th == true){
////                nDialog.dismiss();
////                new ProcessLogin().execute();
////            }
////            else{
////                nDialog.dismiss();
////                loginErrorMsg.setText("Errore di connessione. Riprova");
////            }
////        }
////    }
////
////    /**
////     * Async Task riceve ed invia dati al database MySql attraverso JSON.
////     **/
////    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {
////        private ProgressDialog pDialog;
////        String username,password;
////
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////
////            inputUsername = (EditText) findViewById(R.id.campoEmail);
////            inputPassword = (EditText) findViewById(R.id.pword);
////            username = inputUsername.getText().toString();
////            password = inputPassword.getText().toString();
////            pDialog = new ProgressDialog(Login.this);
////            pDialog.setTitle("Contatto il server");
////            pDialog.setMessage("Login in corso..");
////            pDialog.setIndeterminate(false);
////            pDialog.setCancelable(true);
////            pDialog.show();
////        }
////
////        @Override
////        protected JSONObject doInBackground(String... args) {
////            UserFunctions userFunction = new UserFunctions();
////            JSONObject json = userFunction.loginUser(username, password);
////            return json;
////        }
////
////        @Override
////        protected void onPostExecute(JSONObject json) {
////            try {
////            	if (json.getString(KEY_SUCCESS) != null) {
////            		String res = json.getString(KEY_SUCCESS);
////                    if(Integer.parseInt(res) == 1){
////                        pDialog.setMessage("Login nel pannello utente");
////                        pDialog.setTitle("Ricevo i dati");
////                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
////                        JSONObject json_user = json.getJSONObject("user");
////                        /**
////                         * Clear all previous data in SQlite database.
////                         **/
////                        UserFunctions logout = new UserFunctions();
////                        logout.logoutUser(getApplicationContext());
////                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
////                       /**
////                        *Se le informazioni di JSON sono contenute in SQlite questo lancia il menù principale (la classe main)
////                        **/
////                        Intent upanel = new Intent(getApplicationContext(), menu_principale.class);
////                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        pDialog.dismiss();
////                        startActivity(upanel);
////                        /**
////                         * Chiude la schermata di Login
////                         **/
////                        finish();
////                    }else{
////                        pDialog.dismiss();
////                        loginErrorMsg.setText("Username/password errati");
////                    }
////                }
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        }
////    }
////    public void NetAsync(View view){
////        new NetCheck().execute();
////    }
//	@Override
//	public void onSignInFailed() {
//		// Sign in has failed. So show the user the sign-in button.
//	    findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//	    findViewById(R.id.sign_out_button).setVisibility(View.GONE);
//	}
//	@Override
//	public void onSignInSucceeded() {
//		// show sign-out button, hide the sign-in button
//	    findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//	    findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
//
//	    // (your code here: update UI, enable functionality that depends on sign in, etc)
//	    //onConnected();
//	}
//	@Override
//	public void onClick(View view) {
//		if (view.getId() == R.id.sign_in_button) {
//	        // start the asynchronous sign in flow
//	        beginUserInitiatedSignIn();
//	    }
//	    else if (view.getId() == R.id.sign_out_button) {
//	        // sign out.
//	        signOut();
//
//	        // show sign-in button, hide the sign-out button
//	        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//	        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
//	    }
//	}
//
//	@Override
//	public void onConnectionFailed(ConnectionResult result) {
//		
//		if (!mIntentInProgress && result.hasResolution()) {
//		    try {
//		      mIntentInProgress = true;
//		      // Store the ConnectionResult so that we can use it later when the user clicks
//			  // 'sign-in'.
//		      mConnectionResult = result;
//		      
////		      startIntentSenderForResult(result.getIntentSender(),
////		          RC_SIGN_IN, null, 0, 0, 0);
//		      if (mSignInClicked) {
//			      // The user has already clicked 'sign-in' so we attempt to resolve all
//			      // errors until the user is signed in, or they cancel.
//			      resolveSignInError();
//		      }
//		      result.startResolutionForResult(this,RC_SIGN_IN);
//		    } catch (SendIntentException e) {
//		      // The intent was canceled before it was sent.  Return to the default
//		      // state and attempt to connect to get an updated ConnectionResult.
//		      mIntentInProgress = false;
//		      mGoogleApiClient.connect();
//		    }
//		  }		
//	}
//
//	/* A helper method to resolve the current ConnectionResult error. */
//	private void resolveSignInError() {
//	  if (mConnectionResult.hasResolution()) {
//	    try {
//	      mIntentInProgress = true;
//	      mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
//	    } catch (SendIntentException e) {
//	      // The intent was canceled before it was sent.  Return to the default
//	      // state and attempt to connect to get an updated ConnectionResult.
//	      mIntentInProgress = false;
//	      mGoogleApiClient.connect();
//	    }
//	  }
//	}
//	
//	public String getPersonNameLogged(){
//		return personName;
//	}
//	
//	public String getPersonIDLogged(){
//		return personID;
//	}
//	
//	public String getPersonEmailLogged(){
//		return personemail;
//	}
//	
//	public String getPersonURLProfilog(){
//		return personGooglePlusProfile;
//	}
//	
//	public String getPersonURLFotog(){
//		return personPhotoUrl;
//	}
//	
//	@Override
//	public void onConnected(Bundle connectionHint) {
//		
//		System.out.println("2 onConnected metodo ........................");
//		mSignInClicked = false;
//		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
//		     currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//		     personName = currentPerson.getDisplayName();
//		     personID = currentPerson.getId();
//		     personPhoto = currentPerson.getImage();
//		     personPhotoUrlTemp = currentPerson.getImage().getUrl();
//		     personPhotoUrl = currentPerson.getImage().getUrl().substring(0, personPhotoUrlTemp.length() - 2) + 200;
//		     personGooglePlusProfile = currentPerson.getUrl();
//		     personemail = Plus.AccountApi.getAccountName(mGoogleApiClient);
//		    
//		   //Query di inserimento
//     		ContentValues contentValues = new ContentValues();
//     		
//     		contentValues.put("id", personID);
//     		contentValues.put("nome", personName);
//     		contentValues.put("email", personemail);
//     		contentValues.put("profilog", personGooglePlusProfile);
//     		contentValues.put("fotog", personPhotoUrl);
//     		//Accedo al database in scrittura
//     		SQLiteDatabase db = mMioDbHelper.getWritableDatabase();
//     		db.insert("user", null, contentValues);	//Inserisco i dati
//     		System.out.println("Nuovo record inserito!");
//     		//Toast.makeText(getApplicationContext(), "Nuovo record aggiunto", Toast.LENGTH_SHORT).show();
//     		
////            final String sql = "SELECT * FROM user";
////            Cursor cursor = db.rawQuery(sql, null);
////            cursor.moveToFirst();
////            for(int i=0; i<cursor.getCount()*5/*x 5 xkè sono le righe per i campi in totale*5*/; i++){
////            	System.out.println("campo numero:" +i+" ---->\n"+cursor.getString(i));
////            }
//            
////            if(cursor.moveToFirst()) { 
////			//final TextView tView = (TextView) this.findViewById(R.id.mainTextViewNumeroRecord);
////    		//tView.setText(c.getString(0));
////            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(0));
////            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(1));
////            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(2));
////            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(3));
////            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(4));
////            }
//		     
////		    System.out.println("personName ------->"+personName);
////		    System.out.println("personEmail ------->"+personemail);
////		    System.out.println("personID ------->"+personID);
////		    System.out.println("personGPURL ------->"+personGooglePlusProfile);
////		    System.out.println("personPhotoUrl ------->"+personPhotoUrl);
//		  }
//		
//		//personid - personame - personemail - personGooglePlusProfile
//			
//	}
//
//	@Override
//    public void onConnectionSuspended(int arg0)
//    {
//            mGoogleApiClient.connect();
//
//    }
//
//	protected void onActivityResult(int requestCode, int responseCode, Intent intent)
//    {
//            if (requestCode == RC_SIGN_IN)
//            {
//                    if (responseCode != RESULT_OK)
//                    {
//                            mSignInClicked = false;
//                    }
//
//                    mIntentInProgress = false;
//
//                    if (!mGoogleApiClient.isConnecting())
//                    {
//                            mGoogleApiClient.connect();
//                    }
//            }
//            super.onActivityResult(requestCode, responseCode, intent);
//
//    }
//
//	
//	
//	@Override
//	public void onResult(com.google.android.gms.plus.People.LoadPeopleResult peopleData) {
//		if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
//		    PersonBuffer personBuffer = peopleData.getPersonBuffer();
//		    try {
//		      int count = personBuffer.getCount();
//		      for (int i = 0; i < count; i++) {
//		        Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
//		      }
//		    } finally {
//		      personBuffer.close();
//		    }
//		  } else {
//		    Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
//		  }		
//	}
//	
//	/* Track whether the sign-in button has been clicked so that we know to resolve
//	 * all issues preventing sign-in without waiting.
//	 */
//	private boolean mSignInClicked;
//
//	/* Store the connection result from onConnectionFailed callbacks so that we can
//	 * resolve them when the user clicks sign-in.
//	 */
//	private ConnectionResult mConnectionResult;
//
//
//
//
//	
//	
//	
//}