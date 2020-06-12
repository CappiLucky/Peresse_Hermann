import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.lang.Math;



public class SeamCarving {

	//images declaration
	private static BufferedImage inputImage; // images than you want to resize
	private static BufferedImage outputImage;//resized image
	private int height; // actual height
	private int width; // actual width
	private int[][] rgbImage; // table of each pixels color (in rgb)
	private int[][] energyImage; //table of each pixels energy
	private int[][] My; //M matrice with highers numbers on the right
	private int[][] Mx; //M matrice with highers numbers on the top
	private int[][] pathX;//vertical path of the less important pixels
	private int[][] pathY;//horizontal path of the less important pixels


	public SeamCarving(final BufferedImage img, final String mode){
		this.height = img.getHeight();
		this.width = img.getWidth();
		this.rgbImage = new int[width][height];
		this.energyImage = new int[width][height];
		this.Mx = new int[width][height];
		this.My = new int[width][height];
		this.pathX = new int[height][2];
		this.pathY = new int[width][2];
		this.getrgbImage(img);
		this.setEnergy();

		if(mode.equals("x")){
			this.calculMx();
			this.getPathX();
		}
		else if(mode.equals("y")){
			this.calculMy();
			this.getPathY();
		} else if(mode.equals("0")){
			return;
		} else {
			System.out.println("error mode");
			System.exit(1);
		}

	}

	public void getrgbImage(final BufferedImage inputImage){
		for(int i = 0; i < width;i++){
			for(int j = 0; j < height;j++){
				rgbImage[i][j] = inputImage.getRGB(i,j);
			}
		}
	}

	public static void printGrille(final int[][] grille){
		for(int y=0; y < grille[0].length;y++){
			for(int x=0; x < grille.length;x++){
				System.out.print(grille[x][y]+" ");
			}
			System.out.println("");
		}
	}

	private  int getLuminance(final int rgb){
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

	public  void setEnergy(){
		for (int y = 0; y < this.height ; y++){
			for (int x = 0; x < this.width ; x++){
				energyImage[x][y] = gradient(x,y);
			}
		}
	}

	public void calculMx(){

		//cas initial
		for(int x=0; x < Mx.length; x++){Mx[x][0]= energyImage[x][0];}

		//cas général
		for (int y = 1 ; y < Mx[0].length ; y++){

			for(int x = 0 ; x < Mx.length; x++){

				if(x == Mx.length-1) Mx[x][y] = energyImage[x][y] + Math.min(Mx[x][y-1],Mx[x-1][y-1]);

				else if (x == 0) Mx[x][y]= energyImage[x][y]+ Math.min(Mx[x][y-1],Mx[x+1][y-1]);

				else Mx[x][y] = energyImage[x][y] + Math.min(Mx[x-1][y-1],Math.min(Mx[x][y-1],Mx[x+1][y-1]));
			}
		}
	}

	public void getPathX(){

		pathX[Mx[0].length-1][0] = minLigne();
		pathX[Mx[0].length-1][1] = Mx[0].length-1;

		for (int y = Mx[0].length-1 ; y > 0 ; y--){
			pathX[y-1][1] = y-1; //on met a jour le y
			int x_actual = pathX[y][0];

			if (x_actual == 0){//cas ou on est tout a gauche
				if(Mx[x_actual][y-1] <= Mx[x_actual+1][y-1]){
					pathX[y-1][0] = x_actual;
				} else {
					pathX[y-1][0] = x_actual+1;
				}
			}

			else if (x_actual == Mx.length -1){ //cas ou on est tout a droite
				if(Mx[x_actual][y-1] <= Mx[x_actual-1][y-1]){
					pathX[y-1][0] = x_actual;
				} else pathX[y-1][0] = x_actual-1;
			}


			else { //cas général
				if (Mx[x_actual][y-1] <= Mx[x_actual+1][y-1] && Mx[x_actual][y-1] <= Mx[x_actual-1][y-1]) {
					pathX[y-1][0] = x_actual;
				}
				else if (Mx[x_actual+1][y-1] <= Mx[x_actual][y-1] && Mx[x_actual+1][y-1] <= Mx[x_actual-1][y-1]){
					pathX[y-1][0] = x_actual+1;
				}
				else {
					pathX[y-1][0] = x_actual-1;
				}
			}
		}

	}

	public void calculMy(){
		//cas initial
		for(int y=0; y < My[0].length;y++){My[0][y] = energyImage[0][y];}

		//cas général
		for (int x = 1; x < My.length ; x++) {
			for (int y = 0 ; y < My[x].length ; y++) {
				if (y == My[0].length-1) My[x][y] = energyImage[x][y] + Math.min(My[x-1][y], My[x-1][y-1]);//tout en bas
				else if (y == 0) My[x][y] = energyImage[x][y] + Math.min(My[x-1][y], My[x-1][y+1]);//tout en haut
				else My[x][y] = energyImage[x][y] + Math.min(My[x-1][y-1], Math.min(My[x-1][y], My[x-1][y+1]));//cas general
			}
		}
	}

	public void getPathY(){
		pathY[My.length-1][0] = My.length-1; //x
		pathY[My.length-1][1] = minColonne();  //y

		for (int x= this.width-1; x > 0; x--){
			pathY[x-1][0] = x-1;
			int y_actual = pathY[x][1];

			if (y_actual == 0){//cas ou on est tout en haut
				if(My[x-1][y_actual] <= My[x-1][y_actual+1]){
					pathY[x-1][1] = y_actual;
				} else {
					pathY[x-1][1] = y_actual+1;
				}
			}

			else if (y_actual == this.height-1){//cas ou on est tout en bas
					if(My[x-1][y_actual] <= My[x-1][y_actual - 1]){
						pathY[x-1][1]=y_actual;
					} else {
						pathY[x-1][1]=y_actual-1;
					}
			}

			else {//cas général
				if(My[x-1][y_actual] <= My[x-1][y_actual+1] && My[x-1][y_actual] <= My[x-1][y_actual -1]){
					pathY[x-1][1] = y_actual;
				}
				else if(My[x-1][y_actual +1] <= My[x-1][y_actual] && My[x-1][y_actual+1] <= My[x-1][y_actual-1]){
					pathY[x-1][1]=y_actual+1;
				}
				else {
					pathY[x-1][1]=y_actual-1;
				}
			}
		}
	}

	public int minColonne(){
		int min = Integer.MAX_VALUE, indice = 0;
		for(int y=0; y < height; y++){
			if (My[My.length-1][y] < min){
				min = My[My.length-1][y];
				indice = y;
			}
		}
		return indice;
	}

	public int minLigne(){
		int min = Integer.MAX_VALUE, indice = -1;
		for(int x = 0; x < width; x++){
			if(Mx[x][Mx[0].length-1] < min){
				min = Mx[x][Mx[0].length-1];//max
				indice = x;//indice du max
			}
		}
		return indice;
	}

	public static BufferedImage removeX(final BufferedImage img, final int[][] pathx){
		BufferedImage new_img = new BufferedImage(img.getWidth()-1, img.getHeight(),img.getType());
		int xp=0;
		for (int y = 0; y < new_img.getHeight();y++){
			xp=0;
			for( int x = 0; x < new_img.getWidth();x++){
					if(xp == pathx[y][0]){
						xp++;
					}
					new_img.setRGB(x,y,img.getRGB(xp,y));

					xp++;
			}
		}

		return new_img;
	}

	public static BufferedImage removeY (final BufferedImage img, final int [][] pathy) {
		BufferedImage new_img = new BufferedImage(img.getWidth(), img.getHeight()-1, img.getType());
		int yp = 0;
		for (int x = 0; x < new_img.getWidth() -1;x++){
			yp=0;
			for( int y = 0; y < new_img.getHeight() -1;y++){
					if(yp	 == pathy[x][1]){
						yp++;
					}
					new_img.setRGB(x,y,img.getRGB(x,yp));
					yp++;
			}
		} // et si t'enleve celui du main XD ca va marcher c est sur. faut pas mettre getWidth -1 ? ET PAREIL POUR LA HAUTEUR c est ca !
		return new_img;
	}

	public static int getPercentage(final String percentage){
		String pa = percentage; //a modifier pour enlever tout ce qui n'est pas un chiffre
		int p = 100;
		try {
			p = Integer.parseInt(pa);
		} catch (NumberFormatException e){
			System.out.println("Erreur dans la saisie des pourentage");
			System.exit(1);
		}
		return p;
	}

	public static void openImg(final String path){
		try {
			File inputFile = new File(path);
			inputImage = ImageIO.read(inputFile);
		} catch (Exception e){
			System.out.println("Error while opennig image.");
			System.out.println(e.toString());
			System.exit(1);
		}
	}

	public static void main(String[] args){
		if (args.length != 3){
			System.out.println("Erreur : parametres incorrects. \n Format des parametres : chemin %reductionX %reductionY");
			System.exit(1);
		}

		openImg(args[0]);

		SeamCarving seam = new SeamCarving(inputImage,"0");
		int required_x = getPercentage(args[1])*seam.width/100;
		int required_y = getPercentage(args[2])*seam.height/100;

		outputImage = inputImage;



		for(int i=outputImage.getWidth(); i >= required_x; i--){
			seam = new SeamCarving(seam.outputImage,"x");
			outputImage = removeX(outputImage,seam.pathX);
		}


		for(int j=outputImage.getHeight();j >= required_y;j--){
			seam = new SeamCarving(seam.outputImage,"y");
			outputImage = removeY(outputImage,seam.pathY);
		}






		String extension = args[0].split("\\.")[1];

		try {
			File outputFile = new File(String.valueOf(required_x)+"x"+String.valueOf(required_y)+args[0]);
			ImageIO.write(outputImage,extension.toUpperCase(),outputFile); //modifier pour metre l'image finale
		} catch (Exception e){
			System.out.println(e.toString());
			System.exit(1);
		}



	}
}
