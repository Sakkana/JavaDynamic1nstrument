package org.Ag3nt;

import java.lang.instrument.Instrumentation;

public class Ag3ntMain {
	public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("【Ag3ntMain: premain】 agent args: " + agentArgs);
        // add a transform
        inst.addTransformer(new Ag3ntTransform(), true);
	}
}
