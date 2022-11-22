package nthcoding.parser;

public abstract class Test {
    protected String var1, var2;
    public final boolean var3;

    public Test(boolean var3) {
        this.var3 = var3;
    }

    public Test(String var1, String var2, boolean var3) {
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }

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