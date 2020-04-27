package serp.bytecode.lowlevel;

/**
 * Interface implemented by entries representing constant values. Allows
 * generic access the constant value regardless of type.
 *
 * @author Abe White
 */
public interface ConstantEntry {
    /**
     * Return the value of the constant held by this entry.
     * 
     * @return the value of the constant held by this entry
     */
    public Object getConstant();

    /**
     * Set the value of the constant held by this entry.
     * 
     * @param value the constant to set
     */
    public void setConstant(Object value);
}
