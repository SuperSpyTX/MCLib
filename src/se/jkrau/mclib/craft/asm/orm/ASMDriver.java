package se.jkrau.mclib.craft.asm.orm;

import se.jkrau.mclib.craft.orm.ORMDriver;
import se.jkrau.mclib.craft.orm.annotations.*;
import se.jkrau.mclib.org.objectweb.asm.*;
import se.jkrau.mclib.org.objectweb.asm.tree.*;
import se.jkrau.mclib.utils.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// WHILE WINGS OF GLORY - I'M STUCK INSIDE.....
public class ASMDriver implements ORMDriver {

	String originalClassName;
	String targetClassName;
	ClassVisitor preVisitor;
	ClassVisitor postVisitor;
	Map<String, MethodNode> before = new HashMap<String, MethodNode>();
	Map<String, MethodNode> after = new HashMap<String, MethodNode>();
	Map<String, MethodNode> replace = new HashMap<String, MethodNode>();
	Map<String, Map.Entry<Integer, MethodNode>> line = new HashMap<String, Map.Entry<Integer, MethodNode>>();

	@Override
	public void setup(String className, ClassVisitor preVisitor, ClassVisitor postVisitor) {
		this.originalClassName = className;
		this.preVisitor = preVisitor;
		this.postVisitor = postVisitor;
		try {
			ClassReader cr = new ClassReader(className);
			ClassNode cn = new ClassNode(Opcodes.ASM5);
			if (preVisitor != null) {
				// this way any visitors that need to take place, can be done so here.
				// however this does not support extra visitors that *actually* need the delegated
				// class visitor underneath them.  It needs to still get to ClassNode....right?
				this.preVisitor.setCv(cn);
				cr.accept(preVisitor, 0);
			} else {
				cr.accept(cn, 0);
			}


			for (MethodNode mn : cn.methods) {
				if (mn.invisibleAnnotations != null && mn.invisibleAnnotations.size() > 0) {
					for (AnnotationNode an : mn.invisibleAnnotations) {
						String typeName = an.desc.substring(1, an.desc.length() - 1).replace("/", ".");
						if (Before.class.getName().equals(typeName)) {

							// Still relevant, unfortunately :(
							for (int i = mn.instructions.toArray().length - 1; i > -1; i--) {
								AbstractInsnNode insnNode = mn.instructions.get(i);
								if (ClassUtils.getOpcode(insnNode.getOpcode()).contains("RETURN")) {
									mn.instructions.remove(insnNode);
									break;
								}
							}

							before.put(mn.name + mn.desc, mn);
						} else if (After.class.getName().equals(typeName)) {
							after.put(mn.name + mn.desc, mn);
						} else if (Replace.class.getName().equals(typeName)) {

							replace.put(mn.name + mn.desc, mn);
						} else if (Line.class.getName().equals(typeName)) {
							int lineNum = -0xff;
							boolean after = false;
							for (Object obj : an.values) {
								if (obj instanceof Integer) {
									lineNum = (Integer) obj;
								} else if (obj instanceof Boolean) {
									after = (Boolean) obj;
								}
							}

							// Still relevant, unfortunately :(
							// But i guess it makes sense after all :)
							for (int i = mn.instructions.toArray().length - 1; i > -1; i--) {
								AbstractInsnNode insnNode = mn.instructions.get(i);
								if (ClassUtils.getOpcode(insnNode.getOpcode()).contains("RETURN")) {
									mn.instructions.remove(insnNode);
									break;
								}
							}

							if (lineNum == -0xff) {
								continue;
							}

							if (!after && lineNum > 0) {
								lineNum *= -1;
							}

							line.put(mn.name + mn.desc, (new HashMap.SimpleEntry<Integer, MethodNode>(lineNum, mn)));
						}
					}
				}
			}
//			ClassWriter cw = new ClassWriter(0);
//			cn.accept(cw);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public byte[] process(InputStream in, String className) {
		try {
			this.targetClassName = className;
			ClassReader cr = new ClassReader(in);
			ClassNode cn = new ClassNode(Opcodes.ASM5);
			cr.accept(cn, 0);

			for (MethodNode mn : cn.methods) {
				if (before.containsKey(mn.name + mn.desc)) {
					MethodNode inject = before.get(mn.name + mn.desc);

                    MethodNode newInject = new MethodNode();
                    inject.accept(new ASMMethodRemapper(newInject, this.originalClassName, this.targetClassName));
                    inject = newInject;

					for (int i = 0; i < mn.instructions.toArray().length; i++) {
						AbstractInsnNode insnNode = mn.instructions.get(i);
						if (insnNode instanceof LabelNode) {
							mn.instructions.insertBefore(mn.instructions.get(i), inject.instructions);
							break;
						}
					}

					mn.instructions.resetLabels();
                    if (inject.tryCatchBlocks != null) {
                        mn.tryCatchBlocks.addAll(inject.tryCatchBlocks);
                    }
                    if (inject.exceptions != null) {
                        mn.exceptions.addAll(inject.exceptions);
                    }
				} else if (after.containsKey(mn.name + mn.desc)) {
					MethodNode inject = after.get(mn.name + mn.desc);

                    MethodNode newInject = new MethodNode();
                    inject.accept(new ASMMethodRemapper(newInject, this.originalClassName, this.targetClassName));
                    inject = newInject;

                    for (int i = mn.instructions.toArray().length - 1; i > -1; i--) {
						AbstractInsnNode insnNode = mn.instructions.get(i);
						if (ClassUtils.getOpcode(insnNode.getOpcode()).contains("RETURN")) {
							mn.instructions.insertBefore(mn.instructions.get(i), inject.instructions);
							break;
						}
					}

					mn.instructions.resetLabels();
                    if (inject.tryCatchBlocks != null) {
                        mn.tryCatchBlocks.addAll(inject.tryCatchBlocks);
                    }
                    if (inject.exceptions != null) {
                        mn.exceptions.addAll(inject.exceptions);
                    }
				} else if (replace.containsKey(mn.name + mn.desc)) {
					MethodNode inject = replace.get(mn.name + mn.desc);

                    MethodNode newInject = new MethodNode();
                    inject.accept(new ASMMethodRemapper(newInject, this.originalClassName, this.targetClassName));
                    inject = newInject;

                    mn.instructions.clear();
                    if (inject.tryCatchBlocks != null) {
                        mn.tryCatchBlocks.clear();
                    }
                    if (inject.exceptions != null) {
                        mn.exceptions.clear();
                    }
					mn.instructions.add(inject.instructions);
                    if (inject.tryCatchBlocks != null) {
                        mn.tryCatchBlocks.addAll(inject.tryCatchBlocks);
                    }
                    if (inject.exceptions != null) {
                        mn.exceptions.addAll(inject.exceptions);
                    }
				} else if (line.containsKey(mn.name + mn.desc)) {
					Map.Entry<Integer, MethodNode> entry = line.get(mn.name + mn.desc);
					int lineNum = entry.getKey();
					boolean after = lineNum > 0;
					MethodNode inject = entry.getValue();
					boolean injected = false;

                    MethodNode newInject = new MethodNode();
                    inject.accept(new ASMMethodRemapper(newInject, this.originalClassName, this.targetClassName));
                    inject = newInject;

                    if (lineNum < 0) {
						lineNum *= -1;
					}

					for (AbstractInsnNode insn : mn.instructions.toArray()) {
						if (insn instanceof LineNumberNode && ((LineNumberNode) insn).line >= lineNum) {
							if (after) {
								mn.instructions.insert(insn, inject.instructions);
							} else {
								mn.instructions.insertBefore(insn, inject.instructions);
							}
							injected = true;
							break;
						}
					}

					// If the line number was not found, let's resort to @After or @Before's behavior.
					// Unless it was before!
					if (!injected) {
						if (after) {
							for (int i = mn.instructions.toArray().length - 1; i > -1; i--) {
								AbstractInsnNode insnNode = mn.instructions.get(i);
								if (ClassUtils.getOpcode(insnNode.getOpcode()).contains("RETURN")) {
									mn.instructions.insertBefore(mn.instructions.get(i), inject.instructions);
									break;
								}
							}
						} else {
							for (int i = 0; i < mn.instructions.toArray().length; i++) {
								AbstractInsnNode insnNode = mn.instructions.get(i);
								if (insnNode instanceof LabelNode) {
									mn.instructions.insertBefore(mn.instructions.get(i), inject.instructions);
									break;
								}
							}
						}
					}

					mn.instructions.resetLabels();
                    if (inject.tryCatchBlocks != null) {
                        mn.tryCatchBlocks.addAll(inject.tryCatchBlocks);
                    }
                    if (inject.exceptions != null) {
                        mn.exceptions.addAll(inject.exceptions);
                    }
				}

			}

//			cn.accept(new TraceClassVisitor(new PrintWriter(System.out)));

			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			if (postVisitor != null) {
				// this way any visitors that need to take place, can be done so here.
				// however this does not support extra visitors that *actually* need the delegated
				// class visitor underneath them.  It needs to still get to ClassWriter....right?
				this.postVisitor.setCv(cw);
				cn.accept(postVisitor);
			} else {
				cn.accept(cw);
			}
//          FileOutputStream fos = new FileOutputStream(className + "23.class");
//			fos.write(cw.toByteArray());
//			fos.close();
			return cw.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
