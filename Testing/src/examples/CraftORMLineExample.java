package examples;

import se.jkrau.mclib.craft.orm.CraftORM;
import se.jkrau.mclib.craft.orm.annotations.Line;

public class CraftORMLineExample extends CraftORM {

	@Line(after = true, number = 11)
	public static void main(String[] args) {
		System.out.println("HEY THIS IS AFTER LINE NUMBER ELEVEN! FUCK YEAH!");
	}
}
