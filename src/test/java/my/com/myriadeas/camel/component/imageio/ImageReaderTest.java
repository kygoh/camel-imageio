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
    public void testReadTiffFromFolder() {
        String pathToTiffDirectory = "src/data/tiff";
        File path = new File(pathToTiffDirectory);
        assertTrue(path.isDirectory());
        
        File[] tiffFiles = path.listFiles();
        assertNotNull(tiffFiles);
        assertEquals(22, tiffFiles.length);
        
        for(File tiffFile: tiffFiles) {
            testReadImageFile(tiffFile);
        }
    }
    
    @Test
    public void testReadTestImagesFromW3C() {
        String pathToTiffDirectory = "src/data/w3c";
        File path = new File(pathToTiffDirectory);
        assertTrue(path.isDirectory());
        
        File[] files = path.listFiles();
        assertNotNull(files);
        assertEquals(17, files.length);
        
        for(File file: files) {
            testReadImageFile(file);
        }
    }
    
    private void testReadImageFile(File file) {
        ImageInputStream is = null;
        ImageReader reader = null;
        try {
            is = ImageIO.createImageInputStream(file);
            assertNotNull(is);
            assertTrue(is.length() > 0);
            
            Iterator<ImageReader> iterator = ImageIO.getImageReaders(is);
            assertNotNull(iterator);
            assertTrue(iterator.hasNext());
            
            reader = (ImageReader) iterator.next();
            iterator = null;
            reader.setInput(is);
            int nbPages = reader.getNumImages(true);
            assertTrue(nbPages > 0);

            System.out.println(String.format("%s has %d page(s)", file, nbPages));
            for(int page=0; page < nbPages; page++) {
                BufferedImage image = reader.read(page);
                System.out.println(String.format("%dx%d @ %d bit(s)", image.getWidth(), image.getHeight(), image.getColorModel().getPixelSize()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } finally {
            try {
                if (reader != null) {
                    reader.dispose();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
				fail();
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
