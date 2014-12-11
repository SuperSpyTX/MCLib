package se.jkrau.mclib.craft.asm;

import se.jkrau.mclib.craft.Craft;
import se.jkrau.mclib.org.objectweb.asm.ClassReader;
import se.jkrau.mclib.org.objectweb.asm.ClassWriter;

/**
 * Craft that implements the ASM library.  Base foundation for crafts using the ASM library.
 */
public abstract class CraftASM implements Craft {

	public abstract ClassWriter process(ClassReader in);

	@Override
	public byte[] process(byte[] in, String className) {
		ClassReader cr = new ClassReader(in);
		ClassWriter cw = process(cr);
		
		if (cw == null) {
			return null;
		} else {
			return cw.toByteArray();
		}
	}
}
