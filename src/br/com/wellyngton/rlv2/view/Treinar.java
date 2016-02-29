/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.wellyngton.rlv2.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import br.com.wellyngton.rlv2.controller.TreinoController;
import br.com.wellyngton.rlv2.util.MessageUtil;

/**
 *
 * @author wellyngton
 */
public class Treinar extends Activity{
    
    public TreinoController treinoController;
    private SQLiteDatabase db;
    private EditText nome = null;
    private EditText idade = null;
    private Button botaoCadastrar = null;
    private ToggleButton botaoPlayer = null;
    private AlertDialog alertaDialog = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treinar);        
        //treinoController = new TreinoController(this);
        nome = (EditText)findViewById(R.id.nome_usuario);
        idade = (EditText)findViewById(R.id.idade_usuario);
        botaoCadastrar = (Button)findViewById(R.id.cadastra_usuario);
        botaoPlayer = (ToggleButton)findViewById(R.id.gravar);
	botaoPlayer.setEnabled(false);
	try{
    	db = openOrCreateDatabase("Locutores", Context.MODE_WORLD_WRITEABLE, null);
        treinoController = new TreinoController(db);
    }catch(SQLException e){
    	Log.e("Erro MAIN: ", e.getLocalizedMessage());
    }
    }
    
    public void cadastrar_Onclick(View view){
        try{
            if(treinoController.inserirUsuario(nome.getText().toString(), 
                    Integer.valueOf(idade.getText().toString()))==true){
                botaoCadastrar.setEnabled(false);
                constroiAlertaUsuario();
            }else{
                MessageUtil.toast(this, "Usuário já cadastrado, verifique as informações.");
            }
        }catch(Exception e){
            //MessageUtil.toast(this, "Erro ao tentar cadastrar: "+e.getMessage());
        	System.out.println("Erro ao tentar cadastrar: "+e.getMessage());
        }
    }
    
    public void constroiAlertaUsuario(){
        try{
            //Vamos inflar o layout da view
            LayoutInflater li = getLayoutInflater();
            //inflamos o layout alerta.xml
            View view = li.inflate(R.layout.alerta, null);
            //construindo alerta
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("RESULTADO DE CADASTRO");
            builder.setView(view);
            alertaDialog =  builder.create();
            alertaDialog.show();
        }catch(Exception e){
            MessageUtil.toast(this, "Erro ao construir alerta: "+e.getMessage());
        }
    }
    
    public void botao_alerta_Onclick(View view){
        try{
            nome.setEnabled(false);
            idade.setEnabled(false);
            botaoPlayer.setEnabled(true);
            alertaDialog.dismiss();
        }catch(Exception e){
            MessageUtil.toast(this, "Erro ao confirmar cadastrar usuario: "+e.getMessage());
        }
    }

    public void player_Onclick(View view){
        try{
            //while(!treinoController.atingiuLimite()){
            if(!treinoController.atingiuLimite()){   
        	if(botaoPlayer.isChecked()){
                    treinoController.iniciarGravacao();
                }else{
                    treinoController.pararGravacao();
                    constroiConfirmacaoGravacao();
                }}
            //}

        }catch (Exception e){
            MessageUtil.toast(this, "Erro ao clicar no player: "+e.getMessage());
        }
    }

    public void constroiConfirmacaoGravacao(){
        try{
            //Vamos inflar o layout da view
            LayoutInflater li = getLayoutInflater();
            //inflamos o layout confirma_gravacao.xml
            View view = li.inflate(R.layout.confirma_gravacao, null);
            //construindo alerta
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("CONFIRMAÇÃO DE GRAVAÇÃO");
            builder.setView(view);
            alertaDialog =  builder.create();
            alertaDialog.show();
        }catch(Exception e){
            MessageUtil.toast(this, "Erro ao construir alerta: "+e.getMessage());
        }
    }

    public void confirma_gravacao_Onclick(View view){
        try{
        	treinoController.addVetorEntrada();
        	treinoController.incrementaContador();
        	if(treinoController.atingiuLimite()){
        		if(treinoController.treina())
        			constroiEncerraTreino();
        	}else{
        	alertaDialog.dismiss();
        }
        }catch(Exception e){
        	System.out.println("Erro ao confirmar gravacao: "+e.getLocalizedMessage());
        }
    }

    public void cancela_gravacao_Onclick(View view){
        treinoController.decrementaContador();
        alertaDialog.dismiss();
    }
    
    public void constroiEncerraTreino(){
        try{
            //Vamos inflar o layout da view
            LayoutInflater li = getLayoutInflater();
            //inflamos o layout confirma_gravacao.xml
            View view = li.inflate(R.layout.encerra_treinamento, null);
            //construindo alerta
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("TREINO ENCERRADO");
            builder.setView(view);
            alertaDialog =  builder.create();
            alertaDialog.show();
        }catch(Exception e){
            MessageUtil.toast(this, "Erro ao construir alerta: "+e.getMessage());
        }
    }

    public void encerrou_treino_Onclick(View view){
        alertaDialog.dismiss();
        Intent intent;
		intent = new Intent(Treinar.this, Main.class);
		startActivityForResult(intent, BIND_AUTO_CREATE);
    }
}
