package serp.bytecode.lowlevel;

import java.io.*;

/**
 * Base class for field, method, and interface method constant pool
 * entries. All complex entries reference the {@link ClassEntry} of the
 * class that owns the entity and a {@link NameAndTypeEntry} describing
 * the entity.
 *
 * @author Abe White
 */
public abstract class ComplexEntry extends Entry {
    private int _classIndex = 0;
    private int _nameAndTypeIndex = 0;

    /**
     * Default constructor.
     */
    public ComplexEntry() {
    }

	/**
	 * Constructor.
	 *
	 * @param classIndex       the constant pool index of the {@link ClassEntry}
	 *                         describing the owner of this entity
	 * @param nameAndTypeIndex the constant pool index of the
	 *                         {@link NameAndTypeEntry} describing this entity
	 */
    public ComplexEntry(int classIndex, int nameAndTypeIndex) {
        _classIndex = classIndex;
        _nameAndTypeIndex = nameAndTypeIndex;
    }

	/**
	 * Return the constant pool index of the {@link ClassEntry} describing the
	 * owning class of this entity. Defaults to 0.
	 * 
	 * @return the constant pool index of the {@link ClassEntry} describing the
	 *         owning class of this entity. Defaults to 0
	 */
    public int getClassIndex() {
        return _classIndex;
    }

    /**
     * Set the constant pool index of the {@link ClassEntry} describing
     * the owning class of this entity.
     * 
     * @param classIndex the constant pool index to set
     */
    public void setClassIndex(int classIndex) {
        Object key = beforeModify();
        _classIndex = classIndex;
        afterModify(key);
    }

    /**
     * Return the referenced {@link ClassEntry}. This method can only
     * be run for entries that have been added to a constant pool.
     * 
     * @return the referenced {@link ClassEntry}
     */
    public ClassEntry getClassEntry() {
        return (ClassEntry) getPool().getEntry(_classIndex);
    }

	/**
	 * Return the constant pool index of the {@link NameAndTypeEntry} describing
	 * this entity.
	 * 
	 * @return the constant pool index of the {@link NameAndTypeEntry} describing
	 *         this entity
	 */
    public int getNameAndTypeIndex() {
        return _nameAndTypeIndex;
    }

    /**
     * Set the constant pool index of the {@link NameAndTypeEntry}
     * describing this entity.
     * 
     * @param nameAndTypeIndex the index to set
     */
    public void setNameAndTypeIndex(int nameAndTypeIndex) {
        Object key = beforeModify();
        _nameAndTypeIndex = nameAndTypeIndex;
        afterModify(key);
    }

    /**
     * Return the referenced {@link NameAndTypeEntry}. This method can only
     * be run for entries that have been added to a constant pool.
     * 
     * @return the referenced {@link NameAndTypeEntry}
     */
    public NameAndTypeEntry getNameAndTypeEntry() {
        return (NameAndTypeEntry) getPool().getEntry(_nameAndTypeIndex);
    }

    void readData(DataInput in) throws IOException {
        _classIndex = in.readUnsignedShort();
        _nameAndTypeIndex = in.readUnsignedShort();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeShort(_classIndex);
        out.writeShort(_nameAndTypeIndex);
    }
}
