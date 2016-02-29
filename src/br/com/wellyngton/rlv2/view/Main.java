/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.wellyngton.rlv2.view;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import br.com.wellyngton.rlv2.controller.TreinoController;
import br.com.wellyngton.rlv2.util.MessageUtil;

/**
 *
 * @author wellyngton
 */
public class Main extends ListActivity {

    public static final int TREINAR = 0;
    public static final int RECONHECER = 1;
    public TreinoController treinoController;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        registerForContextMenu(getListView());
    }
    
    public void onClick(View view){
        switch(view.getId()){
            case R.id.treinar:
                chamaTela(TREINAR);
                break;
            case R.id.reconhecer:
                chamaTela(RECONHECER);
                break;
        }
    }
    
    public void chamaTela(int tela){
        try{
            Intent intencao = null;
            if(tela==0){
                intencao = new Intent(this, Treinar.class);
            }else{
                intencao = new Intent(this, Reconhecer.class);
            }
            intencao.putExtra("tipo", tela);
            startActivityForResult(intencao, tela);
        }catch(Exception e){
            MessageUtil.toast(getApplicationContext(), "ERRO: "+e.getLocalizedMessage());
        }
    }
    
}
