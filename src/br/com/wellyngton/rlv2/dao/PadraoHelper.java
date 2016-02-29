/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.wellyngton.rlv2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.wellyngton.rlv2.model.Padrao;
import br.com.wellyngton.rlv2.model.SOMVetor;
import br.com.wellyngton.rlv2.model.Usuario;
import br.com.wellyngton.rlv2.model.Padrao.PadraoBD;

/**
 *
 * @author wellyngton
 */
public class PadraoHelper extends SQLiteOpenHelper {
    
    public static final String TIPO_TEXTO = " TEXT";
    public static final String TIPO_INTEGER = " INTEGER";
    public static final String TIPO_REAL = " REAL";
    public static final String VIRGULA = " , ";
    private String sql_cria_tabela_padrao;
    public static final String SQL_APAGA_ENTRADAS_TABELA_PADRAO = "DROP TABLE IF EXISTS "+
            PadraoBD.NOME_TABELA;
    public static final int VERSAO_BD = 2;
    public static final String NOME_BD = "Locutores.db";
    private static PadraoHelper instance;
    
    public static synchronized PadraoHelper getHelper(Context context) {
        if (instance == null)
            instance = new PadraoHelper(context);
        return instance;
    }

    public PadraoHelper(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }

    
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(getSqlCriaTabelaPadrao());
        }catch(SQLException e){
            System.out.println("Nao criou a tabela Padrao: "+e.getLocalizedMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	try{
    		if(oldVersion < 2)
    			db.execSQL(getSqlCriaTabelaPadrao());
        }catch(SQLException e){
            System.out.println("Nao criou a tabela Padrao: "+e.getLocalizedMessage());
        }
    }
    
    public void onDrop(SQLiteDatabase db) {
        try{
            db.beginTransaction();
            db.execSQL(SQL_APAGA_ENTRADAS_TABELA_PADRAO);
        }catch(SQLException e){
            System.out.println("Nao apagou as entrada da tabela Padrao: "+
                    e.getLocalizedMessage());
        }finally{
            db.endTransaction();
            db.close();
        }
        
    }

    public String getSqlCriaTabelaPadrao(){
        
        String retorno = "CREATE TABLE IF NOT EXISTS "+PadraoBD.NOME_TABELA+" ("+
            PadraoBD.COLUNA_PADRAO_ID+" INTEGER PRIMARY KEY, "+
            PadraoBD.COLUNA_TAXA_APRENDIZAGEM+TIPO_REAL+VIRGULA+
            PadraoBD.COLUNA_USUARIO+TIPO_INTEGER+VIRGULA;
        for(int i=0;i<34;i++){
            if(i==33){
                retorno = retorno.concat("c"+i+TIPO_REAL+", FOREIGN KEY(usuario) REFERENCES usuario(usuario_id))");
            }
            retorno = retorno.concat("c"+i+TIPO_REAL+VIRGULA);
        }
        sql_cria_tabela_padrao = retorno;
        return this.sql_cria_tabela_padrao;
    }
    
}
