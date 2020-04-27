package serp.bytecode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import serp.bytecode.lowlevel.ConstantPool;
import serp.bytecode.visitor.BCVisitor;
import serp.bytecode.visitor.VisitAcceptor;

/**
 * An opcode in a method of a class.
 *
 * @author Abe White
 */
public class Instruction extends CodeEntry implements BCEntity, VisitAcceptor {
    private Code _owner = null;
    private int _opcode = Constants.NOP;

    Instruction(Code owner) {
        _owner = owner;
    }

    Instruction(Code owner, int opcode) {
        _owner = owner;
        _opcode = opcode;
    }

    /**
     * Return the code block that owns this instruction.
     * 
     * @return the code block that owns this instruction
     */
    public Code getCode() {
        return _owner;
    }

    /**
     * Return the name of this instruction.
     * 
     * @return the name of this instruction
     */
    public String getName() {
        return Constants.OPCODE_NAMES[_opcode];
    }

    /**
     * Return the opcode this instruction represents.
     * 
     * @return the opcode this instruction represents
     */
    public int getOpcode() {
        return _opcode;
    }

    /**
     * Set the opcode this instruction represents. For internal use only.
     *
     * @param opcode the opcode to set
     * @return this instruction, for method chaining
     */
    Instruction setOpcode(int opcode) {
        _opcode = opcode;
        return this;
    }

	/**
	 * Return the index in the method code byte block at which this opcode starts.
	 * Note that this information may be out of date if the code block has been
	 * modified since last read/written.
	 * 
	 * @return the index in the method code byte block at which this opcode starts
	 */
    public int getByteIndex() {
        if (_owner != null)
            return _owner.getByteIndex(this);
        return 0;
    }

    /**
     * Notification that a change has been made to this instruction that
     * alters the structure of the code block, invalidating byte indexes.
     */
    void invalidateByteIndexes() {
        if (_owner != null)
            _owner.invalidateByteIndexes();
    }

    /**
     * Return the line number of this instruction, or null if none. This
     * method is subject to the validity constraints of {@link #getByteIndex}.
     *
     * @see LineNumberTable#getLineNumber(Instruction)
     * 
     * @return the line number of this instruction, or null if none
     */
    public LineNumber getLineNumber() {
        LineNumberTable table = _owner.getLineNumberTable(false);
        if (table == null)
            return null;
        return table.getLineNumber(this);
    }

    /**
     * Return the length in bytes of this opcode, including all arguments.
     * For many opcodes this method relies on an up-to-date byte index.
     * 
     * @return the length in bytes of this opcode, including all arguments
     */
    int getLength() {
        return 1;
    }

	/**
	 * Return the logical number of stack positions changed by this instruction. In
	 * other words, ignore weirdness with longs and doubles taking two stack
	 * positions.
	 * 
	 * @return the logical number of stack positions changed by this instruction
	 */
    public int getLogicalStackChange() {
        return getStackChange();
    }

	/**
	 * Return the number of stack positions this instruction pushes or pops during
	 * its execution.
	 *
	 * @return 0 if the stack is not affected by this instruction, a positive number
	 *         if it pushes onto the stack, and a negative number if it pops from
	 *         the stack
	 */
    public int getStackChange() {
        return 0;
    }

    /**
     * Instructions are equal if their opcodes are the same. Subclasses
     * should override this method to perform a template comparison:
     * instructions should compare equal to other instructions of the same
     * type where the data is either the same or the data is unset.
     * 
     * @param other the instruction to compare
     * @return true if their opcodes are the same
     */
    public boolean equalsInstruction(Instruction other) {
        if (other == this)
            return true;
        return other.getOpcode() == getOpcode();
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
    }

    void invalidate() {
        _owner = null;
    }

    /**
     * Copy the given instruction data.
     * 
     * @param orig the instruction data
     */
    void read(Instruction orig) {
    }

    /**
     * Read the arguments for this opcode from the given stream.
     * This method should be overridden by opcodes that take arguments.
     * 
     * @param in the input stream
     */
    void read(DataInput in) throws IOException {
    }

    /**
     * Write the arguments for this opcode to the given stream.
     * This method should be overridden by opcodes that take arguments.
     * 
     * @param out the output stream
     */
    void write(DataOutput out) throws IOException {
    }
}
