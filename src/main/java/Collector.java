import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Collector extends VoidVisitorAdapter<List<String>> {
    private static final List<PositionData> line = new ArrayList<>();

    /**
     * get information of class object.
     * @param n ClassDeclaration
     * @return info of class
     */
    private static String getClassInfo(ClassOrInterfaceDeclaration n) {
        StringBuilder str = new StringBuilder();

        str.append(Collector.getSpace(n)).append("Class information: (*)\n");

        str.append(Collector.getSpace(n)).append(" - Name: ").append(n.getNameAsString()).append("\n");

        str.append(Collector.getSpace(n)).append(" - Modifier: ");

        // Class does not have access modifier -> private
        if (n.getModifiers().size() == 0 ||
                (n.getModifiers().size() == 1 && n.getModifiers().get(0).toString().equals("static"))) {
            str.append("private");
        } else {
            for (int modiferCount = 0; modiferCount < n.getModifiers().size(); ++modiferCount) {
                if (modiferCount != 0) str.append(", ");
                str.append(n.getModifiers().get(modiferCount).toString());
            }
        }

        str.append("\n").append("|_________________________________________\n");
        return str.toString();
    }

    /**
     * get information of field object.
     * @param n FieldDeclaration
     * @return info of field
     */
    private static String getFieldInfo(FieldDeclaration n) {
        StringBuilder str = new StringBuilder();

        str.append(Collector.getSpace(n)).append("Field information:");
            for (int index = 0; index < n.getVariables().size(); ++index) {
                VariableDeclarator variable = n.getVariables().get(index);
                str.append("\n").append(Collector.getSpace(n)).append(" - Variable ").append(index + 1).append(": ")
                        .append("\n").append(Collector.getSpace(n)).append("  + Name: ").append(variable.getNameAsString())
                        .append("\n").append(Collector.getSpace(n)).append("  + Type: ").append(variable.getTypeAsString())
                        .append("\n").append(Collector.getSpace(n)).append("  + Modifier: ");

                for (int modiferCount = 0; modiferCount < n.getModifiers().size(); ++modiferCount) {
                    if (modiferCount != 0) str.append(", ");
                    str.append(n.getModifiers().get(modiferCount));
                }
            }

        str.append("\n").append("|_________________________________________\n");
        return str.toString();
    }

    /**
     * get information of method object.
     * @param n MethodDeclaration
     * @return info of method
     */
    private static String getMethodInfo(MethodDeclaration n) {
        StringBuilder str = new StringBuilder();

        str.append(Collector.getSpace(n)).append("Method information:\n");

        str.append(Collector.getSpace(n)).append(" - Name: ").append(n.getNameAsString()).append("\n");
        str.append(Collector.getSpace(n)).append(" - Type: ").append(n.getTypeAsString()).append("\n");

        str.append(Collector.getSpace(n)).append(" - Modifier: ");
        for (int modiferCount = 0; modiferCount < n.getModifiers().size(); ++modiferCount) {
            if (modiferCount != 0) str.append(", ");
            str.append(n.getModifiers().get(modiferCount).toString());
        }

        str.append("\n").append("|_________________________________________\n");
        return str.toString();
    }

    /**
     * get indents and spacing.
     * @param n T
     * @return (String) spacing
     * @param <T> extend Node
     */
    private static <T extends Node> String getSpace(T n) {
        StringBuilder space = new StringBuilder();
        space.append("| ");
        Optional<Position> begin = n.getBegin();
        Optional<Position> end = n.getEnd();

        if (begin.isPresent() && end.isPresent()) {
            for (PositionData present : line) {
                if (begin.get().isBefore(present.getEnd()) && begin.get().isAfter(present.getStart())) {
                    space.append("   ");
                }
            }
        }
        return space.toString();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<String> collector) {
        collector.add(getClassInfo(n));

        // New class found -> Tab in
        if (n.getBegin().isPresent() && n.getEnd().isPresent()) {
            line.add(new PositionData(n.getBegin().get(), n.getEnd().get()));
        }
        super.visit(n, collector);
    }

    @Override
    public void visit(FieldDeclaration n, List<String> collector) {
        collector.add(getFieldInfo(n));
        super.visit(n, collector);
    }

    @Override
    public void visit(MethodDeclaration n, List<String> collector) {
        collector.add(getMethodInfo(n));
        super.visit(n, collector);
    }
}