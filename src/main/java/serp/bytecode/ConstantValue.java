package serp.bytecode;

import java.io.*;

import serp.bytecode.lowlevel.*;
import serp.bytecode.visitor.*;

/**
 * A constant value for a member field.
 *
 * @author Abe White
 */
public class ConstantValue extends Attribute {
    int _valueIndex = 0;

    ConstantValue(int nameIndex, Attributes owner) {
        super(nameIndex, owner);
    }

    int getLength() {
        return 2;
    }

    /**
     * Return the owning field.
     * 
     * @return the owning field
     */
    public BCField getField() {
        return (BCField) getOwner();
    }

    /**
     * Return the {@link ConstantPool} index of the {@link ConstantEntry}
     * holding the value of this constant. Defaults to 0.
     * 
     * @return the {@link ConstantPool} index
     */
    public int getValueIndex() {
        return _valueIndex;
    }

    /**
     * Set the {@link ConstantPool} of the {@link ConstantEntry}
     * holding the value of this constant.
     * 
     * @param valueIndex the index
     */
    public void setValueIndex(int valueIndex) {
        _valueIndex = valueIndex;
    }

    /**
     * Return the type of constant this attribute represents, or null if
     * not set.
     * 
     * @return the type name
     */
    public String getTypeName() {
        Class<?> type = getType();
        if (type == null)
            return null;
        return type.getName();
    }

    /**
     * Return the type of constant this attribute represents (String.class,
     * int.class, etc), or null if not set.
     * 
     * @return the type
     */
    public Class<?> getType() {
        Object value = getValue();
        if (value == null)
            return null;

        Class<?> type = value.getClass();
        if (type == Integer.class)
            return int.class;
        if (type == Float.class)
            return float.class;
        if (type == Double.class)
            return double.class;
        if (type == Long.class)
            return long.class;
        return String.class;
    }

    /**
     * Return the bytecode for the type of constant this attribute represents.
     * 
     * @return the bytecode class
     */
    public BCClass getTypeBC() {
        return getProject().loadClass(getType());
    }

    /**
     * Return the value of this constant as an Object of the appropriate
     * type (String, Integer, Double, etc), or null if not set.
     * 
     * @return the value of this constant
     */
    public Object getValue() {
        if (_valueIndex <= 0)
            return null;
        return ((ConstantEntry) getPool().getEntry(_valueIndex)).getConstant();
    }

    /**
     * Set the value of this constant using the appropriate wrapper Object
     * type (String, Integer, Double, etc). Types that are not directly
     * supported will be converted accordingly if possible.
     * 
     * @param value the value to set
     */
    public void setValue(Object value) {
        Class<?> type = value.getClass();
        if (type == Boolean.class)
            setIntValue((((Boolean) value).booleanValue()) ? 1 : 0);
        else if (type == Character.class)
            setIntValue((int) ((Character) value).charValue());
        else if (type == Byte.class || type == Integer.class 
            || type == Short.class)
            setIntValue(((Number) value).intValue());
        else if (type == Float.class)
            setFloatValue(((Number) value).floatValue());
        else if (type == Double.class)
            setDoubleValue(((Number) value).doubleValue());
        else if (type == Long.class)
            setLongValue(((Number) value).longValue());
        else
            setStringValue(value.toString());
    }

    /**
     * Get the value of this int constant, or 0 if not set.
     * 
     * @return the value of this int constant
     */
    public int getIntValue() {
        if (getValueIndex() <= 0)
            return 0;
        
        return ((IntEntry) getPool().getEntry(getValueIndex())).getValue();
    }

    /**
     * Set the value of this int constant.
     * 
     * @param value the value to set
     */
    public void setIntValue(int value) {
        setValueIndex(getPool().findIntEntry(value, true));
    }

    /**
     * Get the value of this float constant.
     * 
     * @return the value of this float constant
     */
    public float getFloatValue() {
        if (getValueIndex() <= 0)
            return 0F;
        return ((FloatEntry) getPool().getEntry(getValueIndex())).getValue();
    }

    /**
     * Set the value of this float constant.
     * 
     * @param value the value to set
     */
    public void setFloatValue(float value) {
        setValueIndex(getPool().findFloatEntry(value, true));
    }

    /**
     * Get the value of this double constant.
     * 
     * @return the value of this double constant
     */
    public double getDoubleValue() {
        if (getValueIndex() <= 0)
            return 0D;
        return ((DoubleEntry) getPool().getEntry(getValueIndex())).getValue();
    }

    /**
     * Set the value of this double constant.
     * 
     * @param value the value to set
     */
    public void setDoubleValue(double value) {
        setValueIndex(getPool().findDoubleEntry(value, true));
    }

    /**
     * Get the value of this long constant.
     * 
     * @return the value of this long constant
     */
    public long getLongValue() {
        if (getValueIndex() <= 0)
            return 0L;
        return ((LongEntry) getPool().getEntry(getValueIndex())).getValue();
    }

    /**
     * Set the value of this long constant.
     * 
     * @param value the value to set
     */
    public void setLongValue(long value) {
        setValueIndex(getPool().findLongEntry(value, true));
    }

    /**
     * Get the value of this string constant.
     * 
     * @return the string value
     */
    public String getStringValue() {
        if (getValueIndex() <= 0)
            return null;
        return ((StringEntry) getPool().getEntry(getValueIndex())).
            getStringEntry().getValue();
    }

    /**
     * Set the value of this string constant.
     * 
     * @param value the value to set
     */
    public void setStringValue(String value) {
        setValueIndex(getPool().findStringEntry(value, true));
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterConstantValue(this);
        visit.exitConstantValue(this);
    }

    void read(Attribute other) {
        setValue(((ConstantValue) other).getValue());
    }

    void read(DataInput in, int length) throws IOException {
        setValueIndex(in.readUnsignedShort());
    }

    void write(DataOutput out, int length) throws IOException {
        out.writeShort(getValueIndex());
    }
}
