package se.jkrau.mclib.craft.orm;

import se.jkrau.mclib.org.objectweb.asm.ClassVisitor;

import java.io.InputStream;

/**
 * The driver that performs all the magic.
 *
 * @see se.jkrau.mclib.craft.orm.CraftORM
 */
public interface ORMDriver {

	public void setup(String className, ClassVisitor preVisitor, ClassVisitor postVisitor);

	public byte[] process(InputStream in, String className);
}
