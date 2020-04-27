package serp.bytecode.lowlevel;

import java.io.*;

import serp.bytecode.visitor.*;

/**
 * A constant pool entry describing a package.
 * Package entries are used to represent a package. Each package entry contains the constant pool
 * index of the {@link UTF8Entry} that stores the package name, which is
 * represented in internal form.
 *
 * @author Abe White
 */
public class PackageEntry extends Entry implements ConstantEntry {
    private int _nameIndex = 0;

    /**
     * Default constructor.
     */
    public PackageEntry() {
    }

	/**
	 * Constructor.
	 *
	 * @param nameIndex the constant pool index of the {@link UTF8Entry} containing
	 *                  the package name
	 */
    public PackageEntry(int nameIndex) {
        _nameIndex = nameIndex;
    }

	/**
	 * Return the constant pool index of the {@link UTF8Entry} containing the class
	 * name. Defaults to 0.
	 * 
	 * @return the constant pool index of the {@link UTF8Entry} containing the class
	 *         name. Defaults to 0
	 */
    public int getNameIndex() {
        return _nameIndex;
    }

	/**
	 * Set the constant pool index of the {@link UTF8Entry} containing the package
	 * name.
	 * 
	 * @param nameIndex the constant pool index of the {@link UTF8Entry} containing
	 *                  the package name
	 */
    public void setNameIndex(int nameIndex) {
        Object key = beforeModify();
        _nameIndex = nameIndex;
        afterModify(key);
    }

    /**
     * Return the referenced {@link UTF8Entry}. This method can only
     * be run for entries that have been added to a constant pool.
     * 
     * @return the referenced {@link UTF8Entry}
     */
    public UTF8Entry getNameEntry() {
        return (UTF8Entry) getPool().getEntry(_nameIndex);
    }

    public int getType() {
        return Entry.PACKAGE;
    }

    public Object getConstant() {
        return getNameEntry().getValue();
    }

    public void setConstant(Object value) {
        getNameEntry().setConstant(value);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterPackageEntry(this);
        visit.exitPackageEntry(this);
    }

    void readData(DataInput in) throws IOException {
        _nameIndex = in.readUnsignedShort();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeShort(_nameIndex);
    }
}
