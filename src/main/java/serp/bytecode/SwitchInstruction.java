package serp.bytecode;

import java.io.*;
import java.util.*;

/**
 * Contains functionality common to the different switch types
 * (TableSwitch and LookupSwitch).
 *
 * @author Eric Lindauer
 */
public abstract class SwitchInstruction extends JumpInstruction {
    private List<InstructionPtrStrategy> _cases = new LinkedList<>();

    public SwitchInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }

	/**
	 * Returns the current byte offsets for the different switch cases in this
	 * Instruction.
	 * 
	 * @return the current byte offsets for the different switch cases in this
	 *         Instruction
	 */
    public int[] getOffsets() {
        int bi = getByteIndex();
        int[] offsets = new int[_cases.size()];
        for (int i = 0; i < offsets.length; i++)
            offsets[i] = _cases.get(i).getByteIndex()
                - bi;
        return offsets;
    }

	/**
	 * Sets the offsets for the instructions representing the different switch
	 * statement cases. WARNING: these offsets will not be changed in the event that
	 * the code is modified following this call. It is typically a good idea to
	 * follow this call with a call to updateTargets as soon as the instructions at
	 * the given offsets are valid, at which point the Instructions themselves will
	 * be used as the targets and the offsets will be updated as expected.
	 * 
	 * @param offsets the offsets to set
	 */
    public void setOffsets(int[] offsets) {
        int bi = getByteIndex();
        _cases.clear();
        for (int i = 0; i < offsets.length; i++) {
            InstructionPtrStrategy next = new InstructionPtrStrategy(this);
            next.setByteIndex(offsets[i] + bi);
            _cases.add(next);
        }
    }

    public int countTargets() {
        return _cases.size();
    }

    int getLength() {
        // don't call super.getLength(), cause JumpInstruction will return
        // value assuming this is an 'if' or 'goto' instruction
        int length = 1;

        // make the first byte of the 'default' a multiple of 4 from the
        // start of the method
        int byteIndex = getByteIndex() + 1;
        for (; (byteIndex % 4) != 0; byteIndex++, length++);
        return length;
    }

    /**
     * Synonymous with {@link #getTarget}.
     * 
     * @return the default target instruction
     */
    public Instruction getDefaultTarget() {
        return getTarget();
    }

    /**
     * Synonymous with {@link #getOffset}.
     * 
     * @return the default offset
     */
    public int getDefaultOffset() {
        return getOffset();
    }

    /**
     * Synonymous with {@link #setOffset}.
     * 
     * @param offset the offset
     * @return the modified instruction
     */
    public SwitchInstruction setDefaultOffset(int offset) {
        setOffset(offset);
        return this;
    }

    /**
     * Synonymous with {@link #setTarget}.
     * 
     * @param ins the target instruction
     * @return the modified instruction
     */
    public SwitchInstruction setDefaultTarget(Instruction ins) {
        return (SwitchInstruction) setTarget(ins);
    }

    /**
     * Return the targets for this switch, or empty array if not set.
     * 
     * @return the targets for this switch, or empty array if not set
     */
    public Instruction[] getTargets() {
        Instruction[] result = new Instruction[_cases.size()];
        for (int i = 0; i < _cases.size(); i++)
            result[i] = ((InstructionPtrStrategy) _cases.get(i)).
                getTargetInstruction();
        return result;
    }

    /**
     * Set the jump points for this switch.
     *
     * @param targets the targets to add
     * @return this instruction, for method chaining
     */
    public SwitchInstruction setTargets(Instruction[] targets) {
        _cases.clear();
        if (targets != null)
            for (int i = 0; i < targets.length; i++)
                addTarget(targets[i]);
        return this;
    }

    /**
     * Add a target to this switch.
     *
     * @param target the target to add
     * @return this instruction, for method chaining
     */
    public SwitchInstruction addTarget(Instruction target) {
        _cases.add(new InstructionPtrStrategy(this, target));
        return this;
    }

    public int getStackChange() {
        return -1;
    }

    public void updateTargets() {
        super.updateTargets();
        for (Iterator<InstructionPtrStrategy> itr = _cases.iterator(); itr.hasNext();)
            itr.next().updateTargets();
    }

    public void replaceTarget(Instruction oldTarget, Instruction newTarget) {
        super.replaceTarget(oldTarget, newTarget);
        for (Iterator<InstructionPtrStrategy> itr = _cases.iterator(); itr.hasNext();)
            itr.next().replaceTarget(oldTarget,
                newTarget);
    }

    void read(Instruction orig) {
        super.read(orig);

        SwitchInstruction ins = (SwitchInstruction) orig;
        _cases.clear();
        InstructionPtrStrategy incoming;
        for (Iterator<InstructionPtrStrategy> itr = ins._cases.iterator(); itr.hasNext();) {
            incoming = itr.next();
            InstructionPtrStrategy next = new InstructionPtrStrategy(this);
            next.setByteIndex(incoming.getByteIndex());
            _cases.add(next);
        }
    }

    void clearTargets() {
        _cases.clear();
    }

    void readTarget(DataInput in) throws IOException {
        InstructionPtrStrategy next = new InstructionPtrStrategy(this);
        next.setByteIndex(getByteIndex() + in.readInt());
        _cases.add(next);
    }

	/**
	 * Set the match-jumppt pairs for this switch.
	 *
	 * @param matches the new case matches
	 * @param targets the target instruction
	 * @return this instruction, for method chaining
	 */
    public SwitchInstruction setCases(int[] matches, Instruction[] targets) {
        setMatches(matches);
        setTargets(targets);
        return this;
    }

    public SwitchInstruction setMatches(int[] matches) {
        clearMatches();
        for (int i = 0; i < matches.length; i++)
            addMatch(matches[i]);
        return this;
    }

	/**
	 * Add a case to this switch.
	 *
	 * @param match  the new case match
	 * @param target the target instruction
	 * @return this instruction, for method chaining
	 */
    public SwitchInstruction addCase(int match, Instruction target) {
        addMatch(match);
        addTarget(target);
        return this;
    }

    public abstract SwitchInstruction addMatch(int match);

    public abstract int[] getMatches();

    abstract void clearMatches();

    void calculateOpcode() {
    }
}
