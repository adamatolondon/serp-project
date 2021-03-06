package serp.bytecode.lowlevel;

import java.io.*;

import serp.bytecode.visitor.*;

/**
 * Entry containing indexes referencing a name and a descriptor. Used
 * to describe fields and methods of other classes referenced by opcodes.
 *
 * @author Abe White
 */
public class NameAndTypeEntry extends Entry {
    private int _nameIndex = 0;
    private int _descriptorIndex = 0;

    /**
     * Default constructor.
     */
    public NameAndTypeEntry() {
    }

	/**
	 * Constructor.
	 *
	 * @param nameIndex       the constant pool index of the {@link UTF8Entry}
	 *                        containing the name of this entity
	 * @param descriptorIndex the constant pool index of the {@link UTF8Entry}
	 *                        containing the descriptor for this entity
	 */
    public NameAndTypeEntry(int nameIndex, int descriptorIndex) {
        _nameIndex = nameIndex;
        _descriptorIndex = descriptorIndex;
    }

    public int getType() {
        return Entry.NAMEANDTYPE;
    }

	/**
	 * Return the constant pool index of the {@link UTF8Entry} containing the name
	 * of this entity.
	 * 
	 * @return the constant pool index of the {@link UTF8Entry} containing the name
	 *         of this entity
	 */
    public int getNameIndex() {
        return _nameIndex;
    }

	/**
	 * Set the constant pool index of the {@link UTF8Entry} containing the name of
	 * this entity.
	 * 
	 * @param nameIndex the constant pool index of the {@link UTF8Entry} containing
	 *                  the name of this entity
	 */
    public void setNameIndex(int nameIndex) {
        Object key = beforeModify();
        _nameIndex = nameIndex;
        afterModify(key);
    }

    /**
     * Return the name's referenced {@link UTF8Entry}. This method can only
     * be run for entries that have been added to a constant pool.
     * 
     * @return the name's referenced {@link UTF8Entry}
     */
    public UTF8Entry getNameEntry() {
        return (UTF8Entry) getPool().getEntry(_nameIndex);
    }

	/**
	 * Return the constant pool index of the {@link UTF8Entry} containing the
	 * descriptor for this entity.
	 * 
	 * @return the constant pool index of the {@link UTF8Entry} containing the
	 *         descriptor for this entity
	 */
    public int getDescriptorIndex() {
        return _descriptorIndex;
    }

	/**
	 * Set the constant pool index of a {@link UTF8Entry} containing the descriptor
	 * for this entity.
	 * 
	 * @param descriptorIndex the constant pool index of a {@link UTF8Entry}
	 *                        containing the descriptor for this entity
	 */
    public void setDescriptorIndex(int descriptorIndex) {
        Object key = beforeModify();
        _descriptorIndex = descriptorIndex;
        afterModify(key);
    }

    /**
     * Return the descriptor's referenced {@link UTF8Entry}. This method
     * can only be run for entries that have been added to a constant pool.
     * 
     * @return the descriptor's referenced {@link UTF8Entry}
     */
    public UTF8Entry getDescriptorEntry() {
        return (UTF8Entry) getPool().getEntry(_descriptorIndex);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterNameAndTypeEntry(this);
        visit.exitNameAndTypeEntry(this);
    }

    void readData(DataInput in) throws IOException {
        _nameIndex = in.readUnsignedShort();
        _descriptorIndex = in.readUnsignedShort();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeShort(_nameIndex);
        out.writeShort(_descriptorIndex);
    }
}
