
package com.yilnz.bluesteel.processor;

/*
import com.google.auto.service.AutoService;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ReferenceType;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SupportedAnnotationTypes({"com.yilnz.bluesteel.processor.GenerateInterface"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class GenerateInterfaceProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "开始生成接口...");
        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                String className = ((ReferenceType)element.asType()).toString();
                System.out.println("Handling:" + className);
                try {
                    writeFile(className);
                    addInterface(className, element);
                } catch (IOException | NotFoundException | CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private String transform(CharSequence data, String simpleName){
      */
/*  CompilationUnit parse = StaticJavaParser.parse((String) data);
        Optional<TypeDeclaration<?>> first = parse.getTypes().stream().filter(e -> e.getName().equals(simpleName)).findFirst();
        if(first.isPresent()){
            first.get().add
        }*//*

        Pattern compile = Pattern.compile("class\\s*" + simpleName);
        Matcher matcher = compile.matcher(data);
        StringBuffer sb = new StringBuffer();
        if(matcher.find()){
            matcher.appendReplacement(sb, "class " + simpleName + " implements " + simpleName + "Dubbo" );
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    private void addInterface(String className, Element element) throws NotFoundException, IOException, CannotCompileException {
        int lastDot = className.lastIndexOf(".");
        String packageName = className.substring(0, lastDot);
        String classSimpleName = className.substring(lastDot + 1);
        FileObject fileObject = processingEnv.getFiler().getResource(StandardLocation.SOURCE_PATH, packageName, classSimpleName + ".java");
        CharSequence charContent = fileObject.getCharContent(false);
        String transform = transform(charContent, classSimpleName);
        FileObject fileObject2 = processingEnv.getFiler().createSourceFile(className, element);
        Writer writer = fileObject2.openWriter();
        writer.append(transform);
        writer.close();
        //processingEnv.getFiler().getResource()
       */
/* ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get(className);
        ctClass.addInterface(classPool.get(className+"Dubbo"));
        ctClass.writeFile();*//*

    }

    private void writeFile(String className) throws IOException {
        int lastDot = className.lastIndexOf(".");
        String packageName = className.substring(0, lastDot);
        String classSimpleName = className.substring(lastDot+1);
        String dubboSimpleName = classSimpleName + "Dubbo";
        String dubboName = className + "Dubbo";
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(dubboName);
        try(PrintWriter pw = new PrintWriter(sourceFile.openWriter())){
            if(packageName != null){
                pw.print("package ");
                pw.print(packageName);
                pw.println(";");
                pw.println();
            }
            pw.print("public interface ");
            pw.print(dubboSimpleName);
            pw.println("{");
            pw.println("}");
        }
    }
}
*/
