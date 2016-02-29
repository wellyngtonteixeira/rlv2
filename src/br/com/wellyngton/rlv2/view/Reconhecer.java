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
import android.net.Uri;
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
public class Reconhecer extends Activity{
    
    public TreinoController treinoController;
    private SQLiteDatabase db;
    private EditText nome = null;
    private EditText idade = null;
    private Button botaoBuscar = null;
    private ToggleButton botaoPlayer = null;
    private AlertDialog alertaDialog = null;
    private boolean reconheceu = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reconhecer);        
        //treinoController = new TreinoController(this);
        nome = (EditText)findViewById(R.id.nome_usuario_reconhece);
        idade = (EditText)findViewById(R.id.idade_usuario_reconhece);
        botaoBuscar = (Button)findViewById(R.id.busca_usuario);
        botaoPlayer = (ToggleButton)findViewById(R.id.gravar_reconhece);
	botaoPlayer.setEnabled(false);
	try{
    	db = openOrCreateDatabase("Locutores", Context.MODE_WORLD_WRITEABLE, null);
        treinoController = new TreinoController(db);
    }catch(SQLException e){
    	Log.e("Erro MAIN: ", e.getLocalizedMessage());
    }
    }
    
    public void buscar_Onclick(View view){
        try{
            if(treinoController.buscarUsuario(nome.getText().toString(), 
                    Integer.valueOf(idade.getText().toString()))){
                botaoBuscar.setEnabled(false);
                constroiAlertaBusca();
            }else{
                MessageUtil.toast(this, "Usuário não cadastrado, verifique as informações.");
            }
        }catch(Exception e){
            //MessageUtil.toast(this, "Erro ao tentar cadastrar: "+e.getMessage());
        	System.out.println("Erro ao tentar cadastrar: "+e.getMessage());
        }
    }
    
    public void constroiAlertaBusca(){
        try{
            //Vamos inflar o layout da view
            LayoutInflater li = getLayoutInflater();
            //inflamos o layout alerta.xml
            View view = li.inflate(R.layout.alerta_busca, null);
            //construindo alerta
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("RESULTADO DE BUSCA");
            builder.setView(view);
            alertaDialog =  builder.create();
            alertaDialog.show();
        }catch(Exception e){
            MessageUtil.toast(this, "Erro ao construir alerta: "+e.getMessage());
        }
    }
    
    public void alerta_busca_Onclick(View view){
        try{
            nome.setEnabled(false);
            idade.setEnabled(false);
            botaoPlayer.setEnabled(true);
            alertaDialog.dismiss();
        }catch(Exception e){
            MessageUtil.toast(this, "Erro ao confirmar BUSCA usuario: "+e.getMessage());
        }
    }

    public void player_reconhece_Onclick(View view){
        try{
        	if(botaoPlayer.isChecked()){
                    treinoController.iniciarGravacao();
                }else{
                    treinoController.pararGravacao();
                    constroiConfirmacaoGravacao();
                }
        }catch (Exception e){
            MessageUtil.toast(this, "Erro ao clicar no player: "+e.getMessage());
        }
    }

    public void constroiConfirmacaoGravacao(){
        try{
            //Vamos inflar o layout da view
            LayoutInflater li = getLayoutInflater();
            //inflamos o layout confirma_gravacao.xml
            View view = li.inflate(R.layout.confirma_gravacao_padrao, null);
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

    public void confirma_gravacao_padrao_Onclick(View view){
        try{
        	boolean rp = treinoController.reconhece();
        	constroiJanReconheceu(rp);
        	/*if(rp){
        		//MessageUtil.toast(this, "RECONHECEU");
        		//alertaDialog.dismiss();
        		
        	}else{
        		MessageUtil.toast(this, "NÃO RECONHECEU");
        		alertaDialog.dismiss();
        	}*/
        }catch(Exception e){
        	System.out.println("Erro ao confirmar gravacao: "+e.getLocalizedMessage());
        }
    }
    
    public void constroiJanReconheceu(boolean rt){
    	try{
    		//Vamos inflar o layout da view
            LayoutInflater li = getLayoutInflater();
            //inflamos o layout confirma_gravacao.xml
            View view;
            if(rt){
            	view = li.inflate(R.layout.resposta_positiva, null);
            	reconheceu = true;
            }            	
            else
            	view = li.inflate(R.layout.resposta_negativa, null);
            //construindo alerta
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("RECONHECIMENTO ENCERRADO");
            builder.setView(view);
            alertaDialog =  builder.create();
            alertaDialog.show();
    	}catch(Exception e){
    		Log.e("RECONHECER: ", "Não construiu dialogJanReconheceu - "+e.getLocalizedMessage());
    	}
    }
    
    public void resposta_reconhecimento_Onclick(View view){
    	Uri uri = Uri.parse("mailto:wellyngtonteixeira@gmail.com");
    	Intent itEmail = new Intent(Intent.ACTION_SEND, uri);
    	itEmail.setType("plain/text");
    	itEmail.putExtra(Intent.EXTRA_SUBJECT, "Resultado RLV");
    	itEmail.putExtra(Intent.EXTRA_TEXT, "RLV apresentou o seguinte resultado:\n"+
    	"Usuário - "+treinoController.getUsuario().getNome()+"\n"+
    			"Idade - "+treinoController.getUsuario().getIdade()+"\n"+
    	"Reconheceu? "+reconheceu);
    	
    	itEmail.putExtra(Intent.EXTRA_EMAIL, "wellyngtonteixeira@gmail.com");
    	alertaDialog.dismiss();
    	startActivity(Intent.createChooser(itEmail,"Escolha a App para envio do e-mail..."));
    }

    public void cancela_gravacao_padrao_Onclick(View view){
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
		intent = new Intent(Reconhecer.this, Main.class);
		startActivityForResult(intent, BIND_AUTO_CREATE);
    }
}
