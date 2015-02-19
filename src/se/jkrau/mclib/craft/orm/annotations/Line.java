package se.jkrau.mclib.craft.orm.annotations;

/**
 * At a specific line in the method instructions.<br/><br/>
 * <strong>NOTE: </strong>Line number is relative to the class!
 */
public @interface Line {
	/**
	 * The line number in the method instructions.
	 *
	 * @return the line number.
	 */
	public int number();

	/**
	 * Should we insert after the line number, or before it (false)?
	 *
	 * @return whether we should do the above or not.
	 */
	public boolean after();
}
