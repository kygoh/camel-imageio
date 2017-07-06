package my.com.myriadeas.camel.component.imageio;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import my.com.myriadeas.camel.component.imageio.BufferedImageConverter;

import org.junit.Test;

public class BufferedImageConverterTest {

	@Test
	public void testConvertFile() {
		try {
			File file = new File("src/data/tiff/CCITT_1.TIF");
			List<BufferedImage> images = BufferedImageConverter.toBufferedImageList(file);
			assertNotNull(images);
			assertEquals(1, images.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testConvertMultipageTiff() {
		try {
			File file = new File("src/data/tiff/large_multipage_tiff_example_v7.tif");
			List<BufferedImage> images = BufferedImageConverter.toBufferedImageList(file);
			assertNotNull(images);
			assertEquals(10, images.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
