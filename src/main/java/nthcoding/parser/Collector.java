package nthcoding.parser;

import com.github.javaparser.Position;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import wagu.Board;
import wagu.Table;

import java.util.*;

public class Collector extends VoidVisitorAdapter<List<String>> {
    enum modifierCode {
        PRIVATE,
        PUBLIC,
        PROTECTED,
        DEFAULT,
        STATIC,
        ABSTRACT,
        FINAL,
    }
    private static final List<ClassOrInterfaceDeclaration> classes = new ArrayList<>();
    private static List<String> output = new ArrayList<>();
    private static final List<String> header = new ArrayList<>();

    /**
     * get getModifier (modifierCode).
     *
     * @param modifier Modifier
     * @return modifierCode
     */
    private static modifierCode getModifierCode(Modifier modifier) {
        if (Objects.equals(modifier, Modifier.privateModifier())) return modifierCode.PRIVATE;
        else if (Objects.equals(modifier, Modifier.publicModifier())) return modifierCode.PUBLIC;
        else if (Objects.equals(modifier, Modifier.protectedModifier())) return modifierCode.PROTECTED;
        else if (Objects.equals(modifier, Modifier.abstractModifier())) return modifierCode.ABSTRACT;
        else if (Objects.equals(modifier, Modifier.staticModifier())) return modifierCode.STATIC;
        return modifierCode.FINAL;
    }

    private static String getAccessModifier(Modifier modifier) {
        modifierCode modi = getModifierCode(modifier);

        String code = null;
        switch (modi) {
            case PRIVATE:
                code = "-";
                break;
            case PUBLIC:
                code = "+";
                break;
            case PROTECTED:
                code = "#";
                break;
            case FINAL:
                code = "final";
                break;
            case STATIC:
                code = "static";
                break;
            case ABSTRACT:
                code = "<<abstract>>";
                break;
            case DEFAULT:
                code = "default";
                break;
        }
        return code;
    }

    /**
     * get information of class object.
     *
     * @param n ClassDeclaration
     * @return info of class
     */
    private static List<String> getClassInfo(ClassOrInterfaceDeclaration n) {
        StringBuilder str = new StringBuilder();

        // Class does not have access modifier -> private
        for (int modiferCount = 0; modiferCount < n.getModifiers().size(); ++modiferCount) {
            Modifier modifier = n.getModifiers().get(modiferCount);
            if (getAccessModifier(modifier).length() != 1 && !getAccessModifier(modifier).equals("static"))
                str.append(getAccessModifier(modifier)).append(" ");

            if (getAccessModifier(modifier).equals("static")) {
                str.append("<<").append(getAccessModifier(modifier)).append(">> ");
            }
        }

        if (!getOuter(n).isEmpty()) {
            str.append("(").append(getOuter(n)).append(") ");
        }
        str.append(n.getNameAsString()).append("\n");


        // spacing and indent
        StringBuilder spacing = new StringBuilder();
        for (int i = 0; i < (73 - str.length()) / 2; ++i) {
            spacing.append(" ");
        }

        header.add(spacing + str.toString());

        // Fields
        if (n.getFields().size() != 0) {
            output.add(getFieldTotalInfo(n.getFields()));
        }

        // Constructors
        if (n.getConstructors().size() != 0) {
            output.add(getConstructorTotalInfo(n.getConstructors()));
        }

        // Methods
        if (n.getMethods().size() != 0) {
            output.add(getMethodTotalInfo(n.getMethods()));
        }

        return output;
    }

    /**
     * get all the information of fields.
     *
     * @param fields List<FieldDeclaration>
     * @return String
     */
    private static String getFieldTotalInfo(List<FieldDeclaration> fields) {
        StringBuilder str = new StringBuilder();

        for (FieldDeclaration field : fields) {
            for (int i = 0; i < field.getVariables().size(); ++i) {

                VariableDeclarator variable = field.getVariables().get(i);
                for (int modiferCount = 0; modiferCount < field.getModifiers().size(); ++modiferCount) {
                    Modifier modifier = field.getModifiers().get(modiferCount);
                    str.append(getAccessModifier(modifier)).append(" ");
                }
                str.append(variable.getTypeAsString()).append(" ");
                str.append(variable.getNameAsString()).append(" ");

                str.append("\n");
            }
        }
        return str.toString();
    }

    /**
     * get all the information of constructors.
     *
     * @param constructors List<ConstructorDeclaration>
     * @return String
     */
    private static String getConstructorTotalInfo(List<ConstructorDeclaration> constructors) {
        StringBuilder str = new StringBuilder();

        for (ConstructorDeclaration constructor : constructors) {
//            str.append(Collector.getSpace(constructor));

            for (int modiferCount = 0; modiferCount < constructor.getModifiers().size(); ++modiferCount) {
                Modifier modifier = constructor.getModifiers().get(modiferCount);
                str.append(getAccessModifier(modifier)).append(" ");
            }
            str.append(constructor.getNameAsString());
            str.append(constructor.getParameters()).append(" ");

            str.append("\n");
        }

        return str.toString();
    }

    /**
     * get all the information of methods.
     *
     * @param methods List<MethodDeclaration>
     * @return String
     */
    private static String getMethodTotalInfo(List<MethodDeclaration> methods) {
        StringBuilder str = new StringBuilder();

        for (MethodDeclaration method : methods) {
//            str.append(Collector.getSpace(method));

            for (int modiferCount = 0; modiferCount < method.getModifiers().size(); ++modiferCount) {
                Modifier modifier = method.getModifiers().get(modiferCount);
                str.append(getAccessModifier(modifier)).append(" ");
            }
            str.append(method.getNameAsString());
            str.append(method.getParameters()).append(" ");

            str.append("\n");
        }
        return str.toString();
    }

    /**
     * get class outer this class.
     *
     * @param n   T
     * @param <T> extend Node
     * @return (String) spacing
     */
    private static <T extends Node> String getOuter(T n) {
        StringBuilder space = new StringBuilder();
        Optional<Position> begin = n.getBegin();
        Optional<Position> end = n.getEnd();

        if (begin.isPresent() && end.isPresent()) {

            for (ClassOrInterfaceDeclaration present : classes) {
                if (present.getBegin().isPresent() && present.getEnd().isPresent()) {
                    PositionData positionData = new PositionData(present.getBegin().get(), present.getEnd().get());

                    if (begin.get().isBefore(positionData.getEnd()) && begin.get().isAfter(positionData.getStart())) {
                        space.append(present.getNameAsString()).append(".");
                    }

                }
            }
        }
        return space.toString();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<String> collector) {
        header.clear();

        // Add class to the list
        classes.add(n);

        output = getClassInfo(n);
        Board board = new Board(75);

        List<List<String>> row = new ArrayList<>();
        for (String out : output) {
            row.add(Collections.singletonList(out));
        }

        String tableString = board.setInitialBlock(
                new Table(board, 75, header, row).tableToBlocks()).build().getPreview();

        tableString += "\n";
        collector.add(tableString);

        super.visit(n, collector);
    }
}