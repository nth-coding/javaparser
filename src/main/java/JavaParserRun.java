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
    /**
     * constructor will print out the result or save it in output.txt.
     * @param path (String) path from content root of the file you wanted to parse
     * @param outfile (boolean) true if you want to output to file, false if not
     * @throws IOException in/out exception
     */
    public JavaParserRun(String path, boolean outfile) throws IOException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(Files.newInputStream(Paths.get(path)));
        List<String> output = new ArrayList<>();

        // Beautify
        output.add("._________________________________________\n");

        // Create class visitor and visit.
        VoidVisitor<List<String>> classVisitor = new Collector();
        classVisitor.visit(compilationUnit, output);

        if (outfile) {
            OutputStream os = Files.newOutputStream(Paths.get("output.txt"));
            for (String op : output) {
                os.write(op.getBytes());
            }

            os.flush();
            //close the stream
            os.close();
        } else {
            output.forEach(System.out::println);
        }
    }
}
