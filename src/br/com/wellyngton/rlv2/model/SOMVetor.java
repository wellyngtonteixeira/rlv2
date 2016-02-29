/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.wellyngton.rlv2.model;

/**
 *
 * @author wellyngton
 */
public class SOMVetor extends java.util.Vector<Double>{

    public SOMVetor() {
    }
    
    public double distEuclidiana(SOMVetor v2){
        if(v2.size()!=size())
            return -999;
        double somatoria =0, temp;
        for (int x=0; x<size(); x++) {
			temp = ((Double)elementAt(x)).doubleValue() -
				   ((Double)v2.elementAt(x)).doubleValue();
			temp *= temp;
			somatoria += temp;
		}
		
		return somatoria;
    }
}
