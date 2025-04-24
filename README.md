### 我的 maven central的地址
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
