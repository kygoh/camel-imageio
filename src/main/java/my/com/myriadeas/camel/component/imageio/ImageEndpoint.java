package my.com.myriadeas.camel.component.imageio;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * Represents a Image endpoint.
 */
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
		// TODO Auto-generated method stub
		return null;
	}

    public ImageComponent getComponent() {
    	return component;
    }
    
    public boolean isSingleton() {
        return true;
    }

}
