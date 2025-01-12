package org.Ag3nt;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;

public class Ag3ntMain {
    private static final String []modes = new String[] {
            "Core",
            "Tree",
            "Mix"
    };

    public Ag3ntMain() {

    }

    private static String getModeByIndex(int index) {
        return modes[index];
    }

	public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("【Ag3ntMain: premain】 agent args: " + agentArgs);
        // add a transform
        int index = 1;
        if (index > 2) {
            System.out.println("【Ag3ntMain: premain】 mode index out of range");
            System.exit(-1);
        }
        String mode = getModeByIndex(index);
        inst.addTransformer(new Ag3ntTransform(mode), true);
	}
}
