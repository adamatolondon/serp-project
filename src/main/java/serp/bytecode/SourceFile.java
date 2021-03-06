package serp.bytecode;

import java.io.*;

import serp.bytecode.lowlevel.*;
import serp.bytecode.visitor.*;

/**
 * Attribute naming the source file for this class.
 *
 * @author Abe White
 */
public class SourceFile extends Attribute {
    int _sourceFileIndex = 0;

    SourceFile(int nameIndex, Attributes owner) {
        super(nameIndex, owner);
    }

    int getLength() {
        return 2;
    }

	/**
	 * Return the index into the class {@link ConstantPool} of the {@link UTF8Entry}
	 * naming the source file for this class, or 0 if not set.
	 * 
	 * @return the index into the class {@link ConstantPool} of the
	 *         {@link UTF8Entry} naming the source file for this class, or 0 if not
	 *         set
	 */
    public int getFileIndex() {
        return _sourceFileIndex;
    }

    /**
     * Set the index into the class {@link ConstantPool} of the
     * {@link UTF8Entry} naming the source file for this class.
     * 
     * @param sourceFileIndex the index to set
     */
    public void setFileIndex(int sourceFileIndex) {
        if (sourceFileIndex < 0)
            sourceFileIndex = 0;
        _sourceFileIndex = sourceFileIndex;
    }

    /**
     * Return the name of the source file, or null if not set.
     * 
     * @return the name of the source file, or null if not set
     */
    public String getFileName() {
        if (_sourceFileIndex == 0)
            return null;
        return ((UTF8Entry) getPool().getEntry(_sourceFileIndex)).getValue();
    }

    /**
     * Return the file object for the source file, or null if not set.
     *
     * @param dir the directory of the file, or null
     * @return the file object for the source file, or null if not set
     */
    public File getFile(File dir) {
        String name = getFileName();
        if (name == null)
            return null;
        return new File(dir, name);
    }

    /**
     * Set the name of the source file. The name should be the file name
     * only; it should not include the path to the file.
     * 
     * @param name the file name to set
     */
    public void setFile(String name) {
        if (name == null)
            setFileIndex(0);
        else
            setFileIndex(getPool().findUTF8Entry(name, true));
    }

    /**
     * Set the source file. Note that only the file name is recorded;
     * the path to the file is discarded.
     * 
     * @param file the file name to set
     */
    public void setFile(File file) {
        if (file == null)
            setFile((String) null);
        else
            setFile(file.getName());
    }

    /**
     * Set the file name from the current class name plus the .java extension.
     */
    public void setFromClassName() {
        setFile(((BCClass) getOwner()).getClassName() + ".java");
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterSourceFile(this);
        visit.exitSourceFile(this);
    }

    void read(Attribute other) {
        setFile(((SourceFile) other).getFileName());
    }

    void read(DataInput in, int length) throws IOException {
        setFileIndex(in.readUnsignedShort());
    }

    void write(DataOutput out, int length) throws IOException {
        out.writeShort(getFileIndex());
    }
}
