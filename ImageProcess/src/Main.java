import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.lang.Math.*;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		// original image file path
//		String image_path = "\\Users\\user\\Desktop\\image\\origin_image.png";
		String image_path = "/Users/user/Desktop/image/origin_image.png";
		ImageHandler image_handler = new ImageHandler(image_path);
		
		// convert original image to gray
		BufferedImage origin_image = image_handler.loadImage();
		image_handler.outputImage(Filter.toGray(origin_image), "gray");
		BufferedImage gray = Filter.toGray(origin_image);
		image_handler.outputImage(Filter.toNegative(gray), "negative");
		
		// set gamma equal 1
		BufferedImage origin_image1 = image_handler.loadImage();
		BufferedImage gamma_equal_1 = Filter.toGray(origin_image1);
		image_handler.outputImage(Filter.setGamma(gamma_equal_1, 1), "gamma=1");
		
		// set gamma smaller than 1
		BufferedImage origin_image2 = image_handler.loadImage();
		BufferedImage gamma_smaller_than_1 = Filter.toGray(origin_image2);
		image_handler.outputImage(Filter.setGamma(gamma_smaller_than_1, 0.5), "gamma_smaller_than_1");
		
		// set gamma bigger than 1
		BufferedImage origin_image3 = image_handler.loadImage();
		BufferedImage gamma_bigger_than_1 = Filter.toGray(origin_image3);
		image_handler.outputImage(Filter.setGamma(gamma_bigger_than_1, 2), "gamma_bigger_than_1");

		// put salt and pepper noise on gray image
		image_handler.outputImage(Filter.saltpepper(gamma_smaller_than_1), "SaltAndPepper");
		
		// use 3 X 3 median filter
		image_handler.outputImage(Filter.median_filter(gamma_smaller_than_1), "Median_Filter");
		
		// use 3 X 3 mean filter
		BufferedImage origin_image4 = image_handler.loadImage();
		BufferedImage gamma_smaller_than_1_clone = Filter.toGray(origin_image4);
		Filter.saltpepper(gamma_smaller_than_1_clone);
		image_handler.outputImage(Filter.mean_filter(gamma_smaller_than_1_clone), "Mean_Filter");

		// usb Sobel Filter
		image_handler.outputImage(Filter.sobel_filter(gamma_equal_1), "Sobel_Filter");
		
		// usb threshold to Binarization
		BufferedImage origin_image5 = image_handler.loadImage();
		BufferedImage gamma_equal_1_clone = Filter.toGray(origin_image5);
		Filter.setGamma(gamma_equal_1_clone, 1);
		image_handler.outputImage(Filter.scale_2_binary(gamma_equal_1_clone), "binary");;
	}
}