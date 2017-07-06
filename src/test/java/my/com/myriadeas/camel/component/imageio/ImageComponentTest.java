package my.com.myriadeas.camel.component.imageio;

import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ImageComponentTest extends CamelTestSupport {

    private static final String OUTPUT_FORMAT = "JPEG";
    //private static final String PNG_OUTPUT_FORMAT = "png";
    //private static final String TIF_OUTPUT_FORMAT = "tif";
    
    @Test
    public void testImage() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        // wait for one minute
        mock.setResultWaitTime(60000);
        mock.expectedMinimumMessageCount(1);
        assertMockEndpointsSatisfied();
        
        ImageInputStream iis = ImageIO.createImageInputStream(new File("src/data/out/barcode-download.jpg"));
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        assertNotNull(readers);
        assertTrue(readers.hasNext());
        assertEquals(OUTPUT_FORMAT, readers.next().getFormatName());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                 from("file://src/data/in?noop=true")
	                .to("log:my.com.myriadeas?level=INFO&showHeaders=true")
                 	.streamCaching()
	                .to("image:" + OUTPUT_FORMAT)
	                .split()
	                .body()
	                .to("log:my.com.myriadeas?level=INFO&showHeaders=true")
	                .to("file://src/data/out")
	                .end()
	                .to("mock:result");
            }
        };
    }

}
