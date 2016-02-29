/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.wellyngton.rlv2.controller;

import java.io.IOException;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.View;
import br.com.wellyngton.rlv2.dao.DAO;
import br.com.wellyngton.rlv2.dao.PadraoDAO;
import br.com.wellyngton.rlv2.dao.UsuarioDAO;
import br.com.wellyngton.rlv2.model.Padrao;
import br.com.wellyngton.rlv2.model.SOMQuadro;
import br.com.wellyngton.rlv2.model.SOMTreinamento;
import br.com.wellyngton.rlv2.model.SOMVetor;
import br.com.wellyngton.rlv2.model.Usuario;
import br.com.wellyngton.rlv2.model.Usuario.UsuarioBD;
import br.com.wellyngton.rlv2.util.DSP;
import br.com.wellyngton.rlv2.util.ExtAudioRecorder;
import br.com.wellyngton.rlv2.util.SoundRecord;

/**
 *
 * @author wellyngton
 */
public class TreinoController {
    
    private Usuario usuario = new Usuario();
    private Padrao padrao = new Padrao();
    private SOMVetor caracteristicas;
    private DAO dao;
    private UsuarioDAO usuarioDAO;
    private PadraoDAO padraoDAO;
    private int contaGravacoes;
    //private ExtAudioRecorder gravador = null;
    private SoundRecord gravador = null;
    private String local_arquivo = null;
    private Vector<SOMVetor> entradas = null;
    private SOMVetor somVetor = null;
    private SOMQuadro quadro = null;
    private SOMTreinamento treinamento = null;
    
    public TreinoController(SQLiteDatabase db){
    	usuarioDAO = new UsuarioDAO(db);
        padraoDAO = new PadraoDAO(db);
        contaGravacoes = 0;
        entradas = new Vector<SOMVetor>();
        quadro = new SOMQuadro();
        treinamento = new SOMTreinamento();
    }
    
    public boolean inserirUsuario(String nome, int idade){
        Usuario resposta = null;
        boolean retorno = false;
        try{
            //busca para ver se há um usuário com mesmo nome e idade no BD
            //se a resposta for nula então se insere o novo usuário
            if(usuarioDAO.buscaPorUsuario(nome, idade)==null){
                resposta = usuarioDAO.inserir(nome, idade);
                if(resposta!=null){
                	setUsuario(resposta);
                	retorno = true;
                }
                System.out.println("inseriu usuario. ");
            }
        }catch(SQLException e){
            System.out.println("Nao inseriu na tabela Usuario: " +
                    e.getLocalizedMessage());
            retorno = false;
        }
        return retorno;
    }
    
    public boolean buscarUsuario(String nome, int idade){
    	boolean retorno = false;
    	Usuario ru;
    	Padrao rp;
    	try{
    		ru = usuarioDAO.buscaPorUsuario(nome, idade);
    		if(ru!=null){
    			rp = padraoDAO.buscaPorPadrao(padraoDAO.parametrosDeBusca(ru.getId()));
    			if(rp!=null){
    				usuario = ru;
    				padrao = rp;
    				retorno = true;
    			}else{
    				Log.e("Controller: ", "Padrão");
    			}
    		}else{
    			Log.e("Controller: ", "Usuário nulo");
    		}
    	}catch(Exception e){
    		Log.e("Controller: ", "Erro ao buscar Usuário");
    	}
    	return retorno;
    }

    public boolean atingiuLimite(){
        return contaGravacoes == 3;
    }

    public void incrementaContador(){
        contaGravacoes++;
    }

    public void decrementaContador(){
        contaGravacoes--;
    }

    public void iniciarGravacao(){
        try{
        	gravador = new SoundRecord();
        	gravador.startRecording();
        }catch (Exception ee){
        	Log.e(SoundRecord.class.getName(), "Erro no inicio da gravacao: " + ee.getMessage());
        }
    }

    public void pararGravacao(){
        try{
        	gravador.stopRecording();
        	local_arquivo = gravador.getFilename();
            gravador=null;

        }catch (Exception ee){
        	Log.e(SoundRecord.class.getName(), "Erro ao parar gravacao: " + ee.getMessage());
        }
    }
    
    public void addVetorEntrada(){
    	try{
    		DSP sinal = new DSP(Local_arquivo());
    		entradas.addElement(sinal.retornaMediaMFC());
    	}catch(Exception e){
    		System.out.println("Erro ao addVetorENtrada: "+e.getMessage());
    	}
    }
    
    public boolean treina(){
    	boolean retorno = false;
    	try{
    		treinamento.setTraining(quadro, entradas);
    		treinamento.start();
    		padrao = new Padrao(usuario, treinamento.getTaxaAprendizagem(),
    				treinamento.getBMUEscolhido());
    		Padrao resultado = padraoDAO.inserir(padrao);
    		if(resultado!=null)
    			retorno = true;
    		else
    			System.out.println("padrao nulo");
    	}catch(SQLiteException e){
    		//System.out.println("Erro ao Treinar: "+e.getMessage());
    		throw new SQLiteException("Erro ao Treinar: "+e.getLocalizedMessage());
    	}
    	return retorno;
    }
    
    public boolean reconhece(){
    	boolean retorno = false;
    	try{
    		DSP sinal = new DSP(Local_arquivo());
    		somVetor = sinal.retornaMediaMFC();
    		if(comparacao(somVetor, padrao))
    			retorno = true;
    	}catch(Exception e){
    		Log.e("RECONHECE: ", "Erro ao executar reconhecimento: " + e.getMessage());
    	}
    	return retorno;
    }
    
    public boolean comparacao(SOMVetor vetor, Padrao padrao){
    	boolean retorno = false;
    	double sup, rp;
    	double inf = 0.0;
    	try{
    		sup = padrao.getTaxa_aprendizagem();
    		inf -= padrao.getTaxa_aprendizagem();
    		rp = padrao.getCaracteristicas().distEuclidiana(vetor);
    		Log.i("COMPARANDO: ", "LIM INF: "+inf+" | LIM SUP: "+sup+" | RESPOSTA: "+rp);
    		if(rp <= sup && rp >= inf){
    			retorno = true;
    		}
    	}catch(Exception e){
    		Log.e("RECONHECE: ", "Erro ao comparar vetores: " + e.getMessage());
    	}
    	return retorno;
    	
    }
    
    public String Local_arquivo(){
    	return local_arquivo;
    }
    public UsuarioDAO getUsuarioDAO(){
        return usuarioDAO;
    }
    
    public PadraoDAO getPadraoDAO(){
        return padraoDAO;
    }
    
    public void setUsuario(Usuario usuario){
    	this.usuario = usuario;
    }
    
    public Usuario getUsuario(){
    	return this.usuario;
    }
    
}
