MCLib
=====

A powerful code injection framework that allows for easy instrumenting and transforming of any class.

You can use this library to inject & modify code dynamically on the fly via it's own injected ClassLoader.
This could be used to make Minecraft mods, without the need to wait for the next MCP update (although you will have to find the class renamings yourself, but if you do it right, you won't have to)

**Obviously this does not need to be used just for Minecraft.**

Why am I releasing this?
==============================

**Q:** You would think, a neat little framework that has quite a bit of power, is being released to the public.  Why would you release it instead of keeping it to yourself.

**A:** I'm not sure why either.  The reason must be.. Well I quit minecraft.  It sucks.  I used this library to make a minecraft client but just kind of stopped afterward since it got too awkward.

Ultimately, I enjoyed most of the time dissecting the game into little pieces and having fun with the game code to make crazy things out of it, but things can get boring after you see these giant networks taking over.  Now all of the sudden it's become too difficult to really do anything anymore.

I hope somebody finds fun use like I did.  It's kind of a magical work of art that I did this summer.

Reason why I packed ASM in MCLib
-------------------------------------
The convenience of the ASM library being packed into the same library has some pretty significant advantages.
Obviously, I have made some modifications to improve ASM's functionality a little bit for this framework.  For example, see {@link se.jkrau.mclib.utils.ClassUtils#getOpcode(int)}
