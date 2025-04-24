### maven central的地址
https://central.sonatype.com/search?q=io.github.richzjc
---
### 仓库
mavenCentral()
---
### 依赖方式 
```
implementation 'io.github.richzjc:netannotation:1.0.0'
implementation 'io.github.richzjc:network:1.0.0'
kapt 'io.github.richzjc:netCompiler:1.0.0'
```
---
### 博客地址
https://zhuanlan.zhihu.com/p/1894702829630895262

---
### 优点
该库简化了写网络监听的相关代码。真正的实现了变向切面的编程！！
只需要通过一个注解就能实现相应的网络变化

---
### 注解说明

```
@NetAvailable
该注解表示，监听到有网络了，如果想在监听到有网络时执行相应操作，则只需要给方法添加此注解

@NetLose
该注解表示，监听到无网络了，如果想在监听到无网络时执行相应操作，则只需要给方法添加此注解

@NetChange(netType = NetType.AUTO)
该注解表示，监听到网络切换了，如果想在监听到网络切换时执行相应操作，则只需要给方法添加此注解

```
---
### NetType有四种取值

```
public enum NetType {
    AUTO, WIFI, MOBILE, NONE;
}
```

**AUTO** : 如果传入的参数是AUTO； 就相当于NetAvailable注解，监听到有网络
**NONE** : 如果传入的参数是NONE； 就相当于NetLOSE注解，监听到无网络

---
### 示例代码
```
@NetLose
public void test(){
     //执行无网络的操作
}

@NetAvailable
public void testl(){
    //执行有网络的操作
}

@NetChange(netType =NetType.WIFI){
    //网络变成WIFI后，执行相应的操作
}
```

---
### 实现原理

基于APT注解处理器， 结合JavaPoet, 在编译时，动态生成类文件，可以下载工程自己跑一遍。

---
### 展示一下apt生成的类的源码
```
public class NetChanger implements SubscribeInfoIndex {
  private static final Map<Class, SimpleSubscribeInfo> SUBSCRIBER_INDEX;

  static {
    SUBSCRIBER_INDEX = new HashMap<Class, SimpleSubscribeInfo>();
    List<SubscribeMethod> availableList;
    List<SubscribeMethod> loseList;
    List<SubscribeMethod> changeList;
    availableList = new ArrayList();
    loseList = new ArrayList();
    changeList = new ArrayList();
    availableList.add(new SubscribeMethod("test1", null));
    loseList.add(new SubscribeMethod("test", null));
    changeList.add(new SubscribeMethod("test2", NetType.AUTO));
    changeList.add(new SubscribeMethod("test3", NetType.WIFI));
    SUBSCRIBER_INDEX.put(MainActivity.class, new SimpleSubscribeInfo(availableList, loseList, changeList));
  }

  @Override
  public Map<Class, SimpleSubscribeInfo> getSubscriberInfo() {
    return SUBSCRIBER_INDEX;
  }
}
```

---
### 配置

需要在使用到注解的module下面的build.gradle文件中，添加如下配置：
```
javaCompileOptions {
            annotationProcessorOptions {
                arguments = [ moduleName : project.getName() ]
            }
        }
```
---
### 初始化

将各个模块下面生成的NetChanner类添加到NetManager中：
```
NetManager.addIndex(new NetChanger());
NetManager.init(this);
```

---
### 注册与取消注册

在使用到注解的对应类里面，调用注册方法与取消注册方法，比如说在Activity里面：
```
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetManager.bind(this);
    }


@Override
    protected void onDestroy() {
        super.onDestroy();
        NetManager.unBind(this);
    }
```

---
### 添加混淆规则

```
-keepclasseswithmembernames class * {
     @com.richzjc.netannotation.* <methods>;
 }
```

若写得不对， 欢迎大家共同讨论！！！！

