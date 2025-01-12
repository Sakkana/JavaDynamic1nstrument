package org.Ag3nt.ASMFramework;

import org.Ag3nt.Ag3ntClassVisitor.HelloWorldClassVisitorAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.security.ProtectionDomain;
import java.util.ArrayList;

public class JavaASMCoreAPIFrameWork {
    private ArrayList<String> targetClasses;

    public JavaASMCoreAPIFrameWork() {
        targetClasses = new ArrayList<String>();
        targetClasses.add("HelloWorld");
        targetClasses.add("NotHelloWorld");
    }

    public byte[] runCore(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        // System.out.println("【Ag3ntTransform: transform】className:" + className);

        if (className.equals(targetClasses.get(0))) {
            System.out.println("【Ag3ntTransform: transform】Hacking Class:" + className);
            try {
                /**
                 * modify the byte code
                 */

                // read the class byte code
                ClassReader classReader = new ClassReader(classfileBuffer);

                // create a writer
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);

                // visit the class byte code
                System.out.println("【Ag3ntTransform: transform】before helloWorldClassVisitorAdapter");
                ClassVisitor helloWorldClassVisitorAdapter = new HelloWorldClassVisitorAdapter(Opcodes.ASM9, classWriter);
                System.out.println("【Ag3ntTransform: transform】after helloWorldClassVisitorAdapter");

                // accept the modification of the class byte code
                System.out.println("【Ag3ntTransform: transform】before helloWorldClassVisitorAdapter accept");
                classReader.accept(helloWorldClassVisitorAdapter, ClassReader.EXPAND_FRAMES);
                System.out.println("【Ag3ntTransform: transform】after helloWorldClassVisitorAdapter accept");

                // return the modified class byte code
                return classWriter.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // no need to modify, return the original byte code
        return classfileBuffer;
    }
}
