package br.com.wellyngton.rlv2.model;



import br.com.wellyngton.rlv2.model.SOMElemento;

/**
 *
 * @author  wellyngton
 */
public class SOMQuadro {
	
	private static final int LARGURA = 40;
        private static final int ALTURA = 40;
	private SOMElemento[][] matriz;
	
	/** Creates a new instance of SOMLattice,
	 *  which is a 2x2 array of SOMNodes. For now, it
	 *  assumes an input vector of three values, and
	 *  randomly initializes the array as such.
	 */
	public SOMQuadro() {
		matriz = new SOMElemento[LARGURA][ALTURA];
		for (int x=0; x<LARGURA; x++) {
			for (int y=0; y<ALTURA; y++) {
				matriz[x][y] = new SOMElemento(34);
				matriz[x][y].setX(x);
				matriz[x][y].setY(y);
			}
		}
	}
	
	// Returns the SOMNode at the given point (x,y)
	public SOMElemento getElemento(int x, int y) {
		return matriz[x][y];
	}
	
        public int getLargura(){
            return LARGURA;
        }
        
        public int getAltura(){
            return ALTURA;
        }
	/*public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}*/
	
	/** Finds the best matching unit for the given
	 *  inputVector
	 */
	public SOMElemento getBMU(SOMVetor vetorEntrada) {
		// Start out assuming that 0,0 is our best matching unit
		SOMElemento bmu = matriz[0][0];
		double bestDist = vetorEntrada.distEuclidiana(bmu.getVetor());
		double curDist;
		
		// Now step through the entire matrix and check the euclidean distance
		// between the input vector and the given node
		for (int x=0; x<LARGURA; x++) {
			for (int y=0; y<ALTURA; y++) {
				curDist = vetorEntrada.distEuclidiana(matriz[x][y].getVetor());
				if (curDist < bestDist) {
					// If the distance from the current node to the input vector
					// is less than the distance to our current BMU, we have a 
					// new BMU
					bmu = matriz[x][y];
					bestDist = curDist;
				}
			}
		}
		
		return bmu;
	}
	
}
