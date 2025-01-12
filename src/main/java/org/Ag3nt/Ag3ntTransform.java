package org.Ag3nt;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import org.Ag3nt.ASMFramework.JavaASMCoreAPIFrameWork;
import org.Ag3nt.ASMFramework.JavaASMTreeAPIFrameWork;

public class Ag3ntTransform implements ClassFileTransformer{
    private String mode;
    JavaASMCoreAPIFrameWork coreFrameWork;
    JavaASMTreeAPIFrameWork treeFrameWork;

    Ag3ntTransform (String mode) {
        // mode: Core or Tree or Mix
        this.mode = new String(mode);
        if (mode.equals("Tree")) {
            // Tree API Processing Framework
            treeFrameWork = new JavaASMTreeAPIFrameWork();
        } else if (mode.equals("Core")) {
            // Core API Processing Framework
            coreFrameWork = new JavaASMCoreAPIFrameWork();
        } else {
            System.out.println("Could not be here! System exit.");
            System.exit(-1);
        }
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (mode.equals("Tree")) {
            // Tree API Processing Framework
            return treeFrameWork.runTree(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        } else if (mode.equals("Core")) {
            // Core API Processing Framework
            return coreFrameWork.runCore(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        }

        return null;
    }
}
