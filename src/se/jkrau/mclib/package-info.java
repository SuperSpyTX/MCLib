/**
 * A powerful code injection framework that allows for easy instrumenting and transforming of any class.  <br/>
 * You can use this library to inject & modify code dynamically on the fly via it's own injected ClassLoader. <br/>
 * This could be used to make Minecraft mods, without the need to wait for the next MCP update. <br/>
 * Obviously this does not need to be used just for Minecraft.  <br/><br/>
 *
 * <strong>Reason why I packed ASM in MCLib</strong><br/><br/>
 * The convenience of the ASM library being packed into the same library has some pretty significant advantages.
 * Obviously, I have made some modifications to improve ASM's functionality a little bit.  For example, see {@link se.jkrau.mclib.utils.ClassUtils#getOpcode(int)}
 *
 * @author Joe
 * @version 1.3
 */
package se.jkrau.mclib;