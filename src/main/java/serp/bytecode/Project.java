package serp.bytecode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import serp.bytecode.visitor.BCVisitor;
import serp.bytecode.visitor.VisitAcceptor;
import serp.util.Strings;

/**
 * The Project represents a working set of classes. It caches parsed
 * bytecode and is responsible for bytecode class creation. Currently
 * changes made in one class are <strong>not</strong> reflected in other
 * classes, though this will be an option in the future.
 *
 * <p>Bytecode that has been parsed is held in a cache so that retrieving
 * a class with the same name multiple times always returns the same
 * {@link BCClass} instance.</p>
 *
 * <p>A future goal is to eventually have facilities for traversing jars
 * or directory structures to find classes that meet a given criteria (such
 * as implementing a given interface, etc) and to perform operations on entire
 * projects, similar to aspect-oriented programming.</p>
 *
 * @author Abe White
 */
public class Project implements VisitAcceptor {
    private final String _name;
    private final Map<String,BCClass> _cache = new HashMap<>();
    private final NameCache _names = new NameCache();

    /**
     * Default constructor.
     */
    public Project() {
        this(null);
    }

    /**
     * Construct a named project.
     * 
     * @param name the project name
     */
    public Project(String name) {
        _name = name;
    }

    /**
     * Return the project name, or null if unset.
     * 
     * @return the project name, or null if unset
     */
    public String getName() {
        return _name;
    }

    /**
     * Return the name cache, which includes utilities for converting names
     * from internal to external form and vice versa.
     * 
     * @return the name cache
     */
    public NameCache getNameCache() {
        return _names;
    }

    /**
     * Load a class with the given name.
     *
     * @see #loadClass(String,ClassLoader)
	 * @param name   the name of the class, including package
	 * @return the loaded class
     */
    public BCClass loadClass(String name) {
        return loadClass(name, null);
    }

	/**
	 * Load the bytecode for the class with the given name. If a {@link BCClass}
	 * with the given name already exists in this project, it will be returned.
	 * Otherwise, a new {@link BCClass} will be created with the given name and
	 * returned. If the name represents an existing type, the returned instance will
	 * contain the parsed bytecode for that type. If the name is of a primitive or
	 * array type, the returned instance will act accordingly.
	 *
	 * @throws RuntimeException on parse error
	 * @param name   the name of the class, including package
	 * @param loader the class loader to use to search for an existing class with
	 *               the given name; if null defaults to the context loader of the
	 *               current thread
	 * @return the loaded class
	 */
    public BCClass loadClass(String name, ClassLoader loader) {
        // convert to proper Class.forName() form
        name = _names.getExternalForm(name, false);

        BCClass cached = checkCache(name);
        if (cached != null)
            return cached;

        // check for existing type
        if (loader == null)
            loader = Thread.currentThread().getContextClassLoader();
        try {
            return loadClass(Strings.toClass(name, loader));
        } catch (Exception e) {
        }

        String componentName = _names.getComponentName(name);
        BCClass ret = new BCClass(this);
        if (componentName != null)
            ret.setState(new ArrayState(name, componentName));
        else {
            ret.setState(new ObjectState(_names));
            ret.setName(name);
            ret.setSuperclass(Object.class);
        }
        cache(name, ret);
        return ret;
    }

    /**
     * Load the bytecode for the given class.
     * If a {@link BCClass} with the name of the given class already exists in
     * this project, it will be returned. Otherwise, the bytecode of the given
     * class will be parsed and returned as a new {@link BCClass}. If the
     * given class is an array or primitive type, the returned instance will
     * act accordingly.
     *
     * @throws RuntimeException on parse error
     * @param type the class to parse
     * @return the loaded class
     */
    public BCClass loadClass(Class<?> type) {
        BCClass cached = checkCache(type.getName());
        if (cached != null)
            return cached;

        BCClass ret = new BCClass(this);
        if (type.isPrimitive())
            ret.setState(new PrimitiveState(type, _names));
        else if (type.isArray())
            ret.setState(new ArrayState(type.getName(), _names.getExternalForm
                (type.getComponentType().getName(), false)));
        else {
            ret.setState(new ObjectState(_names));
            try {
                ret.read(type);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe.toString());
            }
        }
        cache(type.getName(), ret);
        return ret;
    }

    /**
     * Load the bytecode from the given class file.
     * If this project already contains the class in the given file, it will
     * be returned. Otherwise a new {@link BCClass} will be created from the
     * given bytecode.
     *
     * @throws RuntimeException on parse error
     * @param classFile the class file
     * @return the loaded class
     */
    public BCClass loadClass(File classFile) {
        return loadClass(classFile, null);
    }

    /**
     * Load the bytecode from the given class file.
     * If this project already contains the class in the given file, it will
     * be returned. Otherwise a new {@link BCClass} will be created from the
     * given bytecode.
     *
     * @throws RuntimeException on parse error
     * @param classFile the class file
     * @param loader the class loader
     * @return the loaded class
     */
    public BCClass loadClass(File classFile, ClassLoader loader) {
        // parse the bytecode from the file
        BCClass ret = new BCClass(this);
        ret.setState(new ObjectState(_names));
        try {
            ret.read(classFile, loader);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.toString());
        }

        String name = ret.getName();
        BCClass cached = checkCache(name);
        if (cached != null)
            return cached;

        cache(name, ret);
        return ret;
    }

    /**
     * Load the bytecode from the given stream.
     * If this project already contains the class in the given stream,
     * it will be returned. Otherwise a new {@link BCClass} will be created
     * from the given bytecode.
     *
     * @throws RuntimeException on parse error
     * @param in the input stream
     * @return the loaded class
     */
    public BCClass loadClass(InputStream in) {
        return loadClass(in, null);
    }

    /**
     * Load the bytecode from the given stream.
     * If this project already contains the class in the given stream,
     * it will be returned. Otherwise a new {@link BCClass} will be created
     * from the given bytecode.
     *
     * @throws RuntimeException on parse error
     * @param in the input stream
     * @param loader the class loader
     * @return the loaded class
     */
    public BCClass loadClass(InputStream in, ClassLoader loader) {
        BCClass ret = new BCClass(this);
        ret.setState(new ObjectState(_names));
        try {
            ret.read(in, loader);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.toString());
        }

        String name = ret.getName();
        BCClass cached = checkCache(name);
        if (cached != null)
            return cached;

        cache(name, ret);
        return ret;
    }

    /**
     * Import the given bytecode from another project. If a {@link BCClass}
     * with the same name already exists in this project, it will be returned.
     * Otherwise, a new {@link BCClass} will be created from the
     * information in the given class.
     * 
     * @param bc the class to load
     * @return the loaded class
     */
    public BCClass loadClass(BCClass bc) {
        String name = bc.getName();
        BCClass cached = checkCache(name);
        if (cached != null)
            return cached;

        BCClass ret = new BCClass(this);
        if (bc.isPrimitive())
            ret.setState(new PrimitiveState(bc.getType(), _names));
        else if (bc.isArray())
            ret.setState(new ArrayState(bc.getName(), bc.getComponentName()));
        else {
            ret.setState(new ObjectState(_names));
            ret.read(bc);
        }

        cache(name, ret);
        return ret;
    }

    /**
     * Clears all classes from this project.
     */
    public void clear() {
        Collection<BCClass> values = _cache.values();
        BCClass bc;
        for (Iterator<BCClass> itr = values.iterator(); itr.hasNext();) {
            bc = itr.next();
            itr.remove();
            bc.invalidate();
        }
        _names.clear();
    }

    /**
     * Remove a class from this project. After removal, the result of any
     * further operations on the class is undefined.
     *
     * @param type the class
     * @return true if the class belonged to this project, false otherwise
     */
    public boolean removeClass(String type) {
        return removeClass(checkCache(type));
    }

    /**
     * Remove a class from this project. After removal, the result of any
     * further operations on the class is undefined.
     *
     * @param type the class
     * @return true if the class belonged to this project, false otherwise
     */
    public boolean removeClass(Class<?> type) {
        if (type == null)
            return false;
        return removeClass(checkCache(type.getName()));
    }

    /**
     * Remove a class from this project. After removal, the result of any
     * further operations on the class is undefined.
     *
     * @param type the class
     * @return true if the class belonged to this project, false otherwise
     */
    public boolean removeClass(BCClass type) {
        if (type == null)
            return false;
        if (!removeFromCache(type.getName(), type))
            return false;
        type.invalidate();
        return true;
    }

    /**
     * Return all loaded classes in the project.
     * 
     * @return all loaded classes in the project
     */
    public BCClass[] getClasses() {
        Collection<BCClass> values = _cache.values();
        return (BCClass[]) values.toArray(new BCClass[values.size()]);
    }

    /**
     * Return true if the project already contains the given class.
     * 
     * @param type the class
     * @return true if the project already contains the given class
     */
    public boolean containsClass(String type) {
        return _cache.containsKey(type);
    }

    /**
     * Return true if the project already contains the given class.
     * 
     * @param type the class
     * @return true if the project already contains the given class
     */
    public boolean containsClass(Class<?> type) {
        return (type == null) ? false : containsClass(type.getName());
    }

    /**
     * Return true if the project already contains the given class.
     * 
     * @param type the class
     * @return true if the project already contains the given class
     */
    public boolean containsClass(BCClass type) {
        return (type == null) ? false : containsClass(type.getName());
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterProject(this);
        BCClass[] classes = getClasses();
        for (int i = 0; i < classes.length; i++)
            classes[i].acceptVisit(visit);
        visit.exitProject(this);
    }

	/**
	 * Renames the given class within this project. Used internally by
	 * {@link BCClass} instances when their name is modified.
	 *
	 * @throws IllegalStateException if a class with the new name already exists
	 * 
	 * @param oldName the old name
	 * @param newName the new name
	 * @param bc      the cached class
	 */
    void renameClass(String oldName, String newName, BCClass bc) {
        if (oldName.equals(newName))
            return;

        BCClass cached = (BCClass) checkCache(newName);
        if (cached != null)
            throw new IllegalStateException("A class with name " + newName +
                " already exists in this project");

        removeFromCache(oldName, bc);
        cache(newName, bc);
    }

    /**
     * Check the cache for a loaded type.
     * 
	 * @param name the key name
	 * @return the cache class
     */
    private BCClass checkCache(String name) {
        return (BCClass) _cache.get(name);
    }

    /**
     * Cache a class.
     * 
	 * @param name the key name
	 * @param bc   the class to cache
     */
    private void cache(String name, BCClass bc) {
        _cache.put(name, bc);
    }

	/**
	 * Remove a cached class.
	 * 
	 * @param name the key name
	 * @param bc   the class to remove
	 * @return true if removed
	 */
    private boolean removeFromCache(String name, BCClass bc) {
        BCClass rem = (BCClass) checkCache(name);
        if (rem != bc)
            return false;
        _cache.remove(name);
        return true;
    }
}
