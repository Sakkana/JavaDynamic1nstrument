package org.Ag3nt.Utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;

public class APIConsumerGenerator extends APIConsumer{
    static public MethodInsnNode consumeBasicIntInstGen() {
        return new MethodInsnNode(Opcodes.INVOKESTATIC,
                "org/Ag3nt/Utils/APIConsumerGenerator",
                "consumeBasicInt",
                "(I)V",
                false);
    }
}
