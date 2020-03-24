package com.richzjc.netcompiler;

import com.google.auto.service.AutoService;
import com.richzjc.netannotation.NetAvailable;
import com.richzjc.netannotation.NetChange;
import com.richzjc.netannotation.NetLose;
import com.richzjc.netannotation.nettype.NetType;
import com.richzjc.netcompiler.util.EmptyUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class NetCompiler extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Filer filer;

    private String packageName;
    private String className;

    private Map<TypeElement, List<ExecutableElement>> availableMethods = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> loseMethods = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> changeMethods = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        parsePackage(processingEnv);
    }

    private void parsePackage(ProcessingEnvironment processingEnv) {
        packageName = processingEnv.getOptions().get("moduleName");
        packageName = "com.richzjc." + packageName.toLowerCase();
        if (!EmptyUtils.isEmpty(packageName)) {
            className = "NetChanger";
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(NetAvailable.class.getName());
        set.add(NetLose.class.getName());
        set.add(NetChange.class.getName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!EmptyUtils.isEmpty(annotations)) {
            try {
                Set<? extends Element> availables = roundEnv.getElementsAnnotatedWith(NetAvailable.class);
                Set<? extends Element> loses = roundEnv.getElementsAnnotatedWith(NetLose.class);
                Set<? extends Element> changes = roundEnv.getElementsAnnotatedWith(NetChange.class);
                parseAvailabe(availables);
                parseLose(loses);
                parseChange(changes);
                generateFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void parseAvailabe(Set<? extends Element> availables) {
        if (availables != null && !availables.isEmpty()) {
            for (Element element : availables) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!availableMethods.containsKey(typeElement)) {
                    List<ExecutableElement> method = new ArrayList<>();
                    availableMethods.put(typeElement, method);
                }
                List<ExecutableElement> methods = availableMethods.get(typeElement);
                List list = element.getEnclosedElements();
                if (list != null && list.size() > 0) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "不能够带有参数");
                } else {
                    methods.add((ExecutableElement) element);
                }
            }
        }
    }

    private void parseLose(Set<? extends Element> lose) {
        if (lose != null && !lose.isEmpty()) {
            for (Element element : lose) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!loseMethods.containsKey(typeElement)) {
                    List<ExecutableElement> method = new ArrayList<>();
                    loseMethods.put(typeElement, method);
                }
                List<ExecutableElement> methods = loseMethods.get(typeElement);
                List list = element.getEnclosedElements();
                if (list != null && list.size() > 0) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "不能够带有参数");
                } else {
                    methods.add((ExecutableElement) element);
                }
            }
        }
    }

    private void parseChange(Set<? extends Element> change) {
        if (change != null && !change.isEmpty()) {
            for (Element element : change) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!changeMethods.containsKey(typeElement)) {
                    List<ExecutableElement> method = new ArrayList<>();
                    changeMethods.put(typeElement, method);
                }
                List<ExecutableElement> methods = changeMethods.get(typeElement);
                List list = element.getEnclosedElements();
                if (list != null && list.size() > 0) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "不能够带有参数");
                } else {
                    methods.add((ExecutableElement) element);
                }
            }
        }
    }

    private void generateFile() throws IOException {
        if (EmptyUtils.isEmpty(packageName) || EmptyUtils.isEmpty(className)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "在gradle里面传的值是不对的，导致包名和类名没有获取得到");
            return;
        }

        TypeName typeName = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Class.class), ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)));


        MethodSpec methodSpec = MethodSpec.methodBuilder("getSubscriberInfo")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get(elementUtils.getTypeElement(Const.OVERRIDE_ANNOTATION)))
                .returns(typeName)
                .addStatement("return SUBSCRIBER_INDEX")
                .build();


        FieldSpec fieldSpec = FieldSpec.builder(typeName, "SUBSCRIBER_INDEX", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .build();

        CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("SUBSCRIBER_INDEX = new $T<$T, $T>()", ClassName.get(HashMap.class), ClassName.get(Class.class), ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)));

        addStatementToBuilder(builder);

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_INFO_INDEX)))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .addField(fieldSpec)
                .addStaticBlock(builder.build())
                .build();


        JavaFile.builder(packageName, typeSpec)
                .build()
                .writeTo(filer);
    }


    private void addStatementToBuilder(CodeBlock.Builder builder) {
        builder.addStatement("$T<$T> availableList", ClassName.get(List.class), ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)));
        builder.addStatement("$T<$T> loseList", ClassName.get(List.class), ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)));
        builder.addStatement("$T<$T> changeList", ClassName.get(List.class), ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)));

        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : availableMethods.entrySet()) {
            List sizeMethods = entry.getValue();
            List progressMethod = loseMethods.get(entry.getKey());
            List reqeustMethod = changeMethods.get(entry.getKey());

            loseMethods.remove(entry.getKey());
            changeMethods.remove(entry.getKey());
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, builder, entry.getKey());
        }

        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : loseMethods.entrySet()) {
            List sizeMethods = null;
            List progressMethod = entry.getValue();
            List reqeustMethod = changeMethods.get(entry.getKey());
            changeMethods.remove(entry.getKey());
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, builder, entry.getKey());
        }


        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : changeMethods.entrySet()) {
            List sizeMethods = null;
            List progressMethod = null;
            List reqeustMethod = entry.getValue();
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, builder, entry.getKey());
        }
    }

    private void addToBuilder(List<ExecutableElement> sizeMethods,
                              List<ExecutableElement> progressMethod,
                              List<ExecutableElement> reqeustMethod,
                              CodeBlock.Builder builder, TypeElement key) {
        builder.addStatement("availableList = new $T()", ClassName.get(ArrayList.class));
        builder.addStatement("loseList = new $T()", ClassName.get(ArrayList.class));
        builder.addStatement("changeList = new $T()", ClassName.get(ArrayList.class));

        if (sizeMethods != null) {
            for (ExecutableElement element : sizeMethods) {
                builder.addStatement("availableList.add(new $T($S, null))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString());
            }
        }

        if (progressMethod != null) {
            for (ExecutableElement element : progressMethod) {
                builder.addStatement("loseList.add(new $T($S, null))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString());
            }
        }

        if (reqeustMethod != null) {
            for (ExecutableElement element : reqeustMethod) {
                NetChange netChange = element.getAnnotation(NetChange.class);
                if (netChange != null) {
                    NetType type = netChange.netType();
                    if (type == NetType.AUTO) {
                        builder.addStatement("changeList.add(new $T($S, $T.AUTO))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString(), ClassName.get(elementUtils.getTypeElement(Const.NETTYPE_PATH)));
                    } else if (type == NetType.MOBILE) {
                        builder.addStatement("changeList.add(new $T($S, $T.MOBILE))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString(), ClassName.get(elementUtils.getTypeElement(Const.NETTYPE_PATH)));
                    } else if (type == NetType.WIFI) {
                        builder.addStatement("changeList.add(new $T($S, $T.WIFI))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString(), ClassName.get(elementUtils.getTypeElement(Const.NETTYPE_PATH)));
                    } else if (type == NetType.NONE) {
                        builder.addStatement("changeList.add(new $T($S, $T.NONE))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString(), ClassName.get(elementUtils.getTypeElement(Const.NETTYPE_PATH)));
                    }
                }
            }
        }
        builder.addStatement("SUBSCRIBER_INDEX.put($T.class, new $T(availableList, loseList, changeList))", ClassName.get(key), ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)));
    }
}
