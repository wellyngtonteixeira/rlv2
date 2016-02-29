/*
 * SOMTrainer.java
 *
 * Created on December 13, 2002, 2:37 PM
 */

package br.com.wellyngton.rlv2.model;

import java.util.Vector;

import android.provider.Settings.System;

/**
 *
 * @author  alanter
 */
public class SOMTreinamento implements Runnable {
	// These constants can be changed to play with the learning algorithm
	private static final double TAXA_APRENDIZAGEM_INICIAL = 0.07;
	private static final int	NUM_ITERACOES = 100;
	
	// These two depend on the size of the lattice, so are set later
	private double RAIO_QUADRO;
	private double CONSTANTE_TEMPO;
	//private LatticeRenderer renderer;
	private SOMQuadro quadro;
	private Vector entradas;
	private SOMVetor bmuEscolhido;
	private double taxa_aprendizagem;
	private static boolean executando;
	private Thread executor;
	
	/** Creates a new instance of SOMTrainer */
	public SOMTreinamento() {
		executando = false;
	}
	
	private double getNeighborhoodRadius(double iteration) {
		return RAIO_QUADRO * Math.exp(-iteration/CONSTANTE_TEMPO);
	}
	
	private double getDistanceFalloff(double distSq, double radius) {
		double radiusSq = radius * radius;
		return Math.exp(-(distSq)/(2 * radiusSq));
	}
		
	// Train the given lattice based on a vector of input vectors
	public void setTraining(SOMQuadro latToTrain, Vector in)
							//LatticeRenderer latticeRenderer)
	{
		quadro = latToTrain;
		entradas = in;
		//renderer = quadroRenderer;
	}
	
	public void start() {
		if (quadro != null) {
			executor = new Thread(this);
			executor.setPriority(Thread.MIN_PRIORITY);
			executando = true;
			//executor.start();
			java.lang.System.out.println("Iniciou TREINO");
                        run();
		}
	}
	
	public void run() {
		java.lang.System.out.println("Iniciou RUN");
		int lw = quadro.getLargura();
		int lh = quadro.getAltura();
		int xstart, ystart, xend, yend;
		double dist, dFalloff;
		// These two values are used in the training algorithm
		RAIO_QUADRO = Math.max(lw, lh)/2;
		CONSTANTE_TEMPO = NUM_ITERACOES / Math.log(RAIO_QUADRO);
		
		int iteration = 0;
		double nbhRadius;
		SOMElemento bmu = null, temp = null;
		SOMVetor curInput = null;
		double learningRate = TAXA_APRENDIZAGEM_INICIAL;
		
		while (iteration < NUM_ITERACOES && executando) {
			nbhRadius = getNeighborhoodRadius(iteration);
			// For each of the input vectors, look for the best matching
			// unit, then adjust the weights for the BMU's neighborhood
			for (int i=0; i<entradas.size(); i++) {
				curInput = (SOMVetor)entradas.elementAt(i);
				bmu = quadro.getBMU(curInput);
				// We have the BMU for this input now, so adjust everything in
				// it's neighborhood
				
				// Optimization:  Only go through the X/Y values that fall within
				// the radius
				xstart = (int)(bmu.getX() - nbhRadius - 1);
				ystart = (int)(bmu.getY() - nbhRadius - 1);
				xend = (int)(xstart + (nbhRadius * 2) + 1);
				yend = (int)(ystart + (nbhRadius * 2) + 1);
				if (xend > lw) xend = lw;
				if (xstart < 0) xstart = 0;
				if (yend > lh) yend = lh;
				if (ystart < 0) ystart = 0;
				
				for (int x=xstart; x<xend; x++) {
					for (int y=ystart; y<yend; y++) {
						temp = quadro.getElemento(x,y);
//						if (temp != bmu) {
							dist = bmu.distanciaPara(temp);
							if (dist <= (nbhRadius * nbhRadius)) {
								dFalloff = getDistanceFalloff(dist, nbhRadius);
								temp.ajustandoPesos(curInput, learningRate, dFalloff);
							}
//						}
					}
				}
			}
			iteration++;
			learningRate = TAXA_APRENDIZAGEM_INICIAL *
							Math.exp(-(double)iteration/NUM_ITERACOES);
		}
		bmuEscolhido = bmu.getVetor();
		taxa_aprendizagem  = learningRate;
		//renderer.render(quadro, iteration);
		executando = false;
		java.lang.System.out.println("terminou RUN");
	}

	public void stop() {
		if (executor != null) {
			executando = false;
			while (executor.isAlive()) {};
		}
	}
	
	public SOMVetor getBMUEscolhido(){
		java.lang.System.out.println("BMU SIZE: "+bmuEscolhido.size());
		return this.bmuEscolhido;
	}
	
	public double getTaxaAprendizagem(){
		return this.taxa_aprendizagem;
	}
	
}
