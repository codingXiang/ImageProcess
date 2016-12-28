import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHandler {
	private String path;
	public ImageHandler(String path){
		this.path = path;
	}
	public BufferedImage loadImage(){
		BufferedImage image = null;
		try {

			image = ImageIO.read(new File(path));

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return image;
	}
	public void outputImage(BufferedImage image , String filename){
		

		try {
			ImageIO.write(image, "jpg", new File(
					"/Users/user/Desktop/image/" + filename + ".png"));

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
