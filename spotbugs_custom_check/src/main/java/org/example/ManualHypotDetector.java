package org.example;

import edu.umd.cs.findbugs.OpcodeStack;
import org.apache.bcel.Const;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

import static edu.umd.cs.findbugs.OpcodeStack.Item.*;

public class ManualHypotDetector extends OpcodeStackDetector {
    /**
     * Value is a square of local variable or parameter
     */
    private static final @SpecialKind int SQUARE = 100;
    /**
     * Value is a sum of two squares
     */
    private static final @SpecialKind int SUM_SQUARES = 101;
    private final BugReporter bugReporter;

    public ManualHypotDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void afterOpcode(int seen) {
        @SpecialKind int kind = NOT_SPECIAL;
        OpcodeStack stack = getStack();
        if (seen == Const.DMUL) {
            OpcodeStack.Item item1 = stack.getStackItem(0);
            OpcodeStack.Item item2 = stack.getStackItem(1);
            int reg1 = item1.getRegisterNumber();
            int reg2 = item2.getRegisterNumber();
            if (reg1 == reg2 && reg1 != -1) {
                kind = SQUARE;
            }
        }
        if (seen == Const.DADD) {
            OpcodeStack.Item item1 = stack.getStackItem(0);
            OpcodeStack.Item item2 = stack.getStackItem(1);
            if (item1.getSpecialKind() == SQUARE && 
                    item2.getSpecialKind() == SQUARE) {
                kind = SUM_SQUARES;
            }
        }
        super.afterOpcode(seen);
        if (kind != NOT_SPECIAL) {
            stack.getStackItem(0).setSpecialKind(kind);
        }
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Const.INVOKESTATIC && getClassConstantOperand().equals("java/lang/Math") &&
                getNameConstantOperand().equals("sqrt") &&
                getStack().getStackItem(0).getSpecialKind() == SUM_SQUARES) {
            BugInstance bug = new BugInstance(this, "MATH_USE_HYPOT", NORMAL_PRIORITY)
                    .addClassAndMethod(this)
                    .addSourceLine(this, getPC());
            bugReporter.reportBug(bug);
        }
    }
}
