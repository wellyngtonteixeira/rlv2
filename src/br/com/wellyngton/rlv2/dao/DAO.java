package br.com.wellyngton.rlv2.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DAO {
	
	private SQLiteDatabase db;
	private DAOHelper daoHelper;
	private Context context = null;
	
	public DAO(Context context){
    	this.context = context;
        daoHelper = DAOHelper.getHelper(context);
        open();
    }
	
	public synchronized SQLiteDatabase open() throws SQLException{
    	if(daoHelper==null)
    		daoHelper = DAOHelper.getHelper(context);
        db = daoHelper.getWritableDatabase();
        System.out.println("iniciou o bd.");
        return db;
    }
    
    public synchronized void close(){
    	try{
    		 daoHelper.close();
    		 System.out.println("fechou o bd.");
    	}catch(SQLException e){
    		Log.e("DAO: ", "Close - "+e.getLocalizedMessage());
    	}
    }

}
