package com.CreaLeParole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.CreaLeParole.library.DbAdapter;
import com.CreaLeParole.library.MioDbHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.android.gms.plus.model.people.Person.Image;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.skynet.clp.R;

//
////////            	   ..----..
////////            ..-:"''         ''"-..
////////         .-'                      '-.
////////       .'              .     .       '.
////////     .'   .          .    .      .    .''.
////////   .'  .    .       .   .   .     .   . ..:.
//////// .' .   . .  .       .   .   ..  .   . ....::.
////////..   .   .      .  .    .     .  ..  . ....:IA.
////////.:  .   .    .    .  .  .    .. .  .. .. ....:IA.
////////.: .   .   ..   .    .     . . .. . ... ....:.:VHA.
////////'..  .  .. .   .       .  . .. . .. . .....:.::IHHB.
////////.:. .  . .  . .   .  .  . . . ...:.:... .......:HIHMM.
////////.:.... .   . ."::"'.. .   .  . .:.:.:II;,. .. ..:IHIMMA
////////':.:..  ..::IHHHHHI::. . .  ...:.::::.,,,. . ....VIMMHM
////////.:::I. .AHHHHHHHHHHAI::. .:...,:IIHHHHHHMMMHHL:. . VMMMM
////////.:.:V.:IVHHHHHHHMHMHHH::..:" .:HIHHHHHHHHHHHHHMHHA. .VMMM.
////////:..V.:IVHHHHHMMHHHHHHHB... . .:VPHHMHHHMMHHHHHHHHHAI.:VMMI
////////::V..:VIHHHHHHMMMHHHHHH. .   .I":IIMHHMMHHHHHHHHHHHAPI:WMM
////////::". .:.HHHHHHHHMMHHHHHI.  . .:..I:MHMMHHHHHHHHHMHV:':H:WM
////////:: . :.::IIHHHHHHMMHHHHV  .ABA.:.:IMHMHMMMHMHHHHV:'. .IHWW
////////'.  ..:..:.:IHHHHHMMHV" .AVMHMA.:.'VHMMMMHHHHHV:' .  :IHWV
////////:.  .:...:".:.:TPP"   .AVMMHMMA.:. "VMMHHHP.:... .. :IVAI
////////.:.   '... .:"'   .   ..HMMMHMMMA::. ."VHHI:::....  .:IHW
////////...  .  . ..:IIPPIH: ..HMMMI.MMMV:I:.  .:ILLH:.. ...:I
////////:.   .'"' .:.V". .. .  :HMMM:IMMMI::I. ..:HHIIPPHI::'.P:HM.
////////:. .  .  .. ..:.. .    :AMMM IMMMM..:...:IV":T::I::.".:IHIMA
////////'V:.... . .. .  .  .   'VMMV..VMMV :....:V:.:..:....::IHHHMH
////////"IHH:.II:.. .:. .  . . . " :HB"" . . ..PI:.::.:::..:IHHMMV"
////////:IP""HHII:.  .  .    . . .'V:. . . ..:IH:.:.::IHIHHMMMMM"
////////:V:. VIMA:I..  .     .  . .. . .  .:.I:I:..:IHHHHMMHHMMM
////////:"VI:.VWMA::. .:      .   .. .:. ..:.I::.:IVHHHMMMHMMMMI
////////:."VIIHHMMA:.  .   .   .:  .:.. . .:.II:I:AMMMMMMHMMMMMI
////////:..VIHIHMMMI...::.,:.,:!"I:!"I!"I!"V:AI:VAMMMMMMHMMMMMM'
////////':.:HIHIMHHA:"!!"I.:AXXXVVXXXXXXXA:."HPHIMMMMHHMHMMMMMV
////////V:H:I:MA:W'I :AXXXIXII:IIIISSSSSSXXA.I.VMMMHMHMMMMMM
////////'I::IVA ASSSSXSSSSBBSBMBSSSSSSBBMMMBS.VVMMHIMM'"'
////////I:: VPAIMSSSSSSSSSBSSSMMBSSSBBMMMMXXI:MMHIMMI
////////.I::. "H:XIIXBBMMMMMMMMMMMMMMMMMBXIXXMMPHIIMM'
////////:::I.  ':XSSXXIIIIXSSBMBSSXXXIIIXXSMMAMI:.IMM				
////////:::I:.  .VSSSSSISISISSSBII:ISSSSBMMB:MI:..:MM
////////::.I:.  ':"SSSSSSSISISSXIIXSSSSBMMB:AHI:..MMM.			
////////::.I:. . ..:"BBSSSSSSSSSSSSBBBMMMB:AHHI::.HMMI			
///////:..::.  . ..::":BBBBBSSBBBMMMB:MMMMHHII::IHHMI
////////':.I:... ....:IHHHHHMMMMMMMMMMMMMMMHHIIIIHMMV"
//////// "V:. ..:...:.IHHHMMMMMMMMMMMMMMMMHHHMHHMHP'
////////  ':. .:::.:.::III::IHHHHMMMMMHMHMMHHHHM"
////////    "::....::.:::..:..::IIIIIHHHHMMMHHMV"
////////      "::.::.. .. .  ...:::IIHHMMMMHMV"
////////        "V::... . .I::IHHMMVMMV"'
////////          '"VHVHHHAHHHHMMV:"'
////            
// 

//INSERIRE METODO SWITCH


public class menu_principale extends BaseGameActivity
implements View.OnClickListener, RealTimeMessageReceivedListener,
RoomStatusUpdateListener, RoomUpdateListener, OnInvitationReceivedListener, 
GoogleApiClient.ConnectionCallbacks, OnAccessRevokedListener, GoogleApiClient.OnConnectionFailedListener, 
ResultCallback<com.google.android.gms.plus.People.LoadPeopleResult>, OnPreparedListener{
	
	Button impostazioni,nuovaPartita;
    RadioButton rbs,rbd;
    String partmp;
    boolean radioButtonChecked= true;
    ArrayList<String> notifiche = new ArrayList<String>();
    char letteraCliccata = '0', lettCasuale;
    int sinDestr = 0; //0 -> SINISTRA / 1 -> DESTRA
    
    private menu_principale menu = this;
    
    //*********************** LOGIN ************************//
    
    //GoogleApiClient getApiClient();
    Person currentPerson;
    private String personName;
    private String personID;
    private Image personPhoto;
    private String personGooglePlusProfile;
    private String personemail;
    private String personPhotoUrlTemp;
    private String personPhotoUrl;
    //Variabile per la gestione del database
  	private MioDbHelper mMioDbHelper = null;
  	
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;
    
	/* Track whether the sign-in button has been clicked so that we know to resolve
	 * all issues preventing sign-in without waiting.
	 */
	private boolean mSignInClicked;

	/* Store the connection result from onConnectionFailed callbacks so that we can
	 * resolve them when the user clicks sign-in.
	 */
	private ConnectionResult mConnectionResult;
	
    //******************************************************//
    
    
    
    /*
     * API INTEGRATION SECTION. This section contains the code that integrates
     * the game with the Google Play game services API.
     */

    // Debug tag
    final static boolean ENABLE_DEBUG = true;
    final static String TAG = "CreaLeParole";
    
    //MI PERMETTE DI TERMINARE O MENO IL WHILE DI ESECUZIONE DEL GIOCO.
    boolean inGioco = false;
    
    //MI DEFINISCE IL TURNO DEL GIOCATORE. SE E' TRUE, E' IL TURNO DEL PRIMO GIOCATORE, ALTRIMENTI IL SECONDO
    boolean turnoGiocatore = true;

    // Request codes for the UIs that we show with startActivityForResult:
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;
	private static final int REQUEST_LEADERBOARD = 10003;
	private static final int REQUEST_ACHIEVEMENTS = 10004;

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;

    // Are we playing in multiplayer mode?
    boolean mMultiplayer = false;
    
    //variabile firsttime usata solo all'inizio del porco dio gioco. 
    boolean firstTime = true;
    
    //INDICA SE LA PARTITA E' STATA VINTA O MENO. (TRUE = VINTA / FALSE = PERSA);
    boolean partitaVinta = false;
    
    //SCORE DELL'UTENTE
    int mScore = 0; // user's current score
    
    //serve ad indicare se il gioco è all'inizio o meno
    boolean inizioGioco = true;
    
    //Mappa globale
    Map<Integer, String> parole;
    
    //stringa di appoggio quando ricevo i dati
    String tmp = "";
    
    //indica se sono o meno il primo giocatore
    boolean iMakeTheFirstMove;
    
    //indica se la sfida è avviata o meno
    boolean sfida = false;
    
    //Arraylist contenente tutti gli id dei partecipanti
    ArrayList<String> ids;

    boolean breakCercaParola = false;
    
    //variabile di appoggio
    int valoreBuf = 0;
    
    //variabile di appoggio
    boolean varAppogg = false;
    
    //prenota la sfida se viene trovata la parola
    boolean prenotaSfida = false;
    
    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String mMyId = null;
    
    // ID dell'avversario
    String remoteId = "";
    
    // variabile switch
    
    private Switch mySwitch;
    int switchsel=0;
    
    boolean trovata = false;
    
    // VARIABILI DI UTILIZZO DEL DATABASE
//    private Cursor cursor;
//    private com.CreaLeParole.library.MioDbHelper mMioDbHelper = null;
//    private MioDbHelper dbHelper; 
    private DbAdapter gestoredb;
    // If non-null, this is the id of the invitation we received via the
    // invitation listener
    String mIncomingInvitationId = null;

    // Message buffer for sending messages
    byte[] mMsgBuf = new byte[5];
    
    //VARIABILI LEADERBOARD
    	// IDENTIFICATORI
    String classpunteggiototale = "";
    String classpunteggiomigliore = "";
    String classpartiteeffettuate = "";
    String classpartitevinte = "";
    String classpartiteperse = "";
    
    	// SCORE INTERNI
    int punteggioTotale = 0;
    int punteggioMigliore = 0;
    int numeroPartiteEffettuate = 0;
    int numeroPartiteVinte = 0;
    int numeroPartitePerse = 0;
    
    //VARIABILI OBIETTIVI
    	//IDENTIFICATORI
    String obiettivobenvenuto = "";
    String obiettivoPrimaVittoria = "";
    String obiettivoPerdente = "";
    String obiettivoEIlVincitoreE = "";
    String obiettivo666 = "";
    
    MediaPlayer mpp; //mp3 file in res/raw folder
   
    int tutorial=0;
    SharedPreferences app_preferences;
    int seeklenght;
    boolean flagt= false;
    
    /**
     * Called when the activity is first created.
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	System.out.println("onCreate");
  		// set requested clients (games and cloud save)
  	    setRequestedClients(BaseGameActivity.CLIENT_APPSTATE /*|
  	    		BaseGameActivity.CLIENT_***ES*/);
    	// Con questi 2 imposto l'activity a schermo intero (FULL SCREEN)
  		requestWindowFeature(Window.FEATURE_NO_TITLE);
  		super.onCreate(savedInstanceState);
//  		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//  		if (!prefs.getBoolean("firstTime", false)) {
//
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("firstTime", true);
//            editor.commit();
//        }
//  		sp.getPreferences(MODE_PRIVATE).edit().putString("Name of variable",true).commit();
//  		if (app_preferences.getBoolean("switchsel",switchsel)){
//  			System.out.println("switchsel");
//  		}
//  		System.out.println(switchsel);
  		//Inizializiamo la variabile per poter gestire il database
        mMioDbHelper = new MioDbHelper(getApplicationContext());
  		
  		setContentView(R.layout.activity_menu_principale);
  		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
  		findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
  		
  		mpp = MediaPlayer.create(menu_principale.this, R.raw.we_are_one_ole_ola);
  		mpp.setLooping(true);
  		//mpp.start();						// MEDIAPLAYER ATTIVA QUA
  		
  	  	mySwitch = (Switch) findViewById(R.id.switchSuoni);
  	 
  	  	//set the switch to ON 
//  	  	mySwitch.setChecked(true);
  	  	mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	      	 
    		public void onCheckedChanged(CompoundButton buttonView,
  			boolean isChecked) {
    			if(isChecked){
  	  				mpp.start();
	    	  	     	System.out.println("switch is on");
	    	  	     	salvastatoswitch(0);
  	  			}else{
  	  				if(mpp.isPlaying())
	  	                  {
	  				          mpp.pause();
	  				          mpp.seekTo(0);
	  				        System.out.println("switch is off");
	    	  				salvastatoswitch(1);
	  	                  }
    			}
    		}});
//  	  mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//    	  	
//    		public void onCheckedChanged(CompoundButton buttonView,
//  			boolean isChecked) {
//  	  			if(isChecked){
//  	  				mpp.start();
//	    	  	     	System.out.println("switch is on");
//	    	  	     	salvastatoswitch(0);
//  	  			}else{
//  	  				if(mpp.isPlaying())
//	  	                  {
//	  				          mpp.pause();
//	  				          mpp.seekTo(0);
//	  				        System.out.println("switch is off");
//	    	  				salvastatoswitch(1);
//	  	                  }
        // Check login status in database
//        userFunctions = new UserFunctions();
        
        //Carico la mappa!
        try {
			caricamentoMappa();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
//        if(userFunctions.isUserLoggedIn(getApplicationContext())){
  		// la parte tutorial prima stava qui e stava attivata la riga 377 ora sta in onsignsucceded
//    	switchToScreen(R.id.screen_menu_principale);
    	
    	rbs = (RadioButton) findViewById(R.id.sinistra);
        rbd = (RadioButton) findViewById(R.id.destra);
        
    	// set up a click listener for everything we care about
        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }
            
            // PARTE UTILIZZO DATABASE --- INIZIO
            
            //Inizializiamo la variabile per poter gestire il database
            //dbHelper = new MioDbHelper(getApplicationContext());
            gestoredb = new DbAdapter(getApplicationContext());
            //lg = new Login();
            //VARIABILI DA CLASSE LOGIN USER ATTUALMENTE COLLEGATO
           // System.out.println(lg.getPersonIDLogged()+"\n"+lg.getPersonNameLogged()+"\n"+lg.getPersonEmailLogged()+"\n"+lg.getPersonURLProfilog()+"\n"+lg.getPersonURLFotog());
            System.out.println(gestoredb.recuperaContenutoTabUser());
            // PROVA QUERY FILTRO 1 CON NOME
            System.out.println(gestoredb.trovaDatiUtenteByFilterNome(personName));
            System.out.println("*************************************************************");
            // PROVA QUERY FILTRO 2 CON FILTRO INDIRIZZO EMAIL
            System.out.println(gestoredb.trovaDatiUtenteByFilterEmail(personemail));
            System.out.println("*************************************************************");
            // PROVA QUERY FILTRO 3 CON FILTRO PROFILO GOOGLE
            System.out.println(gestoredb.trovaDatiUtenteByFilterProfilog(personGooglePlusProfile));
            System.out.println("*************************************************************");
            // PROVA QUERY FILTRO 4 con FILTRO FOTO PROFILO GOOGLE
            System.out.println(gestoredb.trovaDatiUtenteByFilterFotoG(personPhotoUrl));
            System.out.println("*************************************************************");
//            mMioDbHelper = new com.CreaLeParole.library.MioDbHelper(getApplicationContext());
////            dbHelper = new DbAdapter(this);
////            dbHelper.open();
////            cursor = dbHelper.fetchAllContacts();
////            dbHelper.close();
//     
//           //startManagingCursor(cursor); // CHECK QUA
//           //Loader aa = new Loader(getApplicationContext());
//	         //Chiediamo l'accesso al db
//	       	SQLiteDatabase db = mMioDbHelper.getReadableDatabase();
//            final String sql = "SELECT * FROM user";
//            cursor = db.rawQuery(sql, null);
//            
//            if(cursor.moveToFirst()) { 
//			//final TextView tView = (TextView) this.findViewById(R.id.mainTextViewNumeroRecord);
//    		//tView.setText(c.getString(0));
//		    	System.out.println("la query select * from user è qst da menuprincipale ---->\n"+cursor.getString(0));}
     
//		     while ( cursor.moveToNext() ) {
//		                     
//		        String contentdb = cursor.getString( cursor.getColumnIndex(DbAdapter.KEY_USER_ID) );
//		        Log.d(TAG, "contenuto_db = " + contentdb);        
//		    }           
//           cursor.close();
           
           // PARTE UTILIZZO DATABASE --- FINE
           
//	        }else{
	        	// user is not logged in show login screen
//	        	Intent login = new Intent(getApplicationContext(), Login.class);
//	        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	        	startActivity(login);
//	        	// Closing dashboard screen
//	        	finish();
//	        }
    }
	
//	public void SaveBoolean(String key, int value){
//	       sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//	       SharedPreferences.Editor editor = sp.edit();
//	       editor.putInt(key, value);
//	       editor.commit();
//	}
//	public void LoadBoolean(){
//	       sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//	       savedValue = sharedPreferences.getInt("key", 0);
//	}

	
	/**
	 * Controllo per vedere se c'è o meno la connessione
	 * @return
	 */
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
//	protected void onPause(){
//		if (this.isFinishing()){
//			mpp.pause();
//		}
//		Context context = getApplicationContext();
//		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
//		List<RunningTaskInfo> taskinfo = am.getRunningTasks(1);
//		if (!taskinfo.isEmpty()){
//			ComponentName topActivity = taskinfo.get(0).topActivity;
//			if (!topActivity.getPackageName().equals(context.getPackageName())){
//				mpp.stop();
//				}else{
//				}
//			}
//		super.onPause();
//	}
    
//	@Override
//	public void onResume() {
//	    super.onResume();  // Always call the superclass method first
//	    for (int id : SCREENS) {
//            findViewById(id).setVisibility(msavescreen == id ? View.VISIBLE : View.GONE);
//        }
//	    mpp.start();
//
//	}
	
	
	
    /**
     * Called by the base class (BaseGameActivity) when sign-in has failed. For
     * example, because the user hasn't authenticated yet. We react to this by
     * showing the sign-in button.
     */
    @Override
    public void onSignInFailed() {
    	System.out.println("onSignInFailed");
        Log.d(TAG, "--------> Sign-in failed.");
     // Sign in has failed. So show the user the sign-in button.
	    findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
	    findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        switchToScreen(R.id.screen_login);
    }

    /**
     * Called by the base class (BaseGameActivity) when sign-in succeeded. We
     * react by going to our main screen.
     */
    @Override
    public void onSignInSucceeded() {
    	System.out.println("onSignInSucceeded");
        Log.d(TAG, "--------> Sign-in succeeded.");

        // show sign-out button, hide the sign-in button
	    findViewById(R.id.sign_in_button).setVisibility(View.GONE);
	    findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
	    
        // register listener so we are notified if we receive an invitation to play
        // while we are in the game
        Games.Invitations.registerInvitationListener(getApiClient(), this);

        // if we received an invite via notification, accept it; otherwise, go to main screen
        if (getInvitationId() != null) {
            acceptInviteToRoom(getInvitationId());		//<--------------bisogna aggiungerlo dopo
            return;
        }
        //switchToScreen(R.id.screen_menu_principale);
     // ***************************************** PARTE TUTORIAL
  		// Get the app's shared preferences
  	    app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

  	    // Get the value for the run counter
  	    tutorial = app_preferences.getInt("tutorial", 0);
  	    switchsel = app_preferences.getInt("switchsel", 0);
  	    
  	    if (tutorial==0){
  	    	switchToScreen(R.id.screen_tutorial);
  	    	// IMPOSTA LA VARIABILE TUTORIAL A 1 PER DIRE CHE è GIA' STATO ESEGUITO UNA VOLTA.
  	  	    SharedPreferences.Editor editor = app_preferences.edit();
  	  	    editor.putInt("tutorial", tutorial=1);
  	  	    editor.commit(); // Very important
  	  	    flagt=true;
  	    }else if (tutorial==1){
  	    	System.out.println("tutorial già eseguito!!");
  	    	System.out.println(mCurScreen);
  	    	
  	    	if (mCurScreen==-1)
  	    		switchToScreen(R.id.screen_menu_principale);
  	    	if(mCurScreen==SCREENS[3]){
  	    		if(getApiClient().isConnected())
  	    			switchToScreen(R.id.screen_menu_principale);
  	    		else
  	    			switchToScreen(R.id.screen_login);
  	    	}else if(mCurScreen!=0)
  	    		switchToScreen(mCurScreen);
//  	  	    if(!(msavescreen==0)){
//  		  	  for (int id : SCREENS) {
//  			          findViewById(id).setVisibility(msavescreen == id ? View.VISIBLE : View.GONE);
//  		  	  	}
//  	  	    }
  	    }

  	    if (switchsel == 0){			// se switchsel è = 0 allora vuol dire che lo switch dei suoni è attivato
  	    	mySwitch.setChecked(true);
  	    }else if (switchsel ==1){		// se switchsel è = 1 allora vuol dire che lo switch dei suoni è disattivato
  	    	mySwitch.setChecked(false);
  	    }
  	    
  	// ***************************************** FINE PARTE TUTORIAL
  	    	//switchToScreen(R.id.screen_menu_principale);
//  		  mpp.start();
  	//set the switch to PREVIOUS SAVED STATE
//	  	mySwitch.setChecked(switchsel);
  	    if(mySwitch.isChecked()){
  	  	  	mpp.seekTo(seeklenght);
  	  	  	mpp.start();
  	    }
//      mpp.setOnPreparedListener(new OnPreparedListener() {
//      @Override
//      public void onPrepared(MediaPlayer mp) {
//          mpp.start();
//          // Do something. For example: playButton.setEnabled(true);
//      }
//  });
    }
    
//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//        
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case R.id.sinistra:
//                if (checked)
//                    // Pirates are the best
//                break;
//            case R.id.destra:
//                if (checked)
//                    // Ninjas rule
//                break;
//        }
//    }
    
    public boolean sinistraDestra() {
		if(rbs.isChecked())
			radioButtonChecked=true;	//sinistra
		else
			radioButtonChecked=false;	//destra
		return radioButtonChecked;
	}      
    
    public void salvastatoswitch(int var){		// se 0 switch attivato se 1 switch disattivato
    		SharedPreferences.Editor editor = app_preferences.edit();
	  	    editor.putInt("switchsel", var);
	  	    editor.commit(); // Very important
    }
    
	@Override
	public void onAccessRevoked(ConnectionResult status) {
		// mPlusClient is now disconnected and access has been revoked.
		// We should now delete any data we need to comply with the
		// developer properties. To reset ourselves to the original state,
		// we should now connect again. We don't have to disconnect as that
		// happens as part of the call.
		getApiClient().connect();
 
		// Hide the sign out buttons, show the sign in button.
		findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
	}
	
	public void onPrepared(MediaPlayer player) {
	    player.start();
	}
	
    @Override
    public void onClick(View v) {
    	Intent intent;
    	try{
	    	System.out.println("onClick");
	//        partmp=((TextView) findViewById(R.id.campo_parole_gioco)).getText().toString();
	        
	        switch (v.getId()) {
	//        case R.id.button_single_player_2:
	//            // play a single-player game
	//            resetGameVars();
	////                startGame(false);		
	//            startGame(true);		//<-------------- imposto a TRUE perchè ho sempre bisogno del multiplayer
	//            break;
	//        case R.id.button_sign_in:
	//             user wants to sign in
	//            if (!verifyPlaceholderIdsReplaced()) {
	//                showAlert("Error: sample not set up correctly. Please see README.");
	//                return;
	//            }
	//            beginUserInitiatedSignIn();
	//            break;
	        case R.id.switchSuoni:
//	        	boolean flag= false;
//	        	//check the current state before we display the screen
//	        	  if(flag){
//	        		  mySwitch.setChecked(true);
//	        		  mp.start();
//	         	     System.out.println("switch is on");
//	        	  }
//	        	  else {
//	        		  mp.pause();
//	      	    	System.out.println("switch is off");
//	      	    	flag = true;
//	        	  }
	      	  	//attach a listener to check for changes in state
	      	  	mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	      	  	
	      		public void onCheckedChanged(CompoundButton buttonView,
	    			boolean isChecked) {
	    	  			if(isChecked){
	    	  				mpp.start();
		    	  	     	System.out.println("switch is on");
		    	  	     	salvastatoswitch(0);
	    	  			}else{
	    	  				if(mpp.isPlaying())
		  	                  {
		  				          mpp.pause();
		  				          mpp.seekTo(0);
		  				        System.out.println("switch is off");
		    	  				salvastatoswitch(1);
		  	                  }
	    	  				
	    	  			}
//	    	  			SharedPreferences.Editor editor = app_preferences.edit();
//	    	  	  	    editor.putBoolean("switchsel", isChecked);;
//	    	  	  	    editor.commit(); // Very important
	      			}
	      	  	});
	        	break;
	        case R.id.button_statistiche:
	        	switchToScreen(R.id.screen_statistiche);
	        	break;
	        case R.id.button_obiettivi:
	        	startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), REQUEST_ACHIEVEMENTS);
	        	break;
	        case R.id.buttonImpostazioniMenuPrincipale:
	        	switchToScreen(R.id.screen_impostazioni);
	        	break;
	        case R.id.button_punteggiomigliore:
	        	startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), classpunteggiomigliore), REQUEST_LEADERBOARD);
	        	break;
	        case R.id.button_punteggiototale:
	        	startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), classpunteggiototale), REQUEST_LEADERBOARD);
	        	break;
	        case R.id.button_partiteeffettuate:
	        	startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), classpartiteeffettuate), REQUEST_LEADERBOARD);
	        	break;
	        case R.id.button_partiteperse:
	        	startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), classpartiteperse), REQUEST_LEADERBOARD);
	        	break;
	        case R.id.button_partitevinte:
	        	startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), classpartitevinte), REQUEST_LEADERBOARD);
	        	break;
	        case R.id.button_logout:					/// MODIFICHE EFFETTUATE QUA -------- 24 06 2014
	        	mCurScreen = R.id.screen_login;	//questo è il bottone che si trova nelle IMPOSTAZIONI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	        	if (getApiClient().isConnected()) {
					// Clear the default account in order to allow the user
					// to potentially choose a different account from the
					// account chooser.
	        	     //Plus.AccountApi.clearDefaultAccount(getApiClient());
	 
					// Disconnect from Google Play Services, then reconnect in
					// order to restart the process from scratch.
	        		getApiClient().disconnect();
	        		//getApiClient().connect();
	 
					// Hide the sign out buttons, show the sign in button.
					findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
					findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
					switchToScreen(R.id.screen_login);
				}
	        	break;
            case R.id.button_tutorial:
            	System.out.println("buttontutorial");			// DA COMPLETARE!!!!
            	switchToScreen(R.id.screen_tutorial);
            	break;
            case R.id.buttonInfo:
            	switchToScreen(R.id.screen_info);
            	break;
	        case R.id.sign_in_button:				/// MODIFICHE EFFETTUATE QUA -------- 24 06 2014
	        	// http://stackoverflow.com/questions/21826126/google-plus-sso-sign-out-functionality-in-android-app
	        	// https://gist.github.com/ianbarber/5170508
//	        	if (!getApiClient().isConnected()) {
//					// Show the dialog as we are now signing in.
//					mConnectionProgressDialog.show();
//					// Make sure that we will start the resolution (e.g. fire the
//					// intent and pop up a dialog for the user) for any errors
//					// that come in.
//					mResolveOnFail = true;
//					// We should always have a connection result ready to resolve,
//					// so we can start that process.
//					if (mConnectionResult != null) {
//						startResolution();
//					} else {
//						// If we don't have one though, we can start connect in
//						// order to retrieve one.
//						getApiClient().connect();
//					}
//				}
	        	// start the asynchronous sign in flow
	        	if(!isNetworkAvailable()){
	    			showAlert("Nessuna connessione! Attiva una connessione ad internet per giocare");
	        	}else{
	        		beginUserInitiatedSignIn();
			        getApiClient().connect();
	        	}
		        break;
	        case R.id.sign_out_button:
	        	// show sign-in button, hide the sign-out button
	            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
	            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
	            signOut();
	        	break;
	        case R.id.buttonNuovaPartita:
	        	switchToScreen(R.id.screen_ricerca_partite);
	        	break;
	        case R.id.buttonPartitaCasuale:
	        	switchToScreen(R.id.screen_game);
	        	startQuickGame();
	            break;
	        case R.id.ButtonSfidaUnAmico:
	            // show list of invitable players
	            intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(getApiClient(), 1, 1);
	            switchToScreen(R.id.screen_wait);
	            startActivityForResult(intent, RC_SELECT_PLAYERS);
	            break;
	        case R.id.buttonVediGliInviti:
	        	// show list of pending invitations
                intent = Games.Invitations.getInvitationInboxIntent(getApiClient());
                switchToScreen(R.id.screen_wait);
                startActivityForResult(intent, RC_INVITATION_INBOX);
	            break;
	        case R.id.button_accept_popup_invitation:
	            // user wants to accept the invitation shown on the invitation popup
	            // (the one we got through the OnInvitationReceivedListener).
	            acceptInviteToRoom(mIncomingInvitationId);
	            mIncomingInvitationId = null;
	            break;
	        	//        case R.id.button_invite_players:
	        	//            // show list of invitable players
	        	//            intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(getApiClient(), 1, 3);
	        	//            switchToScreen(R.id.screen_wait);
	        	//            startActivityForResult(intent, RC_SELECT_PLAYERS);
	        	//            break;
	        	//        case R.id.button_see_invitations:
	        	//            // show list of pending invitations
	        	//            intent = Games.Invitations.getInvitationInboxIntent(getApiClient());
	        	//            switchToScreen(R.id.screen_wait);
	        	//            startActivityForResult(intent, RC_INVITATION_INBOX);
	        	//            break;
	        	//        case R.id.button_accept_popup_invitation:
	        	//            // user wants to accept the invitation shown on the invitation popup
	        	//            // (the one we got through the OnInvitationReceivedListener).
	        	//            acceptInviteToRoom(mIncomingInvitationId);
	        	//            mIncomingInvitationId = null;
	        	//            break;
	        case R.id.A:
	        	if(sinistraDestra()){
	//        		partmp="a"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"a";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'a';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.B:
	        	if(sinistraDestra()){
	//        		partmp="b"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"b";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'b';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.C:
	        	if(sinistraDestra()){
	//        		partmp="c"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"c";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'c';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.D:
	        	if(sinistraDestra()){
	//        		partmp="d"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"d";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'd';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.E:
	        	if(sinistraDestra()){
	//        		partmp="e"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"e";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'e';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.F:
	        	if(sinistraDestra()){
	//        		partmp="f"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"f";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'f';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.G:
	        	if(sinistraDestra()){
	//        		partmp="g"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"g";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'g';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.H:
	        	if(sinistraDestra()){
	//        		partmp="h"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"h";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'h';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.I:
	        	if(sinistraDestra()){
	//        		partmp="i"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"i";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'i';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.L:
	        	if(sinistraDestra()){
	//        		partmp="l"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"l";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'l';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.M:
	        	if(sinistraDestra()){
	//        		partmp="m"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"m";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'm';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.N:
	        	if(sinistraDestra()){
	//        		partmp="n"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"n";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'n';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.O:
	        	if(sinistraDestra()){
	//        		partmp="o"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"o";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'o';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.P:
	        	if(sinistraDestra()){
	//        		partmp="p"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"p";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'p';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.Q:
	        	if(sinistraDestra()){
	//        		partmp="q"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"q";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'q';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.R:
	        	if(sinistraDestra()){
	//        		partmp="r"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"r";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'r';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.S:
	        	if(sinistraDestra()){
	//        		partmp="s"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"s";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 's';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.T:
	        	if(sinistraDestra()){
	//        		partmp="t"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"t";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 't';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.U:
	        	if(sinistraDestra()){
	//        		partmp="u"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"u";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'u';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.V:
	        	if(sinistraDestra()){
	//        		partmp="v"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"v";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'v';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.Z:
	        	if(sinistraDestra()){
	//        		partmp="z"+partmp;//scrivere all'inizio il carattere a
	        		sinDestr=0;
	        	}else{
	//        		partmp=partmp+"z";//scrivere alla fine il carattere a
	        		sinDestr=1;
	        	}
	        	letteraCliccata = 'z';
	        	breakCercaParola = true;
	        	if(turnoGiocatore)
	        		updateScoreDisplay2();
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText(partmp);
	//        	scoreOnePoint();
	        	break;
	        case R.id.OK:
	//        	((TextView) findViewById(R.id.campo_parole_gioco)).setText("a");
	        	if(turnoGiocatore){
	        		System.out.println("- - - - - PULSANTE OK");
		        	sfida = true;
	        	}
	        	break;
	//        case R.id.buttonStatistiche:
	        	
	//        case R.id.buttonPartiteTerminate:
	        case R.id.button_torna_menu:		
	        	leaveRoom();
	        	switchToScreen(R.id.screen_menu_principale);
	        }
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * Serve a creare la stanza di gioco
     */
    void startQuickGame() {
    	System.out.println("startQuickGame");
        // quick-start a game with 1 randomly selected opponent
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        
        // auto-match criteria to invite one random automatch opponent.  
        // You can also specify more opponents (up to 3). 
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                MAX_OPPONENTS, 0);
        
        // build the room config:
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        resetGameVars();
        
     // create room:
        Games.RealTimeMultiplayer.create(getApiClient(), rtmConfigBuilder.build());
        
     // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    
    /**
     * Serve a individuare e caricare i parametri per il multiplayer
     */
    @Override
    public void onActivityResult(int requestCode, int responseCode,Intent intent) {
    	System.out.println("onActivityResult");
    	super.onActivityResult(requestCode, responseCode, intent);

        switch (requestCode) {
	        case RC_SIGN_IN:
	        	if (responseCode != RESULT_OK)
	            {
	                    mSignInClicked = false;
	            }
	
	            mIntentInProgress = false;
	
	            if (!getApiClient().isConnecting())
	            {
	                    getApiClient().connect();
	            }
	            break;
            case RC_SELECT_PLAYERS:
                // we got the result from the "select players" UI -- ready to create the room
                handleSelectPlayersResult(responseCode, intent);
                break;
            case RC_INVITATION_INBOX:
                // we got the result from the "select invitation" UI (invitation inbox). We're
                // ready to accept the selected invitation:
                handleInvitationInboxResult(responseCode, intent);
                break;
            case RC_WAITING_ROOM:
                // we got the result from the "waiting room" UI.
                if (responseCode == Activity.RESULT_OK) {
                    // ready to start playing
                    Log.d(TAG, "Starting game (waiting room returned OK).");
                    startGame(true);
                } else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player indicated that they want to leave the room
                	System.out.println("ENTRO QUI DENTRO 1");
                    leaveRoom();
                } else if (responseCode == Activity.RESULT_CANCELED) {
                    // Dialog was cancelled (user pressed back key, for instance). In our game,
                    // this means leaving the room too. In more elaborate games, this could mean
                    // something else (like minimizing the waiting room UI).
                	System.out.println("ENTRO QUI DENTRO 2");
                    leaveRoom();
                }
                break;
            case REQUEST_LEADERBOARD:
            	System.out.println("leaderboard");
	        case REQUEST_ACHIEVEMENTS:
	        	System.out.println("obiettivi");
        }
    }
    
    // Handle the result of the "Select players UI" we launched when the user clicked the
    // "Invite friends" button. We react by creating a room with those players.
    private void handleSelectPlayersResult(int response, Intent data) {
    	System.out.println("handleSelectPlayersResult");
    	if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** select players UI cancelled, " + response);
            switchToScreen(R.id.screen_menu_principale);
            return;
        }

        Log.d(TAG, "Select players UI succeeded.");

        // get the invitee list
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.d(TAG, "Invitee count: " + invitees.size());

        // get the automatch criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
        }

        // create the room
        Log.d(TAG, "Creating room...");
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.addPlayersToInvite(invitees);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        if (autoMatchCriteria != null) {
            rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        }
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        Games.RealTimeMultiplayer.create(getApiClient(), rtmConfigBuilder.build());
        Log.d(TAG, "Room created, waiting for it to be ready...");
    }

    // Handle the result of the invitation inbox UI, where the player can pick an invitation
    // to accept. We react by accepting the selected invitation, if any.
    private void handleInvitationInboxResult(int response, Intent data) {
    	System.out.println("handleInvitationInboxResult");
    	if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** invitation inbox UI cancelled, " + response);
            switchToScreen(R.id.screen_menu_principale);
            return;
        }

        Log.d(TAG, "Invitation inbox UI succeeded.");
        Invitation inv = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);

        // accept invitation
        acceptInviteToRoom(inv.getInvitationId());
    }

    // Accept the given invitation.
    void acceptInviteToRoom(String invId) {
    	resetGameVars();
    	System.out.println("acceptInviteToRoom");
        // accept the invitation
        Log.d(TAG, "Accepting invitation: " + invId);
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setInvitationIdToAccept(invId)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        Games.RealTimeMultiplayer.join(getApiClient(), roomConfigBuilder.build());
    }

    // Activity is going to the background. We have to leave the current room.
    @Override
    public void onStop() {
    	System.out.println("onStop");
        Log.d(TAG, "**** got onStop");
        
        //salviamo la schermata corrente
        for (int id : SCREENS) {
            if(findViewById(id).getVisibility()==0){
            	mCurScreen = id;
            	System.out.println(" > - - - - - SALVO SCREEN: "+mCurScreen);
            }
        }

//        mp.setDataSource(url); 
//        mpp.setOnPreparedListener(this);
//        mpp.prepareAsync();
//        mpp.setOnPreparedListener(new OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mpp.stop();
//                mpp.seekTo(0);
//                // Do something. For example: playButton.setEnabled(true);
//            }
//        });
//        mpp.stop();
        if(mpp.isPlaying())
        {
			mpp.pause();
			seeklenght= mpp.getCurrentPosition();
//			mpp.seekTo(0);
        }
        
        if (mySwitch.isChecked()){
        	salvastatoswitch(0);
        }else
        	salvastatoswitch(1);
        // if we're in a room, leave it.
        leaveRoom();

        // stop trying to keep the screen on
        stopKeepingScreenOn();

//        switchToScreen(R.id.screen_wait);
        super.onStop();
        
        //PARTE LOGIN
        if(getApiClient() != null) {
            if (getApiClient().isConnected()) {
                getApiClient().disconnect();
                System.out.println("disconnessione gooogle api client");
            }
        }
    }
    
    public GoogleApiClient getGoogleApiClient(){
        return getApiClient();
    }

    // Activity just got to the foreground. We switch to the wait screen because we will now
    // go through the sign-in flow (remember that, yes, every time the Activity comes back to the
    // foreground we go through the sign-in flow -- but if the user is already authenticated,
    // this flow simply succeeds and is imperceptible).
    @Override
    public void onStart() {
    	System.out.println("onStart");
        super.onStart();
//        for (int id : SCREENS) {
//            findViewById(id).setVisibility(msavescreen == id ? View.VISIBLE : View.GONE);
//        }
//        mpp.start();
//      mpp.setOnPreparedListener(new OnPreparedListener() {
//      @Override
//      public void onPrepared(MediaPlayer mp) {
//          mpp.start();
//          // Do something. For example: playButton.setEnabled(true);
//      }
//  });
      //PARTE LOGIN
        if(getApiClient() != null) {
        	System.out.println("1 connessione......");
            getApiClient().connect();
        }
    }

//    /**
//     * Gestisce la chiave per assicurarsi che abbiamo lasciato in maniera pulita
//     * la partita.
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent e) {
//    	System.out.println("onKeyDown");
//        if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_game) {
//        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//        	    @Override
//        	    public void onClick(DialogInterface dialog, int which) {
//        	        switch (which){
//        	        case DialogInterface.BUTTON_POSITIVE:
//        	        	leaveRoom();
//        	        case DialogInterface.BUTTON_NEGATIVE:
//        	            break;
//        	        }
//        	    }
//        	};
//        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        	builder.setMessage("Sei sicuro di voler abbandonare la partita?").setPositiveButton("SI", dialogClickListener)
//        	    .setNegativeButton("NO", dialogClickListener).show();
//        	
//        }else if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_wait) {
//        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//        	    @Override
//        	    public void onClick(DialogInterface dialog, int which) {
//        	        switch (which){
//        	        case DialogInterface.BUTTON_POSITIVE:
//        	        	leaveRoom();
//        	        	switchToScreen(R.id.screen_ricerca_partite);
//        	        case DialogInterface.BUTTON_NEGATIVE:
//        	            break;
//        	        }
//        	    }
//        	};
//        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        	builder.setMessage("Vuoi interrompere la ricerca?").setPositiveButton("SI", dialogClickListener)
//        	    .setNegativeButton("NO", dialogClickListener).show();
//        	return true;
//        }else if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_impostazioni) {
//        	switchToScreen(R.id.screen_menu_principale);
//        	return true;
//        }else if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_statistiche) {
//        	System.out.println("> - - - - - - - - ");
//        	switchToScreen(R.id.screen_menu_principale);
//        	onBackPressed();
//        	return true;
//        }else if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_info) {
//        	switchToScreen(R.id.screen_impostazioni);
//        	return true;
//        }else if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_ricerca_partite) {
//        	switchToScreen(R.id.screen_menu_principale);
//        	return true;
//        }else if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_tutorial && tutorial==1 && flagt==true) {		//	*********************************
//        	switchToScreen(R.id.screen_menu_principale);
//        	flagt= false;
//        	return true;
//        }else if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_tutorial && tutorial==1 && flagt==false) {		//	*********************************
//        	switchToScreen(R.id.screen_impostazioni);
//        	return true;	
//        }else if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_menu_principale) {
//        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//        	    @Override
//        	    public void onClick(DialogInterface dialog, int which) {
//        	        switch (which){
//        	        case DialogInterface.BUTTON_POSITIVE:
//        	        	finish();
//        	        case DialogInterface.BUTTON_NEGATIVE:
//        	            break;
//        	        }
//        	    }
//        	};
//        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        	builder.setMessage("Vuoi uscire dal gioco?").setPositiveButton("SI", dialogClickListener)
//        	    .setNegativeButton("NO", dialogClickListener).show();
//        }else if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_login) {
//        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//        	    @Override
//        	    public void onClick(DialogInterface dialog, int which) {
//        	        switch (which){
//        	        case DialogInterface.BUTTON_POSITIVE:
//        	        	finish();
//        	        case DialogInterface.BUTTON_NEGATIVE:
//        	            break;
//        	        }
//        	    }
//        	};
//        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        	builder.setMessage("Vuoi uscire dal gioco?").setPositiveButton("SI", dialogClickListener)
//        	    .setNegativeButton("NO", dialogClickListener).show();
//        }
//        return super.onKeyDown(keyCode, e);
//    }

    /**
     * Gestisce la chiave per assicurarsi che abbiamo lasciato in maniera pulita
     * la partita.
     */
    @Override
    public void onBackPressed(){
    	System.out.println("onbackpressed");
        if (mCurScreen == R.id.screen_game) {
        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        	        switch (which){
        	        case DialogInterface.BUTTON_POSITIVE:
        	        	leaveRoom();
        	        case DialogInterface.BUTTON_NEGATIVE:
        	            break;
        	        }
        	    }
        	};
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Sei sicuro di voler abbandonare la partita?").setPositiveButton("SI", dialogClickListener)
        	    .setNegativeButton("NO", dialogClickListener).show();
        	
        }else if (mCurScreen == R.id.screen_wait) {
        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        	        switch (which){
        	        case DialogInterface.BUTTON_POSITIVE:
        	        	leaveRoom();
        	        	switchToScreen(R.id.screen_ricerca_partite);
        	        case DialogInterface.BUTTON_NEGATIVE:
        	            break;
        	        }
        	    }
        	};
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Vuoi interrompere la ricerca?").setPositiveButton("SI", dialogClickListener)
        	    .setNegativeButton("NO", dialogClickListener).show();
        }else if (mCurScreen == R.id.screen_impostazioni) {
        	switchToScreen(R.id.screen_menu_principale);
        }else if (mCurScreen == R.id.screen_statistiche) {
        	switchToScreen(R.id.screen_menu_principale);
        }else if (mCurScreen == R.id.screen_info) {
        	switchToScreen(R.id.screen_impostazioni);
        }else if (mCurScreen == R.id.screen_ricerca_partite) {
        	switchToScreen(R.id.screen_menu_principale);
        }else if (mCurScreen == R.id.screen_tutorial && tutorial==1 && flagt==true) {		//	*********************************
        	switchToScreen(R.id.screen_menu_principale);
        	flagt= false;
        }else if (mCurScreen == R.id.screen_tutorial && tutorial==1 && flagt==false) {		//	*********************************
        	switchToScreen(R.id.screen_impostazioni);
        }else if (mCurScreen == R.id.screen_menu_principale) {
        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        	        switch (which){
        	        case DialogInterface.BUTTON_POSITIVE:
        	        	finish();
        	        case DialogInterface.BUTTON_NEGATIVE:
        	            break;
        	        }
        	    }
        	};
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Vuoi uscire dal gioco?").setPositiveButton("SI", dialogClickListener)
        	    .setNegativeButton("NO", dialogClickListener).show();
        }else if (mCurScreen == R.id.screen_login) {
        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        	        switch (which){
        	        case DialogInterface.BUTTON_POSITIVE:
        	        	finish();
        	        case DialogInterface.BUTTON_NEGATIVE:
        	            break;
        	        }
        	    }
        	};
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Vuoi uscire dal gioco?").setPositiveButton("SI", dialogClickListener)
        	    .setNegativeButton("NO", dialogClickListener).show();
        }
    }
    
    /**
     * Richiamato quando si lascia la stanza. 
     */
    void leaveRoom() {
    	System.out.println("leaveRoom");
        Log.d(TAG, "Leaving room.");
        stopKeepingScreenOn();
        resetGameVars();
        if (mRoomId != null) {
        	System.out.println("-- - - - gesu");
            Games.RealTimeMultiplayer.leave(getApiClient(), this, mRoomId);
            mRoomId = null;
        } else {
        	System.out.println("-- - - - bambino");
            //switchToScreen(R.id.screen_menu_principale);			---------> ATTENZIONE RIGA RIMOSSA PER AGGIUNTA DI SALVATAGGIO SCHERMATA APP.
            inGioco= false;
        }
    }

    /**
     * Mostriamo la UI della sala d'attesa per tracciare i progressi degli altri utenti che si stanno connettendo ad essa.
     * @param room
     */
    void showWaitingRoom(Room room) {
    	System.out.println("showWaitingRoom");
    	
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(getApiClient(), room, MIN_PLAYERS);

        // show waiting room UI
        startActivityForResult(i, RC_WAITING_ROOM);
    }

    /**
     * Chiamato quando veniamo invitati ad effettuare una partita. Viene mostrata la richiesta a video.
     */
    @Override
    public void onInvitationReceived(Invitation invitation) {
    	System.out.println("onInvitationReceived");
        // We got an invitation to play a game! So, store it in
        // mIncomingInvitationId
        // and show the popup on the screen.
        mIncomingInvitationId = invitation.getInvitationId();
//        ((TextView) findViewById(R.id.incoming_invitation_text)).setText		DA AGGIUNGERE DOPO
//                invitation.getInviter().getDisplayName() + " " +
//                        getString(R.string.is_inviting_you));
        switchToScreen(mCurScreen); // This will show the invitation popup
    }

    /**
     * Chiamata quando un invito avuto in precedenza è stato rimosso dal device locale.
     * Ad esempio può verificarsi quando un giocatore che ha inviato la richiesta di gioco 
     * lascia la partita (e quindi la stanza).
     */
    @Override
    public void onInvitationRemoved(String invitationId) {
    	System.out.println("onInvitationRemoved");
    	if (mIncomingInvitationId.equals(invitationId)) {
            mIncomingInvitationId = null;
            switchToScreen(mCurScreen); // This will hide the invitation popup
        }
    }
    
    /*
     * CALLBACKS SECTION. This section shows how we implement the several games
     * API callbacks.
     */

    /**
     * Chiamato quando veniamo connessi alla stanza. Non siamo ancora pronti a giocare (potrebbero non essere ancora
     * connessi tutti).
     */
    @Override
    public void onConnectedToRoom(Room room) {
    	System.out.println("onConnectedToRoom");
        Log.d(TAG, "onConnectedToRoom.");

        // get room ID, participants and my ID:
        mRoomId = room.getRoomId();
        mParticipants = room.getParticipants();
        ids = room.getParticipantIds();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(getApiClient()));

        // print out the list of participants (for debug purposes)
        Log.d(TAG, "Room ID: " + mRoomId);
        Log.d(TAG, "My ID " + mMyId);
        Log.d(TAG, "<< CONNECTED TO ROOM>>");
    }
    
    
//    /**
//     * Ritorna true quando ci sono abbastanza giocatori per iniziare il gioco
//     * @param room
//     * @return
//     */
//    public boolean shouldStartGame(Room room) {
//        int connectedPlayers = 0;
//        for (Participant p : room.getParticipants()) {
//            if (p.isConnectedToRoom()) ++connectedPlayers;
//        }
//        return connectedPlayers == 2;
//    }

    /**
     * Chiamato quando lasciamo con successo la stanza (quando lasciamo volontariamente la stanza chiamando
     * leaveRoom()). Se veniamo disconnessi, viene chiamato il metodo onDisconnectedFromRoom().
     */
    @Override
    public void onLeftRoom(int statusCode, String roomId) {
    	System.out.println("onLeftRoom");
        // we have left the room; return to main screen.
        Log.d(TAG, "onLeftRoom, code " + statusCode);
//        switchToMainScreen();
        System.out.println("- - - - - -STANZA LASCIATA CON SUCCESSO");
        switchToScreen(R.id.screen_menu_principale);
        inGioco=false;
    }

    /**
     * Chiamato quando veniamo disconnessi dalla stanza. Veniamo riportati nel menù principale.
     */
    @Override
    public void onDisconnectedFromRoom(Room room) {
    	System.out.println("onDisconnectedFromRoom");
        mRoomId = null;
        showGameError();
    }

    /**
     * Mostra un messaggio di errore di gioco mentre viene cancellato e si viene riportati nel menù principale.
     */
    void showGameError() {
    	System.out.println("showGameError");
    	if (inGioco)
    		if(getString(R.string.game_problem).equalsIgnoreCase("An error occurred while starting the game. Please try again.")){
    			showAlert("Il giocatore ha abbandonato la partita");
    			switchToScreen(R.id.screen_menu_principale);
    		}
    	if(getString(R.string.game_problem).equalsIgnoreCase("An error occurred while starting the game. Please try again."))
    		if(!isNetworkAvailable()){
    			showAlert("Nessuna connessione! Attiva una connessione ad internet per giocare");
    			switchToScreen(R.id.screen_menu_principale);
    		}
//    		showAlert(getString(R.string.game_problem));
//        switchToMainScreen();
        inGioco=false;
    }

    /**
     * Chiamato quando la stanza è stata creata.
     */
    @Override
    public void onRoomCreated(int statusCode, Room room) {
    	System.out.println("onRoomCreated");
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            System.out.println("ERROREEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
            showGameError();
            switchToScreen(R.id.screen_menu_principale);
            return;
        }

//        // show the waiting room UI
        showWaitingRoom(room);					//<------------------------ TOLGO SHOWWAITINGROOM in modo tale da non far vedere la fase di connessione
    }

    /**
     * Chiamato quando tutti i giocatori sono completamente connessi alla stanza.
     */
    @Override
    public void onRoomConnected(int statusCode, Room room) {
    	System.out.println("onRoomConnected");
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }
        updateRoom(room);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
    	System.out.println("onJoinedRoom");
    	Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }

//        // show the waiting room UI
        showWaitingRoom(room);						//<------------------------ TOLGO SHOWWAITINGROOM in modo tale da non far vedere la fase di connessione
    }

    // We treat most of the room update callbacks in the same way: we update our list of
    // participants and update the display. In a real game we would also have to check if that
    // change requires some action like removing the corresponding player avatar from the screen,
    // etc.
    @Override
    public void onPeerDeclined(Room room, List<String> arg1) {
    	System.out.println("onPeerDeclined");
    	updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> arg1) {
    	System.out.println("onPeerInvitedToRoom");
    	updateRoom(room);
    }

    @Override
    public void onP2PDisconnected(String participant) {
    	System.out.println("onP2PDisconnected");
    }

    @Override
    public void onP2PConnected(String participant) {
    	System.out.println("onP2PConnected");
    	
    }

    @Override
    public void onPeerJoined(Room room, List<String> arg1) {
    	System.out.println("onPeerJoined");
    	lettCasuale = genlettera();
    	System.out.println("- - - - - LETTERA GENERATA: "+lettCasuale);
    	updateRoom(room);
    }

    @Override
    public void onPeerLeft(Room room, List<String> peersWhoLeft) {
    	inGioco = false;
    	System.out.println("onPeerLeft");
    	updateRoom(room);
    }

    @Override
    public void onRoomAutoMatching(Room room) {
    	System.out.println("onRoomAutoMatching");
    	updateRoom(room);
    }

    @Override
    public void onRoomConnecting(Room room) {
    	System.out.println("onRoomConnecting");
        updateRoom(room);
    }

    @Override
    public void onPeersConnected(Room room, List<String> peers) {
//    	if(shouldStartGame(room)){
    		updateRoom(room);
//    		startGame(true);
//    	}
    	System.out.println("onPeersConnected");
    	
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
    	System.out.println("onPeersDisconnected");
    	updateRoom(room);
    }

    void updateRoom(Room room) {
    	System.out.println("updateRoom");
        if (room != null) {
            mParticipants = room.getParticipants();
        }
        if (mParticipants != null) {
            updatePeerScoresDisplay();
        }
    }
    
    
    
    /*
     **************************** GAME LOGIC SECTION. Methods that implement the game's rules.
     */
    
    // Current state of the game:
    int mSecondsLeft; // how long until the game ends (seconds)
//    final static int GAME_DURATION = 20; // game duration, seconds.
    String parolaCompleta = "";
  //Score dell'avversario
    int mScoreAvversario;
    
    // Reset game variables in preparation for a new game.
    void resetGameVars() {
    	System.out.println("resetGameVars");
    	((TextView) findViewById(R.id.cronaca)).setText("");
//    	((TextView) findViewById(R.id.campo_parole_gioco)).setText("");
        mSecondsLeft = 10;
        mScore = 0;
        notifiche.clear();
        mScoreAvversario = 0;
        findViewById(R.id.image_vittoria).setVisibility(View.INVISIBLE);
        findViewById(R.id.image_pareggio).setVisibility(View.INVISIBLE);
        findViewById(R.id.image_sconfitta).setVisibility(View.INVISIBLE);
        varAppogg = false;
        valoreBuf = 0;
        parolaCompleta = "";
        mParticipantScore.clear();
        mFinishedParticipants.clear();
        inizioGioco = true;
        firstTime = true;
        prenotaSfida = false;
        tmp = "";
        sfida = false;
		letteraCliccata = '0';
		trovata = false;
		gestisciBottoni(false);
    }
    
    public char genlettera(){
    	Random r = new Random();
    	char c = 'a';
    	int tmp = r.nextInt(26);
    	
    	//genero una lettera casuale per iniziare la partita
    	while(tmp==9||tmp==10||tmp==22||tmp==23||tmp==24){
    		tmp = r.nextInt(26);
    	}
    	c = (char) (tmp + 'a');
    	return c;
    }
    
    /**
     * Gameplay vero e proprio
     * @param multiplayer
     */
    void startGame(boolean multiplayer) {
    	System.out.println("startGame");
    	//DECIDIAMO CHI INIZIA A GIOCARE PER PRIMO
    	for(int i=0; i<ids.size(); i++){
    	    String test = ids.get(i);
    	    if( !test.equals(mMyId) ){
    	        remoteId = test;
    	        break;
    	    }
    	}
    	iMakeTheFirstMove = (mMyId.compareTo(remoteId)>0);	//se è > significa che myID è più grande, altrimenti è più grande quello remoto
    	
    	if(iMakeTheFirstMove==true){
//    		((TextView) findViewById(R.id.turno)).setText("PRIMO");
    		turnoGiocatore = true;	//IL PRIMO GIOCATORE GIOCA
    	}else{
//    		((TextView) findViewById(R.id.turno)).setText("SECONDO");
    		turnoGiocatore = false;	//IL SECONDO GIOCATORE STA FERMO
    	}
    	
    	//ABBIAMO DEFINITO CHI GIOCA PER PRIMO, ADESSO IMPOSTIAMO LE CONDIZIONI PER I TURNI
        mMultiplayer = multiplayer;	// serve a capire se sto giocando in multiplayer o no
        updateScoreDisplay();
        broadcastScore(0);	//INIZIO
        switchToScreen(R.id.screen_game);
        
        inGioco = true;

        // run the gameTick() method every second to update the game.
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!inGioco){
                	System.out.println("- - - - GIOCO FINITO FACCIAMO IL RETURN");
                    return;
                }
                else{
                	if(inizioGioco){
                		mSecondsLeft = 16;
                		inizioGioco = false;
                	}
	                gameTick();
	                h.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
    
    /**
     * Permette di poter calcolare la lettera comune per entrambi i giocatori e quindi 
     * da la possibilità di poter iniziare la partita.
     * @param a
     * @param b
     * @return
     */
    public char calcolaLetteraComune(char a, char b){
		char c = ' ';
    	
    	c = (char) ((a + b)/2);
//    	System.out.println(c);
    	if(c == 'j'){
    		c = (char) (c - 1);
//    		System.out.println("entro");
    	}
    	else if(c == 'k'){
    		c = (char) (c - 2);
//    		System.out.println("entro");
    	}
    	else if(c == 'w'){
    		c = (char) (c - 1);
//    		System.out.println("entro");
    	}
    	else if(c == 'x'){
    		c = (char) (c - 2);
//    		System.out.println("entro");
    	}
    	else if(c == 'y'){
    		c = (char) (c - 3);
//    		System.out.println("entro");
    	}
//    	System.out.println("Valore finale: "+c);
    	return c;
    }
    
    /**
     * Permette di poter gestire gli score.
     * @param valore
     */
    public void gestisciScore(int valore){
    	if(valore==0){		//vinco partita + 10
    		mScore += 10;
    		notifiche.add("+10 punti");
//    		((TextView) findViewById(R.id.cronaca)).setText("+10 punti");
    	}
		else if(valore==1){	//inserisco lettera corretta +5
			mScore += 5;
			notifiche.add("+5 punti");
//			((TextView) findViewById(R.id.cronaca)).setText("+5 punti");
		}
		else if(valore==2){	//non inserisco nulla -5
			mScore -= 5;
			notifiche.add("-5 punti");
//			((TextView) findViewById(R.id.cronaca)).setText("-5 punti");
    	}
		else if(valore==3){	//inserisco lettera sbagliata -10
			mScore -= 10;
			notifiche.add("-10 punti");
//			((TextView) findViewById(R.id.cronaca)).setText("-10 punti");
    	}
		else if(valore==4){
			notifiche.add("-20 punti");
			mScore -= 20;	//hai vinto la partita ma hai effettuato la sfida con una parola non esistente
//			((TextView) findViewById(R.id.cronaca)).setText("-20 punti");
		}
    	if(mScore<=0)
    		mScore=0;
    }
    
    
    // indicates the player scored one point
	void scoreOnePoint() {
		System.out.println("scoreOnePoint");
	    if (mSecondsLeft <= 0)
	        return; // too late!
	    //++mScore;
	    
	    if(sfida && valoreBuf==1){
	    	System.out.println("- - - SESSIONE FINALE");
	    	broadcastScore(2);		//FINISH - Chiamato quando perdo una sfida.
//	    	((TextView) findViewById(R.id.cronaca)).setText("VEDO SE AVVERSARIO HA VINTO..");
	    	notifiche.add("VEDO SE AVVERSARIO HA VINTO..");
	    }
	    else{
	    	broadcastScore(1);		//UPDATE - Chiamato normalmente o quando vinco una sfida.
	    	System.out.println(" - - - PASSO QUA");
	    }
	    updatePeerScoresDisplay();
	    updateScoreDisplay();
	}

	/*
	     **************************** COMMUNICATIONS SECTION. Methods that implement the game's network
	     **************************** protocol.
	     */
	    
	 // Score of other participants. We update this as we receive their scores
	    // from the network.
	    Map<String, Integer> mParticipantScore = new HashMap<String, Integer>();
	//    Map<String, String> mLetteraIniziale = new HashMap<String, String>();
	
	// Participants who sent us their final score.
	Set<String> mFinishedParticipants = new HashSet<String>();

	
	/**
	 * Mostra tutti i messaggi all'utente
	 */
	public void mostraNotificheUtente(){
		String notifica = null;
		boolean contenuto = notifiche.isEmpty();
		if(contenuto)
			notifica = "";
		else
			notifica = notifiche.get(0);
		if(notifica==null)
			((TextView) findViewById(R.id.cronaca)).setText("");
		else
			((TextView) findViewById(R.id.cronaca)).setText(notifica);
		if(!notifiche.isEmpty())
			notifiche.remove(0);
	}
	

	// Game tick -- update countdown, check if game ended.
    void gameTick() {
//    	System.out.println("- - - - "+letteraCliccata);
    	
    	//ATTIVO/DISATTIVO LA TASTIERA
    	if(mSecondsLeft<=10)
    		gestisciBottoni(turnoGiocatore);
		//se è il mio turno eseguo l'if altrimenti l'else
		if(turnoGiocatore){		//è il mio turno (il contatore non è ancora sceso a 0)
			if(mSecondsLeft > 0){
				if(mSecondsLeft>13 && mSecondsLeft<=16){	///stiamo all'inizio del gioco
//					((TextView) findViewById(R.id.cronaca)).setText("INIZIO TRA "+(mSecondsLeft-13));
					notifiche.add("INIZIO TRA "+(mSecondsLeft-13));
				}
				else if(mSecondsLeft==13)
//					((TextView) findViewById(R.id.cronaca)).setText("INIZIA LA PARTITA!");
					notifiche.add("INIZIA LA PARTITA!");
				else if(mSecondsLeft==12){
					((TextView) findViewById(R.id.cronaca)).setText("E' IL TUO TURNO");
					notifiche.add("E' IL TUO TURNO");
				}
				else if(mSecondsLeft==11){
//					((TextView) findViewById(R.id.cronaca)).setText("");
				}
				--mSecondsLeft;
//				((TextView) findViewById(R.id.TextView03)).setText("TUO TURNO");
//				System.out.println("----> mSecondsLeft--");
			}else{
//				System.out.println("entro 2");
				//RIPRISTINIAMO I SECONDI A 10
				mSecondsLeft = 10;
				turnoGiocatore = false;
				trovata =  readFromFile(tmp);
				System.out.println("- - - - TMP: "+tmp+" TROVATA: "+trovata);
				if(letteraCliccata != '0' && trovata==true){
					if(prenotaSfida){
						sfida = true;
						prenotaSfida = false;
					}
					System.out.println("- - - - - PAROLA TROVATA");
					gestisciScore(1);
//		    		((TextView) findViewById(R.id.cronaca)).setText("Parola esistente");
		    		notifiche.add("Parola esistente");
		    		valoreBuf = 0;
		    		if(sfida && varAppogg){			//SFIDA SUPERATA - Entro qui solo quando supero la sfida con una lettera esatta
		    			gestisciScore(0);			//vengono aggiunti 10 punti perchè ho vinto la sfida.
		    			sfida = false;
						System.out.println("- - - - - SFIDA SUPERATA");
						varAppogg = false;
					}
					scoreOnePoint();
				}else{
					System.out.println("- - - - - PAROLA NON TROVATA");
					((TextView) findViewById(R.id.campo_parole_gioco)).setText(parolaCompleta);
					if(letteraCliccata == '0'){
//						((TextView) findViewById(R.id.cronaca)).setText("Non hai inserito niente");
						notifiche.add("Non hai inserito niente");
						if(!sfida){
							gestisciScore(2);
						}
						letteraCliccata = '0';
						scoreOnePoint();
					}
					else{
//						((TextView) findViewById(R.id.cronaca)).setText("Parola non esistente");
						notifiche.add("Parola non esistente");
						if(!sfida){
							gestisciScore(3);
						}
						letteraCliccata = '0';
						scoreOnePoint();
					}
				}
				tmp = "";
				letteraCliccata = '0';
				trovata = false;
			}
			mostraNotificheUtente();
			// update countdown
			((TextView) findViewById(R.id.countdown)).setText((mSecondsLeft < 10 ? "0" : "") + String.valueOf(mSecondsLeft));
		}else{					//il contatore è sceso a 0. Devo bloccare i bottoni
			if(mSecondsLeft > 10){
				if(mSecondsLeft>13 && mSecondsLeft<=16){	///stiamo all'inizio del gioco
//					((TextView) findViewById(R.id.cronaca)).setText("INIZIO TRA "+(mSecondsLeft-13));
					notifiche.add("INIZIO TRA "+(mSecondsLeft-13));
				}
				else if(mSecondsLeft==13)
//					((TextView) findViewById(R.id.cronaca)).setText("INIZIA LA PARTITA!");
					notifiche.add("INIZIA LA PARTITA!");
				else if(mSecondsLeft==12){
//					((TextView) findViewById(R.id.cronaca)).setText("E' IL TURNO DELL'AVVERSARIO");
					notifiche.add("E' IL TURNO DELL'AVVERSARIO");
				}
				else if(mSecondsLeft==11){
//					((TextView) findViewById(R.id.cronaca)).setText("");
				}
				--mSecondsLeft;
//				System.out.println("entro 3");
//				((TextView) findViewById(R.id.TextView03)).setText("TURNO AVVERSARIO");
			}
			else{
			}
			// update countdown
			((TextView) findViewById(R.id.countdown)).setText("WAIT");
			mostraNotificheUtente();
		}
    	
    	
//    	System.out.println("gameTick");
//        if (mSecondsLeft > 0)
//            --mSecondsLeft;
//
//        // update countdown
//        ((TextView) findViewById(R.id.countdown)).setText((mSecondsLeft < 10 ? "0" : "") + String.valueOf(mSecondsLeft));

//        if (mSecondsLeft <= 0) {
//            // finish game
//            //findViewById(R.id.button_click_me).setVisibility(View.GONE);
//            broadcastScore(2);	//FINAL
//        }
    }
    
    
    
    
    /*
     **************************** COMMUNICATIONS SECTION. Methods that implement the game's network
     **************************** protocol.
     */
    
 // Called when we receive a real-time message from the network.
    // Messages in our game are made up of 2 bytes: the first one is 'F' or 'U'
    // indicating
    // whether it's a final or interim score. The second byte is the score.
    // There is also the
    // 'S' message, which indicates that the game should start.
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
    	System.out.println("onRealTimeMessageReceived");
    	byte[] buf = rtm.getMessageData();
        String sender = rtm.getSenderParticipantId();
        Log.d(TAG, "Message received: " + (char) buf[0] + "/" + (int) buf[1] + "/" + (char) buf[2] + "/" + (int) buf[3] + "/" + (int) buf[4]);

        valoreBuf = (int) buf[4];
        
        if(buf[0] == 'C'){
        	mScoreAvversario=buf[1];
        	if((int) buf[3]==1){			//l'avversario ha vinto
//        		((TextView) findViewById(R.id.cronaca)).setText("SFIDA PERSA");
        		notifiche.add("SFIDA PERSA");
        	}else if((int) buf[3]==0){		//l'avversario ha perso
//        		((TextView) findViewById(R.id.cronaca)).setText("SFIDA VINTA");
        		notifiche.add("SFIDA VINTA");
        	}
        	updatePeerScoresDisplay();
        	updateScoreDisplay();
        	inGioco = false;
        	mostraRisultati();
        }
        
        if (buf[0] == 'F' || buf[0] == 'U' || buf[0] == 'I') {
//        	tmp= mLetteraIniziale.get(sender);
        	if(buf[0] == 'F'){
//        		((TextView) findViewById(R.id.cronaca)).setText("AVVERSARIO HA PERSO LA SFIDA");
        		notifiche.add("AVVERSARIO HA PERSO LA SFIDA");
        		mFinishedParticipants.add(rtm.getSenderParticipantId());
        		mScoreAvversario=buf[1];
        		sessioneFinale();
        	}else{
	        	if(firstTime){
	        		System.out.println("- - - - - - ENTRO IN FIRST TIME");
	        		System.out.println("---------------------------------------------------"+
	        		"\n-----LETTERA GENERATA DA ME: "+lettCasuale+"---------------"+
	        		"\n-----LETTERA GENERATA DALL'AVVERSARIO: "+buf[2]+"-----"+
	        		"---------------------------------------------------");
	        		parolaCompleta = ""+calcolaLetteraComune(lettCasuale, (char) buf[2]);
	        		System.out.println("- - - - - - - LA LETTERA COMUNE E': "+parolaCompleta);
	        		firstTime = false;
	        	}else{
		//        	System.out.println("----------------------tmp----------"+tmp);
	        		if(buf[2]=='0'&&buf[1]==mScoreAvversario-5){		//l'avversario non ha inserito nulla.
	        			System.out.println("- - - - - - PRIMA SESSIONE");
	        		}else if(buf[1]==mScoreAvversario-10){				//l'avversario ha inserito una lettera errata.
	        			System.out.println("- - - - - - SECONDA SESSIONE");
	        		}else if(buf[2]!='0'){
	        			System.out.println("- - - - - - TERZA SESSIONE");
			        	if(buf[3]==0){			//SINISTRA
			//        		if(tmp==null)
			//        			tmp="";
			        		parolaCompleta = (char) buf[2]+parolaCompleta;
			        	}else if(buf[3]==1){	//DESTRA
			//        		if(tmp==null)
			//        			tmp="";
			        		parolaCompleta = parolaCompleta + (char) buf[2];
			        	}
	        		}
		        	mScoreAvversario=buf[1];
		        	
		        	if(valoreBuf==1){
	        			sfida = true;		//L'AVVERSARIO HA AVVIATO LA SFIDA
	        			//sblocco il pulsante SFIDA (OK)
	        			
	        			varAppogg = true;
//	        			((TextView) findViewById(R.id.cronaca)).setText("SFIDA AVVIATA");
	        			notifiche.add("SFIDA AVVIATA");
		        	}
	        		else
	        			sfida = false;
	        	}
	//        	mLetteraIniziale.put(sender,tmp);	//aggiorna il map di colui che ha inviato la lettera!
	//        	mLetteraIniziale.put(mMyId,tmp);	//aggiorna il mio map!
	//        	parolaCompleta = tmp;
	//        	System.out.println("----------------------parolaCompleta----------"+parolaCompleta);
	            // score update.
	            int existingScore = mParticipantScore.containsKey(sender) ?
	                    mParticipantScore.get(sender) : 0;
	            int thisScore = (int) buf[1];
	            if (thisScore > existingScore) {
	                // this check is necessary because packets may arrive out of
	                // order, so we
	                // should only ever consider the highest score we received, as
	                // we know in our
	                // game there is no way to lose points. If there was a way to
	                // lose points,
	                // we'd have to add a "serial number" to the packet.
	                mParticipantScore.put(sender, thisScore);
	            }
	
	            // update the scores on the screen
	            updatePeerScoresDisplay();
	
	            // if it's a final score, mark this participant as having finished
	            // the game
	            if ((char) buf[0] == 'F') {
	                mFinishedParticipants.add(rtm.getSenderParticipantId());
	            }
	            if ((char) buf[0] == 'U') {
	                turnoGiocatore = true;
		        	mSecondsLeft = 10;
		        	tmp = "";
		        	letteraCliccata = '0';
		        	trovata = false;
	            }
        	}
        }
        
    }
    
    
    /**
     * Serve a mostrare tutti i risultati e ad aggiornare la leaderboard degli sfidanti
     */
    public void mostraRisultati(){
    	switchToScreen(R.id.screen_risultati);
    	((TextView) findViewById(R.id.nome_giocatore)).setText(personName);
    	if(mScore>mScoreAvversario){	//vinto
//    	System.out.println("mScore in mscore>mscoreavv------> "+mScore);
//    	System.out.println("mScoreAvv in mscore>mscoreavv------> "+mScoreAvversario);
//    		((TextView) findViewById(R.id.vinto_perso)).setText("HAI VINTO");
    		updateLeaderBoardNumeroPartiteVinte(getApiClient(), classpartitevinte, menu);
    		findViewById(R.id.image_vittoria).setVisibility(View.VISIBLE);
    	}else if(mScore==mScoreAvversario){
    		findViewById(R.id.image_pareggio).setVisibility(View.VISIBLE);
    	}else if(mScore<mScoreAvversario){
//        	System.out.println("mScore in mscore<mscoreavv------> "+mScore);
//        	System.out.println("mScoreAvv in mscore<mscoreavv------> "+mScoreAvversario);
//    		((TextView) findViewById(R.id.butt)).setText("HAI PERSO");
    		updateLeaderBoardNumeroPartitePerse(getApiClient(), classpartiteperse, menu);
    		findViewById(R.id.image_sconfitta).setVisibility(View.VISIBLE);
    	}
    	((TextView) findViewById(R.id.score_tuo_ris)).setText(Integer.toString(mScore));
    	((TextView) findViewById(R.id.score_avversario_ris)).setText(Integer.toString(mScoreAvversario));
    	//aggiorno leaderboard
    	updateLeaderBoardPunteggioMigliore(getApiClient(), classpunteggiomigliore, menu);
		updateLeaderBoardPunteggioTotale(getApiClient(), classpunteggiototale, menu);
		updateLeaderBoardNumeroPartiteEffettuate(getApiClient(), classpartiteeffettuate);
    }
    
    
    /**
     * Qui dentro ci entra solo il giocatore che ha avviato la sfida e che deve vedere se ha effettivamente vinto o perso
     */
    public void sessioneFinale(){
    	//vediamo se effettivamente hai vinto o perso
    	trovata =  readFromFileParolaCompleta(parolaCompleta);
		if(trovata){					//la parola con cui hai sfidato è completa
			gestisciScore(0);			//vengono aggiunti 10 punti perchè ho vinto
			sinDestr = 1;				//"1" perchè indico che ho vinto
//			((TextView) findViewById(R.id.cronaca)).setText("SFIDA VINTA");
			notifiche.add("SFIDA VINTA");
			broadcastScore(3);
		}
		else{					//la parola con cui hai sfidato non è completa
			gestisciScore(4);		//vengono sottratti 20 punti perchè ho perso sia il gioco che la sfida
			sinDestr = 0;				//"0" perchè indico che ho vinto
//			((TextView) findViewById(R.id.cronaca)).setText("SFIDA NON VINTA");
			notifiche.add("SFIDA NON VINTA");
			broadcastScore(3);
		}
		updatePeerScoresDisplay();
	    updateScoreDisplay();
		// update the scores on the screen
		inGioco = false;
		mostraRisultati();
    }
    
    // Broadcast my score to everybody else. IL VALORE DI FINALSCORE INDICA SE SI TRATTA DI UN PUNTEGGIO PARZIALE O FINALE
    // SE FINALSCORE F=TRUE SI TRATTA DEL PFINALE INDICATO CON F E QUINDI INVIAMO UN RELIABLEMESSAGE (TCP)
    // SE FINALSCORE F=FALSE SI TRATTA DEL PINTERMEDIO INDICATO CON U E QUINDI INVIAMO UN UNRELIABLEMESSAGE(+VELOCE) (UDP)
    void broadcastScore(int finalScore) {
    	System.out.println("broadcastScore");
        if (!mMultiplayer)
            return; // playing single-player mode

        // First byte in message indicates whether it's a final score or not
//        mMsgBuf[0] = (byte) (finalScore ? 'F' : 'U');
        
        if(finalScore==0){
        	mMsgBuf[0] = (byte) 'I';
        	// Second byte is the score.
        	mMsgBuf[1] = (byte) mScore;
        	mMsgBuf[2] = (byte) lettCasuale;
        	mMsgBuf[3] = (byte) sinDestr;
        }
        else if(finalScore==1){
        	mMsgBuf[0] = (byte) 'U';
        	// Second byte is the score.
        	mMsgBuf[1] = (byte) mScore;
        	mMsgBuf[2] = (byte) letteraCliccata;
        	mMsgBuf[3] = (byte) sinDestr;
        }
        else if(finalScore==2){
        	mMsgBuf[0] = (byte) 'F';
        	// Second byte is the score.
        	mMsgBuf[1] = (byte) mScore;
        	mMsgBuf[2] = (byte) letteraCliccata;
        	mMsgBuf[3] = (byte) sinDestr;
        }else if(finalScore==3){
        	mMsgBuf[0] = (byte) 'C';				//significa CONCLUSIONE
        	mMsgBuf[1] = (byte) mScore;				//invia lo score finale aggiornato
        	mMsgBuf[2] = (byte) letteraCliccata;	//non mi interessa del suo valore
        	mMsgBuf[3] = (byte) sinDestr;			//se vale "1" chi ha avviato la sfida vince, altrimenti perde
        }
        
        if(sfida){
        	mMsgBuf[4] = (byte) 1;
        	System.out.println("- - - -SFIDA TRUE IMPOSTO BYTE 1");
        }
        else
        	mMsgBuf[4] = (byte) 0;
        
        if(mMsgBuf[3]==0){			//SINISTRA
        	if(letteraCliccata!='0')
        		parolaCompleta = letteraCliccata + parolaCompleta;
    	}else if(mMsgBuf[3]==1){	//DESTRA
    		if(letteraCliccata!='0')
    			parolaCompleta = parolaCompleta + letteraCliccata;
    	}
        
        // Send to every other participant.
        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus() != Participant.STATUS_JOINED)
                continue;
            // final score notification must be sent via reliable message
            System.out.println("- - - - - - ENTRIAMO IN BROADCAST - INVIO");
            System.out.println("- - - - - - MROOMID: "+mRoomId);
            if(mRoomId!=null)
            	Games.RealTimeMultiplayer.sendReliableMessage(getApiClient(), null, mMsgBuf, mRoomId, p.getParticipantId());
            else{
            	System.out.println("- - - - - - MROOMID E' NULL: l'avversario è uscito, verrai disconnesso");
            	leaveRoom();
            }
        }
    }
    
    
    /*
     **************************** UI SECTION. Methods that implement the game's UI.
     */
    
 public void gestisciBottoni(boolean turno){
		if(turno){
			findViewById(R.id.A).setEnabled(true);
			findViewById(R.id.B).setEnabled(true);
			findViewById(R.id.C).setEnabled(true);
			findViewById(R.id.D).setEnabled(true);
			findViewById(R.id.E).setEnabled(true);
			findViewById(R.id.F).setEnabled(true);
			findViewById(R.id.G).setEnabled(true);
			findViewById(R.id.H).setEnabled(true);
			findViewById(R.id.I).setEnabled(true);
			findViewById(R.id.L).setEnabled(true);
			findViewById(R.id.M).setEnabled(true);
			findViewById(R.id.N).setEnabled(true);
			findViewById(R.id.O).setEnabled(true);
			findViewById(R.id.P).setEnabled(true);
			findViewById(R.id.Q).setEnabled(true);
			findViewById(R.id.R).setEnabled(true);
			findViewById(R.id.S).setEnabled(true);
			findViewById(R.id.T).setEnabled(true);
			findViewById(R.id.U).setEnabled(true);
			findViewById(R.id.V).setEnabled(true);
			findViewById(R.id.Z).setEnabled(true);
			findViewById(R.id.OK).setEnabled(true);
			if(!sfida)
				findViewById(R.id.OK).setEnabled(true);
			else
				findViewById(R.id.OK).setEnabled(false);
		}else{
			findViewById(R.id.A).setEnabled(false);
			findViewById(R.id.B).setEnabled(false);
			findViewById(R.id.C).setEnabled(false);
			findViewById(R.id.D).setEnabled(false);
			findViewById(R.id.E).setEnabled(false);
			findViewById(R.id.F).setEnabled(false);
			findViewById(R.id.G).setEnabled(false);
			findViewById(R.id.H).setEnabled(false);
			findViewById(R.id.I).setEnabled(false);
			findViewById(R.id.L).setEnabled(false);
			findViewById(R.id.M).setEnabled(false);
			findViewById(R.id.N).setEnabled(false);
			findViewById(R.id.O).setEnabled(false);
			findViewById(R.id.P).setEnabled(false);
			findViewById(R.id.Q).setEnabled(false);
			findViewById(R.id.R).setEnabled(false);
			findViewById(R.id.S).setEnabled(false);
			findViewById(R.id.T).setEnabled(false);
			findViewById(R.id.U).setEnabled(false);
			findViewById(R.id.V).setEnabled(false);
			findViewById(R.id.Z).setEnabled(false);
			findViewById(R.id.OK).setEnabled(false);
		}
	}


	// This array lists everything that's clickable, so we can install click
    // event handlers.
    final static int[] CLICKABLES = {
            R.id.buttonNuovaPartita, R.id.button_tutorial, R.id.switchSuoni, R.id.button_obiettivi, R.id.button_statistiche, R.id.button_partiteeffettuate, R.id.button_partiteperse,
            R.id.button_partitevinte, R.id.button_punteggiomigliore, R.id.button_punteggiototale, R.id.button_tutorial, R.id.buttonInfo,
            R.id.button_logout, R.id.sign_in_button, R.id.sign_out_button, R.id.buttonPartitaCasuale, R.id.ButtonSfidaUnAmico, 
            R.id.buttonVediGliInviti, R.id.buttonImpostazioniMenuPrincipale, R.id.button_accept_popup_invitation,
            R.id.button_statistiche, R.id.A,R.id.B,R.id.C,R.id.D,R.id.E,R.id.F,R.id.G,R.id.H,
            R.id.I,R.id.L,R.id.M,R.id.N,R.id.O,R.id.P,R.id.Q,R.id.R,R.id.S,R.id.T,R.id.U,R.id.V,R.id.Z,R.id.OK,R.id.button_torna_menu
    };
    
    //    // This array lists all the individual screens our game has.
    final static int[] SCREENS = {
            R.id.screen_game, R.id.screen_ricerca_partite, R.id.screen_menu_principale, R.id.screen_login,
            R.id.screen_wait, R.id.invitation_popup, R.id.screen_impostazioni, R.id.screen_info, R.id.screen_statistiche
            , R.id.screen_risultati, R.id.screen_splash, R.id.screen_tutorial};
    
    int mCurScreen = -1;
    
    
    
    /**
     * Imposta lo screen di interesse a VISIBLE e il resto a GONE
     * @param screenId
     */
    void switchToScreen(int screenId) {
    	System.out.println("switchToScreen");
        // make the requested screen visible; hide all others.
        for (int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
        }
        mCurScreen = screenId;
        // should we show the invitation popup?
        boolean showInvPopup;
        if (mIncomingInvitationId == null) {
            // no invitation, so no popup
            showInvPopup = false;
        } else if (mMultiplayer) {
            // if in multiplayer, only show invitation on main screen
            showInvPopup = (mCurScreen == R.id.screen_menu_principale);
        } else {
            // single-player: show on main screen and gameplay screen
             showInvPopup = (mCurScreen == R.id.screen_ricerca_partite || mCurScreen == R.id.screen_game);	// NN CI SERVE !!
        }
        findViewById(R.id.invitation_popup).setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
    }
    
    // updates the label that shows my score
    void updateScoreDisplay() {
    	System.out.println("updateScoreDisplay");
    	((TextView) findViewById(R.id.campo_parole_gioco)).setText(parolaCompleta);
//    	((TextView) findViewById(R.id.score_giocatore)).setText(mScore);
//    	System.out.println("---------------------updateScoreDisplay-------------"+parolaCompleta);
    }
    
 // updates the label that shows my score
    void updateScoreDisplay2() throws Exception {
    	System.out.println("updateScoreDisplay2");
    	if(letteraCliccata != '0'){
	    	if(sinDestr==0){
	    		tmp = letteraCliccata + parolaCompleta;
	    	}else
	    		tmp = parolaCompleta + letteraCliccata;
	    	((TextView) findViewById(R.id.campo_parole_gioco)).setText(tmp);
    	}else
    		System.out.println("UPDATESCOREDISPLAY -> letteraCliccata vuota !!!");
    }

    // formats a score as a three-digit number
    String formatScore(int i) {
    	System.out.println("formatScore");
        if (i < 0)
            i = 0;
        String s = String.valueOf(i);
        return s.length() == 1 ? "0" + s : s.length() == 2 ? "0" + s : s;
    }

    // updates the screen with the scores from our peers
    void updatePeerScoresDisplay() {
    	System.out.println("updatePeerScoresDisplay");
        ((TextView) findViewById(R.id.score_giocatore)).setText(formatScore(mScore));
        ((TextView) findViewById(R.id.campo_parole_gioco)).setText(parolaCompleta);
        ((TextView) findViewById(R.id.score_avversario)).setText(formatScore(mScoreAvversario));
        System.out.println("- - - - - - VALORE DI parolaCompleta IN updatePeerScoresDisplay: "+parolaCompleta);
    }
    
    

    
    /**
     * SEZIONE METODI VARI
     */
    
 // Sets the flag to keep this screen on. It's recommended to do that during
    // the
    // handshake when setting up a game, because if the screen turns off, the
    // game will be
    // cancelled.
    /**
     * MANTIENE IL DISPLAY SEMPRE ACCESO
     */
    void keepScreenOn() {
    	System.out.println("keepScreenOn");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * TOGLIE IL FLAG DI DISPLAY SEMPRE ACCESO
     */
    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
    	System.out.println("stopKeepingScreenOn");
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    

    
    
//    /**
//     * Individua l'Activity nella quale andare quando si preme il pulsante "back"
//     */
//    @Override
//	public void onBackPressed() {
//    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//    	    @Override
//    	    public void onClick(DialogInterface dialog, int which) {
//    	        switch (which){
//    	        case DialogInterface.BUTTON_POSITIVE:
//    	        	finish();
//    	        case DialogInterface.BUTTON_NEGATIVE:
//    	            break;
//    	        }
//    	    }
//    	};
//
//    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    	builder.setMessage("Vuoi uscire dal gioco").setPositiveButton("Si", dialogClickListener)
//    	    .setNegativeButton("No", dialogClickListener).show();
//	}
    
    
    /**
     * Vedo se la stringa passata è situata nell'hashmap
     * @param parola
     * @return
     */
	private boolean readFromFile(String parola) {
        boolean trovata = false;
        
        breakCercaParola = false;
//        System.out.println("dimensione mappa: "+parole.size());
        for(int i=0;i<this.parole.size()-1;i++){
        	if(!breakCercaParola){
	        	if(parole.get(i).contains(parola)){
	    			trovata = true;
	    			break;
	        	}
        	}else
        		break;
        }
        return trovata;
    }
    
    /**
     * Vedo se la stringa passata è situata nell'hashmap
     * @param parola
     * @return
     */
	private boolean readFromFileParolaCompleta(String parola) {
        boolean trovata = false;
        
        breakCercaParola = false;
//        System.out.println("dimensione mappa: "+parole.size());
        for(int i=0;i<this.parole.size()-1;i++){
        	if(!breakCercaParola){
	        	if(parole.get(i).equalsIgnoreCase(parola)){
	    			trovata = true;
	    			break;
	        	}
        	}else
        		break;
        }
        return trovata;
    }
    
    /**
     * Carico l'intera mappa per poterla sfruttare dopo
     * @throws IOException
     */
    @SuppressLint("UseSparseArrays")
	public void caricamentoMappa() throws IOException{
    	parole = new HashMap<Integer, String>();
    	String receiveString = "";
    	int i=0;
    	
    	//carico il file
    	InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open("parole.txt"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        
        //carico tutto nella mappa
        do{
        	receiveString=bufferedReader.readLine();
        	parole.put(i, receiveString);
        	i++;
	    }while(receiveString!=null);
    }

    /**
     * Update LeaderBoardPunteggioTotale 
     * @param googleApiClient
     * @param leaderboardId
     */
    @SuppressWarnings("unused")
	private static void updateLeaderBoardPunteggioTotale(final GoogleApiClient googleApiClient, final String leaderboardId, final menu_principale menuPrincipale) {
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
                googleApiClient,
                leaderboardId,
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_PUBLIC
        ).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
 
            @Override
            public void onResult(Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
                if (loadPlayerScoreResult != null) {
                    if (GamesStatusCodes.STATUS_OK == loadPlayerScoreResult.getStatus().getStatusCode()) {
                        long score = 0;
                        
                        Log.d(TAG, "----------1--------------------------loadPlayerScoreResult.getScore() - "+loadPlayerScoreResult.getScore());
                        if (loadPlayerScoreResult.getScore() != null) {
                            score = loadPlayerScoreResult.getScore().getRawScore();
                        }
                        score = score + (long) menuPrincipale.mScore;
                        Games.Leaderboards.submitScore(googleApiClient, leaderboardId, score);
                    }
                }
            }
 
        });
    }
    
    /**
     * Update LeaderBoardPunteggioMigliore 
     * @param googleApiClient
     * @param leaderboardId
     */
    @SuppressWarnings("unused")
	private static void updateLeaderBoardPunteggioMigliore(final GoogleApiClient googleApiClient, final String leaderboardId,final menu_principale menuPrincipale) {
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
                googleApiClient,
                leaderboardId,
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_PUBLIC
        ).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
 
            @Override
            public void onResult(Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
            	Log.d(TAG, "------------------------------------MI TROVO QUA DENTRO");
                if (loadPlayerScoreResult != null) {
                	Log.d(TAG, "------------------------------------MI TROVO QUA DENTRO 2");
                    if (GamesStatusCodes.STATUS_OK == loadPlayerScoreResult.getStatus().getStatusCode()) {
                    	Log.d(TAG, "------------------------------------MI TROVO QUA DENTRO 3");
                    	Log.d(TAG, "----------2--------------------------loadPlayerScoreResult.getScore() - "+loadPlayerScoreResult.getScore());
                    	Log.d(TAG, "\n\n----------2--------------------------loadPlayerScoreResult - "+loadPlayerScoreResult);
                    	long score = 0;
                        long scoreTmp = 0;
                        scoreTmp = (long) menuPrincipale.mScore;
                        if (loadPlayerScoreResult.getScore() != null) {
                        	Log.d(TAG, "------------------------------------MI TROVO QUA DENTRO 4");
                            score = loadPlayerScoreResult.getScore().getRawScore();
                            Log.d(TAG, "score - "+score+"\nscoreTmp"+scoreTmp);
                        }
                        if(score<=scoreTmp){
                        	Games.Leaderboards.submitScore(googleApiClient, leaderboardId, scoreTmp);
                        }
                    }
                }
            }
 
        });
    }
    
    /**
     * Update LeaderBoardNumeroPartiteEffettuate 
     * @param googleApiClient
     * @param leaderboardId
     */
    @SuppressWarnings("unused")
	private static void updateLeaderBoardNumeroPartiteEffettuate(final GoogleApiClient googleApiClient, final String leaderboardId) {
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
                googleApiClient,
                leaderboardId,
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_PUBLIC
        ).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
 
            @Override
            public void onResult(Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
                if (loadPlayerScoreResult != null) {
                    if (GamesStatusCodes.STATUS_OK == loadPlayerScoreResult.getStatus().getStatusCode()) {
                        long score = 0;
                        if (loadPlayerScoreResult.getScore() != null) {
                            score = loadPlayerScoreResult.getScore().getRawScore();
                        }
                        Games.Leaderboards.submitScore(googleApiClient, leaderboardId, ++score);
                    }
                }
            }
 
        });
    }
    
    /**
     * Update LeaderBoardNumeroPartiteVinte 
     * @param googleApiClient
     * @param leaderboardId
     */
    @SuppressWarnings("unused")
	private static void updateLeaderBoardNumeroPartiteVinte(final GoogleApiClient googleApiClient, final String leaderboardId, final menu_principale menuPrincipale) {
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
                googleApiClient,
                leaderboardId,
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_PUBLIC
        ).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
 
            @Override
            public void onResult(Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
                if (loadPlayerScoreResult != null) {
                    if (GamesStatusCodes.STATUS_OK == loadPlayerScoreResult.getStatus().getStatusCode()) {
                        long score = 0;
                        if (loadPlayerScoreResult.getScore() != null) {
                            score = loadPlayerScoreResult.getScore().getRawScore();
                        }
                        if(score==666)
                        	Games.Achievements.unlock(menuPrincipale.getApiClient(), menuPrincipale.obiettivo666);
                        if(score==100)
                        	Games.Achievements.unlock(menuPrincipale.getApiClient(), menuPrincipale.obiettivoEIlVincitoreE);
                        if(score==1)
                        	Games.Achievements.unlock(menuPrincipale.getApiClient(), menuPrincipale.obiettivoPrimaVittoria);
                        Games.Leaderboards.submitScore(googleApiClient, leaderboardId, ++score);
                    }
                }
            }
 
        });
    }
    
    /**
     * Update LeaderBoardNumeroPartitePerse 
     * @param googleApiClient
     * @param leaderboardId
     */
    @SuppressWarnings("unused")
	private static void updateLeaderBoardNumeroPartitePerse(final GoogleApiClient googleApiClient, final String leaderboardId, final menu_principale menuPrincipale) {
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
                googleApiClient,
                leaderboardId,
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_PUBLIC
        ).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
 
            @Override
            public void onResult(Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
                if (loadPlayerScoreResult != null) {
                    if (GamesStatusCodes.STATUS_OK == loadPlayerScoreResult.getStatus().getStatusCode()) {
                        long score = 0;
                        if (loadPlayerScoreResult.getScore() != null) {
                            score = loadPlayerScoreResult.getScore().getRawScore();
                        }
                        if(score==10)
                        	Games.Achievements.unlock(menuPrincipale.getApiClient(), menuPrincipale.obiettivoPerdente);
                        Games.Leaderboards.submitScore(googleApiClient, leaderboardId, ++score);
                    }
                }
            }
 
        });
    }

	@Override
	public void onResult(com.google.android.gms.plus.People.LoadPeopleResult peopleData) {
		if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
		    PersonBuffer personBuffer = peopleData.getPersonBuffer();
		    try {
		      int count = personBuffer.getCount();
		      for (int i = 0; i < count; i++) {
		        Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
		      }
		    } finally {
		      personBuffer.close();
		    }
		  } else {
		    Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
		  }		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress && result.hasResolution()) {
		    try {
		      mIntentInProgress = true;
		      // Store the ConnectionResult so that we can use it later when the user clicks
			  // 'sign-in'.
		      mConnectionResult = result;
		      
//		      startIntentSenderForResult(result.getIntentSender(),
//		          RC_SIGN_IN, null, 0, 0, 0);
		      if (mSignInClicked) {
			      // The user has already clicked 'sign-in' so we attempt to resolve all
			      // errors until the user is signed in, or they cancel.
			      resolveSignInError();
		      }
		      result.startResolutionForResult(this,RC_SIGN_IN);
		    } catch (SendIntentException e) {
		      // The intent was canceled before it was sent.  Return to the default
		      // state and attempt to connect to get an updated ConnectionResult.
		      mIntentInProgress = false;
		      getApiClient().connect();
		    }
		  }				
	}

	/* A helper method to resolve the current ConnectionResult error. */
	private void resolveSignInError() {
	  if (mConnectionResult.hasResolution()) {
	    try {
	      mIntentInProgress = true;
	      mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
	    } catch (SendIntentException e) {
	      // The intent was canceled before it was sent.  Return to the default
	      // state and attempt to connect to get an updated ConnectionResult.
	      mIntentInProgress = false;
	      getApiClient().connect();
	    }
	  }
	}
	
	public String getPersonNameLogged(){
		return personName;
	}
	
	public String getPersonIDLogged(){
		return personID;
	}
	
	public String getPersonEmailLogged(){
		return personemail;
	}
	
	public String getPersonURLProfilog(){
		return personGooglePlusProfile;
	}
	
	public String getPersonURLFotog(){
		return personPhotoUrl;
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		System.out.println("2 onConnected metodo ........................");
		mSignInClicked = false;
		switchToScreen(R.id.screen_menu_principale);
		if (Plus.PeopleApi.getCurrentPerson(getApiClient()) != null) {
			Games.Achievements.unlock(getApiClient(), obiettivobenvenuto);
		     currentPerson = Plus.PeopleApi.getCurrentPerson(getApiClient());
		     personName = currentPerson.getDisplayName();
		     personID = currentPerson.getId();
		     personPhoto = currentPerson.getImage();
		     personPhotoUrlTemp = currentPerson.getImage().getUrl();
		     personPhotoUrl = currentPerson.getImage().getUrl().substring(0, personPhotoUrlTemp.length() - 2) + 200;
		     personGooglePlusProfile = currentPerson.getUrl();
		     personemail = Plus.AccountApi.getAccountName(getApiClient());
		    
		     if(gestoredb.trovaDatiUtenteByFilterEmail(personemail)!="")
		    	 System.out.println("il record: "+personName+" è gia esistente!");
		     else{
		    	//Query di inserimento
		     		ContentValues contentValues = new ContentValues();
		     		
		     		contentValues.put("id", personID);
		     		contentValues.put("nome", personName);
		     		contentValues.put("email", personemail);
		     		contentValues.put("profilog", personGooglePlusProfile);
		     		contentValues.put("fotog", personPhotoUrl);
		     		//Accedo al database in scrittura
		     		SQLiteDatabase db = mMioDbHelper.getWritableDatabase();
		     		db.insert("user", null, contentValues);	//Inserisco i dati
		     		System.out.println("Nuovo record inserito!");
		     }
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
     		//Toast.makeText(getApplicationContext(), "Nuovo record aggiunto", Toast.LENGTH_SHORT).show();
     		
//            final String sql = "SELECT * FROM user";
//            Cursor cursor = db.rawQuery(sql, null);
//            cursor.moveToFirst();
//            for(int i=0; i<cursor.getCount()*5/*x 5 xkè sono le righe per i campi in totale*5*/; i++){
//            	System.out.println("campo numero:" +i+" ---->\n"+cursor.getString(i));
//            }
            
//            if(cursor.moveToFirst()) { 
//			//final TextView tView = (TextView) this.findViewById(R.id.mainTextViewNumeroRecord);
//    		//tView.setText(c.getString(0));
//            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(0));
//            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(1));
//            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(2));
//            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(3));
//            	System.out.println("la query select * from user è qst LOGIN---->\n"+cursor.getString(4));
//            }
		     
//		    System.out.println("personName ------->"+personName);
//		    System.out.println("personEmail ------->"+personemail);
//		    System.out.println("personID ------->"+personID);
//		    System.out.println("personGPURL ------->"+personGooglePlusProfile);
//		    System.out.println("personPhotoUrl ------->"+personPhotoUrl);
		  }
		
		//personid - personame - personemail - personGooglePlusProfile
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		getApiClient().connect();		
	}
}