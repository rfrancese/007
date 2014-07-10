package com.CreaLeParole.library;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.TextView;
 
	public class DbAdapter {
	  @SuppressWarnings("unused")
	  private static final String LOG_TAG = DbAdapter.class.getSimpleName();
	         
	  private Context context;
	  private SQLiteDatabase database;
	  private MioDbHelper dbHelper = null;
	 
	  // Database fields
	  private static final String DATABASE_TABLE_USER	= "user";
	 
	  public static final String KEY_USER_ID = "id";
	  public static final String KEY_NOME = "nome";
	  public static final String KEY_EMAIL = "email";
	  public static final String KEY_PROFILOG = "profilog";
	  public static final String KEY_FOTOG = "fotog";
	 
	  public DbAdapter(Context context) {
	    this.context = context;
	  }
	 
	  public DbAdapter open() throws SQLException {
	    dbHelper = new MioDbHelper(context);
	    database = dbHelper.getWritableDatabase();
	    return this;
	  }
	 
	  public void close() {
	    dbHelper.close();
	  }
	 
	  private ContentValues createContentValues(String id, String nome, String email, String profilog, String fotog) {
	    ContentValues values = new ContentValues();
	    values.put( KEY_USER_ID, id );
	    values.put( KEY_NOME, nome );
	    values.put( KEY_EMAIL, email );
	    values.put( KEY_PROFILOG, profilog );
	    values.put( KEY_FOTOG, fotog );
	     
	   return values;
	  }
	  
	  //	*********************************** INIZIO METODI RICERCA TABELLA USER *********************************** /
	  // RECUPERA TUTTI I DATI DELLA TABELLA USER
	  public String recuperaContenutoTabUser() {
		  
		  	  String ret= "";
		  	  open();
			  final String sql = "SELECT * FROM user";
	          Cursor cursor = database.rawQuery(sql, null);   // database è la variabile che si riferisce a dbhelper che è riferita alla classe miodbhelper e al db dbutenti
	          cursor.moveToFirst();
	          for(int i=0; i<cursor.getCount()*5/*x 5 xkè sono le colonne per getCount che saranno le righe. Il prodotto fornisce i campi totali*/; i++){
	          	//System.out.println("DA RECUPERACONTENUTOTABUSER CLASSE DBADAPTER campo numero:" +i+" ---->\n"+cursor.getString(i));
	          	ret = ret + cursor.getString(i) + "\n";
	          }
	          close();
	    	return ret;
	    }
	  
	  
	// TROVA I DATI DEGLI UTENTI NELLA TABELLA USER FILTRANDOLI ATTRAVERSO IL CAMPO NOME.
	  public String trovaDatiUtenteByFilterNome(String filter_nome) {
		  	String ret="";
		  	open();
		    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
		    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
		                                    KEY_NOME + " like '%"+ filter_nome + "%'", null, null, null, null, null);
		    mCursor.moveToFirst();
		    for(int i=0; i<mCursor.getCount()*5/*x 5 xkè sono le colonne per getCount che saranno le righe. Il prodotto fornisce i campi totali*/; i++){
	          	//System.out.println("DA TROVACONTACTSBYFILTERNOME CLASSE DBADAPTER campo numero:" +i+" ---->\n"+mCursor.getString(i));
	          	ret = ret + mCursor.getString(i) + "\n";
	          }
	          close();
		    return ret;
	  }
	  
		// TROVA I DATI DEGLI UTENTI NELLA TABELLA USER FILTRANDOLI ATTRAVERSO IL CAMPO ID.
		  public String trovaDatiUtenteByFilterID(String filter_id) {
			  	String ret="";
			  	open();
			    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
			    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
			    								KEY_USER_ID + " like '%"+ filter_id + "%'", null, null, null, null, null);
			    mCursor.moveToFirst();
			    for(int i=0; i<mCursor.getCount()*5/*x 5 xkè sono le colonne per getCount che saranno le righe. Il prodotto fornisce i campi totali*/; i++){
		          	//System.out.println("DA TROVACONTACTSBYFILTEID CLASSE DBADAPTER campo numero:" +i+" ---->\n"+mCursor.getString(i));
		          	ret = ret + mCursor.getString(i) + "\n";
		          }
		          close();
			    return ret;
		  }
		  
		// TROVA I DATI DEGLI UTENTI NELLA TABELLA USER FILTRANDOLI ATTRAVERSO IL CAMPO EMAIL.
		  public String trovaDatiUtenteByFilterEmail(String filter_email) {
			  	String ret="";
			  	open();
			    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
			    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
			    								KEY_EMAIL + " like '%"+ filter_email + "%'", null, null, null, null, null);
			    mCursor.moveToFirst();
			    for(int i=0; i<mCursor.getCount()*5/*x 5 xkè sono le colonne per getCount che saranno le righe. Il prodotto fornisce i campi totali*/; i++){
		          	//System.out.println("DA TROVACONTACTSBYFILTEREMAIL CLASSE DBADAPTER campo numero:" +i+" ---->\n"+mCursor.getString(i));
		          	ret = ret + mCursor.getString(i) + "\n";
		          }
		          close();
			    return ret;
		  }
		  
		// TROVA I DATI DEGLI UTENTI NELLA TABELLA USER FILTRANDOLI ATTRAVERSO IL CAMPO PROFILOG.
		  public String trovaDatiUtenteByFilterProfilog(String filter_profilog) {
			  	String ret="";
			  	open();
			    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
			    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
			    								KEY_PROFILOG + " like '%"+ filter_profilog + "%'", null, null, null, null, null);
			    mCursor.moveToFirst();
			    for(int i=0; i<mCursor.getCount()*5/*x 5 xkè sono le colonne per getCount che saranno le righe. Il prodotto fornisce i campi totali*/; i++){
		          	//System.out.println("DA TROVACONTACTSBYFILTERPROFILOG CLASSE DBADAPTER campo numero:" +i+" ---->\n"+mCursor.getString(i));
		          	ret = ret + mCursor.getString(i) + "\n";
		          }
		          close();
			    return ret;
		  }
		  
		// TROVA I DATI DEGLI UTENTI NELLA TABELLA USER FILTRANDOLI ATTRAVERSO IL CAMPO FOTOG.
		  public String trovaDatiUtenteByFilterFotoG(String filter_fotog) {
			  	String ret="";
			  	open();
			    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
			    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
			    								KEY_FOTOG + " like '%"+ filter_fotog + "%'", null, null, null, null, null);
			    mCursor.moveToFirst();
			    for(int i=0; i<mCursor.getCount()*5/*x 5 xkè sono le colonne per getCount che saranno le righe. Il prodotto fornisce i campi totali*/; i++){
		          	//System.out.println("DA TROVACONTACTSBYFILTERFOTOG CLASSE DBADAPTER campo numero:" +i+" ---->\n"+mCursor.getString(i));
		          	ret = ret + mCursor.getString(i) + "\n";
		          }
		          close();
			    return ret;
		  }
//			*********************************** FINE METODI RICERCA TABELLA USER *********************************** /
	  
		  
//			*********************************** INIZIO METODI GESTIONE TABELLA USER *********************************** /

		  //create a contact
		  public long createContact(String id, String nome, String email, String profilog, String fotog ) {
		    ContentValues initialValues = createContentValues(id, nome, email, profilog, fotog);
		    return database.insertOrThrow(DATABASE_TABLE_USER, null, initialValues);
		  }
		 
		  //update a contact
		  public boolean updateContact( String id, String nome, String email, String profilog, String fotog ) {
			  ContentValues updateValues = createContentValues(id, nome, email, profilog, fotog);
		    return database.update(DATABASE_TABLE_USER, updateValues, KEY_USER_ID + "=" + id, null) > 0;
		  }
		                 
		  //delete a contact     
		  public boolean deleteContact(String id) {
		    return database.delete(DATABASE_TABLE_USER, KEY_USER_ID + "=" + id, null) > 0;
		  }
//			*********************************** FINE METODI GESTIONE TABELLA USER *********************************** /

		  ////////////////////////////////////////////////////// METODI PRECEDENTI ///////////////////////////////////////////////////////

	 
	  //fetch all contacts
	  public Cursor fetchAllContacts() {
		  return database.query(DATABASE_TABLE_USER, new String[] { KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG}, null, null, null, null, null);
	  }
	   
	  //fetch contacts filter by a string
	  public Cursor fetchContactsByFilterUserID(String filter_user_id) {
	    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
	    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
	    								KEY_USER_ID + " like '%"+ filter_user_id + "%'", null, null, null, null, null);
	         
	    return mCursor;
	  }
	  
	//fetch contacts filter by a string
	  public Cursor fetchContactsByFilterNome(String filter_nome) {
	    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
	    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
	                                    KEY_NOME + " like '%"+ filter_nome + "%'", null, null, null, null, null);
	         
	    return mCursor;
	  }
	  
	//fetch contacts filter by a string
	  public Cursor fetchContactsByFilterEmail(String filter_email) {
	    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
	    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
	    								KEY_EMAIL + " like '%"+ filter_email + "%'", null, null, null, null, null);
	         
	    return mCursor;
	  }
	  
	//fetch contacts filter by a string
	  public Cursor fetchContactsByFilterProfilog(String filter_profilog) {
	    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
	    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
	    								KEY_PROFILOG + " like '%"+ filter_profilog + "%'", null, null, null, null, null);
	         
	    return mCursor;
	  }
	  
	//fetch contacts filter by a string
	  public Cursor fetchContactsByFilterFotoG(String filterfotog) {
	    Cursor mCursor = database.query(true, DATABASE_TABLE_USER, new String[] {
	    								KEY_USER_ID, KEY_NOME, KEY_EMAIL, KEY_PROFILOG, KEY_FOTOG },
	    								KEY_FOTOG + " like '%"+ filterfotog + "%'", null, null, null, null, null);
	         
	    return mCursor;
	  }
	  
}