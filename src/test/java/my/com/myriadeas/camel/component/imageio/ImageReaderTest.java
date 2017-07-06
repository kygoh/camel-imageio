package my.com.myriadeas.camel.component.imageio;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.junit.Test;

public class ImageReaderTest {

	@Test
	public void testSinglePageTiff() {
		/* 
		 * Reference: http://stackoverflow.com/questions/17770071/splitting-a-multipage-tiff-image-into-individual-images-java
		 */
		try {
			String pathToImage = "src/data/tiff/CCITT_1.TIF";
			ImageInputStream is = ImageIO.createImageInputStream(new File(pathToImage));
			if (is == null || is.length() == 0){
				fail();
			}
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(is);
			if (iterator == null || !iterator.hasNext()) {
				throw new IOException("Image file format not supported by ImageIO: " + pathToImage);
			}
			// We are just looking for the first reader compatible:
			ImageReader reader = (ImageReader) iterator.next();
			iterator = null;
			reader.setInput(is);
			int nbPages = reader.getNumImages(true);
			assertEquals(1, nbPages);

			for(int page=0; page < nbPages; page++) {
				BufferedImage image = reader.read(page);
				System.out.println(image.getWidth() + " x " + image.getHeight());
			}
			reader.dispose();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testMultipageTiff() {
		/* 
		 * Reference: http://stackoverflow.com/questions/17770071/splitting-a-multipage-tiff-image-into-individual-images-java
		 */
		try {
			String pathToImage = "src/data/tiff/large_multipage_tiff_example_v7.tif";
			ImageInputStream is = ImageIO.createImageInputStream(new File(pathToImage));
			if (is == null || is.length() == 0){
				fail();
			}
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(is);
			if (iterator == null || !iterator.hasNext()) {
				throw new IOException("Image file format not supported by ImageIO: " + pathToImage);
			}
			// We are just looking for the first reader compatible:
			ImageReader reader = (ImageReader) iterator.next();
			iterator = null;
			reader.setInput(is);
			int nbPages = reader.getNumImages(true);
			assertEquals(10, nbPages);

			List<BufferedImage> images = new ArrayList<BufferedImage>();
			for(int page=0; page < nbPages; page++) {
				BufferedImage image = reader.read(page);
				images.add(image);
			}
			reader.dispose();
			is.close();

			assertEquals(10, images.size());
			for(BufferedImage image : images) {
				System.out.println(image.getWidth() + " x " + image.getHeight());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
