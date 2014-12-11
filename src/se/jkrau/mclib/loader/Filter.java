package se.jkrau.mclib.loader;

/**
 * A class responsible for filtering class names going through the {@link se.jkrau.mclib.loader.Loader}
 * @author Joe
 */
public abstract class Filter {

	/**
	 * Checks if the class name passed is allowed to be loaded by {@link Loader} <br /><br />
	 *
	 * <em>Note:</em> because custom classloaders cannot load java.* and sun.* classes, they're already filtered by default.
	 *
	 * @param className {@link java.lang.String}
	 * @return true if allowed to pass through, false otherwise.
	 */
	public abstract boolean allow(String className);
}
