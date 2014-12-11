package se.jkrau.mclib.craft;

/**
 * The artisan's blank canvas for injecting code.
 * This class is used by the {@link se.jkrau.mclib.loader.Loader} to process classes, to make changes, etc.
 * @author Joe
 */

public interface Craft {

	/**
	 * The method that processes classes going through the {@link se.jkrau.mclib.loader.Loader}
	 *
	 * @param in The bytecode going in.
	 * @param className The class name going in.
	 * @return The bytecode with all the modifications.
	 */
	public abstract byte[] process(byte[] in, String className);
}
