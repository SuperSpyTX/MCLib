package se.jkrau.mclib.loader;

import se.jkrau.mclib.MCLib;
import se.jkrau.mclib.craft.Craft;

import java.util.Map;

/**
 * TODO: There seems to be more than what's here.  To be documented when a final plan goes for this.
 */
public class ByteArrayLoader extends Loader {
	public ByteArrayLoader(MCLib lib) {
		super(lib);
	}

	public ByteArrayLoader(Filter filter, Craft[] craftArray, Map<String, Craft> craftMap) {
		super(filter, craftArray, craftMap);
	}

	public byte[] loadClass(byte[] array, String name) {
		return this.injectClass(array, name);
	}
}
