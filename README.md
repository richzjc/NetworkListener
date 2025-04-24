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
@NetLose
@NetChange(net)
```
