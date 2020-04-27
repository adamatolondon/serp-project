package serp.bytecode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import serp.bytecode.lowlevel.ConstantPool;
import serp.bytecode.visitor.BCVisitor;
import serp.bytecode.visitor.VisitAcceptor;

/**
 * A line number corresponds to a sequence of opcodes that map logically
 * to a line of source code.
 *
 * @author Abe White
 */
public class LineNumber implements Comparable, InstructionPtr, BCEntity,
    VisitAcceptor {
    private int _line = 0;
    private LineNumberTable _owner = null;
    InstructionPtrStrategy _target = new InstructionPtrStrategy(this);

    LineNumber(LineNumberTable owner) {
        _owner = owner;
    }

    LineNumber(LineNumberTable owner, int startPc) {
        this(owner);
        setStartPc(startPc);
    }

    /**
     * Line numbers are stored in a {@link LineNumberTable}.
     * 
     * @return the {@link LineNumberTable}
     */
    public LineNumberTable getTable() {
        return _owner;
    }

    void invalidate() {
        _owner = null;
    }

    /**
     * Return source line number.
     * 
     * @return the source line number
     */
    public int getLine() {
        return _line;
    }

    /**
     * Set the source line number.
     * 
     * @param lineNumber the source line number
     */
    public void setLine(int lineNumber) {
        _line = lineNumber;
    }

    /**
     * Return the instruction marking the beginning of this line.
     * 
     * @return the instruction marking the beginning of this line
     */
    public Instruction getStart() {
        return _target.getTargetInstruction();
    }

    /**
     * Return the index into the code byte array at which this line starts.
     * 
     * @return the index into the code byte array at which this line starts
     */
    public int getStartPc() {
        return _target.getByteIndex();
    }

    /**
     * Set the index into the code byte array at which this line starts.
     * 
     * @param startPc the index into the code byte array at which this line starts
     */
    public void setStartPc(int startPc) {
        _target.setByteIndex(startPc);
    }

    /**
     * Set the {@link Instruction} marking the beginning this line.
     * The instruction must already be a part of the method.
     * 
     * @param instruction the {@link Instruction} marking the beginning this line
     */
    public void setStart(Instruction instruction) {
        _target.setTargetInstruction(instruction);
    }

    public void updateTargets() {
        _target.updateTargets();
    }

    public void replaceTarget(Instruction oldTarget, Instruction newTarget) {
        _target.replaceTarget(oldTarget, newTarget);
    }

    public Project getProject() {
        return _owner.getProject();
    }

    public ConstantPool getPool() {
        return _owner.getPool();
    }

    public ClassLoader getClassLoader() {
        return _owner.getClassLoader();
    }

    public boolean isValid() {
        return _owner != null;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterLineNumber(this);
        visit.exitLineNumber(this);
    }

    public int compareTo(Object other) {
        if (!(other instanceof LineNumber))
            return -1;

        LineNumber ln = (LineNumber) other;
        if (getStartPc() == ln.getStartPc())
            return 0;
        if (getStartPc() < ln.getStartPc())
            return -1;
        return 1;
    }

    void read(DataInput in) throws IOException {
        setStartPc(in.readUnsignedShort());
        setLine(in.readUnsignedShort());
    }

    void write(DataOutput out) throws IOException {
        out.writeShort(getStartPc());
        out.writeShort(getLine());
    }

    public Code getCode() {
        return _owner.getCode();
    }
}
