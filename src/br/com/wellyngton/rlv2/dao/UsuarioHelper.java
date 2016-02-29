/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.wellyngton.rlv2.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.wellyngton.rlv2.model.Usuario.UsuarioBD;

/**
 *
 * @author wellyngton
 */
public class UsuarioHelper extends SQLiteOpenHelper {
    
    public static final String TIPO_TEXTO = " TEXT";
    public static final String TIPO_INTEGER = " INTEGER";
    public static final String VIRGULA = " , ";
    public static final String SQL_CRIA_TABELA_USUARIO = "CREATE TABLE "+
            UsuarioBD.NOME_TABELA+" ("+
            UsuarioBD.COLUNA_USUARIO_ID+" INTEGER PRIMARY KEY, "+
            UsuarioBD.COLUNA_NOME_USUARIO+TIPO_TEXTO+VIRGULA+
            UsuarioBD.COLUNA_IDADE+TIPO_INTEGER+")";
    
    public static final String SQL_APAGA_ENTRADAS_TABELA_USUARIO = "DROP TABLE IF EXISTS "+
            UsuarioBD.NOME_TABELA;
    
    public static final int VERSAO_BD = 1;
    public static final String NOME_BD = "Locutores.db";
    private static UsuarioHelper instance;
    
    public static synchronized UsuarioHelper getHelper(Context context) {
        if (instance == null)
            instance = new UsuarioHelper(context);
        return instance;
    }

    public UsuarioHelper(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }

    
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(SQL_CRIA_TABELA_USUARIO);
            System.out.println("criou a tabela Usuario: ");
        }catch(SQLException e){
            System.out.println("Nao criou a tabela Usuario: "+e.getLocalizedMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void onDrop(SQLiteDatabase db) {
        try{
            db.beginTransaction();
            db.execSQL(SQL_APAGA_ENTRADAS_TABELA_USUARIO);
        }catch(SQLException e){
            System.out.println("Nao apagou as entrada da tabela Padrao: "+
                    e.getLocalizedMessage());
        }finally{
            db.endTransaction();
            db.close();
        }
    }
 
}
