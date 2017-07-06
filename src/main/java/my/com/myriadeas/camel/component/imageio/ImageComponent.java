package my.com.myriadeas.camel.component.imageio;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;


import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the component that manages {@link ImageEndpoint}.
 */
public class ImageComponent extends DefaultComponent {

    private static final Logger LOG = LoggerFactory.getLogger(ImageComponent.class);

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new ImageEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }

    public void process(Exchange exchange, String format) {
		try {
			Message in = exchange.getIn();
			
			LOG.info("Entering ImageComponent::process");
			
			Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName(format);
		    if (iterator == null || !iterator.hasNext()) {
	            LOG.error("Image file format {} not supported by ImageIO", format);
	            throw new IOException("Image file format not supported by ImageIO");
	        }
		    ImageWriter writer = iterator.next();

            List<ByteArrayOutputStream> baosList = new ArrayList<ByteArrayOutputStream>();
            
			List<BufferedImage> images = BufferedImageConverter.toBufferedImageList(in.getBody(InputStream.class));
			
			if (images != null && images.size() > 0) {
			    if (writer.canWriteSequence()) {
			        LOG.debug("writeSequence");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    MemoryCacheImageOutputStream cache = new MemoryCacheImageOutputStream(baos);
			        writer.setOutput(cache);
			        
			        ImageWriteParam params = writer.getDefaultWriteParam();
			        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			        
			        writer.prepareWriteSequence(null);
			        for(BufferedImage image: images) {
                        writer.writeToSequence(new IIOImage(image, null, null), params);
                    }
			        writer.endWriteSequence();
			        writer.dispose();
			        cache.close();
			        
			        LOG.debug("added OutputStream with size={}", baos.size());
                    baosList.add(baos);
			    } else {
    			    for(BufferedImage image: images) {
    			        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    			        ImageIO.write(image, format, baos);
                        LOG.debug("added OutputStream with size={}", baos.size());
    			        baosList.add(baos);
    			    }
			    }
			}
			in.setBody(baosList);

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
