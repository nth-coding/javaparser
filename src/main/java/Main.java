import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    public static List<PositionData> line = new ArrayList<>();
    public static class ClassCollector extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, List<String> collector) {
            StringBuilder space = new StringBuilder();
            Optional<Position> begin = n.getBegin();
            Optional<Position> end = n.getEnd();
            if (begin.isPresent() && end.isPresent()) {
//            collector.add("Class information :");

                for (PositionData present : line) {
                    if (begin.get().isBefore(present.getEnd()) && begin.get().isAfter(present.getStart())) {
                        space.append("   ");
                    }
                }
                collector.add(space + "Class Name: " + n.getNameAsString());

                line.add(new PositionData(begin.get(), end.get()));

                super.visit(n, collector);
            }
        }

        @Override
        public void visit(FieldDeclaration n, List<String> collector) {
            StringBuilder space = new StringBuilder();
            Optional<Position> begin = n.getBegin();
            Optional<Position> end = n.getEnd();
            if (begin.isPresent() && end.isPresent()) {
                for (PositionData present : line) {
                    if (begin.get().isBefore(present.getEnd()) && begin.get().isAfter(present.getStart())) {
                        space.append("   ");
                    }
                }

                for (VariableDeclarator variable : n.getVariables()) {
                    collector.add(space + "Variable: " + variable.getNameAsString());
                }
//            collector.add("________________________________________________");
                super.visit(n, collector);
            }
        }

        @Override
        public void visit(MethodDeclaration n, List<String> collector) {
            StringBuilder space = new StringBuilder();
            Optional<Position> begin = n.getBegin();
            Optional<Position> end = n.getEnd();
            if (begin.isPresent() && end.isPresent()) {
                for (PositionData present : line) {
                    if (begin.get().isBefore(present.getEnd()) && begin.get().isAfter(present.getStart())) {
                        space.append("   ");
                    }
                }
                collector.add(space + "Method: " + n.getNameAsString());

//            methodCount++;
//            collector.add("________________________________________________");
                super.visit(n, collector);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        String filename = "src/main/java/Shape.java";
        CompilationUnit compilationUnit = StaticJavaParser.parse(Files.newInputStream(Paths.get(filename)));

        List<String> output = new ArrayList<>();

        // Create class visitor and visit.
        VoidVisitor<List<String>> classVisitor = new ClassCollector();
        classVisitor.visit(compilationUnit, output);

        output.forEach(System.out::println);
    }
}
