package examples;

import se.jkrau.mclib.craft.orm.CraftORM;
import se.jkrau.mclib.craft.orm.annotations.Before;

public class CraftORMFieldExample extends CraftORM {
	private static String testField;

	@Before
	public static void main(String[] args) {
		testField = "I changed it!";
	}

}
