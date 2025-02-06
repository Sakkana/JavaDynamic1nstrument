package org.Ag3nt.Utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Function;

public class APIConsumerGenerator extends APIConsumer{
    static public InsnList paramsConsumeInstSeriesGenV0(String descriptor) {
        List<String> parameterTypes = getParameterTypes(descriptor);

        InsnList insnList = new InsnList();

        for (String t: parameterTypes) {
            insnList.add(consumerRouter(t));
        }

        return insnList;
    }

    static private MethodInsnNode consumerRouter(String type) {
        switch (type) {
            case "I":
                return consumeBasicIntInstGen();
            default:
                break;
        }

        throw new RuntimeException("type not found");
    }

    static private List<String> getParameterTypes(String descriptor) {
        // 丢弃返回值部分
        int start = descriptor.indexOf('(') + 1;
        int end = descriptor.indexOf(')');
        String parameterPart = descriptor.substring(start, end);

        // 解析参数类型
        List<String> parameterTypes = new ArrayList<>();
        int index = 0;
        while (index < parameterPart.length()) {
            char c = parameterPart.charAt(index);
            switch (c) {
                case '[':
                    // 处理数组类型
                    StringBuilder arrayType = new StringBuilder();
                    while (parameterPart.charAt(index) == '[') {
                        arrayType.append('[');
                        index++;
                    }
                    c = parameterPart.charAt(index);
                    if (c == 'L') {
                        int semiIndex = parameterPart.indexOf(';', index);
                        arrayType.append(parameterPart, index, semiIndex + 1);
                        index = semiIndex + 1;
                    } else {
                        arrayType.append(c);
                        index++;
                    }
                    parameterTypes.add(arrayType.toString());
                    break;
                case 'L':
                    // 处理引用类型
                    int semiIndex = parameterPart.indexOf(';', index);
                    String referenceType = parameterPart.substring(index, semiIndex + 1);
                    parameterTypes.add(referenceType);
                    index = semiIndex + 1;
                    break;
                default:
                    // 处理基本类型
                    String basicType = String.valueOf(c);
                    parameterTypes.add(basicType);
                    index++;
                    break;
            }
        }

        return parameterTypes;
    }

    static public InsnList paramsConsumeInstSeriesGenMock(String signature) {
        InsnList insnList = new InsnList();
        insnList.add(consumeBasicIntInstGen());
        insnList.add(consumeBasicIntInstGen());
        return insnList;
    }

    static public MethodInsnNode consumeBasicIntInstGen() {
        return new MethodInsnNode(Opcodes.INVOKESTATIC,
                "org/Ag3nt/Utils/APIConsumerGenerator",
                "consumeBasicInt",
                "(I)V",
                false);
    }
}
