import examples.CraftORMFieldExample;
import examples.CraftORMLineExample;
import se.jkrau.mclib.MCLib;
import examples.CraftORMExample;
import java.lang.reflect.InvocationTargetException;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException {
		MCLib mclib = new MCLib("Test2");

//		mclib.craft(new CraftSurisPenis(), "Test2");
//		mclib.craft(new CraftVisitorExample(), "Test2");
//		mclib.craft(new CraftORMExample(), "Test2");
//		mclib.craft(new CraftORMLineExample(), "Test2");
		mclib.craft(new CraftORMFieldExample(), "Test2");
		//mclib.craft(new CraftNodeExample(), "Test2");
		//mclib.craft(new Exampleehkwiuh());

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
