package examples;

import se.jkrau.mclib.craft.asm.basic.CraftVisitor;
import se.jkrau.mclib.org.objectweb.asm.Label;
import se.jkrau.mclib.org.objectweb.asm.MethodVisitor;
import se.jkrau.mclib.org.objectweb.asm.Opcodes;

public class CraftVisitorExample extends CraftVisitor {

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv;
		mv = cv.visitMethod(access, name, desc, signature, exceptions);
		if (mv != null && name.equals("main")) {
			mv = new InjectHelloWorld(mv);
		}

		return mv;
	}

	public class InjectHelloWorld extends MethodVisitor {
		public InjectHelloWorld(MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
		}

		public void visitCode() {
			Label l2 = new Label();
			super.visitLabel(l2);
			super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			super.visitLdcInsn("Hello World of Small Paineses!");
			super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			super.visitCode();
		}
	}
}
