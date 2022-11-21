# Javaparser

This is a project which can parse and analyse your code in Java, using [JavaParser](https://javaparser.org/) library. Output will have objects' information such as name, modifiers, type. 

# How to use

You need to init `JavaParserRun` with the filepath (path from content root).
- use `getOutputToFile(String filename)` to get the output to file.
- use `getOutputToString()` to get the output as String.

# Output

For example, we can transform this java code...
<h4 a><strong><code>Test.java</code></strong></h4>

```
public class Test {
    protected String var1, var2;
    public boolean var3;

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public boolean isVar3() {
        return var3;
    }

    public void setVar3(boolean var3) {
        this.var3 = var3;
    }

    protected static class TestInner {
        public int var4;

        public int getVar4() {
            return var4;
        }

        public void setVar4(int var4) {
            this.var4 = var4;
        }

        private void helloTestInner() {
            System.out.println();
        }
    }

    private void helloTest() {
        System.out.println();
    }
}

class Test2 {
    private int var5;

    public int getVar5() {
        return var5;
    }

    public void setVar5(int var5) {
        this.var5 = var5;
    }

    private void helloTest2() {
        System.out.println();
    }
}

class A {
    static class B {
        static class C {
            static class D {
                private int varD;
            }
        }
    }
}
```

</br>
...To this.
<h4 a><strong><code>output.txt</code></strong></h4>

._________________________________________
| class <<abstract>> Test 
|_________________________________________
|    # String var1 
|    # String var2 
|    + final boolean var3 
|_________________________________________
|    + Test[boolean var3] 
|    + Test[String var1, String var2, boolean var3] 
|
|    + getVar1[] 
|    + setVar1[String var1] 
|    + getVar2[] 
|    + setVar2[String var2] 
|    + isVar3[] 
|    - helloTest[] 
+_________________________________________

._________________________________________
|    class <<static>> TestInner 
|_________________________________________
|       + int var4 
|
|       + getVar4[] 
|       + setVar4[int var4] 
|       - helloTestInner[] 
+_________________________________________

._________________________________________
| class Test2 
|_________________________________________
|    - int var5 
|
|    + getVar5[] 
|    + setVar5[int var5] 
|    - helloTest2[] 
+_________________________________________

._________________________________________
| class A 
+_________________________________________

._________________________________________
|    class <<static>> B 
+_________________________________________

._________________________________________
|       class <<static>> C 
+_________________________________________

._________________________________________
|          class <<static>> D 
|_________________________________________
|             - int varD 
+_________________________________________

```


