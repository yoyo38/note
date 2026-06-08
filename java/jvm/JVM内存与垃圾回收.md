## JVM内存与垃圾回收

##### 类的加载过程

- Loading
- Linking
  - Verify
  - Prepare
  - Resolve
- 初始化 ： 执行 <clinit>的过程

##### 类加载器的分类

- 启动类加载器（引导类加载器，Bootstrap Class Loader）
  - 使用c/c++语言实现，嵌套在jvm内部
  - 用来加载java的核心库
  - 并不继承自java.lang.ClassLoader，没有父加载器
  - 加载扩展类和应用程序类加载器，并指定为他们的父类加载器
- 扩展类加载器（Extension ClassLoader）
  - Java语言编写
  - 派生于ClassLoader类
  - 父类加载器为启动类加载器
  - 加载 jre/lib/ext 下的类库
- 应用程序类加载器（系统类加载器，AppClassLoader）
  - Java语言编写
  - 父类为扩展类加载器
  - 程序中默认的类加载器