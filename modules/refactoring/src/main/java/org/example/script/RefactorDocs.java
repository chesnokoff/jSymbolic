package org.example.script;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
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
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;


public class RefactorDocs implements Runnable {
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
        ClassOrInterfaceDeclaration classDeclaration = unit.getClassByName(pair.left).get();
        classDeclaration.getMethods().forEach(methodDeclaration -> {
            if (methodDeclaration.isAnnotationPresent(
                Override.class)) {
                methodDeclaration.setJavadocComment("{@inheritDoc}");
            }
        });

        try {
            FileWriter file = new FileWriter(DIR + "/" + pair.left + ".java", false);
            file.write(unit.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
