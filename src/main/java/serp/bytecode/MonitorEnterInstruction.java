package serp.bytecode;

import serp.bytecode.visitor.BCVisitor;

/**
 * The <code>monitorenter</code> instruction.
 *
 * @author Abe White
 */
public class MonitorEnterInstruction extends MonitorInstruction {
    MonitorEnterInstruction(Code owner) {
        super(owner, Constants.MONITORENTER);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterMonitorEnterInstruction(this);
        visit.exitMonitorEnterInstruction(this);
    }
}
