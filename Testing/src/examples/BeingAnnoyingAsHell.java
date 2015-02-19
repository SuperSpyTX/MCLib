package examples;

import se.jkrau.mclib.craft.orm.CraftORM;
import se.jkrau.mclib.craft.orm.annotations.Before;

public class BeingAnnoyingAsHell extends CraftORM {
    @Before
    public static void main(String[] args) {
        System.out.println("I LIKE TO BE ANNOYING AS HELL!");
    }
}
