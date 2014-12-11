package examples;

import se.jkrau.mclib.craft.orm.CraftORM;
import se.jkrau.mclib.craft.orm.annotations.*;
import se.jkrau.mclib.org.objectweb.asm.ClassVisitor;
import se.jkrau.mclib.org.objectweb.asm.Opcodes;

public class CraftORMExample extends CraftORM {

	@Before
	public static void main(String[] args) {
		System.out.println("Oh by the way, did you know you can inject code, WITHOUT KNOWLEDGE OF THE FUCKING CHINESE JVM!?!?!!?!?!?!?!?!?!?!??!");
		if (args.length > 0) {
			System.out.println("Oh shit dude there's arguments!");
		}
		/*String declared = "Penis";
		try {
			throw new Exception("Fuck yes?");
		} catch (Exception e) {
			System.out.println("Yes caught a fuckin error!");
		}*/
	}
}
