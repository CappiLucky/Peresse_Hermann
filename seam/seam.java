import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;
import javax.imageio.ImageIO;

public class seam {
	static BufferedImage input;
	static BufferedImage output;
	int width;
	int height;

	public seam( final String path){
		try {
			File imageIN = new File(path);
			input = ImageIO.read(imageIN);
		} catch (Exception e){}
		width = input.getWidth();
		height = input.getHeight();
		System.out.println(width + " " +height);


	}


	public static void main(String[] args){

		String reduc = args[1];

		if( reduc.contains("%")) {
			reduc = reduc.replace("%","");
		}

		int pourcentage = Integer.parseInt(reduc);	

		seam imag = new seam(args[0]);
		writeImg(args[0],pourcentage);
		System.out.println(input.getRGB(1,1));
	}

	public static void writeImg(final String path, final int reduc){
		try{
			File outputfile = new File(path.replace(".jpg","")+reduc+"%.jpg");
			ImageIO.write(input,"jpg",outputfile);
		} catch (Exception e){}
	}

	

	public static int[][] calculEnergie(final int[][] RGBimage){
		int width = RGBimage[0].length, height = RGBimage.length;
		int[][] energie = new int[height][width];
		

		return energie;	

	}

	public static int getEnergie(final int x, final int y, final BufferedImage input){

		int energie = 0;


		
		return energie;

	}

	public static int[][] getRGBimage (final BufferedImage input){
		int width = input.getWidth(), height = input.getHeight();
		int[][] RGBimage = new int[height][width];

		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				RGBimage[row][col] = input.getRGB(col,row);
			}
		}
		return RGBimage;
	}


}
