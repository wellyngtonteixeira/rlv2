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
public class Padrao {
    
    private int id;
    private Usuario usuario;
    private double taxa_aprendizagem;
    private SOMVetor caracteristicas;

    public Padrao() {
    }
    
    public Padrao(int id, Usuario usuario, double taxa, SOMVetor caracteristicas){
        this.id = id;
        this.usuario = usuario;
        this.taxa_aprendizagem = taxa;
        this.caracteristicas = caracteristicas;
    }
    
    public Padrao(Usuario usuario, double taxa, SOMVetor caracteristicas){
        this.usuario = usuario;
        this.taxa_aprendizagem = taxa;
        this.caracteristicas = caracteristicas;
    }
    
    public static abstract class PadraoBD implements BaseColumns{
        public static final String NOME_TABELA = "padrao";
        public static final String COLUNA_PADRAO_ID = "padrao_id";
        public static final String COLUNA_TAXA_APRENDIZAGEM = "taxa_aprendizagem";
        public static final String COLUNA_USUARIO = "usuario";
    }
    
    public Padrao(Usuario usuario){
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public SOMVetor getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(SOMVetor caracteristicas) {
        try{
            this.caracteristicas = caracteristicas;       
        }catch(Exception a){
            if(caracteristicas.size()>34){
                System.out.println("Tamanho do vetor excedido: "+a.getLocalizedMessage());
            }                
        }        
    }

    public double getTaxa_aprendizagem() {
        return taxa_aprendizagem;
    }

    public void setTaxa_aprendizagem(double taxa_aprendizagem) {
        this.taxa_aprendizagem = taxa_aprendizagem;
    }
    
}
