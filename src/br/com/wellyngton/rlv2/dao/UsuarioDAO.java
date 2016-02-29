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
import android.database.sqlite.SQLiteException;
import android.util.Log;
import br.com.wellyngton.rlv2.model.Usuario;
import br.com.wellyngton.rlv2.model.Usuario.UsuarioBD;

/**
 *
 * @author wellyngton
 */
public class UsuarioDAO {
    
	public static final String TIPO_TEXTO = " TEXT";
    public static final String TIPO_INTEGER = " INTEGER";
    public static final String TIPO_REAL = " REAL";
    public static final String VIRGULA = ", ";
    public static final String SQL_CRIA_TABELA_USUARIO = "CREATE TABLE IF NOT EXISTS "+
            UsuarioBD.NOME_TABELA+" ("+
            UsuarioBD.COLUNA_USUARIO_ID+" INTEGER PRIMARY KEY, "+
            UsuarioBD.COLUNA_NOME_USUARIO+TIPO_TEXTO+VIRGULA+
            UsuarioBD.COLUNA_IDADE+TIPO_INTEGER+");";
    
    public static final String SQL_APAGA_ENTRADAS_TABELA_USUARIO = "DROP TABLE IF EXISTS "+
            UsuarioBD.NOME_TABELA;
	private SQLiteDatabase db;
	private DAOHelper daoHelper;
	private Context context;
	
	public UsuarioDAO(SQLiteDatabase db){
		this.db = db;
		try{
			criaTabela(db);
		}catch(SQLException e){
			Log.e("USUARIODAO: ", e.getLocalizedMessage());
		}
	}
	
	public void criaTabela(SQLiteDatabase db){
		try{
			db.execSQL(SQL_CRIA_TABELA_USUARIO);
		}catch(SQLException e){
			Log.e("USUARIODAO: ", "Cria tabela -"+e.getLocalizedMessage());
		}
	}
	/*public UsuarioDAO(Context context){
    	this.context = context;
    	daoHelper = new DAOHelper(context);
    	try{
    		open();
    	}catch(SQLException e){
    		Log.e("USUARIODAO", "SQLException on openning database " + e.getMessage());
			e.printStackTrace();
    	}
    }
	
	public void open() throws SQLException {
		db = daoHelper.getWritableDatabase();
	}

	public void close() {
		daoHelper.close();
	}*/

    public ContentValues valoresParaInserir(String nome, int idade){
        ContentValues valores = new ContentValues();
        valores.put(UsuarioBD.COLUNA_USUARIO_ID, retornaNovoId());
        valores.put(UsuarioBD.COLUNA_NOME_USUARIO, nome);
        valores.put(UsuarioBD.COLUNA_IDADE, idade);
        return valores;
    }
       
    public Usuario inserir(String nome, int idade){
        Usuario retorno = null;
        try{
            ContentValues valores = valoresParaInserir(nome, idade);
            db.beginTransaction();
            long resp = db.insert(Usuario.UsuarioBD.NOME_TABELA, null, valores);
            if(resp!=-1){
            	System.out.println("inseriu Usuario. ");
            	db.setTransactionSuccessful();
            	db.endTransaction();
            	//close();
            	retorno = buscaPorUsuario(nome, idade);
            	System.out.println("buscou Usuario. ");
            }
        }catch(SQLException e){
            System.out.println("Nao inseriu na tabela Usuario: "+
                    e.getLocalizedMessage());
            retorno = null;
        }
        return retorno;
    }
    
    public int retornaNovoId(){
        int retorno = 0;
        Cursor cursor;
        try{
        	db.beginTransaction();
            cursor = db.query(Usuario.UsuarioBD.NOME_TABELA, new String[]{Usuario.UsuarioBD.COLUNA_USUARIO_ID,
                Usuario.UsuarioBD.COLUNA_NOME_USUARIO, Usuario.UsuarioBD.COLUNA_IDADE}, null	, null, null, null, null);
            retorno = cursor.getCount()+1;
            db.setTransactionSuccessful();
            System.out.println("ID Usuario: "+retorno);
        }catch(SQLException e){
            System.out.println("Erro ao retornar ID Usuario: "+e.getLocalizedMessage());
            retorno = -999;
        }finally{
            db.endTransaction();
            //close();
        }
        return retorno;
    }
    
    public String parametrosDeBusca(String nome, int idade){
        String retorno = UsuarioBD.COLUNA_NOME_USUARIO+"='"+nome+"' AND "+
                UsuarioBD.COLUNA_IDADE+"='"+idade+"'";
        return retorno;
    }
    
    public Usuario buscaPorUsuario(String nome, int idade){
        String parametros = null;
    	Usuario retorno = null;
        Cursor cursor;
        try{
        	parametros = parametrosDeBusca(nome, idade);
        	db.beginTransaction();
            cursor = db.query(true, Usuario.UsuarioBD.NOME_TABELA, new String[]{Usuario.UsuarioBD.COLUNA_USUARIO_ID,
                Usuario.UsuarioBD.COLUNA_NOME_USUARIO, Usuario.UsuarioBD.COLUNA_IDADE}, parametros, null, null, null, null, null);
            //Se houver usuário encontrado na busca
            System.out.println("buscar usuario dao p1 ");
            if(cursor.getCount()==1){
                System.out.println("buscar usuario dao p2 ");
                while(cursor.moveToNext()){
                	System.out.println("ID: "+cursor.getInt(0)+" | NOME: "+cursor.getString(1)+" | IDADE: "+cursor.getInt(2));
                	retorno = new Usuario(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
                }
                db.setTransactionSuccessful();
            }
            else{
                System.out.println("cursor usuario vazio: "+cursor.toString());
            }
        }catch(SQLException e){
            System.out.println("Erro ao buscar usuario: "+e.getLocalizedMessage());
        }finally{
            db.endTransaction();
            //close();
        }
        return retorno;
    }
    
}
