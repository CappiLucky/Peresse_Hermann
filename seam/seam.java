import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;
import javax.imageio.ImageIO;

public class seam {
	BufferedImage aImage;
	int width;
	int height;

	public seam(){
		File input = new File("test.jpg");
		try {
			 aImage = ImageIO.read(input);
		} catch (Exception e){}
		width = aImage.getWidth();
		height = aImage.getHeight();
		System.out.println(width + " " +height);
	}

	public static void main(String[] args){
		seam imag = new seam();
	}
	




}
