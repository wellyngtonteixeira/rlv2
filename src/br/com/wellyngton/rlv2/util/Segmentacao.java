package br.com.wellyngton.rlv2.util;
import java.io.IOException;


public class Segmentacao {
	
	private int n; //Numero de Amostras por segmento
	private int sob; //Quantidade de pontos sobrepostos entre dois segmentos
	private int quantidadeSegmentos; //Quantidade de segmentos existentes
	private double[][] segm; //matriz de segmentos
	private Wav wav;
	
	
	public Segmentacao(String arq)
	{
		try{
		wav = new Wav(arq);
		int n_aux = (int)wav.retornaNumeroAmostras();
		//numero de amostras
		n = (int)Math.pow(2.0,(int)(Math.log(n_aux/1.5)/Math.log(2.0))+1);
		//calculo do numero de pontos sobrepostos
		sob = n/2;
		//definindo quantidade de segmentos
		if((wav.numAmostras%sob)!=0)
		{
			quantidadeSegmentos=(int)wav.numAmostras/sob;
		}
		else
		{
			quantidadeSegmentos=((int)wav.numAmostras/sob)-1;
		}
		
		criaMatrizSegmentos();
		}catch(Exception e){
			System.out.println("ERRO inicia Segmentacao: "+e.getMessage());
		}
	}

	public int retornaQuantSegmentos()
	{
		return this.quantidadeSegmentos;
	}
	
	public int retornaTamanhoSegmento()
	{
		return this.n;
	}
	
	public long retornaTaxaAmostragem()
	{
		return wav.retornaTaxaAmostragem();
	}
	
	private void criaMatrizSegmentos() {
		// TODO Auto-generated method stub
		int indice;
		int aux=0;
		int ind=0;
		double fim;
		
		segm = new double[retornaQuantSegmentos()][retornaTamanhoSegmento()];
		for(indice=0;indice<retornaQuantSegmentos();indice++)
		{
		for(ind=0;ind<retornaTamanhoSegmento();ind++)
		{
			segm[indice][ind]=wav.buffer[aux];
		aux++;
		if(aux==wav.numAmostras)
		break;
		}
		if(aux==wav.numAmostras)
		break;
		aux=aux-sob;
		}
		
		fim = (double)0.0;
		ind++;
		while(ind<n)
		{
			segm[indice][ind]=fim;
			ind++;
		}
	}
	
	public double[][] getMatriz()
	{
		return segm;
	}

}
