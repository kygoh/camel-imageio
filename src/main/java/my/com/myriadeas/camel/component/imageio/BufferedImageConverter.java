package my.com.myriadeas.camel.component.imageio;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.camel.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class BufferedImageConverter {
	private static final Logger LOG = LoggerFactory.getLogger(BufferedImageConverter.class);

	/**
	* Utility classes should not have a public constructor.
	*/
    private BufferedImageConverter() {
    }

    @Converter
    public static ImageInputStream toImageInputStream(File file) throws IOException {
        LOG.debug("File -> ImageInputStream");
		ImageInputStream iis = ImageIO.createImageInputStream(file);
		if (iis == null || iis.length() == 0) {
			throw new IOException("No suitable javax.imageio.spi.ImageInputStreamSpi exists");
		}
		return iis;
    }

    @Converter
    public static ImageInputStream toImageInputStream(InputStream is) throws IOException {
        LOG.debug("InputStream -> ImageInputStream");
		ImageInputStream iis = ImageIO.createImageInputStream(is);
		if (iis == null || iis.length() == 0) {
			throw new IOException("No suitable javax.imageio.spi.ImageInputStreamSpi exists");
		}
		return iis;
    }

    @Converter
    public static List<BufferedImage> toBufferedImageList(InputStream is) throws IOException {
        LOG.debug("InputStream -> List<BufferedImage>");
    	return toBufferedImageList(toImageInputStream(is));
    }

    @Converter
    public static List<BufferedImage> toBufferedImageList(File file) throws IOException {
        LOG.debug("File -> List<BufferedImage>");
    	return toBufferedImageList(toImageInputStream(file));
    }
    
    
    @Converter
    public static List<BufferedImage> toBufferedImageList(ImageInputStream is) throws IOException {
		Iterator<ImageReader> iterator = ImageIO.getImageReaders(is);
		if (iterator == null || !iterator.hasNext()) {
		    LOG.error("Image file format not supported by ImageIO");
			throw new IOException("Image file format not supported by ImageIO");
		}
		ImageReader reader = iterator.next();
		iterator = null;
		reader.setInput(is);
		int pages = reader.getNumImages(true);
		LOG.debug("Found {} page(s)", pages);

		List<BufferedImage> images = new ArrayList<BufferedImage>();
		for(int page=0; page < pages; page++) {
			BufferedImage image = reader.read(page);
			images.add(image);
		}
        reader.dispose();
        
		is.close();
        LOG.debug("ImageInputStream closed");

		return images;
    }

}
