package br.com.wellyngton.rlv2.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import br.com.wellyngton.rlv2.model.Padrao.PadraoBD;
import br.com.wellyngton.rlv2.model.Usuario.UsuarioBD;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DAOHelper extends SQLiteOpenHelper{
	
	public static final String TIPO_TEXTO = " TEXT";
    public static final String TIPO_INTEGER = " INTEGER";
    public static final String TIPO_REAL = " REAL";
    public static final String VIRGULA = ", ";
    private String sql_cria_tabela_padrao;
    public static final String SQL_APAGA_ENTRADAS_TABELA_PADRAO = "DROP TABLE IF EXISTS "+
            PadraoBD.NOME_TABELA;
    public static final String SQL_CRIA_TABELA_USUARIO = "CREATE TABLE "+
            UsuarioBD.NOME_TABELA+" ("+
            UsuarioBD.COLUNA_USUARIO_ID+" INTEGER PRIMARY KEY, "+
            UsuarioBD.COLUNA_NOME_USUARIO+TIPO_TEXTO+VIRGULA+
            UsuarioBD.COLUNA_IDADE+TIPO_INTEGER+");";
    
    public static final String SQL_APAGA_ENTRADAS_TABELA_USUARIO = "DROP TABLE IF EXISTS "+
            UsuarioBD.NOME_TABELA;
    public static final int VERSAO_BD = 1;
    public static final String NOME_BD = "Locutores.db";
    private static DAOHelper instance;

	public DAOHelper(Context context) {
		super(context, NOME_BD, null, VERSAO_BD);
		// TODO Auto-generated constructor stub
	}
	
	public DAOHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized DAOHelper getHelper(Context context) {
        if (instance == null)
            instance = new DAOHelper(context);
        return instance;
    }
	
	/*public void onOpen(SQLiteDatabase db) throws SQLException
    {
      super.onOpen(db);
      if (!db.isReadOnly())
      {
        db.execSQL("PRAGMA foreign_keys=ON;");
      }
    }*/


	@Override
	public void onCreate(SQLiteDatabase db) {
		try{
			Log.i("DAOHELPER:","passou on create");
			db.execSQL(getSqlCriaTabelaPadrao());
			db.execSQL(SQL_CRIA_TABELA_USUARIO);
		}catch(SQLException e){
			Log.e("DAOHELPER:","ERRO AO CRIAR BANCO DE DADOS: "+e.getMessage());
			e.printStackTrace();
		}		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		try{
			db.execSQL(SQL_APAGA_ENTRADAS_TABELA_USUARIO);
	        db.execSQL(SQL_APAGA_ENTRADAS_TABELA_PADRAO);
	        onCreate(db);
	        Log.i("DAOHELPER: ", "Banco atualizado. Todos os dados foram perdidos");
		}catch(SQLException e){
			Log.e("DAOHELPER: ", "Upgrade - "+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
public String getSqlCriaTabelaPadrao(){
        
        String retorno = "CREATE TABLE "+PadraoBD.NOME_TABELA+"("+
            PadraoBD.COLUNA_PADRAO_ID+" INTEGER PRIMARY KEY, "+
            PadraoBD.COLUNA_TAXA_APRENDIZAGEM+TIPO_REAL+VIRGULA+
            PadraoBD.COLUNA_USUARIO+TIPO_INTEGER+VIRGULA;
        int i=0;
        while(i<=33){
        	if(i==33){
                retorno = retorno.concat("c"+i+TIPO_REAL+");");
            }else{
            	retorno = retorno.concat("c"+i+TIPO_REAL+VIRGULA);
            }
        	i++;
        }
        /*for(int i=0;i<34;i++){
            if(i==33){
                retorno = retorno.concat("c"+i+TIPO_REAL+");");
            }else{
            	retorno = retorno.concat("c"+i+TIPO_REAL+VIRGULA);
            }
        }*/
        sql_cria_tabela_padrao = retorno;
        return this.sql_cria_tabela_padrao;
    }

}
