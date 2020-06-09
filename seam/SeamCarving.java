import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.lang.Math;

public class SeamCarving {

	//images declaration
	private static BufferedImage inputImage; // images than you want to resize
	private BufferedImage outputImage;//resized image
	private int height; // actual height
	private int width; // actual width
	private int[][] rgbImage; // table of each pixels color (in rgb)
	private int[][] energyImage; //table of each pixels energy
	private int[][] My;
	private int[][] Mx;


	public SeamCarving(final BufferedImage img){
		this.height = img.getHeight();
		this.width = img.getWidth();
		this.rgbImage = new int[width][height];
		this.energyImage = new int[width][height];
		this.Mx = new int[width][height];
		getrgbImage(img);
	}

	public void getrgbImage(final BufferedImage inputImage){
		for(int i = 0; i < width;i++){
			for(int j = 0; j < height;j++){
				rgbImage[i][j] = inputImage.getRGB(i,j);
			}
		}
	}

	public  void printGrille(final int[][] grille){
		for(int i=0; i < grille.length;i++){
			for(int j=0; j < grille[i].length;j++){
				System.out.print(grille[i][j]+" ");
			}
			System.out.println("");
		}
	}

	private  int getLuminance(final int rgb) {
		int r = (rgb >>16 ) & 0xFF;
		int g = (rgb >> 8 ) & 0xFF;
		int b = rgb & 0xFF;
		int gray = (299*r + 587*g + 114*b)/1000;
		return gray;
	}

	private int gradient(int x, int y){
		// Coordinates of 8 neighbours pixels
		int px = x - 1;  // previous x
		int nx = x + 1;  // next x
		int py = y - 1;  // previous y
		int ny = y + 1;  // next y

		// limit to image dimension (spheric)
		if (px < 0)	px=this.width-1;
		if (nx >= this.width) nx=0;
		if (py < 0)	py=this.height-1;
		if (ny >= this.height) ny=0;

		// Intensity of the 8 neighbours
		int Ipp = getLuminance( this.rgbImage[px][py] );
		int Icp = getLuminance( this.rgbImage[ x][py] );
		int Inp = getLuminance( this.rgbImage[nx][py] );
		int Ipc = getLuminance( this.rgbImage[px][ y] );
		int Inc = getLuminance( this.rgbImage[nx][ y] );
		int Ipn = getLuminance( this.rgbImage[px][ny] );
		int Icn = getLuminance( this.rgbImage[ x][ny] );
		int Inn = getLuminance( this.rgbImage[nx][ny] );

		// Local gradient (sobel)
		int gradx = (Inc-Ipc)*2 + (Inp-Ipp) + (Inn-Ipn);
		int grady = (Icn-Icp)*2 + (Ipn-Ipp) + (Inn-Inp);
		int norme = (int)Math.sqrt(gradx*gradx+grady*grady)/4;

		return norme;
	}

	public  void setEnergy(final int xmin, final int xmax, final int ymin, final int ymax){
		for (int y = ymin; y < ymax ; y++){
			for (int x = xmin; x < xmax ; x++){
				energyImage[x][y] = gradient(x,y);
			}
		}
	}


	public void calculMx(){

		//cas initial
		for(int c=0; c < 5; c++){Mx[0][c]= energyImage[0][c];}

		//cas général
		for (int l = 1 ; l < Mx.length ; l++){

			for(int c = 0 ; c < Mx[0].length; c++){

				if(c == Mx[0].length-1) Mx[l][c] = energyImage[l][c] + Math.min(Mx[l-1][c],Mx[l-1][c-1]);

				else if (c == 0) Mx[l][c]= energyImage[l][c]+ Math.min(Mx[l-1][c],Mx[l-1][c+1]);

				else Mx[l][c] = energyImage[l][c] + Math.min(Mx[l-1][c-1],Math.min(Mx[l-1][c],Mx[l-1][c+1]));
			}
		}
	}

	public static void main(String[] args){
		try {
			File inputFile = new File(args[0]);
			inputImage = ImageIO.read(inputFile);
		} catch (Exception e){
			System.out.println("Error while opennig image.");
			System.exit(1);
		}
		SeamCarving seam = new SeamCarving(inputImage);
		seam.setEnergy(0,seam.width,0,seam.height);
		seam.calculMx();
		seam.printGrille(seam.Mx);
	}

}
