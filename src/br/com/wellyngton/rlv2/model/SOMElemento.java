package br.com.wellyngton.rlv2.model;


import br.com.wellyngton.rlv2.model.SOMVetor;



/**
 *
 * @author  wellyngton
 */
public class SOMElemento {
	private SOMVetor pesos;
	private int xp, yp;

	public SOMElemento(int numPesos) {
		pesos = new SOMVetor();
		for (int x=0; x<numPesos; x++) {
			pesos.addElement(new Double(Math.random()));
		}
	}
	
	public void setX(int xpos) {
		xp = xpos;
	}
	
	public void setY(int ypos) {
		yp = ypos;
	}
	
	public int getX() {
		return xp;
	}
	
	public int getY() {
		return yp;
	}
	
	/** Computes the distance to another node.  Used for
	 *  neighborhood determination.  Returns the SQUARE of the distance
	 */
	public double distanciaPara(SOMElemento n2) {
		int xleg, yleg;
		xleg = getX() - n2.getX();
		xleg *= xleg;
		yleg = getY() - n2.getY();
		yleg *= yleg;
		return xleg + yleg;
	}
	
	public void setPeso(int w, double value) {
		if (w >= pesos.size())
			return;
		pesos.setElementAt(new Double(value), w);
	}
	
	public double getPeso(int w) {
		if (w >= pesos.size())
			return 0;
		
		return ((Double)pesos.elementAt(w)).doubleValue();
	}
	
	public SOMVetor getVetor() {
		return pesos;
	}
        
        public void imprimePesos(){
            if(!pesos.isEmpty() || pesos != null){
                System.out.println("VETOR PESOS: \n");
                for(int x=0; x<pesos.size(); x++){
                    System.out.println("p["+x+"]: "+getPeso(x)+"\n");
                }
            }
        }
	
	public void ajustandoPesos(SOMVetor entrada, double taxaAprendizagem,
							  double distanceFalloff)
	{
		double wt, vw;
		for (int w=0; w<pesos.size(); w++) {
			wt = ((Double)pesos.elementAt(w)).doubleValue();
			vw = ((Double)entrada.elementAt(w)).doubleValue();
			wt += distanceFalloff * taxaAprendizagem * (vw - wt);
			pesos.setElementAt(new Double(wt), w);
		}
	}
}