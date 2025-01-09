package org.Ag3nt.Ag3ntClassVisitor;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.Ag3nt.Ag3ntMethodVisitor.HelloWorldMethodVisitorAdapter;

public class HelloWorldClassVisitorAdapter extends ClassVisitor{
    public HelloWorldClassVisitorAdapter(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        /**
         * must init the parent class, or it will throw:
         * Error: LinkageError occurred while loading main class xxx
         *         java.lang.UnsupportedClassVersionError: xxx (class file version 0.0) was compiled with an invalid major version
         */
        super.visit(version, access, name, signature, superName, interfaces);
        System.out.println("【HelloWorldClassVisitorAdapter: visit】inside HelloWorldClassVisitorAdapter");
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // obtain the next method, a part of chain invocation
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

        // hack the specified method
        if("main".equals(name)) {
            return new HelloWorldMethodVisitorAdapter(Opcodes.ASM9, methodVisitor);
        }

        return methodVisitor;
    }

}
