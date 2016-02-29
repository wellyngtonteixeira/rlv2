package br.com.wellyngton.rlv2.util;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Vector;

import br.com.wellyngton.rlv2.model.SOMVetor;

public class DSP {
	
	String nomeArquivo;
	double[][] segmentos=null;
	double[][] fft2=null;
	double[][] mfc=null;
	double[] mediaMFC = new double[34];
	int tamanhoSegmento, quantSegmentos, tamanhoFFT,quantidadeFiltros;
	
	public DSP(String arq)
	{
		try{
		Segmentacao segm = new Segmentacao(arq);
		segmentos = segm.getMatriz();
		tamanhoSegmento = segm.retornaTamanhoSegmento();
		quantSegmentos = segm.retornaQuantSegmentos();
		Hamming janela = new Hamming(segmentos,tamanhoSegmento,quantSegmentos);
		FFT potencias = new FFT(janela.getSegJan(),tamanhoSegmento,quantSegmentos);
		fft2=potencias.getFFT2();
		int k = tamanhoSegmento/2 +1;
		tamanhoFFT=potencias.getTamanhoFFT();
		Filtros melCoeficient = new Filtros(fft2,segm.retornaTaxaAmostragem(),tamanhoFFT,quantSegmentos);
		quantidadeFiltros = melCoeficient.retornaQuantidadeFiltros();
		
		//Aloca memoria para os coeficientes que resultarao da aplicacao do filtro
		mfc = new double[quantSegmentos][];
		for(int cont=0;cont<quantSegmentos;cont++)
		{
		mfc[cont]=new double[quantidadeFiltros];
		}
		//Aplica os filtros a cada segmento
		for(int i=0;i<quantSegmentos;i++)
		{
		melCoeficient.aplicarFiltro(mfc[i],fft2[i], 0);
		}
		//Vai agrupar os filtros para obter um unico segmento representativo
		//do sinal de voz. Soma elementos de mesmo �ndice de todos os segmentos
		//com energia maior que a encontrada para segmentos equivalentes a
		//trechos de sil�ncio
		int i=0;
		while(i<34)
		{
		mediaMFC[i]=(double)0.0;
		i++;
		}
		int contSegAltaEnergia=0;
		for(i=0;i<quantSegmentos;i++)
		{
			if(potencias.infoEnergia[i]==1)
			{
			for(int j=0;j<34;j++)
			{
			mediaMFC[j]+=mfc[i][j];
			}
			contSegAltaEnergia++;
			}
		}
		//Vai dividir as somas encontras pelo numero de segmentos, somente para
		//diminuir a ordem de grandeza dos elementos do vetor. Equivale a tirar
		//a m�dia dos segmentos para encontrar um �nico.
		for(int j=0;j<34;j++)
		{
		mediaMFC[j]/=contSegAltaEnergia;
		}
		}catch(Exception e){
			System.out.println("Erro ao iniciar DSP: "+e.getMessage());
		}
		
	}

	public SOMVetor retornaMediaMFC()
	{
		SOMVetor result = new SOMVetor();
		for(int i=0;i<mediaMFC.length;i++)
		{
			BigDecimal valor = new BigDecimal(mediaMFC[i]).setScale(3, BigDecimal.ROUND_HALF_UP);
			mediaMFC[i]=valor.doubleValue();
			result.addElement(Double.valueOf((mediaMFC[i])));
		}
		return result;
	}
}
