package com.CreaLeParole.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MioDbHelper extends SQLiteOpenHelper {
	//Nome del database che vogliamo creare
	private static final String DB_NOME = "dbutenti";
	// Lo statement SQL di creazione del database
	private static final String DATABASE_CREATE = "create table user (id text, nome text not null, email text not null, profilog text not null, fotog text not null);";
//	private static final String DATABASE_CREATE = "create table user (id text primary key, nome text not null, email text not null, profilog text not null, fotog text not null);";

	/**
	 * Numero della versione del database.
	 * 
	 * La numerazione della vesione del database deve iniziare dal numero 1.
	 * Quando viene specificata una nuova versione android useguirà la funzione onUpgrade.
	 */
	private static final int DB_VERSIONE = 1;

	public MioDbHelper(Context context) {
		super(context, DB_NOME, null, DB_VERSIONE);
	}

	 // Questo metodo viene chiamato durante la creazione del database
    @Override
    public void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
    }

    // Questo metodo viene chiamato durante l'upgrade del database, ad esempio quando viene incrementato il numero di versione
    @Override
    public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {
             
            database.execSQL("DROP TABLE IF EXISTS user");
            onCreate(database);
             
    }

}
