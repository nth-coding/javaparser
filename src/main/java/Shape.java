public class Shape {
    protected String color, hello;
    public boolean filled;

    public String toString() {
        return "Shape[color=" + this.color + ",filled=" + this.filled + "]";
    }

    private static class Cirle {
        public int cirleVari;

        private void helloCirle() {
            System.out.println();
        }
    }

    private void hello_shape() {
        System.out.println();
    }
}

class Hello8 {
    private int hello8_vari;

    public void setHello8_vari(int hello8_vari) {
        this.hello8_vari = hello8_vari;
    }

    public int getHello8_vari() {
        return hello8_vari;
    }

    private void hello8_me() {
        System.out.println();
    }
}