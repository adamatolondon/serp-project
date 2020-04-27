package serp.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import serp.bytecode.lowlevel.ClassEntry;
import serp.bytecode.lowlevel.ConstantPool;
import serp.bytecode.lowlevel.ConstantPoolTable;
import serp.bytecode.lowlevel.Entry;
import serp.bytecode.lowlevel.InvokeDynamicEntry;
import serp.bytecode.lowlevel.MethodEntry;
import serp.bytecode.lowlevel.MethodHandleEntry;
import serp.bytecode.lowlevel.MethodTypeEntry;
import serp.bytecode.lowlevel.NameAndTypeEntry;
import serp.bytecode.lowlevel.UTF8Entry;


public class TestJava8Lambda {
  private File simpleLambdaUsingClassFile;

  @BeforeEach
  void init() throws URISyntaxException {
    simpleLambdaUsingClassFile=Paths
        .get(getClass().getResource("/java8/SimpleLambdaUsingClass.classz").toURI()).toFile();
  }


  @Test
  public void testLoadClassFile() throws URISyntaxException {
    System.out
        .println("simpleLambdaUsingClassFile = " + simpleLambdaUsingClassFile.getAbsolutePath());

    Project project=new Project();
    BCClass lClass=project.loadClass(simpleLambdaUsingClassFile);
    assertNotNull(lClass);

    assertEquals(52,lClass.getMajorVersion());
    assertEquals(0,lClass.getMinorVersion());

    project.clear();
  }


  /*
   * Looks for:
   * 
   * #15 = NameAndType #16:#17 // test:()Ljava/util/function/Predicate; #16 =
   * Utf8 test #17 = Utf8 ()Ljava/util/function/Predicate; #38 = InvokeDynamic
   * #1:#15 // #1:test:()Ljava/util/function/Predicate;
   * 
   */
  @Test
  public void testInvokeDynamicConstantPoolEntry() {
    Project project=new Project();
    BCClass lClass=project.loadClass(simpleLambdaUsingClassFile);
    assertNotNull(lClass);

    ConstantPool cp=lClass.getPool();
    assertNotNull(cp);

    // #38 = InvokeDynamic #1:#15 // #1:test:()Ljava/util/function/Predicate;
    Entry ent38=cp.getEntry(6);
    assertNotNull(ent38);
    {
      assertTrue(ent38 instanceof InvokeDynamicEntry);

      InvokeDynamicEntry ide=(InvokeDynamicEntry)ent38;
      assertEquals(1,ide.getBootstrapMethodAttrIndex());
      assertEquals(27,ide.getNameAndTypeIndex());

      NameAndTypeEntry nate=ide.getNameAndTypeEntry();
      assertNotNull(nate);
      assertEquals(38,nate.getNameIndex());
      assertEquals(39,nate.getDescriptorIndex());

      UTF8Entry nameEntry=nate.getNameEntry();
      assertNotNull(nameEntry);
      assertEquals("test",nameEntry.getValue());

      UTF8Entry typeEntry=(UTF8Entry)cp.getEntry(nate.getDescriptorIndex());
      assertNotNull(typeEntry);
      assertEquals("()Ljava/util/function/Predicate;",typeEntry.getValue());
    }
  }


  /**
   * Looks for:
   * 
   * #46 = Methodref #47.#49 // java/lang/invoke/LambdaMetafactory.metafactory:
   * (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;
   * Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)
   * Ljava/lang/invoke/CallSite; #47 = Class #48 //
   * java/lang/invoke/LambdaMetafactory #48 = Utf8
   * java/lang/invoke/LambdaMetafactory #52 = MethodHandle #6:#46 //
   * invokestatic java/lang/invoke/LambdaMetafactory.
   * metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/
   * MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/
   * MethodType;)Ljava/lang/invoke/CallSite;
   */
  @Test
  public void testMethodHandleConstantPoolEntry() {
    Project project=new Project();
    BCClass lClass=project.loadClass(simpleLambdaUsingClassFile);
    assertNotNull(lClass);

    ConstantPool cp=lClass.getPool();
    assertNotNull(cp);

    // #52 = MethodHandle #6:#46 // invokestatic
    // java/lang/invoke/LambdaMetafactory.
    // metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/
    // MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/
    // MethodType;)Ljava/lang/invoke/CallSite;

    Entry ent52=cp.getEntry(23);
    assertNotNull(ent52);
    {
      assertTrue(ent52 instanceof MethodHandleEntry);

      MethodHandleEntry mhe=(MethodHandleEntry)ent52;
      assertEquals(6,mhe.getReferenceKind());

      Entry mheEnt=mhe.getReference();
      assertNotNull(mheEnt);
      assertEquals(35,mheEnt.getIndex());

      assertTrue(mheEnt instanceof MethodEntry);
      MethodEntry me=(MethodEntry)mheEnt;
      assertEquals(43,me.getClassIndex());
      assertEquals(44,me.getNameAndTypeIndex());

      ClassEntry ce=me.getClassEntry();
      assertNotNull(ce);
      assertEquals(47,ce.getNameIndex());
      assertEquals("java/lang/invoke/LambdaMetafactory",ce.getNameEntry().getValue());

      NameAndTypeEntry nate=me.getNameAndTypeEntry();
      assertNotNull(nate);
      assertEquals(48,nate.getNameIndex());
      assertEquals(52,nate.getDescriptorIndex());

      UTF8Entry nameEntry=nate.getNameEntry();
      assertNotNull(nameEntry);
      assertEquals("metafactory",nameEntry.getValue());

      String callSiteStr="(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;"
          + "Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;"
          + "Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)"
          + "Ljava/lang/invoke/CallSite;";
      UTF8Entry typeEntry=(UTF8Entry)cp.getEntry(nate.getDescriptorIndex());
      assertNotNull(typeEntry);
      assertEquals(callSiteStr,typeEntry.getValue());
    }
  }


  /**
   * Looks for:
   * 
   * #28 = Utf8 (Ljava/lang/Object;)Z #53 = MethodType #28 //
   * (Ljava/lang/Object;)Z
   */
  @Test
  public void testMethodTypeConstantPoolEntry() {
    Project project=new Project();
    BCClass lClass=project.loadClass(simpleLambdaUsingClassFile);
    assertNotNull(lClass);

    ConstantPool cp=lClass.getPool();
    assertNotNull(cp);

    // #53 = MethodType #28 // (Ljava/lang/Object;)Z
    Entry ent53=cp.getEntry(24);
    assertNotNull(ent53);
    {
      assertTrue(ent53 instanceof MethodTypeEntry);
      MethodTypeEntry mte=(MethodTypeEntry)ent53;
      assertEquals("(Ljava/lang/Object;)Z",mte.getMethodDescriptorEntry().getValue());
    }
  }


  @Test
  public void testInvokeDynamicInstruction() {
    Project project=new Project();
    BCClass lClass=project.loadClass(simpleLambdaUsingClassFile);
    assertNotNull(lClass);

    BCMethod[] methods=lClass.getMethods();
    assertNotNull(methods);

    BCMethod doSomethingWithExpressionLambdasMethod=null;
    for(int index=0; index < methods.length; index++) {
      if("doSomethingWithExpressionLambdas".equals(methods[index].getName())) {
        doSomethingWithExpressionLambdasMethod=methods[index];
        break;
      }
    }
    assertNotNull(doSomethingWithExpressionLambdasMethod);

    Code c=doSomethingWithExpressionLambdasMethod.getCode(false);
    assertNotNull(c);
    assertEquals(12,c.size());

    c.beforeFirst();
    Instruction i=null;
    MethodInstruction idInst=null;
    while(c.hasNext() && (i=c.next()) != null) {
      if(i.getOpcode() == Constants.INVOKEDYNAMIC) {
        idInst=(MethodInstruction)i;
        break;
      }
    }
    assertNotNull(idInst);
    assertEquals(2,idInst.getMethodIndex());
  }


  @Test
  public void testConstantPoolTableParse() throws Exception {
    DataInputStream dis=new DataInputStream(new FileInputStream(simpleLambdaUsingClassFile));
    byte[] bytecode=new byte[(int)simpleLambdaUsingClassFile.length()];

    try {
      int pos=0;
      while(pos < bytecode.length) {
        int bytesRead=dis.read(bytecode,pos,(bytecode.length - pos));
        if(bytesRead > 0) {
          pos+=bytesRead;
        }
      }

      int endIndex=ConstantPoolTable.getEndIndex(bytecode);
      assertEquals((10 + 934),endIndex);
    }
    finally {
      if(dis != null) {
        try {
          dis.close();
        }
        catch(Exception e) {
          // Swallow
        }
      }
    }
  }
}
