package se.jkrau.mclib.craft.orm;

/**
 * The type of {@link se.jkrau.mclib.craft.orm.ORMDriver} to use.  Usually you don't need to do anything with this class.
 * @see se.jkrau.mclib.craft.orm.CraftORM
 * @author Joe
 */
public enum ORMType {
	ASM(0),
	;

	int type;

	ORMType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String toString() {
		return Integer.toString(type);
	}
}
