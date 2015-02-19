MCLib
=====

A powerful code injection framework that allows for easy instrumenting and transforming of any class.

You can use this library to inject & modify code dynamically on the fly via it's own injected ClassLoader.
This could be used to make Minecraft mods, without the need to wait for the next MCP update (although you will have to find the class renamings yourself, but if you do it right, you won't have to)

**Obviously this does not need to be used just for Minecraft.**

Why am I releasing this?
==============================

Because everybody should have a fun time making stuff with this!

Reason why I packed ASM in MCLib
-------------------------------------
The convenience of the ASM library being packed into the same library has some pretty significant advantages.
Obviously, I have made some modifications to improve ASM's functionality a little bit for this framework.  For example, see {@link se.jkrau.mclib.utils.ClassUtils#getOpcode(int)}
