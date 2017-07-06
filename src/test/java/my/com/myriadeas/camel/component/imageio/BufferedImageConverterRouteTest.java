package my.com.myriadeas.camel.component.imageio;

import java.io.File;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import javax.imageio.stream.ImageInputStream;

public class BufferedImageConverterRouteTest extends CamelTestSupport {

	@EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

	@Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void testMultipageTiff() throws Exception {
    	template.sendBody(new File("src/data/tiff/large_multipage_tiff_example_v7.tif"));
    	resultEndpoint.expectedMinimumMessageCount(1);
    	resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testJpeg() throws Exception {
    	template.sendBody(new File("src/data/jpg/barcode-download.jpg"));
    	resultEndpoint.expectedMinimumMessageCount(1);
    	resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                 from("direct:start")
                 	.convertBodyTo(ImageInputStream.class)
                 	.convertBodyTo(List.class)
                 	.split(body()).setBody().simple("${body.tileWidth} x ${body.tileHeight}")
	                .to("log:my.com.myriadeas")
	                .end()
	                .to("mock:result");
            }
        };
    }

}
