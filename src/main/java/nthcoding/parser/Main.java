package nthcoding.parser;

public class Main {
    public static void main(String[] args) throws Exception {
        // Init JavaParserRun with the filepath
        JavaParserRun javaParserRun = new nthcoding.parser.JavaParserRun("src/main/java/nthcoding/parser/Test.java");

        // Get output as String
        System.out.println(javaParserRun.getOutputAsString());

        // Get output to file (output.txt)
        javaParserRun.getOutputAsFile("output.txt");
    }
}
