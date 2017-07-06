package my.com.myriadeas.camel.component.imageio;

import java.net.URI;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Image producer.
 * Reference: 
 */
public class ImageProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(ImageProducer.class);
    private ImageEndpoint endpoint;

    public ImageProducer(ImageEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    	LOG.debug("endpoint");
    }

    public void process(Exchange exchange) throws Exception {
        ImageComponent component = endpoint.getComponent();
        URI uri = URI.create(endpoint.getEndpointUri());
        String format = uri.getHost();
        if (format == null) {
            format = "jpg";
        }
        LOG.debug("scheme={} host={} format={}", uri.getScheme(), uri.getHost(), format);
        component.process(exchange, format);
    }

}
