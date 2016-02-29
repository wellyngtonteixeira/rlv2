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
import br.com.wellyngton.rlv2.model.Padrao;
import br.com.wellyngton.rlv2.model.Padrao.PadraoBD;
import br.com.wellyngton.rlv2.model.Usuario.UsuarioBD;
import br.com.wellyngton.rlv2.model.SOMVetor;
import br.com.wellyngton.rlv2.model.Usuario;

/**
 *
 * @author wellyngton
 */
public class PadraoDAO{
    
	public static final String TIPO_TEXTO = " TEXT";
    public static final String TIPO_INTEGER = " INTEGER";
    public static final String TIPO_REAL = " REAL";
    public static final String VIRGULA = ", ";
    private String sql_cria_tabela_padrao;
    public static final String SQL_APAGA_ENTRADAS_TABELA_PADRAO = "DROP TABLE IF EXISTS "+
            PadraoBD.NOME_TABELA;
	private SQLiteDatabase db;
	private DAOHelper daoHelper;
	private Context context;
	
    public PadraoDAO(SQLiteDatabase db){
    	this.db = db;
		try{
			criaTabela(db);
		}catch(SQLException e){
			Log.e("PADRAODAO: ", e.getLocalizedMessage());
		}
    }
    
    public void criaTabela(SQLiteDatabase db){
    	try{
			db.execSQL(getSqlCriaTabelaPadrao());
		}catch(SQLException e){
			Log.e("PADRAODAO: ", "Cria tabela -"+e.getLocalizedMessage());
		}
    	
    }
    
public String getSqlCriaTabelaPadrao(){
        
        String retorno = "CREATE TABLE IF NOT EXISTS "+PadraoBD.NOME_TABELA+"("+
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
        sql_cria_tabela_padrao = retorno;
        return this.sql_cria_tabela_padrao;
}
	/*public PadraoDAO(Context context) {
    	this.context = context;
    	daoHelper = new DAOHelper(context);
    	try{
    		open();
    	}catch(SQLException e){
    		Log.e("PADRAODAO", "SQLException on openning database " + e.getMessage());
			e.printStackTrace();
    	}
	}
    
    public void open() throws SQLException {
		db = daoHelper.getWritableDatabase();
	}

	public void close() {
		daoHelper.close();
	}*/

	public String parametrosDeBusca(int id, double taxa){
        String retorno = PadraoBD.COLUNA_USUARIO+"='"+id+"' AND "+
                PadraoBD.COLUNA_TAXA_APRENDIZAGEM+"='"+taxa+"'";
        return retorno;
    }
    
    public int retornaNovoId(){
        int retorno = 0;
        Cursor cursor;
        try{
        	db.beginTransaction();
        	 System.out.println("Comecou busca... ");
            cursor = db.query(PadraoBD.NOME_TABELA, getStringBuscaPadrao(), null	, null, null, null, null);
            retorno = cursor.getCount()+1;
            db.setTransactionSuccessful();
            System.out.println("ID Padrao: "+retorno);
        }catch(SQLException e){
            System.out.println("Erro ao retornar ID Padrao: "+e.getLocalizedMessage());
            retorno = -999;
        }finally{
            db.endTransaction();
            //close();
        }
        return retorno;
    }
    
    public ContentValues valoresParaInserir(Padrao padrao){
    	ContentValues valores = new ContentValues();
    	valores.put(PadraoBD.COLUNA_PADRAO_ID, retornaNovoId());
    	valores.put(PadraoBD.COLUNA_USUARIO, padrao.getUsuario().getId());
    	valores.put(PadraoBD.COLUNA_TAXA_APRENDIZAGEM, padrao.getTaxa_aprendizagem());
    	SOMVetor temp = padrao.getCaracteristicas();
    	for(int i=0;i<temp.size();i++){
    		String n = "c"+i;
    		valores.put(n, temp.elementAt(i));
    	}
    	System.out.println("montou valores para inserir padrao ok");
    	return valores;
    }
        
    public Padrao inserir(Padrao padrao){
        Padrao retorno = null;
    	try{
        	ContentValues valores = valoresParaInserir(padrao);
            db.beginTransaction();
            long resp = db.insert(Padrao.PadraoBD.NOME_TABELA, null, valores);
            if(resp!=-1){
            	System.out.println("inseriu padrao ok");
            	db.setTransactionSuccessful();
            	db.endTransaction();
            	//close();
            	retorno = buscaPorPadrao(parametrosDeBusca(padrao.getUsuario().getId(), padrao.getTaxa_aprendizagem()));
            	System.out.println("buscou Padrao. ");
            }
        }catch(SQLException e){
            System.out.println("Nao inseriu na tabela Padrao: "+
                    e.getLocalizedMessage());
        }finally{
        	
            //db.close();
        }
    	return retorno;
    }
    
    public Padrao buscaPorPadrao(String parametros){
        Padrao retorno = null;
        Cursor cursor;
        try{
            db.beginTransaction();
        	cursor = db.query(true, Padrao.PadraoBD.NOME_TABELA, getStringBuscaPadrao(), parametros, null, null, null, null,null);
            //Se houver usuário encontrado na busca
            if(cursor.getCount()==1){
            	while(cursor.moveToNext()){
            		System.out.println("buscou padrao ok");
            		retorno = new Padrao(cursor.getInt(0), new Usuario(), cursor.getDouble(2), montaVetorCaracteristicas(cursor));
            	}
                db.setTransactionSuccessful();
            }
        }catch(SQLException e){
            System.out.println("Erro ao buscar padrao: "+e.getLocalizedMessage());
        }finally{
        	db.endTransaction();
        	//close();
        }
        return retorno;
    }
    
    public String parametrosDeBusca(int usuario){
        String retorno = PadraoBD.COLUNA_USUARIO+"='"+usuario+"'";
        return retorno;
    }
    
    public String[] getStringBuscaPadrao(){
        String[] retorno = new String[37];
        retorno[0]=Padrao.PadraoBD.COLUNA_PADRAO_ID;
        retorno[1]=Padrao.PadraoBD.COLUNA_TAXA_APRENDIZAGEM;
        retorno[2]=Padrao.PadraoBD.COLUNA_USUARIO;
        int t = 0;
        for(int i=3;i<37;i++){
            retorno[i]="c"+t;
            t++;
        }
        return retorno;
    }
    
    public SOMVetor montaVetorCaracteristicas(Cursor cursor){
        SOMVetor retorno = new SOMVetor();
        for(int i=3;i<37;i++){
            retorno.add(cursor.getDouble(i));
        }
        return retorno;
    }
    
}
