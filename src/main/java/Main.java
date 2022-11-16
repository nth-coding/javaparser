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

public class Main {
    public static int fieldCount = 0;
    public static int methodCount = 0;
    public static class ClassCollector extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, List<String> collector) {
            collector.add("Class information :");

            collector.add("- Name: " + n.getNameAsString());

            StringBuilder str = new StringBuilder();
            for (int modiferCount = 0; modiferCount < n.getModifiers().size(); ++modiferCount) {
                if (modiferCount != 0) str.append(", ");
                str.append(n.getModifiers().get(modiferCount).toString());
            }
            collector.add("- Modifiers: " + str);

            collector.add("________________________________________________");
            super.visit(n, collector);
        }

        @Override
        public void visit(FieldDeclaration n, List<String> collector) {
//            collector.add("Field " + (fieldCount + 1) + " information: ");
////            collector.add(n.getModifiers().toString());
//            for (int index = 0; index < n.getVariables().size(); ++index) {
//                VariableDeclarator variable = n.getVariables().get(index);
//                collector.add("- Variable " + (index + 1) + ": " +
//                        "\n + Name: " + variable.getNameAsString() +
//                        "\n + Type: " + variable.getTypeAsString());
//
//                StringBuilder str = new StringBuilder();
//                for (int modiferCount = 0; modiferCount < n.getModifiers().size(); ++modiferCount) {
//                    if (modiferCount != 0) str.append(", ");
//                    str.append(n.getModifiers().get(modiferCount));
//                }
//                collector.add(" + Modifier: " + str);
//            }
//            fieldCount++;
            for (VariableDeclarator variable : n.getVariables()) {
                collector.add("Variable: " + variable.getNameAsString());
            }
            collector.add("________________________________________________");
            super.visit(n, collector);
        }

        @Override
        public void visit(MethodDeclaration n, List<String> collector) {
            collector.add("Method name: " + n.getNameAsString());

//            methodCount++;
            collector.add("________________________________________________");
            super.visit(n, collector);
        }
    }

    public static class FieldsCollector extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(FieldDeclaration n, List<String> collector) {
            collector.add("Field " + (fieldCount + 1) + " information: ");
//            collector.add(n.getModifiers().toString());
            for (int index = 0; index < n.getVariables().size(); ++index) {
                VariableDeclarator variable = n.getVariables().get(index);
                collector.add("- Variable " + (index + 1) + ": " +
                        "\n + Name: " + variable.getNameAsString() +
                        "\n + Type: " + variable.getTypeAsString());

                StringBuilder str = new StringBuilder();
                for (int modiferCount = 0; modiferCount < n.getModifiers().size(); ++modiferCount) {
                    if (modiferCount != 0) str.append(", ");
                    str.append(n.getModifiers().get(modiferCount));
                }
                collector.add(" + Modifier: " + str);
            }
            fieldCount++;
            collector.add("________________________________________________");
//            super.visit(n, collector);
        }
    }

    public static class MethodsCollector extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(MethodDeclaration n, List<String> collector) {
            collector.add("Method name: " + n.getNameAsString());

//            methodCount++;
            collector.add("________________________________________________");
            super.visit(n, collector);
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
