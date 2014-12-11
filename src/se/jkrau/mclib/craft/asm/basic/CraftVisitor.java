package se.jkrau.mclib.craft.asm.basic;

import se.jkrau.mclib.craft.Craft;
import se.jkrau.mclib.org.objectweb.asm.ClassReader;
import se.jkrau.mclib.org.objectweb.asm.ClassVisitor;
import se.jkrau.mclib.org.objectweb.asm.ClassWriter;
import se.jkrau.mclib.org.objectweb.asm.Opcodes;

/**
 * Craft that implements ASM's {@link se.jkrau.mclib.org.objectweb.asm.ClassVisitor} pattern to transform classes.
 */
public abstract class CraftVisitor extends ClassVisitor implements Craft {

	public CraftVisitor() {
		this(Opcodes.ASM5);
	}

	public CraftVisitor(int api) {
		this(api, new ClassWriter(ClassWriter.COMPUTE_FRAMES));
	}

	public CraftVisitor(int api, ClassVisitor cv) {
		super(api, cv);
	}

	public void preProcess(ClassReader cr) {
	}

	@Override
	public byte[] process(byte[] in, String className) {
		ClassReader cr = new ClassReader(in);
		preProcess(cr);
		cr.accept(this, 0);

		if (this.cv == null) {
			return null;
		} else {
			return ((ClassWriter) cv).toByteArray();
		}
	}
}
