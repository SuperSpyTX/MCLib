package se.jkrau.mclib.loader;

import se.jkrau.mclib.utils.ClassUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * A {@link java.lang.ClassLoader} class that loads post-injected classes into the Java Virtual Machine.
 *
 * @author Joe
 */
public class InjectedClassLoader extends ClassLoader {

	private Loader loader;

	public InjectedClassLoader(ClassLoader parent, Loader loader) {
		super(parent);
		this.loader = loader;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		InputStream classData = getResourceAsStream(ClassUtils.toClassFile(name));

		byte[] array;
		try {
			// TODO: change deprecated API statement to something else.
			array = sun.misc.IOUtils.readFully(classData, -1, true);

			array = loader.injectClass(array, name);

			if (array == null) {
				return super.loadClass(name);
			}
		} catch (IOException e) {
			return super.loadClass(name);
		}

		//System.out.println(name);

		return defineClass(name, array, 0, array.length);
	}

}

