package se.jkrau.mclib.loader;

import se.jkrau.mclib.MCLib;
import se.jkrau.mclib.craft.Craft;
import se.jkrau.mclib.utils.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * A {@link java.lang.ClassLoader} class that loads many {@link se.jkrau.mclib.craft.Craft} for dynamic code injection
 * when classes use this classloader.
 * @author Joe
 */
public class Loader extends ClassLoader {

	private Filter filter;
	private Craft[] craftArray;
	private Map<String, Craft> craftMap;

	/**
	 * Shortcut constructor.
	 *
	 * @param parent {@link java.lang.ClassLoader}
	 * @param lib {@link se.jkrau.mclib.MCLib}
	 */
	public Loader(ClassLoader parent, MCLib lib) {
		this(parent, lib.getFilter(), lib.getCraftArray(), lib.getCraftMap());
	}

	/**
	 * Constructor to instantiate this class for dynamic code injection.
	 *
	 * @param parent {@link java.lang.ClassLoader}
	 * @param filter {@link se.jkrau.mclib.loader.Filter}
	 * @param craftArray {@link se.jkrau.mclib.craft.Craft} (array)
	 * @param craftMap {@link java.util.Map}
	 */
	public Loader(ClassLoader parent, Filter filter, Craft[] craftArray, Map<String, Craft> craftMap) {
		super(parent);
		this.filter = filter;
		this.craftArray = craftArray;
		this.craftMap = craftMap;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		InputStream classData = getResourceAsStream(ClassUtils.toClassFile(name));
		if (classData == null || name.startsWith("java") || name.startsWith("sun") || !filter.allow(name)) {
			return super.loadClass(name);
		}
		byte[] array;
		try {
			array = sun.misc.IOUtils.readFully(classData, -1, true);

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
		} catch (IOException e) {
			return super.loadClass(name);
		}

		//System.out.println(name);

		return defineClass(name, array, 0, array.length);
	}

}

