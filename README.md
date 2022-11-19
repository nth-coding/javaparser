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

```
._________________________________________
|* Class information:
| - Name: Test
| - Modifier: public 
|_________________________________________
|   Field information:
|    - Variable 1: 
|     + Name: var1
|     + Type: String
|     + Modifier: protected 
|    - Variable 2: 
|     + Name: var2
|     + Type: String
|     + Modifier: protected 
|_________________________________________
|   Field information:
|    - Variable 1: 
|     + Name: var3
|     + Type: boolean
|     + Modifier: public 
|_________________________________________
|   Method information:
|    - Name: getVar1
|    - Type: String
|    - Modifier: public 
|_________________________________________
|   Method information:
|    - Name: setVar1
|    - Type: void
|    - Modifier: public 
|_________________________________________
|   Method information:
|    - Name: getVar2
|    - Type: String
|    - Modifier: public 
|_________________________________________
|   Method information:
|    - Name: setVar2
|    - Type: void
|    - Modifier: public 
|_________________________________________
|   Method information:
|    - Name: isVar3
|    - Type: boolean
|    - Modifier: public 
|_________________________________________
|   Method information:
|    - Name: setVar3
|    - Type: void
|    - Modifier: public 
|_________________________________________
|   * Class information:
|    - Name: TestInner
|    - Modifier: protected , static 
|_________________________________________
|      Field information:
|       - Variable 1: 
|        + Name: var4
|        + Type: int
|        + Modifier: public 
|_________________________________________
|      Method information:
|       - Name: getVar4
|       - Type: int
|       - Modifier: public 
|_________________________________________
|      Method information:
|       - Name: setVar4
|       - Type: void
|       - Modifier: public 
|_________________________________________
|      Method information:
|       - Name: helloTestInner
|       - Type: void
|       - Modifier: private 
|_________________________________________
|   Method information:
|    - Name: helloTest
|    - Type: void
|    - Modifier: private 
|_________________________________________
|* Class information:
| - Name: Test2
| - Modifier: private
|_________________________________________
|   Field information:
|    - Variable 1: 
|     + Name: var5
|     + Type: int
|     + Modifier: private 
|_________________________________________
|   Method information:
|    - Name: getVar5
|    - Type: int
|    - Modifier: public 
|_________________________________________
|   Method information:
|    - Name: setVar5
|    - Type: void
|    - Modifier: public 
|_________________________________________
|   Method information:
|    - Name: helloTest2
|    - Type: void
|    - Modifier: private 
|_________________________________________
|* Class information:
| - Name: A
| - Modifier: private
|_________________________________________
|   * Class information:
|    - Name: B
|    - Modifier: static 
|_________________________________________
|      * Class information:
|       - Name: C
|       - Modifier: static 
|_________________________________________
|         * Class information:
|          - Name: D
|          - Modifier: static 
|_________________________________________
|            Field information:
|             - Variable 1: 
|              + Name: varD
|              + Type: int
|              + Modifier: private 
|_________________________________________

```


