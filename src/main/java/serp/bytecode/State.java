package serp.bytecode;

import java.util.*;

import serp.bytecode.lowlevel.*;

/**
 * The State type is extended by various concrete types to change
 * the behavior of a {@link BCClass}. All methods in this base
 * implementation throw an {@link UnsupportedOperationException}
 *
 * @author Abe White
 */
class State {
    /**
     * A singleton instance of this type that can be used to make a
     * class invalid.
     */
    public static final State INVALID = new State();

    /**
     * Return the magic number of the bytecode class.
     * 
     * @return the magic number of the bytecode class
     */
    public int getMagic() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the magic number of the bytecode class.
     * 
     * @param magic the magic number
     */
    public void setMagic(int magic) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the major number of the bytecode class.
     * 
     * @return the major number of the bytecode class
     */
    public int getMajorVersion() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the major version of the bytecode class.
     * 
     * @param major the major version
     */
    public void setMajorVersion(int major) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the minor number of the bytecode class.
     * 
     * @return the minor number of the bytecode class
     */
    public int getMinorVersion() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the minor version of the bytecode class.
     * 
     * @param minor the minor version
     */
    public void setMinorVersion(int minor) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the access flags of the bytecode class.
     * 
     * @return the access flags of the bytecode class
     */
    public int getAccessFlags() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the access flags of the bytecode class.
     * 
     * @param access the access flags
     */
    public void setAccessFlags(int access) {
        throw new UnsupportedOperationException();
    }

	/**
	 * Return the {@link ConstantPool} index of the {@link ClassEntry} for this
	 * class, or 0 if none.
	 * 
	 * @return the {@link ConstantPool} index of the {@link ClassEntry} for this
	 *         class, or 0 if none
	 */
    public int getIndex() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the {@link ConstantPool} index of the {@link ClassEntry}
     * for this class.
     * 
     * @param index the {@link ConstantPool} index
     */
    public void setIndex(int index) {
        throw new UnsupportedOperationException();
    }

	/**
	 * Return the {@link ConstantPool} index of the {@link ClassEntry} for the
	 * superclass of this class, or 0 if none.
	 * 
	 * @return the {@link ConstantPool} index of the {@link ClassEntry} for the
	 *         superclass of this class, or 0 if none
	 */
    public int getSuperclassIndex() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the {@link ConstantPool} index of the {@link ClassEntry}
     * for the superclass of this class. Throws
     * {@link UnsupportedOperationException} by default.
     * 
     * @param index the {@link ConstantPool} index
     */
    public void setSuperclassIndex(int index) {
        throw new UnsupportedOperationException();
    }

	/**
	 * Return the {@link ConstantPool} indexes of the {@link ClassEntry}s for the
	 * indexes of this class, or empty list if none. If the state does not support
	 * changing the interfaces, the returned list should be immutable.
	 * 
	 * @return the {@link ConstantPool} indexes of the {@link ClassEntry}s for the
	 *         indexes of this class, or empty list if none
	 */
    public List<Number> getInterfacesHolder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the {@link BCField}s of this class, or empty list if none.
     * If the state does not support changing the fields, the returned
     * list should be immutable.
     * 
     * @return the {@link BCField}s of this class, or empty list if none
     */
    public List<BCField> getFieldsHolder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the {@link BCMethod}s of this class, or empty list if none.
     * If the state does not support changing the methods, the returned
     * list should be immutable.
     * 
     * @return the {@link BCMethod}s of this class, or empty list if none
     */
    public List<BCMethod> getMethodsHolder() {
        throw new UnsupportedOperationException();
    }

	/**
	 * Return the {@link Attribute}s of this class, or empty list if none. If the
	 * state does not support changing the attributes, the returned list should be
	 * immutable.
	 * 
	 * @return the {@link Attribute}s of this class, or empty list if none
	 */
    public Collection<Attribute> getAttributesHolder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the constant pool of the class.
     * 
     * @return the constant pool of the class
     */
    public ConstantPool getPool() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the name of the class. The name should be in a form suitable
     * for a {@link Class#forName} call.
     * 
     * @return the name of the class
     */
    public String getName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the name of the superclass. The name should be in a form
     * suitable for a {@link Class#forName} call, or null if none.
     * 
     * @return the name of the superclass
     */
    public String getSuperclassName() {
        throw new UnsupportedOperationException();
    }

	/**
	 * Return the name of the component type of this array, or null if not an array.
	 * The name should be in a form suitable for a {@link Class#forName} call.
	 * 
	 * @return the name of the component type of this array, or null if not an array
	 */
    public String getComponentName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return true if this class is a primitive.
     * 
     * @return true if this class is a primitive
     */
    public boolean isPrimitive() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return true if this class is an array.
     * 
     * @return true if this class is an array
     */
    public boolean isArray() {
        throw new UnsupportedOperationException();
    }
}
