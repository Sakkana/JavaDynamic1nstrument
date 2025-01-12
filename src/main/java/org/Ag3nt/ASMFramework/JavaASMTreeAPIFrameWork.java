package org.Ag3nt.ASMFramework;

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
            printMethodInfo(mn);

            // find the static method
            if (getAccessFlags(mn.access).contains("static") && mn.name.contains("m1")) {
                // createNewMethod(classNode);
                modifyStaticMethod(mn);
                break;
            }
        }

        /**
         * TODO: end modeification
         */

        // generate new byte code
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        // after accept, transform a ClassNode into bytecode
        classNode.accept(classWriter);

        try {
            byte[] bytes2 = classWriter.toByteArray();
            File targetFile = new File("./new_class.class");
            FileUtils.writeByteArrayToFile(targetFile, bytes2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("===== Transform succeed, start executing!✔ =====");

        return classWriter.toByteArray();
    }

    private MethodNode modifyStaticMethod(MethodNode methodNode) {
        System.out.println(methodNode.instructions);
        for (int i = 0; i < methodNode.instructions.size(); i++) {
            System.out.println(i + ": " + methodNode.instructions.get(i));
        }

        // 清除原有的方法体
        methodNode.instructions.clear();

        // 将字符串 "static method m1 - " 加载到操作数栈顶
        methodNode.visitLdcInsn(" new string - ");

        // 返回 String 类型的结果
        methodNode.visitInsn(Opcodes.ARETURN);

        // 重新计算方法的最大栈深度和局部变量表大小
        methodNode.visitMaxs(1, 0); // 操作数栈最大深度为 1，局部变量表大小为 0
        methodNode.visitEnd();

        for (int i = 0; i < methodNode.instructions.size(); i++) {
            System.out.println(i + ": " + methodNode.instructions.get(i));
        }

        // 修改方法签名，将返回类型改为 String
        // methodNode.desc = "(I)Ljava/lang/String;";
        return null;
    }

    private void createNewMethod(ClassNode classNode) {
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

    private void printMethodInfo(MethodNode mn) {
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

    private void printClassInfo(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            System.out.println("  Method: " + mn.name + " " + mn.desc);
            System.out.println("  Access Flags: " + getAccessFlags(mn.access));
        }
    }

    private String getAccessFlags(int access) {
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
