package org.Ag3nt.Ag3ntMethodVisitor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class HelloWorldMethodVisitorAdapter extends MethodVisitor {
    private MethodVisitor methodVisitor = super.mv;
    private String lastString = null;

    public HelloWorldMethodVisitorAdapter(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
        System.out.println("【MethodVisitor】 init");
    }

    /**
     * ASM fist invoke visitCode at the beginning of analyzing a Method
     */
    @Override
    public void visitCode() {
        System.out.println("【MethodVisitor: visitCode】");
        // insert a statement before the start of method
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        methodVisitor.visitLdcInsn("【💀️ instrumented】visiting class: HelloWorld, method: main");
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        // return to the parent, processing the remaining methods
        super.visitCode();
    }
//
//    /**
//     * Visiting a normal instruction
//     * @param opcode
//     */
//    @Override
//    public void visitInsn(int opcode) {
//        return;
//    }
//
    /**
     * Visiting a Function Call
     * @param opcode
     * @param owner
     * @param name
     * @param descriptor
     * @param isInterface
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        System.out.println("【MethodVisitor: visitMethodInsn】" + name + " " + descriptor);

        String targetMethodOwner ="java/io/PrintStream";
        String targetMethodName ="println";
        String targetMethodDescriptor ="(Ljava/lang/String;)V";

        // identify sout function call
        if (opcode == Opcodes.INVOKEVIRTUAL && checkTargetMethod(targetMethodOwner, targetMethodName, targetMethodDescriptor, owner, name, descriptor)) {
            if (lastString != null && !lastString.equals("Hello")) {

                methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                methodVisitor.visitLdcInsn("【☠️】hack from method level: println");
                methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

                lastString = null;

                // subside the original print statement
                return;
            }
        }

        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    /**
     * Visiting a constant (String, Numeric, Class Object)
     * @param value
     */
    @Override
    public void visitLdcInsn(Object value) {
        System.out.println("【MethodVisitor: visitLdcInsn】" + value.toString());
        if (value != null && "Hello".equals(value)) {
            methodVisitor.visitLdcInsn(value + " - appendix in visitLdcInsn");
            lastString = (String) value;
            System.out.println("modifying " + lastString + " to " + value + " - appendix in visitLdcInsn");
            return;
        }

        super.visitLdcInsn(value);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        System.out.println("【MethodVisitor: visitNaxs】");
        methodVisitor.visitMaxs(2, 1);
    }


    boolean checkTargetMethod(String guessOwner, String guessName, String guessDescriptor, String owner, String name, String descriptor) {
        return guessOwner.equals(owner) && guessName.equals(name) && guessDescriptor.equals(descriptor);
    }

}
