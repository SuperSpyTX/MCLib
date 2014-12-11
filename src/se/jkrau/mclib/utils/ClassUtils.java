package se.jkrau.mclib.utils;

import se.jkrau.mclib.org.objectweb.asm.xml.ASMContentHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for classes.
 *
 * @author Joe
 */
public class ClassUtils {

	/**
	 * Converts class name to a file path with .class file extension.
	 *
	 * @param className string containing the class name
	 * @return string holding the result (as above)
	 */
	public static String toClassFile(String className) {
		return className.replace(".", "/") + ".class";
	}

	/**
	 * ASM specific - returns the string version of the opcode.  Very useful for the different return opcodes that exist, so you could just do
	 * .contains("RETURN") and be done with it.  Does not support all opcodes in Java, only ones supported in the ASM library (that actually
	 * gets assigned an opcode).
	 *
	 * @param op the opcode in integer form.
	 * @return the string version of the given opcode.
	 */
	public static String getOpcode(int op) {
		for (Map.Entry<String, ASMContentHandler.Opcode> opcodeEntry : ASMContentHandler.OPCODES.entrySet()) {
			if (opcodeEntry.getValue().opcode == op) {
				return opcodeEntry.getKey();
			}
		}

		return "UNKNOWN";
	}

	/**
	 * Found this from StackOverflow & also MCP too.  Combines 2 arrays basically.
	 *
	 * @param first  first array
	 * @param second second array
	 * @param <T>    the type
	 * @return the merged array.
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = java.util.Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * Creates a class array from an given object array.
	 *
	 * @param args The object array.
	 * @return The class array.
	 */
	public static Class<?>[] getClassArray(Object[] args) {
		List<Class<?>> objs = new ArrayList<Class<?>>();
		for (Object obj : args) {
			if (obj.getClass().isPrimitive()) {
				if (obj instanceof Byte) {
					objs.add(Byte.TYPE);
				} else if (obj instanceof Short) {
					objs.add(Short.TYPE);
				} else if (obj instanceof Integer) {
					objs.add(Integer.TYPE);
				} else if (obj instanceof Long) {
					objs.add(Long.TYPE);
				} else if (obj instanceof Float) {
					objs.add(Float.TYPE);
				} else if (obj instanceof Double) {
					objs.add(Double.TYPE);
				} else if (obj instanceof Character) {
					objs.add(Character.TYPE);
				} else if (obj instanceof String) {
					objs.add(String.class);
				} else if (obj instanceof Boolean) {
					objs.add(Boolean.TYPE);
				}
			} else {
				objs.add(obj.getClass());
			}
		}

		return objs.toArray(new Class<?>[0]);
	}

	/**
	 * Gets the initialized class name from a type. This is meant to convert primitive types into initialized version of those types.
	 *
	 * @param arg The primitive type.
	 * @return The initialized class.
	 */
	public static Class<?> getClassFromType(Type arg) {
		if (((Class<?>) arg).isPrimitive()) {
			if (arg.toString().contains("byte")) {
				return (Byte.class);
			} else if (arg.toString().contains("short")) {
				return (Short.class);
			} else if (arg.toString().contains("int")) {
				return (Integer.class);
			} else if (arg.toString().contains("long")) {
				return (Long.class);
			} else if (arg.toString().contains("float")) {
				return (Float.class);
			} else if (arg.toString().contains("double")) {
				return (Double.class);
			} else if (arg.toString().contains("char")) {
				return (Character.class);
			} else if (arg.toString().contains("java.lang.String")) {
				return (String.class);
			} else if (arg.toString().contains("boolean")) {
				return (Boolean.class);
			}
		}

		return null;
	}

}
