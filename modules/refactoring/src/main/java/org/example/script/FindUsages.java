package org.example.script;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;


public class FindUsages implements Runnable {
    private static String DIR = "/Users/aleksandr/IdeaProjects/jSymbolic/modules/core/src/main/java/jsymbolic2/features";
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

                classDeclaration.findAll(FieldDeclaration.class).stream()
                    .filter(f -> f.getVariables().get(0).getNameAsString().equals("dependencies"))
                    .findFirst()
                    .ifPresent(field -> {
                        field.getVariables().get(0).getInitializer().ifPresent(init -> {
                            if (init.isArrayInitializerExpr()) {
                                ArrayInitializerExpr arrayInit = init.asArrayInitializerExpr();
                                arrayInit.getValues().forEach(expr ->
                                    System.out.println("Dependency: "));
                            }
                        });
                    });
//        try {
//            FileWriter file = new FileWriter(DIR + "/" + pair.left + ".java", false);
//            file.write(unit.toString());
//            file.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
