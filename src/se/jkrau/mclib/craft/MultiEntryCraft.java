package se.jkrau.mclib.craft;

import java.util.List;

/**
 * Internal class used to provide multiple {@link Craft} entries for a single class.
 * @author Joe
 */
public class MultiEntryCraft implements Craft {
	private List<Craft> craftList;

	public MultiEntryCraft(List<Craft> crafts) {
		craftList = crafts;
	}

	public boolean addEntry(Craft craft) {
		return craftList.add(craft);
	}

	public boolean removeEntry(Craft craft) {
		return craftList.remove(craft);
	}

	/**
	 * {@inheritDoc}
	 **/
	public byte[] process(byte[] in, String className) {
		for (Craft craft : craftList) {
			byte[] result = craft.process(in, className);
			if (result != null) {
				in = result;
			}
		}

		return in;
	}
}
