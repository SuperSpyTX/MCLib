package se.jkrau.mclib.loader;

import se.jkrau.mclib.MCLib;
import se.jkrau.mclib.craft.Craft;

import java.util.Map;

/**
 * A class responsible for loading classes, modified by {@link se.jkrau.mclib.craft.Craft} objects.
 *
 * @author Joe
 */
public class Loader {
	private Filter filter;
	private Craft[] craftArray;
	private Map<String, Craft> craftMap;

	/**
	 * Shortcut constructor.
	 *
	 * @param lib {@link se.jkrau.mclib.MCLib}
	 */
	public Loader(MCLib lib) {
		this(lib.getFilter(), lib.getCraftArray(), lib.getCraftMap());
	}

	/**
	 * Constructor to instantiate this class for dynamic code injection.
	 *
	 * @param filter     {@link se.jkrau.mclib.loader.Filter}
	 * @param craftArray {@link se.jkrau.mclib.craft.Craft} (array)
	 * @param craftMap   {@link java.util.Map}
	 */
	public Loader(Filter filter, Craft[] craftArray, Map<String, Craft> craftMap) {
		this.filter = filter;
		this.craftArray = craftArray;
		this.craftMap = craftMap;
	}

	/**
	 * This method injects the target class represented in a byte array.
	 *
	 * @param clazz the target class represented in a byte array.
	 * @param name  the class name target class name represents.
	 * @return byte array if successful, null if not.
	 */
	protected byte[] injectClass(byte[] clazz, String name) {
		byte[] array = clazz;

		if (array == null || name.startsWith("java") || name.startsWith("sun") || !filter.allow(name)) {
			return null;
		}

		name = name.replace(".", "/");

		// Do we have crafts that accept every class loaded? Let's do that first.
		if (craftArray.length > 0) {
			for (Craft craft : craftArray) {
				byte[] result = craft.process(array, name);
				if (result != null) {
					array = result;
				}
			}
		}

		// Alright now let's process any specific class crafts.
		if (craftMap.size() > 0) {
			Craft craft = craftMap.remove(name);
			if (craft != null) {
				byte[] result = craft.process(array, name);
				if (result != null) {
					array = result;
				}
			}
		}

		return array;
	}
}
