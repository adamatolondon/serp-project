package serp.bytecode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import serp.bytecode.lowlevel.ClassEntry;
import serp.bytecode.lowlevel.ConstantPool;
import serp.bytecode.visitor.BCVisitor;
import serp.bytecode.visitor.VisitAcceptor;
import serp.util.Strings;

/**
 * Represents a <code>try {} catch() {}</code> statement in bytecode.
 *
 * @author Abe White
 */
public class ExceptionHandler implements InstructionPtr, BCEntity,
    VisitAcceptor {
    private int _catchIndex = 0;
    private Code _owner = null;
    private InstructionPtrStrategy _tryStart = new InstructionPtrStrategy(this);
    private InstructionPtrStrategy _tryEnd = new InstructionPtrStrategy(this);
    private InstructionPtrStrategy _tryHandler = new InstructionPtrStrategy
        (this);

    ExceptionHandler(Code owner) {
        _owner = owner;
    }

    /**
     * Return the owning code block.
     * 
     * @return the owning code block
     */
    public Code getCode() {
        return _owner;
    }

    ///////////////////
    // Body operations
    ///////////////////

    /**
     * Return the instruction marking the beginning of the try {} block.
     * 
     * @return the instruction marking the beginning of the try {} block
     */
    public Instruction getTryStart() {
        return _tryStart.getTargetInstruction();
    }

    /**
     * Set the {@link Instruction} marking the beginning of the try block.
     * The instruction must already be a part of the method.
     * 
     * @param instruction the instruction to set
     */
    public void setTryStart(Instruction instruction) {
        _tryStart.setTargetInstruction(instruction);
    }

    /**
     * Return the instruction at the end of the try {} block.
     * 
     * @return the instruction at the end of the try {} block
     */
    public Instruction getTryEnd() {
        return _tryEnd.getTargetInstruction();
    }

    /**
     * Set the Instruction at the end of the try block. The
     * Instruction must already be a part of the method.
     * 
     * @param instruction the instruction to set
     */
    public void setTryEnd(Instruction instruction) {
        _tryEnd.setTargetInstruction(instruction);
    }

    //////////////////////
    // Handler operations
    //////////////////////

    /**
     * Return the instruction marking the beginning of the catch {} block.
     * 
     * @return the instruction marking the beginning of the catch {} block
     */
    public Instruction getHandlerStart() {
        return _tryHandler.getTargetInstruction();
    }

    /**
     * Set the {@link Instruction} marking the beginning of the catch block.
     * The instruction must already be a part of the method.
     * WARNING: if this instruction is deleted, the results are undefined.
     * 
     * @param instruction the instruction to set
     */
    public void setHandlerStart(Instruction instruction) {
        _tryHandler.setTargetInstruction(instruction);
    }

    ////////////////////
    // Catch operations
    ////////////////////

    /**
     * Return the index into the class {@link ConstantPool} of the
     * {@link ClassEntry} describing the exception type this handler catches.
     * 
     * @return the index
     */
    public int getCatchIndex() {
        return _catchIndex;
    }

    /**
     * Set the index into the class {@link ConstantPool} of the
     * {@link ClassEntry} describing the exception type this handler catches.
     * 
     * @param catchTypeIndex the index to set
     */
    public void setCatchIndex(int catchTypeIndex) {
        _catchIndex = catchTypeIndex;
    }

    /**
     * Return the name of the exception type; returns null for catch-all
     * clauses used to implement finally blocks. The name will be returned
     * in a forum suitable for a {@link Class#forName} call.
     * 
     * @return the name of the exception type
     */
    public String getCatchName() {
        if (_catchIndex == 0)
            return null;

        ClassEntry entry = (ClassEntry) getPool().getEntry(_catchIndex);
        return getProject().getNameCache().getExternalForm(entry.getNameEntry().
            getValue(), false);
    }

    /**
     * Return the {@link Class} of the exception type; returns null for
     * catch-all clauses used to implement finally blocks.
     * 
     * @return the {@link Class} of the exception type
     */
    public Class<?> getCatchType() {
        String name = getCatchName();
        if (name == null)
            return null;
        return Strings.toClass(name, getClassLoader());
    }

    /**
     * Return the bytecode of the exception type; returns null for
     * catch-all clauses used to implement finally blocks.
     * 
     * @return the bytecode of the exception type
     */
    public BCClass getCatchBC() {
        String name = getCatchName();
        if (name == null)
            return null;
        return getProject().loadClass(name, getClassLoader());
    }

    /**
     * Set the class of the exception type, or null for catch-all clauses used
     * with finally blocks.
     * 
     * @param name the class exception type
     */
    public void setCatch(String name) {
        if (name == null)
            _catchIndex = 0;
        else
            _catchIndex = getPool().findClassEntry(getProject().getNameCache().
                getInternalForm(name, false), true);
    }

    /**
     * Set the class of the exception type, or null for catch-all clauses used
     * for finally blocks.
     * 
     * @param type the class exception type
     */
    public void setCatch(Class<?> type) {
        if (type == null)
            setCatch((String) null);
        else
            setCatch(type.getName());
    }

    /**
     * Set the class of the exception type, or null for catch-all clauses used
     * for finally blocks.
     * 
     * @param type the class exception type
     */
    public void setCatch(BCClass type) {
        if (type == null)
            setCatch((String) null);
        else
            setCatch(type.getName());
    }

    /////////////////////////////////
    // InstructionPtr implementation
    /////////////////////////////////

    public void updateTargets() {
        _tryStart.updateTargets();
        _tryEnd.updateTargets();
        _tryHandler.updateTargets();
    }

    public void replaceTarget(Instruction oldTarget, Instruction newTarget) {
        _tryStart.replaceTarget(oldTarget, newTarget);
        _tryEnd.replaceTarget(oldTarget, newTarget);
        _tryHandler.replaceTarget(oldTarget, newTarget);
    }

    ///////////////////////////
    // BCEntity implementation
    ///////////////////////////

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

    ////////////////////////////////
    // VisitAcceptor implementation
    ////////////////////////////////

    public void acceptVisit(BCVisitor visit) {
        visit.enterExceptionHandler(this);
        visit.exitExceptionHandler(this);
    }

    //////////////////
    // I/O operations
    //////////////////

    void read(ExceptionHandler orig) {
        _tryStart.setByteIndex(orig._tryStart.getByteIndex());
        _tryEnd.setByteIndex(orig._tryEnd.getByteIndex());
        _tryHandler.setByteIndex(orig._tryHandler.getByteIndex());

        // done at a high level so that if the name isn't in our constant pool,
        // it will be added
        setCatch(orig.getCatchName());
    }

    void read(DataInput in) throws IOException {
        setTryStart(in.readUnsignedShort());
        setTryEnd(in.readUnsignedShort());
        setHandlerStart(in.readUnsignedShort());
        setCatchIndex(in.readUnsignedShort());
    }

    void write(DataOutput out) throws IOException {
        out.writeShort(getTryStartPc());
        out.writeShort(getTryEndPc());
        out.writeShort(getHandlerStartPc());
        out.writeShort(getCatchIndex());
    }

    public void setTryStart(int start) {
        _tryStart.setByteIndex(start);
    }

    public int getTryStartPc() {
        return _tryStart.getByteIndex();
    }

    public void setTryEnd(int end) {
        setTryEnd((Instruction) _owner.getInstruction(end).prev);
    }

    /**
     * Return the program counter end position for this exception handler.
     * This represents an index into the code byte array.
     * 
     * @return the program counter end position
     */
    public int getTryEndPc() {
        return _tryEnd.getByteIndex() + getTryEnd().getLength();
    }

    public void setHandlerStart(int handler) {
        _tryHandler.setByteIndex(handler);
    }

    public int getHandlerStartPc() {
        return _tryHandler.getByteIndex();
    }

    void invalidate() {
        _owner = null;
    }
}
