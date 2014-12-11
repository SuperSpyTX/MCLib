package examples;

import se.jkrau.mclib.craft.asm.basic.CraftNode;
import se.jkrau.mclib.org.objectweb.asm.Opcodes;
import se.jkrau.mclib.org.objectweb.asm.tree.*;

import java.util.List;

/**
 * Created by SSPX on 8/20/14.
 */
public class CraftNodeExample extends CraftNode {

	@Override
	public ClassNode process(ClassNode in) {
		List<MethodNode> methods = in.methods;
		for (MethodNode method : methods) {
			if (method.name.equals("main")) {
				InsnList insnList = method.instructions;
				InsnList println = new InsnList();
				println.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
				println.add(new LdcInsnNode("OMG I AM A TRANSFORMERS!!!!"));
				println.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
				insnList.insertBefore(insnList.getLast().getPrevious(), println);
			}
		}
		return in;
	}
}
