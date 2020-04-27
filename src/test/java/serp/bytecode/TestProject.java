package serp.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;


/**
 * Tests the {@link Project} type.
 *
 * @author Abe White
 */
public class TestProject {
  private Project _project=new Project();

  /**
   * Test the project name.
   */
  @Test
  public void testName() {
    assertNull(_project.getName());
    assertNull(new Project(null).getName());
    assertEquals("foo",new Project("foo").getName());
  }


  /**
   * Test loading classes by name.
   */
  @Test
  public void testLoadByName() {
    BCClass bc;
    BCClass bc2;
    String[] names;
    String[] names2;

    // test primitive types
    names=new String[] { "boolean", "byte", "char", "double", "float", "int", "long", "short",
        "void" };
    names2=new String[] { "Z", "B", "C", "D", "F", "I", "J", "S", "V" };
    for(int i=0; i < names.length; i++) {
      bc=_project.loadClass(names[i]);
      bc2=_project.loadClass(names2[i]);
      assertTrue(bc == bc2,names[i]);
      assertTrue(bc.isPrimitive(),names[i]);
      assertEquals(names[i],bc.getName());
    }

    // test primitive array types
    names=new String[] { "boolean[]", "byte[]", "char[]", "double[]", "float[]", "int[]", "long[]",
        "short[]" };
    names2=new String[] { "[Z", "[B", "[C", "[D", "[F", "[I", "[J", "[S" };
    for(int i=0; i < names.length; i++) {
      bc=_project.loadClass(names[i]);
      bc2=_project.loadClass(names2[i]);
      assertTrue(bc == bc2,names[i]);
      assertTrue(bc.isArray(),names[i]);
      assertEquals(names2[i],bc.getName());
    }

    // test new object type
    bc=_project.loadClass("serp.Foo");
    bc2=_project.loadClass("serp.Foo");
    assertTrue(bc == bc2);
    assertTrue(!bc.isPrimitive());
    assertTrue(!bc.isArray());
    assertEquals("serp.Foo",bc.getName());

    // test new object array type
    bc=_project.loadClass("serp.Foo[]");
    bc2=_project.loadClass("[Lserp.Foo;");
    assertTrue(bc == bc2);
    assertTrue(bc.isArray());
    assertEquals("[Lserp.Foo;",bc.getName());

    // test existing object type
    bc=_project.loadClass(String.class.getName());
    bc2=_project.loadClass(String.class);
    assertTrue(bc == bc2);
    assertTrue(!bc.isPrimitive());
    assertTrue(!bc.isArray());
    assertEquals(String.class.getName(),bc.getName());
    assertEquals("length",bc.getDeclaredMethod("length").getName());

    // test new object array type
    bc=_project.loadClass(String.class.getName() + "[]");
    bc2=_project.loadClass(String[].class);
    assertTrue(bc == bc2);
    assertTrue(bc.isArray());
    assertEquals(String[].class.getName(),bc.getName());
  }


  /**
   * Test loading classes by type.
   */
  @Test
  public void testLoadByType() {
    BCClass bc;
    BCClass bc2;
    Class[] types;
    String[] names;

    // test primitive types
    types=new Class[] { boolean.class, byte.class, char.class, double.class, float.class, int.class,
        long.class, short.class, void.class };
    names=new String[] { "Z", "B", "C", "D", "F", "I", "J", "S", "V" };
    for(int i=0; i < names.length; i++) {
      bc=_project.loadClass(types[i]);
      bc2=_project.loadClass(names[i]);
      assertTrue(bc == bc2,types[i].getName());
      assertTrue(bc.isPrimitive(),types[i].getName());
      assertEquals(types[i].getName(),bc.getName());
    }

    // test primitive array types
    types=new Class[] { boolean[].class, byte[].class, char[].class, double[].class, float[].class,
        int[].class, long[].class, short[].class, };
    names=new String[] { "[Z", "[B", "[C", "[D", "[F", "[I", "[J", "[S" };

    for(int i=0; i < names.length; i++) {
      bc=_project.loadClass(types[i]);
      bc2=_project.loadClass(names[i]);
      assertTrue(bc == bc2,types[i].getName());
      assertTrue(bc.isArray(),types[i].getName());
      assertEquals(types[i].getName(),bc.getName());
    }

    // test existing object type
    bc=_project.loadClass(String.class);
    bc2=_project.loadClass(String.class.getName());
    assertTrue(bc == bc2);
    assertTrue(!bc.isPrimitive());
    assertTrue(!bc.isArray());
    assertEquals(String.class.getName(),bc.getName());
    assertEquals("length",bc.getDeclaredMethod("length").getName());

    // test new object array type
    bc=_project.loadClass(String[].class);
    bc2=_project.loadClass(String.class.getName() + "[]");
    assertTrue(bc == bc2);
    assertTrue(bc.isArray());
    assertEquals(String[].class.getName(),bc.getName());
  }


  /**
   * Test loading classes by file.
   * @throws URISyntaxException 
   */
  @Test
  public void testLoadByFile() throws URISyntaxException {
//    File file=new File(getClass().getResource("TestProject.class").getFile());
    File file=Paths
        .get(getClass().getResource("/TestProject.classz").toURI()).toFile();

    BCClass bc=_project.loadClass(file);
    BCClass bc2=_project.loadClass(file);
    assertTrue(bc == bc2);
    assertTrue(!bc.isPrimitive());
    assertTrue(!bc.isArray());
    assertEquals(getClass().getName(),bc.getName());
    assertEquals("testName",bc.getDeclaredMethod("testName").getName());
  }


  /**
   * Test loading classes by stream.
   * @throws URISyntaxException 
   * @throws FileNotFoundException 
   */
  @Test
  public void testLoadByStream() throws URISyntaxException, FileNotFoundException {
    InputStream in=getClass().getResourceAsStream("/TestProject.classz");
    InputStream in2=getClass().getResourceAsStream("/TestProject.classz");

    BCClass bc=_project.loadClass(in);
    BCClass bc2=_project.loadClass(in2);
    assertTrue(bc == bc2);
    assertTrue(!bc.isPrimitive());
    assertTrue(!bc.isArray());
    assertEquals(getClass().getName(),bc.getName());
    assertEquals("testName",bc.getDeclaredMethod("testName").getName());
  }


  /**
   * Test retrieving all loaded classes.
   */
  @Test
  public void testGetClasses() {
    BCClass[] bcs=_project.getClasses();
    assertEquals(0,bcs.length);

    BCClass[] added=new BCClass[3];
    added[0]=_project.loadClass("int");
    added[1]=_project.loadClass("serp.Foo");
    added[2]=_project.loadClass(String[].class);

    bcs=_project.getClasses();
    assertEquals(3,bcs.length);
    int matches;
    for(int i=0; i < added.length; i++) {
      matches=0;
      for(int j=0; j < bcs.length; j++)
        if(added[i] == bcs[j]) matches++;
      assertEquals(1,matches);
    }
  }


  /**
   * Test renaming classes within the project.
   */
  @Test
  public void testRename() {
    BCClass str=_project.loadClass(String.class);
    BCClass foo=_project.loadClass("serp.Foo");

    str.setName("java.lang.String2");
    assertEquals("java.lang.String2",str.getName());
    foo.setName("serp.Foo2");
    assertEquals("serp.Foo2",foo.getName());
    try {
      str.setName("serp.Foo2");
      fail("Set to existing name");
    }
    catch(IllegalStateException ise) {
    }

    assertEquals("java.lang.String2",str.getName());
    try {
      foo.setName("java.lang.String2");
      fail("Set to existing name");
    }
    catch(IllegalStateException ise) {
    }

    assertEquals("serp.Foo2",foo.getName());

    str.setName("serp.Foo");
    assertEquals("serp.Foo",str.getName());

    foo.setName("java.lang.String");
    assertEquals("java.lang.String",foo.getName());

    assertTrue(foo == _project.loadClass(String.class));
    assertTrue(str == _project.loadClass("serp.Foo"));
  }


  /**
   * Test clearing classes.
   */
  @Test
  public void testClear() {
    _project.clear();

    BCClass bc1=_project.loadClass("int");
    BCClass bc2=_project.loadClass("serp.Foo");
    BCClass bc3=_project.loadClass(String[].class);

    assertTrue(bc1.isValid());
    assertTrue(bc2.isValid());
    assertTrue(bc3.isValid());

    assertEquals(3,_project.getClasses().length);
    _project.clear();
    assertEquals(0,_project.getClasses().length);

    // cleared classes should be invalid
    assertTrue(!bc1.isValid());
    assertTrue(!bc2.isValid());
    assertTrue(!bc3.isValid());
  }


  /**
   * Test removing a class.
   */
  @Test
  public void testRemove() {
    assertTrue(!_project.removeClass((String)null));
    assertTrue(!_project.removeClass((Class)null));
    assertTrue(!_project.removeClass((BCClass)null));

    BCClass bc1=_project.loadClass("int");
    BCClass bc2=_project.loadClass("serp.Foo");
    BCClass bc3=_project.loadClass(String[].class);

    assertTrue(bc1.isValid());
    assertTrue(bc2.isValid());
    assertTrue(bc3.isValid());

    assertTrue(!_project.removeClass(new Project().loadClass("int")));
    assertTrue(_project.removeClass(bc1));
    assertTrue(!bc1.isValid());
    assertEquals(2,_project.getClasses().length);

    assertTrue(_project.removeClass("serp.Foo"));
    assertTrue(!bc1.isValid());
    assertEquals(1,_project.getClasses().length);

    assertTrue(_project.removeClass(String[].class));
    assertTrue(!bc1.isValid());
    assertEquals(0,_project.getClasses().length);
  }
}
