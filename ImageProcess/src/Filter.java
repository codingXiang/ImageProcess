import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferByte;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.lang.Math.*;
import java.util.Arrays;

public class Filter {
	// 轉成灰階值
	public static BufferedImage toGray(BufferedImage image){
		int width = image.getWidth();
		int height = image.getHeight();
		for(int y = 0; y < height; y++){
		  for(int x = 0; x < width; x++){
			  int p = image.getRGB(x,y);
			  int a = (p>>24)&0xff;
			  int r = (p>>16)&0xff;
			  int g = (p>>8)&0xff;
			  int b = p&0xff;
			  int avg = (r+g+b)/3;
			  p = (a<<24) | (avg<<16) | (avg<<8) | avg;
			  image.setRGB(x, y, p);
		  }
		}
		return image;
	}
	// 轉成負片效果
	public static BufferedImage toNegative(BufferedImage image){
		int width = image.getWidth();
		int height = image.getHeight();
		for(int y = 0; y < height; y++){
		  for(int x = 0; x < width; x++){
			  int p = image.getRGB(x,y);
			  int alpha = new Color(image.getRGB(x,y)).getAlpha();
			  int red = new Color(image.getRGB(x,y)).getRed();
			  int green = new Color(image.getRGB(x,y)).getGreen();
			  int blue = new Color(image.getRGB(x,y)).getBlue();
			  alpha = 255 - alpha;
			  red = 255 - red;
			  green = 255 - green;
			  blue = 255 - blue;
	          p = (alpha<<24) | (red<<16) | (green<<8) | blue;
			  image.setRGB(x, y, p);
		  }
		}
		return image;
	}
	// 利用 gamma 轉換
	public static BufferedImage setGamma(BufferedImage image, double gamma){
		image = toGray(image);
		int width = image.getWidth();
		int height = image.getHeight();
	    BufferedImage gamma_cor = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for(int y = 0; y < height; y++){
		  for(int x = 0; x < width; x++){
			  
			  int p = image.getRGB(x,y);
			  int alpha = new Color(image.getRGB(x,y)).getAlpha();
			  int red = new Color(image.getRGB(x,y)).getRed();
			  int green = new Color(image.getRGB(x,y)).getGreen();
			  int blue = new Color(image.getRGB(x,y)).getBlue();
			  
			  red = (int) (255 * (Math.pow((double) red / (double) 255, gamma)));
	          green = (int) (255 * (Math.pow((double) green / (double) 255, gamma)));
	          blue = (int) (255 * (Math.pow((double) blue / (double) 255, gamma)));
	          p = (alpha<<24) | (red<<16) | (green<<8) | blue;
	          gamma_cor.setRGB(x, y, p);
		  }
		}
		return gamma_cor;
	}
	// 胡椒鹽雜訊
	public static BufferedImage saltpepper(BufferedImage image)
    {
        int height = image.getHeight();
        int width = image.getWidth();
        int nSalt = 5;    // Percentage of salt
        int nPepper = 5; 
        int salt = height * width * nSalt / 100;    // Amount of salt
        int pepper = height * width * nSalt / 100;  // Amount of pepper
        
        for( int i = 0; i < salt; i++ )
        {
            int x = (int) (Math.random() * width); 
            int y = (int) (Math.random() * height);
            int p = image.getRGB(x,y);
            int alpha = new Color(image.getRGB(x,y)).getAlpha();
			int red = 255;
			int green = 255;
			int blue = 255;
			  
            p = (alpha<<24) | (red<<16) | (green<<8) | blue;
            image.setRGB( x, y, p );
        }
        
        for( int i = 0; i < pepper; i++ )
        {
        	int x = (int) (Math.random() * width); 
            int y = (int) (Math.random() * height);
            int p = image.getRGB(x,y);
            int alpha = new Color(image.getRGB(x,y)).getAlpha();
			int red = 0;
			int green = 0;
			int blue = 0;
			  
            p = (alpha<<24) | (red<<16) | (green<<8) | blue;
            image.setRGB( x, y, p );
        }
        return image;
    }
	// 3 X 3中值濾波器
	public static BufferedImage median_filter(BufferedImage image) {
		int maskSize = 3;
		int width = image.getWidth();
		int height = image.getHeight();
		int outputPixels[] = new int[width * height];
		int red[], green[], blue[];
        int xMin, xMax, yMin, yMax;
        int argb, reD, greenN, bluE;
        /** Median Filter operation */
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = new Color(image.getRGB(x,y)).getAlpha();
                red = new int[maskSize * maskSize];
                green = new int[maskSize * maskSize];
                blue = new int[maskSize * maskSize];
                int count = 0;
                xMin = x - (maskSize / 2);
                xMax = x + (maskSize / 2);
                yMin = y - (maskSize / 2);
                yMax = y + (maskSize / 2);
                for (int r = yMin; r <= yMax; r++) {
                    for (int c = xMin; c <= xMax; c++) {
                        if (r < 0 || r >= height || c < 0 || c >= width) {
                            /** Some portion of the mask is outside the image. */
                            continue;
                        } else {
                            argb = image.getRGB(c, r);
                            reD = (argb >> 16) & 0xff;
                            red[count] = reD;
                            greenN = (argb >> 8) & 0xff;
                            green[count] = greenN;
                            bluE = (argb) & 0xFF;
                            blue[count] = bluE;
                            count++;
                        }
                    }
                }
 
                /** sort red, green, blue array */
                Arrays.sort(red);
                Arrays.sort(green);
                Arrays.sort(blue);
 
                /** save median value in outputPixels array */
                int index = (count % 2 == 0) ? count / 2 - 1 : count / 2;
                int p = (a << 24) | (red[index] << 16) | (green[index] << 8) | blue[index];
                outputPixels[x + y * width] = p;
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, outputPixels[x + y * width]);
            }
        }
        return image;
	}
	// 3 X 3 平均濾波器
	public static BufferedImage mean_filter(BufferedImage image){
		int height = image.getHeight();
        int width = image.getWidth();
        for (int x = 1; x < height - 1; x++) {
            for (int y = 1; y < width - 1; y++) {
            	 Color p1 = new Color(image.getRGB(x - 1 , y - 1));
            	 Color p2 = new Color(image.getRGB(x - 1 , y));
            	 Color p3 = new Color(image.getRGB(x - 1, y + 1));
            	 Color p4 = new Color(image.getRGB(x, y - 1));
            	 Color p5 = new Color(image.getRGB(x, y));
            	 Color p6 = new Color(image.getRGB(x, y + 1));
            	 Color p7 = new Color(image.getRGB(x + 1, y - 1));
            	 Color p8 = new Color(image.getRGB(x + 1, y));
            	 Color p9 = new Color(image.getRGB(x + 1, y + 1));
            	 int alpha_avg = (int)(p1.getAlpha() + p2.getAlpha() + p3.getAlpha() + p4.getAlpha() + p5.getAlpha() + p6.getAlpha() + p7.getAlpha() + p8.getAlpha() + p9.getAlpha()) / 9;
            	 int red_avg = (int)(p1.getRed() + p2.getRed() + p3.getRed() + p4.getRed() + p5.getRed() + p6.getRed() + p7.getRed() + p8.getRed() + p9.getRed()) / 9;
            	 int green_avg = (int)(p1.getGreen() + p2.getGreen() + p3.getGreen() + p4.getGreen() + p5.getGreen() + p6.getGreen() + p7.getGreen() + p8.getGreen() + p9.getGreen()) / 9;
            	 int blue_avg = (int)(p1.getBlue() + p2.getBlue() + p3.getBlue() + p4.getBlue() + p5.getBlue() + p6.getBlue() + p7.getBlue() + p8.getBlue() + p9.getBlue()) / 9;

            	 int p = (alpha_avg<<24) | (red_avg<<16) | (green_avg<<8) | blue_avg;

            	 image.setRGB(x, y, p);                 
            }
        }
		return image;
	}
	public static BufferedImage sobel_filter(BufferedImage bi){
		int i,j;
        int gx[]={1,0,-1,2,0,-2,1,0,-1};
        int gy[]={1,2,1,0,0,0,-1,-2,-1};
		double Gx[][], Gy[][], G[][];
        int width = bi.getWidth();
        int height = bi.getHeight();
        int[] pixels = new int[width * height];
        int[][] output = new int[width][height];       
        int counter = 0;
        for(i = 0 ; i < width ; i++ )
        {
            for(j = 0 ; j < height ; j++ )
            {
                output[i][j] = bi.getRGB(i, j);
                counter = counter + 1;
            }
        }   
        Gx = new double[width][height];
        Gy = new double[width][height];
        G  = new double[width][height];
        for (i = 1 ; i < width - 1; i++) {
            for (j = 1 ; j < height - 1; j++) {
            	
            	if (i==0 || i==width-2 || j==0 || j==height-1)
            		Gx[i][j] = Gy[i][j] = G[i][j] = 0;
                else{
                    Gx[i][j] = output[i+1][j-1] + 2*output[i+1][j] + output[i+1][j+1] -
                            output[i-1][j-1] - 2*output[i-1][j] - output[i-1][j+1];
                    Gy[i][j] = output[i-1][j+1] + 2*output[i][j+1] + output[i+1][j+1] -
                            output[i-1][j-1] - 2*output[i][j-1] - output[i+1][j-1];
                    G[i][j]  = Math.abs(Gx[i][j]) + Math.abs(Gy[i][j]);
                }
            }
        }
        counter = 0;
        for(int ii = 0 ; ii < width ; ii++ )
        {
            for(int jj = 0 ; jj < height ; jj++ )
            {
                bi.setRGB(ii, jj, (int) G[ii][jj]);
                counter = counter + 1;
            }
        }
        BufferedImage outImg = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
        outImg.getRaster().setPixels(0,0,width,height,pixels);
		
		return bi;
	}
	public static BufferedImage scale_2_binary(BufferedImage image){
	    int height = image.getHeight();
        int width = image.getWidth();
        float threshold = 0;
		for(int y = 0; y < height; y++){
		  for(int x = 0; x < width; x++){
			  int p = image.getRGB(x,y);
			  int a = new Color(p).getAlpha();
			  int r = new Color(p).getRed();
			  int g = new Color(p).getGreen();
			  int b = new Color(p).getBlue();
			 // int rgb = 0xFFFF * r + 0xFF * g + b;
			  float luminance = (r * 0.2126f + g * 0.7152f + b * 0.0722f) / 255;
			  threshold += luminance;
              
		  }
		}
		threshold = threshold / (height * width);
		System.out.println(threshold);
		for(int y = 0; y < height; y++){
		  for(int x = 0; x < width; x++){
			  int p = image.getRGB(x,y);
			  int a = new Color(p).getAlpha();
			  int r = new Color(p).getRed();
			  int g = new Color(p).getGreen();
			  int b = new Color(p).getBlue();
			  float luminance = (r * 0.2126f + g * 0.7152f + b * 0.0722f) / 255;
			  if (luminance > threshold){
		      	  image.setRGB(x, y, Color.white.getRGB());

		        }else{
		      	  image.setRGB(x, y, Color.black.getRGB());
		        }
		  }
		}
		
		return image;
	}
}
