package se.jkrau.mclib.craft;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A quick and dirty craft that dumps the java bytecode into a file.
 */
public class CraftDumper implements Craft {
	@Override
	public byte[] process(byte[] in, String className) {
		try {
			FileOutputStream fos = new FileOutputStream(className + ".class");
			fos.write(in);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
