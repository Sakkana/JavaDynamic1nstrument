package org.Ag3nt.ASMFramework;

import com.sun.source.tree.ReturnTree;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.Opcodes;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.ClassFileTransformer;
import java.util.Objects;

import org.Ag3nt.Utils.classFilter;

public class JavaASMTreeAPIFrameWork {

    private ArrayList<String> targetClasses;
    private ArrayList<String> targetMethods;

    public JavaASMTreeAPIFrameWork() {
        // init class names
        targetClasses = new ArrayList<String>();
        targetClasses.add("HelloWorld");
        targetClasses.add("NotHelloWorld");

        // init method names
        targetMethods = new ArrayList<String>();
        targetMethods.add("main");
        targetMethods.add("test");
    }

    public byte[] runTree(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (!classFilter.isInterestClass(className)) {
            // TODO: difference between return null and bytecode
            return classfileBuffer;
        }

        // read the bytecode
        ClassReader classReader = new ClassReader(classfileBuffer);

        // metadata of the class
        ClassNode classNode = new ClassNode();
        // after accept, transform a .class into concrete field inside a ClassNode
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        /**
         * TODO: start modificatiom
         */

        // modify the class
        classNode.interfaces.add("java/lang/Cloneable");
        // classNode.interfaces.add("org.objectweb.asm.tree.ClassNode"); -> not in class file of HelloWorld (not packaged)

        // get method information
        for (MethodNode mn : classNode.methods) {
            // printMethodInfo(mn);
            if (mn.name.equals("main") && mn.desc.equals("([Ljava/lang/String;)V")) {
                instrumentMain(mn);
            }
        }

        /**
         * TODO: end modification
         */

        // generate new byte code
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        // after accept, transform a ClassNode into bytecode
        classNode.accept(classWriter);

        try {
            byte[] bytes2 = classWriter.toByteArray();
            File targetFile = new File("./bytecode/" + className + ".class");
            System.out.println("writing " + targetFile);
            FileUtils.writeByteArrayToFile(targetFile, bytes2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("===== Transform 【" + className + "】 succeed, start executing!✔ =====");

        return classWriter.toByteArray();
    }

    private void instrumentMain(MethodNode methodNode) {
        System.out.println("I'm " + methodNode.name + " " + methodNode.desc);



        // 遍历方法中的所有指令
        for (int i = 0; i < methodNode.instructions.size(); i++) {
            AbstractInsnNode insn = methodNode.instructions.get(i);
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) insn;
                if (methodInsnNode.getOpcode() == Opcodes.INVOKESTATIC) {
                    System.out.println("Found static method call: " + methodInsnNode.owner + "." + methodInsnNode.name + methodInsnNode.desc);
                    // i = Ag3ntinsertBefore(methodNode.instructions, methodInsnNode, i);

                }
            }
        }
    }

    private void modifyIns(MethodInsnNode methodInsnNode) {

    }

    private static int Ag3ntinsertBefore(InsnList instructions, AbstractInsnNode insn, int i) {
        // 创建打印语句的指令
        InsnList printInsnList = new InsnList();
        printInsnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        printInsnList.add(new LdcInsnNode("👻 Before static method call 👻"));
        printInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        // 将打印语句插入到静态方法调用前面
        instructions.insertBefore(insn, printInsnList);

        return i + 3;   // 回到原来的指令，消除长度增加带来的影响
    }

    /**
     * 有 bug，弃用
     */
    private MethodNode modifyStaticMethod (MethodNode methodNode){
        System.out.println(methodNode.name);
        for (int i = 0; i < methodNode.instructions.size(); i++) {
            System.out.println(i + ": " + methodNode.instructions.get(i));
        }

        /**
         * TODO: modify the method 【return type】 and 【return value】
         */

        // 1 params (int), return type
        // System.out.println(methodNode.desc);
        methodNode.desc = "(I)Lcom/diy/HelloWorld/utils/Ag3ntStringBase;";

        // traverse all the instructions
        for (int i = 0; i < methodNode.instructions.size(); i++) {
            Object insn = methodNode.instructions.get(i);
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) insn;
                if (methodInsnNode.owner.equals("com/diy/HelloWorld/utils/Ag3ntString")) {
                    if (methodInsnNode.name.equals("<init>")) {
                        System.out.println("modifying constructor: " + methodInsnNode.owner + " " + methodInsnNode.name + " " + methodInsnNode.desc);
                    }
                    methodInsnNode.owner = "com/diy/HelloWorld/utils/Ag3ntStringBase";
                }
            } else if (insn instanceof LdcInsnNode) {
                Object cst = ((LdcInsnNode) insn).cst;
                System.out.println("ldc: " + (String) cst);
                if (cst instanceof String && "com/diy/HelloWorld/utils/Ag3ntString".equals(cst)) {
                    ((LdcInsnNode) insn).cst = "com/diy/HelloWorld/utils/Ag3ntStringBase";
                }
            }
        }

        AbstractInsnNode returnInsn = methodNode.instructions.getLast();
        if (returnInsn.getOpcode() == Opcodes.ARETURN) {
            // 在返回指令之前插入检查转换指令
            System.out.println(returnInsn.getOpcode());
            // methodNode.instructions.insertBefore(returnInsn, new TypeInsnNode(Opcodes.CHECKCAST, "TargetType"));
        }

        methodNode.visitEnd();

        return null;
    }

    private int calculateMaxStack (InsnList instructions){
        // 实际的计算逻辑应根据字节码指令进行
        int maxStack = 0;
        int currentStack = 0;
        for (AbstractInsnNode insn : instructions) {
            int stackChange = getStackChange(insn);
            currentStack += stackChange;
            if (currentStack > maxStack) {
                maxStack = currentStack;
            }
        }
        return maxStack;
    }

    private int calculateMaxLocals (InsnList instructions){
        // 实际的计算逻辑应根据字节码指令进行
        int maxLocals = 0;
        for (AbstractInsnNode insn : instructions) {
            int localsChange = getLocalsChange(insn);
            if (localsChange > maxLocals) {
                maxLocals = localsChange;
            }
        }
        return maxLocals;
    }

    private int getStackChange (AbstractInsnNode insn){
        // 根据不同指令类型返回栈变化量
        switch (insn.getOpcode()) {
            case Opcodes.ICONST_1:
                return 1;
            case Opcodes.ACONST_NULL:
                return 1;
            case Opcodes.INVOKEVIRTUAL:
                // return -((MethodInsnNode) insn).getArguments().length + 1;
                // 其他指令根据实际情况添加
            default:
                return 0;
        }
    }

    private int getLocalsChange (AbstractInsnNode insn){
        // 根据不同指令类型返回局部变量表变化量，这里简单示例
        switch (insn.getOpcode()) {
            case Opcodes.ILOAD:
                return 1;
            case Opcodes.ISTORE:
                return 1;
            // 其他指令根据实际情况添加
            default:
                return 0;
        }
    }

    private void createNewMethod (ClassNode classNode){
        MethodNode newMethod = new MethodNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "static_method_m0", "(I)I", null, null);
        newMethod.visitCode();

        // 方法逻辑：将传入的 int 参数加载到操作数栈顶
        newMethod.visitVarInsn(Opcodes.ILOAD, 0);
        // 将常量 2 加载到操作数栈顶
        newMethod.visitIntInsn(Opcodes.BIPUSH, 2);
        // 执行 int 类型的加法操作，将栈顶两个 int 值相加，结果存回栈顶
        newMethod.visitInsn(Opcodes.IADD);
        // 方法返回 int 类型的结果
        newMethod.visitInsn(Opcodes.IRETURN);
        newMethod.visitMaxs(2, 1); // 指定操作数栈最大深度为 2，局部变量表大小为 1
        newMethod.visitEnd();

        // 将新方法添加到类节点中
        classNode.methods.add(newMethod);
    }

    private void printMethodInfo (MethodNode mn){
        String methodName = mn.name;
        String methodDescriptor = mn.desc;
        String methodSignature = mn.signature;

        // print everything
        System.out.println("===== basic info for method: " + methodName + " =====");
        System.out.println("  name: " + methodName);
        System.out.println("  desc: " + methodDescriptor);
        System.out.println("  sig: " + methodSignature);
        System.out.println("  access flags: " + getAccessFlags(mn.access));
        System.out.println("  params: TBD");

//        for (ParameterNode param : mn.parameters) {
//            System.out.println("  param: " + param.name + " " + param.access);
//        }
    }

    private void printClassInfo (ClassNode cn){
        for (MethodNode mn : cn.methods) {
            System.out.println("  Method: " + mn.name + " " + mn.desc);
            System.out.println("  Access Flags: " + getAccessFlags(mn.access));
        }
    }

    private String getAccessFlags ( int access){
        StringBuilder sb = new StringBuilder();
        if ((access & Opcodes.ACC_PUBLIC) != 0) sb.append("public ");
        if ((access & Opcodes.ACC_PRIVATE) != 0) sb.append("private ");
        if ((access & Opcodes.ACC_PROTECTED) != 0) sb.append("protected ");
        if ((access & Opcodes.ACC_STATIC) != 0) sb.append("static ");
        if ((access & Opcodes.ACC_FINAL) != 0) sb.append("final ");
        if ((access & Opcodes.ACC_SYNCHRONIZED) != 0) sb.append("synchronized ");
        if ((access & Opcodes.ACC_NATIVE) != 0) sb.append("native ");
        if ((access & Opcodes.ACC_ABSTRACT) != 0) sb.append("abstract ");
        if ((access & Opcodes.ACC_STRICT) != 0) sb.append("strictfp ");
        return sb.toString().trim();
    }
}
