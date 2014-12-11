package examples;

import se.jkrau.mclib.craft.Craft;

public class BytecodePatchingExample implements Craft {

    @Override
    public byte[] process(byte[] in, String className) {
        // Do patching

        return null;
    }
}
