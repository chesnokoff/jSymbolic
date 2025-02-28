package org.example.script;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class RefactorExtractorsToFeature implements Runnable {
    private static String DIR = "/Users/aleksandr/IdeaProjects/jSymbolic/jSymbolic2/src/jsymbolic2/features";
    private Map<String, Expression> map = new HashMap<>();

    private static List<ImmutablePair<String, CompilationUnit>> parseFiles() {
        List<ImmutablePair<String, CompilationUnit>> units;
        try {
            List<Path> files = Files.walk(Paths.get(DIR)).toList();
            units = files.stream().filter(path -> !new File(String.valueOf(path)).isDirectory())
                    .map(file -> {
                        try {
                            return ImmutablePair.of(FilenameUtils.getBaseName(file.getFileName().toString()), StaticJavaParser.parse(Files.newInputStream(file)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return units;
    }

    @Override
    public void run() {
        List<ImmutablePair<String, CompilationUnit>> units = parseFiles();
        for (ImmutablePair<String, CompilationUnit> unit : units)
            try {
                process(unit);
            } catch (Exception e) {
            }
    }

    private void process(ImmutablePair<String, CompilationUnit> pair) {
        CompilationUnit unit = pair.right;
        VoidVisitor<Void> exprVisitor = new ExprVisitor();
        VoidVisitor<Void> fieldDeclVisitor = new FieldDeclVisitor();
        Optional<ClassOrInterfaceDeclaration> oldClass = unit.getClassByName(pair.left);
        exprVisitor.visit(unit, null);
        fieldDeclVisitor.visit(unit, null);
        CompilationUnit newUnit = new CompilationUnit().setPackageDeclaration("jsymbolic2.features");
        newUnit.addImport("jsymbolic2.featureutils.Feature");
        for (ImportDeclaration anImport : unit.getImports()) newUnit.addImport(anImport);
        ClassOrInterfaceDeclaration classDeclaration = newUnit.addClass(pair.left)
                .setJavadocComment(oldClass.get().getJavadoc().get())
                .addImplementedType("Feature");
        for (var entry : map.entrySet()) {
            if (entry.getKey().contains("getName"))
                addMethod(classDeclaration, "getName", entry.getValue(), "String");
            if (entry.getKey().contains("getDescription"))
                addMethod(classDeclaration, "getDescription", entry.getValue(), "String");
            if (entry.getKey().contains("isSequential"))
                addMethod(classDeclaration, "isSequential", entry.getValue(), "boolean");
            if (entry.getKey().contains("getDimensions"))
                addMethod(classDeclaration, "getDimensions", entry.getValue(), "int");
            if (entry.getKey().contains("getDependencies"))
                addMethod(classDeclaration, "getDependencies", entry.getValue(), "String[]");
            if (entry.getKey().contains("getCode"))
                addMethod(classDeclaration, "getCode", entry.getValue(), "String");
            if (entry.getKey().contains("getOffsets"))
                addMethod(classDeclaration, "getDependencyOffsets", entry.getValue(), "int[]");
        }
        MethodDeclaration oldMethod = oldClass.get().getMethodsByName("extractFeature").get(0);
        classDeclaration.addMethod("extractFeature")
                .setPublic(true)
                .addAnnotation(Override.class)
                .setType("double[]")
                .setBody(oldMethod.getBody().get())
                .setParameters(oldMethod.getParameters()).addThrownException(Exception.class);
        try {
            FileWriter file = new FileWriter(DIR + "/" + pair.left + ".java", false);
            file.write(newUnit.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMethod(ClassOrInterfaceDeclaration classOrInterfaceDeclaration,
                           String name, Expression value, String returns) {
        if (value.toString().contains("number_of_chord_types")) {
            {
                FieldDeclaration fieldDeclaration = classOrInterfaceDeclaration.addField("int", "number_of_chord_types").setFinal(true).setPrivate(true);
                fieldDeclaration.getVariable(0).setInitializer("ChordTypeEnum.values().length");
            }
        }

        classOrInterfaceDeclaration.addMethod(name).setPublic(true)
                .addAnnotation(Override.class)
                .setType(returns).setBody(new BlockStmt().addStatement(new ReturnStmt(value)));
    }

    private class ExprVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(AssignExpr expr, Void arg) {
            super.visit(expr, arg);
            if (expr.getTarget().toString().contains("code")) map.put("getCode", expr.getValue());
            if (expr.getTarget().toString().contains("dependencies")) map.put("getDependencies", expr.getValue());
            if (expr.getTarget().toString().contains("offsets")) map.put("getOffsets", expr.getValue());
        }
    }

    private class FieldDeclVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(VariableDeclarator expr, Void arg) {
            super.visit(expr, arg);
            if (expr.getName().toString().contains("name")) map.put("getName", expr.getInitializer().get());
            if (expr.getName().toString().contains("description")) map.put("getDescription", expr.getInitializer().get());
            if (expr.getName().toString().contains("is_sequential")) map.put("isSequential", expr.getInitializer().get());
            if (expr.getName().toString().contains("dimensions")) map.put("getDimensions", expr.getInitializer().get());
        }
    }
}
