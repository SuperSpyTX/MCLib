import examples.*;
import se.jkrau.mclib.MCLib;
import se.jkrau.mclib.craft.CraftDumper;

import java.lang.reflect.InvocationTargetException;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException {
		MCLib mclib = new MCLib("Test2");

        // Test 2
		mclib.craft(new CraftVisitorExample(), "Test2");
		mclib.craft(new CraftORMExample(), "Test2");
		mclib.craft(new CraftORMLineExample(), "Test2");
		mclib.craft(new CraftORMFieldExample(), "Test2");

        // Test 3
        mclib.craft(new BeingAnnoyingAsHell(), "Test2", "Test3");

		mclib.createLoader();
		try {
			mclib.run(null, (Object) args);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
