package se.jkrau.mclib.craft.asm.basic;

import se.jkrau.mclib.craft.asm.CraftASM;
import se.jkrau.mclib.org.objectweb.asm.ClassReader;
import se.jkrau.mclib.org.objectweb.asm.ClassWriter;
import se.jkrau.mclib.org.objectweb.asm.Opcodes;
import se.jkrau.mclib.org.objectweb.asm.tree.ClassNode;

/**
 * A craft that utilizes ASM's Tree API for transforming classes.
 */
public abstract class CraftNode extends CraftASM {

	public abstract ClassNode process(ClassNode in);

	@Override
	public ClassWriter process(ClassReader in) {
		ClassNode cn = new ClassNode(Opcodes.ASM5);
		in.accept(cn, 0);
		cn = process(cn);

		if (cn == null) {
			return null;
		} else {
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			cn.accept(cw);
			return cw;
		}
	}
}
