package my.com.myriadeas.camel.component.imageio;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;

/**
 * Represents a Image endpoint.
 */
@UriEndpoint(scheme = "image", title = "ImageIO", syntax = "image:format", producerOnly = true, label = "image,transformation")
public class ImageEndpoint extends DefaultEndpoint {

	private ImageComponent component;
	
	public ImageEndpoint() {
    }

    public ImageEndpoint(String uri, ImageComponent component) {
        super(uri, component);
        this.component = component;
    }

    public Producer createProducer() throws Exception {
        return new ImageProducer(this);
    }

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
	    throw new UnsupportedOperationException("Consumer is not supported for Image component:" + getEndpointUri());
	}

    public ImageComponent getComponent() {
    	return component;
    }
    
    public boolean isSingleton() {
        return true;
    }

}
