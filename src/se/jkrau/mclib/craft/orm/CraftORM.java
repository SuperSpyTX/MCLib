package se.jkrau.mclib.craft.orm;

import se.jkrau.mclib.craft.Craft;
import se.jkrau.mclib.org.objectweb.asm.ClassVisitor;

import java.io.ByteArrayInputStream;

/**
 * A craft created of special Illuminati Black Magic consisted of women.
 *
 * This class is specialized to take your code in the class (extending this one) and allow for dynamic code injection, just by writing your own
 * java code! This does not support local variables defined in the method but it supports the parameters, the fields in the class
 * (natively works even when defined in class) and the inherited methods and fields.
 * You must define an annotation for the method.
 * See {@link se.jkrau.mclib.craft.orm.annotations} for more info.
 *
 * @see se.jkrau.mclib.craft.orm.annotations
 */
public class CraftORM implements Craft {

	private ORMDriver ormDriver;

	public CraftORM(ClassVisitor preVisitor, ClassVisitor postVisitor) {
		this(ORMType.ASM, preVisitor, postVisitor);
	}

	public CraftORM() {
		this(ORMType.ASM, null, null);
	}

	public CraftORM(ORMType ormType, ClassVisitor preVisitor, ClassVisitor postVisitor) {
		if (ormType == null) {
			throw new NullPointerException("ORMDriver argument is null!");
		}

		if (ormType == ORMType.ASM) {
			ormDriver = new se.jkrau.mclib.craft.asm.orm.ASMDriver();
		} else {
			throw new IllegalStateException("ORM type passed... your argument is invalid!"); // Totally not a niceme.me
		}

		String currentClassName = getClass().getName().replace(".", "/");
		if (currentClassName.equals(CraftORM.class.getName().replace(".", "/"))) {
			throw new IllegalStateException("Instantiation upon root CraftORM class is not allowed.");
		}

		ormDriver.setup(currentClassName, preVisitor, postVisitor);
	}

	@Override
	public byte[] process(byte[] in, String className) {
		return ormDriver.process(new ByteArrayInputStream(in), className);
	}
}
