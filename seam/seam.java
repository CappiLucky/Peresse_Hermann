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
	}

	public static void writeImg(final String path, final int reduc){
		try{
			File outputfile = new File(path.replace(".jpg","")+reduc+"%.jpg");
			ImageIO.write(input,"jpg",outputfile);
		} catch (Exception e){}
	}

	




}
