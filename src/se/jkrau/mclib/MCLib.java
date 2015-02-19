package se.jkrau.mclib;

import se.jkrau.mclib.craft.Craft;
import se.jkrau.mclib.craft.MultiEntryCraft;
import se.jkrau.mclib.loader.Filter;
import se.jkrau.mclib.loader.InjectedClassLoader;
import se.jkrau.mclib.loader.Loader;
import se.jkrau.mclib.utils.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The official MCLib class.  Instantiating this class will allow you to fully use MCLib's features.
 *
 * @author Joe
 */
public class MCLib {

	// Entry parameters.
	private String entryClass;
	private String entryMethod;

	// Loader parameters.
	private Filter filter;
	private List<Craft> craftList;
	private Map<String, Craft> craftMap;
	private Loader loader;
	private InjectedClassLoader classLoader;

	/**
	 * Shortcut constructor for classes with a public main method (defaults for main(String[] args))
	 *
	 * @param mainClass the class name with the above method
	 */
	public MCLib(String mainClass) {
		this(mainClass, "main");
	}

	/**
	 * Constructor that defines the entry point's class, method, and parameters.
	 *
	 * @param entryClass  the class name
	 * @param entryMethod the method name from the class
	 */
	public MCLib(String entryClass, String entryMethod) {
		this.entryClass = entryClass;
		this.entryMethod = entryMethod;
		this.filter = new Filter() {
			public boolean allow(String className) {
				return true;
			}
		};
		this.craftList = new ArrayList<Craft>();
		this.craftMap = new HashMap<String, Craft>();
	}

	/**
	 * Add a {@link se.jkrau.mclib.craft.Craft} to MCLib for dynamic code injection.
	 *
	 * @param craft {@link se.jkrau.mclib.craft.Craft}
	 */
	public void craft(Craft craft) {
		craftList.add(craft);
	}

	/**
	 * Attach a {@link se.jkrau.mclib.craft.Craft} to one or more classes, then add it to MCLib for dynamic code injection.<br /><br />
	 * <p/>
	 * <em>Note:</em> Despite what you think, this supports duplicate classes.  Just don't attach them in the same method call though (or you'll eat up a lot of PermGen space!)
	 *
	 * @param craft   {@link se.jkrau.mclib.craft.Craft}
	 * @param classes array of strings with one or more class names
	 */
	public void craft(Craft craft, String... classes) {
		for (String classArg : classes) {
			if (craftMap.containsKey(classArg)) {
				Craft existingCraft = craftMap.get(classArg);
				if (!(existingCraft instanceof MultiEntryCraft)) {
					MultiEntryCraft multiEntryCraft = new MultiEntryCraft(new ArrayList<Craft>());
					multiEntryCraft.addEntry(existingCraft);
					multiEntryCraft.addEntry(craft);
					craftMap.put(classArg, multiEntryCraft);
				} else {
					((MultiEntryCraft) existingCraft).addEntry(craft);
				}
			} else {
				craftMap.put(classArg, craft);
			}
		}
	}

	/**
	 * Gets the {@link se.jkrau.mclib.loader.Filter} for MCLib.
	 *
	 * @return {@link se.jkrau.mclib.loader.Filter}
	 * @see se.jkrau.mclib.loader.Filter
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * Sets the {@link se.jkrau.mclib.loader.Filter} for MCLib.
	 *
	 * @param filter {@link se.jkrau.mclib.loader.Filter}
	 * @see se.jkrau.mclib.craft.Craft
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * Gets an array of {@link se.jkrau.mclib.craft.Craft}s added from {@link #craft(se.jkrau.mclib.craft.Craft)}<br /><br />
	 * <p/>
	 * <strong>Warning:</strong> This is meant for the loader only! Only use this unless you know what you're doing.
	 *
	 * @return array of crafts added by above method.
	 */
	public Craft[] getCraftArray() {
		return craftList.toArray(new Craft[0]);
	}

	/**
	 * Gets a map of {@link se.jkrau.mclib.craft.Craft}s added from {@link #craft(se.jkrau.mclib.craft.Craft, String...)}
	 *
	 * @return map of crafts added by above method.
	 */
	public Map<String, Craft> getCraftMap() {
		return craftMap;
	}

	/**
	 * Creates the {@link se.jkrau.mclib.loader.Loader} with all the arguments provided so far.<br /><br />
	 * <p/>
	 * <strong>Warning: Without invoking this method, any injected crafts will not take effect!</strong><br />
	 * <strong>Warning:</strong> Any new craft calls will require this method to be invoked again!
	 */
	public void createLoader() {
		if (this.loader == null) {
			this.loader = new Loader(this);
		}
		this.classLoader = new InjectedClassLoader(Thread.currentThread().getContextClassLoader(), loader);
	}

	/**
	 * Gets the {@link se.jkrau.mclib.loader.Loader} for this MCLib injection.<br /><br />
	 * <p/>
	 * <strong>Warning:</strong> There is no use case of getting the loader!  But use at your own risk anyways.
	 *
	 * @return {@link se.jkrau.mclib.loader.Loader}
	 */
	public Loader getLoader() {
		return this.loader;
	}

	/**
	 * Sets the {@link se.jkrau.mclib.loader.Loader} for this MCLib injection.<br /><br />
	 * <p/>
	 * <strong>Warning: This will not construct the loader for you like the {@link #createLoader()} will!</strong><br />
	 * <strong>Warning:</strong> There is no use case of setting the loader as the one provided works.  But use at your own risk anyways.
	 *
	 * @param loader the {@link se.jkrau.mclib.loader.Loader} to use for MCLib.
	 * @see se.jkrau.mclib.loader.Loader
	 */
	@Deprecated
	public void setLoader(Loader loader) {
		this.loader = loader;
	}

	/**
	 * Executes the entry point method.<br /><br />
	 * <p/>
	 * Sure you could just use {@link #getLoader()} and override the classloader manually, but heck, why do that when this method already has you
	 * covered!<br /><br />
	 * <strong>Warning: Without invoking this method, the {@link se.jkrau.mclib.loader.Loader} will not override the classloader!</strong><br />
	 * <em>Note: Make sure you cast entryArgs to {@link java.lang.Object} as well as make sure they match the parameter types in the main method otherwise you will get an exception.</em>
	 *
	 * @param entryInstance Object with an existing instance of the entry point class, usually null if you're calling the main method.
	 * @param entryArgs     Object array of arguments for the entry point method, usually just passing it along from your main method.
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void run(Object entryInstance, Object... entryArgs) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<?> clazz = (this.classLoader != null ? this.classLoader.loadClass(entryClass) : Class.forName(entryClass));
		Method method = clazz.getMethod(entryMethod, ClassUtils.getClassArray(entryArgs));
		method.invoke(entryInstance, entryArgs);
	}
}
