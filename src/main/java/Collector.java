import com.github.javaparser.Position;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javassist.compiler.ast.Declarator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    ;
    private static final List<PositionData> line = new ArrayList<>();

    /**
     * get getModifier (modifierCode).
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
    private static String getClassInfo(ClassOrInterfaceDeclaration n) {
        StringBuilder str = new StringBuilder();
        str.append("._________________________________________\n");
//        str.append("|          ");
        str.append(getSpace(n));

        str.append("class ");

        // Class does not have access modifier -> private
        for (int modiferCount = 0; modiferCount < n.getModifiers().size(); ++modiferCount) {
            Modifier modifier = n.getModifiers().get(modiferCount);
            if (getAccessModifier(modifier).length() != 1 && !getAccessModifier(modifier).equals("static"))
                str.append(getAccessModifier(modifier)).append(" ");

            if (getAccessModifier(modifier).equals("static")) {
                str.append("<<").append(getAccessModifier(modifier)).append(">> ");

            }
        }
        str.append(n.getNameAsString()).append(" \n");

        // Fields
        if (n.getFields().size() != 0) {
            str.append("|_________________________________________\n");
            str.append(getFieldTotalInfo(n.getFields()));
        }

        // Constructors
        if (n.getConstructors().size() != 0) {
            str.append("|_________________________________________\n");
            str.append(getConstructorTotalInfo(n.getConstructors()));
        }

        // Methods
        if (n.getMethods().size() != 0) {
            str.append("|\n");
            str.append(getMethodTotalInfo(n.getMethods()));
        }

        str.append("+_________________________________________\n\n");

        return str.toString();
    }

    /**
     * get all the information of fields.
     * @param fields List<FieldDeclaration>
     * @return String
     */
    private static String getFieldTotalInfo(List<FieldDeclaration> fields) {
        StringBuilder str = new StringBuilder();

        for (FieldDeclaration field : fields) {
            for (int i = 0; i < field.getVariables().size(); ++i) {
                str.append(Collector.getSpace(field));

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
     * @param constructors List<ConstructorDeclaration>
     * @return String
     */
    private static String getConstructorTotalInfo(List<ConstructorDeclaration> constructors) {
        StringBuilder str = new StringBuilder();

        for (ConstructorDeclaration constructor : constructors) {
            str.append(Collector.getSpace(constructor));

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
     * @param methods List<MethodDeclaration>
     * @return String
     */
    private static String getMethodTotalInfo(List<MethodDeclaration> methods) {
        StringBuilder str = new StringBuilder();

        for (MethodDeclaration method : methods) {
            str.append(Collector.getSpace(method));

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
     * get indents and spacing.
     *
     * @param n   T
     * @param <T> extend Node
     * @return (String) spacing
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

//    private static <T extends Node> String getOuter(T n) {
//        StringBuilder str = new StringBuilder();
//        Optional<Position> begin = n.getBegin();
//        Optional<Position> end = n.getEnd();
//
//        if (begin.isPresent() && end.isPresent()) {
//            for (PositionData present : line) {
//                if (begin.get().isBefore(present.getEnd()) && begin.get().isAfter(present.getStart())) {
//
//                }
//            }
//        }
//        return str.toString();
//    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<String> collector) {
        // New class found -> Tab in
        if (n.getBegin().isPresent() && n.getEnd().isPresent()) {
            line.add(new PositionData(n.getBegin().get(), n.getEnd().get()));
        }

        collector.add(getClassInfo(n));

        super.visit(n, collector);
    }
}