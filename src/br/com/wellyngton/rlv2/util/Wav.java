package br.com.wellyngton.rlv2.util;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Wav {
	
	protected int numCanais = 1;
	protected long taxaAmostragem = 8000;
	protected long numAmostras;
	protected float Duracao;
	protected float[] buffer;
	
	public Wav(String arq) throws IOException
	{
		leitura(arq);		
	}
	
	public long retornaTaxaAmostragem()
	{
		return (taxaAmostragem);
	}
	
	public long retornaNumeroAmostras()
	{
		return (numAmostras);
	}
	public void leitura(String arquivo) throws IOException
	{
		BufferedInputStream retorno = null;
		try {
			retorno = new BufferedInputStream(new FileInputStream(arquivo));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("arquivo nao abriu\n");
		}
		
		byte[] wav = new byte[retorno.available()];
		try {
			retorno.read(wav, 0, wav.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERRO NA LEITURA");
		}
		//String buffer = new String(arquivo);
		int pos = 44;
		int n_am = (wav.length-44)/2; //dividindo o bloco de dados pelo numero de bytes que representam uma amostra
		//System.out.println(n_am);
		byte[] vef = new byte[2];
		buffer = new float[n_am];
		for(int i=0;i<buffer.length;i++)
		{
			for(int z=0;z<2;z++){
				vef[z]=wav[pos];
				pos++;
			}
			buffer[i]=ByteParaFloat(vef);
		}
		setNumAmostras();
		setDuracao();
		preEnfase();
	}
	
	public static float ByteParaFloat(byte[] in)
	{
		//int tam = in.length;
		int i,j,p,x;
		int r=0;
		for(i=0;i<2;i++){
            x=in[i];
            p=i*8;
            x<<=(p);
            for(j=0;j<8;j++){
                p=i*8 + j;
                r=r| (( 1<<p ) & x);
            }
		}
		float mostra = (float)r;
		return mostra;
	}
	
	private void setDuracao()
	{
		if(numAmostras!=0)
			Duracao = numAmostras*(float)0.12;
		else
			Duracao = (float)0.0;
	}
	
	private void setNumAmostras() {
		// TODO Auto-generated method stub
		if(buffer==null)
			numAmostras = 0;
		else
			numAmostras = buffer.length;
	}
	
	public void preEnfase()
	{
		float[] aux = new float[buffer.length];
		int x;
		aux = buffer;
		for(x=1;x<aux.length;x++)
		{
			aux[x]=aux[x]-((float)0.95*aux[x-1]);
		}
		buffer = aux;
	}

}
