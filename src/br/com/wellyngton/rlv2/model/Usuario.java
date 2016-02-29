/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.wellyngton.rlv2.model;

import android.provider.BaseColumns;

/**
 *
 * @author wellyngton
 */
public class Usuario {

    private int id;
    private String nome;
    private int idade;
    private Padrao padrao;
    
    public Usuario() {
    }
    
    public Usuario(String nome){
        this.nome = nome;
    }
    
    public Usuario(int id, String nome, int idade){
        this.id = id;
        this.nome = nome;
        this.idade = idade;
    }
    
    public static abstract class UsuarioBD implements BaseColumns{
        public static final String NOME_TABELA = "usuario";
        public static final String COLUNA_USUARIO_ID = "usuario_id";
        public static final String COLUNA_NOME_USUARIO = "usuario_nome";
        public static final String COLUNA_IDADE = "usuario_idade";        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Padrao getPadrao() {
        return padrao;
    }

    public void setPadrao(Padrao padrao) {
        this.padrao = padrao;
    }
    
    
}
