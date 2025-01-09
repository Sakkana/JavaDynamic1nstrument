package org.Ag3nt;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.Ag3nt.Ag3ntClassVisitor.HelloWorldClassVisitorAdapter;

public class Ag3ntTransform implements ClassFileTransformer{
    private ArrayList<String> targetClasses;

    Ag3ntTransform () {
        targetClasses = new ArrayList<String>();
        targetClasses.add("HelloWorld");
        targetClasses.add("NotHelloWorld");
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
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
                ClassVisitor helloWorldClassVisitorAdapter = new HelloWorldClassVisitorAdapter(Opcodes.ASM9, classWriter);

                // accept the modification of the class byte code
                classReader.accept(helloWorldClassVisitorAdapter, ClassReader.EXPAND_FRAMES);

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
