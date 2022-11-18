import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JavaParserRun {
    private static List<String> output = new ArrayList<>();

    /**
     * constructor will print out the result or save it in output.txt.
     * @param path (String) path from content root of the file you wanted to parse
     * @throws IOException in/out exception
     */
    public JavaParserRun(String path) throws IOException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(Files.newInputStream(Paths.get(path)));

        // Beautify
        output.add("._________________________________________\n");

        // Create class visitor and visit.
        VoidVisitor<List<String>> classVisitor = new Collector();
        classVisitor.visit(compilationUnit, output);
    }

    public String getOutputAsString() {
        StringBuilder str = new StringBuilder();
        for (String s : output) {
            str.append(s);
        }
        return str.toString();
    }

    public void getOutputAsFile(String filename) throws IOException {
        OutputStream os = Files.newOutputStream(Paths.get(filename));
        for (String op : output) {
            os.write(op.getBytes());
        }
        os.flush();

        //close the stream
        os.close();
    }
}
