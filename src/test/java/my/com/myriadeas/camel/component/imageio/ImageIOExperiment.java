package my.com.myriadeas.camel.component.imageio;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.junit.Test;

public class ImageIOExperiment {

	@Test
	public void testFileSuffixes() {
		String[] fileSuffixes = ImageIO.getReaderFileSuffixes();
		assertNotNull(fileSuffixes);
		assertTrue(fileSuffixes.length > 0);
		for(String fileSuffix : fileSuffixes) {
			System.out.println("Suffix:" + fileSuffix);
		}
	}

	@Test
	public void testFormatNames() {
		String[] formatNames = ImageIO.getReaderFormatNames();
		assertNotNull(formatNames);
		assertTrue(formatNames.length > 0);
		for(String formatName : formatNames) {
			System.out.println("Format:" + formatName);
		}
	}

	@Test
	public void testMIMETypes() {
		String[] MIMETypes = ImageIO.getReaderMIMETypes();
		assertNotNull(MIMETypes);
		assertTrue(MIMETypes.length > 0);
		for(String MIMEType : MIMETypes) {
			System.out.println("MIMEType:" + MIMEType);
		}
	}

	@Test
	public void testRelatedMIMETypesFileSuffixes() {
		HashMap<Class<?>, String> mapMIMEType = new HashMap<Class<?>, String>();
		for(String MIMEType : ImageIO.getReaderMIMETypes()) {
			Iterator<ImageReader> iterator = ImageIO.getImageReadersByMIMEType(MIMEType);
			while(iterator.hasNext()) {
				mapMIMEType.put(iterator.next().getClass(), MIMEType);
			}
		}
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		for(String fileSuffix : ImageIO.getReaderFileSuffixes()) {
			Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix(fileSuffix);
			while(iterator.hasNext()) {
				String MIMEType = mapMIMEType.get(iterator.next().getClass());
				List<String> suffixList = map.get(MIMEType);
				if (suffixList == null) {
					suffixList = new ArrayList<String>();
					map.put(MIMEType, suffixList);
				}
				suffixList.add(fileSuffix);
			}
		}
		for(Entry<String, List<String>> entry  : map.entrySet()) {
			String suffixes = "";
			for(String suffix : entry.getValue()) {
				if (suffixes.length() > 0) {
					suffixes += "; ";
				}
				suffixes += "*." + suffix;
			}
			System.out.println(entry.getKey() + " (" + suffixes + ")");
		}
	}
}
