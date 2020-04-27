package serp.bytecode.lowlevel;

import java.io.*;

import serp.bytecode.visitor.*;

/**
 * A unicode string value in the constant pool.
 *
 * @author Abe White
 */
public class UTF8Entry extends Entry implements ConstantEntry {
    private String _value = "";

    /**
     * Default constructor.
     */
    public UTF8Entry() {
    }

    /**
     * Constructor.
     *
     * @param value the constant string value of this entry
     */
    public UTF8Entry(String value) {
        _value = value;
    }

    public int getType() {
        return Entry.UTF8;
    }

    /**
     * Return the value of the entry.
     * 
     * @return the value of the entry
     */
    public String getValue() {
        return _value;
    }

    /**
     * Set the value of the entry.
     * 
     * @param value the value to set
     */
    public void setValue(String value) {
        if (value == null)
            throw new NullPointerException("value = null");
        Object key = beforeModify();
        _value = value;
        afterModify(key);
    }

    @Override
    public Object getConstant() {
        return getValue();
    }

    @Override
    public void setConstant(Object value) {
        setValue((String) value);
    }
    
    @Override
    public void acceptVisit(BCVisitor visit) {
        visit.enterUTF8Entry(this);
        visit.exitUTF8Entry(this);
    }

    void readData(DataInput in) throws IOException {
        _value = in.readUTF();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeUTF(_value);
    }
}
