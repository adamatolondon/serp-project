package serp.util;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Tests the {@link Strings} type.
 *
 * @author Abe White
 */
public class TestStrings {
  /**
   * Test {@link Strings#split}.
   */
  @Test
  public void testSplit() {
    String str="boo:and:foo";

    assertEquals(new String[] { "boo", "and:foo" },Strings.split(str,":",2));
    assertEquals(new String[] { "boo:and:foo" },Strings.split(str,":",1));
    assertEquals(new String[] { "boo", "and", "foo" },Strings.split(str,":",0));
    assertEquals(new String[] { "boo", "and", "foo" },Strings.split(str,":",-2));
    assertEquals(new String[] { "b", "", ":and:f", "", "" },Strings.split(str,"o",5));
    assertEquals(new String[] { "b", "", ":and:f", "o" },Strings.split(str,"o",4));
    assertEquals(new String[] { "b", "", ":and:f", "", "" },Strings.split(str,"o",-2));
    assertEquals(new String[] { "b", "", ":and:f" },Strings.split(str,"o",0));
    assertEquals(new String[] { "", "b", "", ":and:f" },Strings.split("o" + str,"o",0));
  }


  /**
   * Test {@link Strings#classForName}.
   */
  @Test
  public void testClassForName() {
    // test primitives
    Assertions.assertEquals(boolean.class,Strings.toClass("boolean",null));
    Assertions.assertEquals(byte.class,Strings.toClass("byte",null));
    Assertions.assertEquals(char.class,Strings.toClass("char",null));
    Assertions.assertEquals(double.class,Strings.toClass("double",null));
    Assertions.assertEquals(float.class,Strings.toClass("float",null));
    Assertions.assertEquals(int.class,Strings.toClass("int",null));
    Assertions.assertEquals(long.class,Strings.toClass("long",null));
    Assertions.assertEquals(short.class,Strings.toClass("short",null));
    Assertions.assertEquals(void.class,Strings.toClass("void",null));

    // test objects
    Assertions.assertEquals(String.class,Strings.toClass(String.class.getName(),null));

    // test arrays
    Assertions.assertEquals(boolean[].class,Strings.toClass("[Z",null));
    Assertions.assertEquals(byte[].class,Strings.toClass("[B",null));
    Assertions.assertEquals(char[].class,Strings.toClass("[C",null));
    Assertions.assertEquals(double[].class,Strings.toClass("[D",null));
    Assertions.assertEquals(float[].class,Strings.toClass("[F",null));
    Assertions.assertEquals(int[].class,Strings.toClass("[I",null));
    Assertions.assertEquals(long[].class,Strings.toClass("[J",null));
    Assertions.assertEquals(short[].class,Strings.toClass("[S",null));
    Assertions.assertEquals(String[].class,Strings.toClass(String[].class.getName(),null));
    Assertions.assertEquals(boolean[][].class,Strings.toClass("[[Z",null));
    Assertions.assertEquals(String[][].class,Strings.toClass(String[][].class.getName(),null));

    Assertions.assertEquals(boolean[].class,Strings.toClass("boolean[]",null));
    Assertions.assertEquals(byte[].class,Strings.toClass("byte[]",null));
    Assertions.assertEquals(char[].class,Strings.toClass("char[]",null));
    Assertions.assertEquals(double[].class,Strings.toClass("double[]",null));
    Assertions.assertEquals(float[].class,Strings.toClass("float[]",null));
    Assertions.assertEquals(int[].class,Strings.toClass("int[]",null));
    Assertions.assertEquals(long[].class,Strings.toClass("long[]",null));
    Assertions.assertEquals(short[].class,Strings.toClass("short[]",null));
    Assertions.assertEquals(String[].class,Strings.toClass("java.lang.String[]",null));

    try {
      Strings.toClass("[V",null);
      fail("Allowed invalid class name");
    }
    catch(RuntimeException re) {
    }

    try {
      Strings.toClass("java.lang.Foo",null);
      fail("Allowed invalid class name");
    }
    catch(RuntimeException re) {
    }
  }


  private void assertEquals(String[] arr1, String[] arr2) {
    Assertions.assertEquals(arr1.length,arr2.length);

    for(int i=0; i < arr1.length; i++)
      Assertions.assertEquals(arr1[i],arr2[i]);
  }

}
