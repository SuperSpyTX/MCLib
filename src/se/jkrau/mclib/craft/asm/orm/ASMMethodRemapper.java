package se.jkrau.mclib.craft.asm.orm;

import se.jkrau.mclib.org.objectweb.asm.Label;
import se.jkrau.mclib.org.objectweb.asm.MethodVisitor;
import se.jkrau.mclib.org.objectweb.asm.Type;

public class ASMMethodRemapper extends MethodVisitor {
	private String originalClassName;
	private String targetClassName;

	public ASMMethodRemapper(MethodVisitor methodVisitor, String originalClassName, String targetClassName) {
		super(327680, methodVisitor);
		this.originalClassName = originalClassName;
		this.targetClassName = targetClassName;
	}

	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		if (owner.equals(this.originalClassName)) {
			owner = this.targetClassName;
		}
		super.visitFieldInsn(opcode, owner, name, desc);
	}

	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		if (owner.equals(this.originalClassName)) {
			owner = this.targetClassName;
		}
		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		Type type = Type.getType(desc);
		if (type.getClassName().equals(this.originalClassName)) {
			desc = Type.getObjectType(this.targetClassName).getDescriptor();
		}
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}
}