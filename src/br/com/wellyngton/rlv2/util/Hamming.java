package br.com.wellyngton.rlv2.util;



public class Hamming {
	
	private double[] janela;
	private double[][] matriz_segmento;
	private int tam_janela;
	private int num_segmentos;
	public Hamming(double[][] mtr, int n, int quant)
	{
		try{
		matriz_segmento=mtr;
		tam_janela=n;
		num_segmentos=quant;
		janela=new double[tam_janela];
		for(int x=0; x<n ; x++)
			janela[x]=(double)(0.54-0.46*Math.cos((2.0*Math.PI*x)/(n-1)));
			aplicaJanela();
			}catch(Exception e){
				System.out.println("ERRO HAMMING "+e.getMessage());
			}
	}
	
	public void aplicaJanela()
	{
		for(int i=0;i<num_segmentos;i++)
		{
			for(int j=0;j<tam_janela;j++)
				matriz_segmento[i][j] *= janela[j];
		}
	}
	
	public double[][] getSegJan()
	{
		return matriz_segmento;
	}
	
}
