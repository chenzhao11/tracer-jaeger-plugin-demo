# 在 Spring 工程中上传数据到Jaeger

这个例子是在官方[在Spring工程中使用SOFATracer](https://github.com/glmapper/tracer-zipkin-plugin-demo)的基础上修改的在Spring工程中上传数据到Jaeger的例子。

## 前提条件

* Jaeger Server 的搭建可以 [参考此文档](https://www.jaegertracing.io/docs/1.26/getting-started/) 中All in one部分进行配置和搭建
* 新建Spring工程  [tracer-zipkin-plugin-demo](https://github.com/glmapper/tracer-zipkin-plugin-demo/tree/master)。
* SOFATracer Github [ sofa-tracer ](https://github.com/alipay/sofa-tracer)

##  引入插件

在这个例子中使用的是本地的jar包

```xml
        <dependency>
            <groupId>com.alipay.sofa</groupId>
            <artifactId>tracer-sofa-boot-starter</artifactId>
            <version>3.1.1</version>
            <scope>system</scope>
            <systemPath>${pom.basedir}/src/main/resources/lib/tracer-sofa-boot-starter-3.1.1.jar
            </systemPath>
        </dependency>

        <dependency>
            <groupId>com.alipay.sofa</groupId>
            <artifactId>sofa-tracer-jaeger-plugin</artifactId>
            <version>3.1.1</version>
            <scope>system</scope>
            <systemPath>${pom.basedir}/src/main/resources/lib/sofa-tracer-jaeger-plugin-3.1.1.jar
            </systemPath>
        </dependency>
        <dependency>
            <groupId>com.alipay.sofa</groupId>
            <artifactId>tracer-core</artifactId>
            <version>3.1.1</version>
            <scope>system</scope>
            <systemPath>${pom.basedir}/src/main/resources/lib/tracer-core-3.1.1.jar</systemPath>
        </dependency>
        <dependency>
            <groupId>com.alipay.sofa</groupId>
            <artifactId>sofa-tracer-springmvc-plugin</artifactId>
            <version>${sofa.tracer.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alipay.sofa</groupId>
            <artifactId>sofa-tracer-httpclient-plugin</artifactId>
            <version>${sofa.tracer.version}</version>
        </dependency>
        <!--jaeger dependences-->
        <dependency>
            <groupId>io.jaegertracing</groupId>
            <artifactId>jaeger-client</artifactId>
            <version>1.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
            <version>3.12.1</version>
        </dependency>
        <!--end jaeger dependences-->

        <!-- HttpClient Dependency -->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpasyncclient</artifactId>
            <version>4.1.3</version>
        </dependency>
```

> sofa-tracer-springmvc-plugin 是基于 标准 Servlet 实现的，因此即使是非 SpringMvc 工程，只要是标准的Servlet 工程，均可以使用该插件。


## 配置

这部分包括 filter 配置、配置文件配置、jaeger bean配置等。

### Filter 配置

在 web.xml 中配置 sofa-tracer-springmvc-plugin 插件的 Filter。 

```xml
  <filter>
    <filter-name>jaegerFilter</filter-name>
    <filter-class>
      com.alipay.sofa.tracer.plugins.springmvc.SpringMvcSofaTracerFilter
    </filter-class>
  </filter>
  <filter-mapping>
    <filter-name>jaegerFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

### 配置文件

在 resources 目录下新建 sofa.tracer.properties 文件，并且增加 jaeger 上报所需的配置：

```properties
# Application Name
spring.application.name=SOFATracerJaeger
com.alipay.sofa.tracer.jaeger.enabled=true
com.alipay.sofa.tracer.jaeger.receiver=collector
com.alipay.sofa.tracer.jaeger.collector.max-packet-size-bytes=2097154
com.alipay.sofa.tracer.jaeger.collector.base-url=http://127.0.0.1:14268
#com.alipay.sofa.tracer.jaeger.receiver=agent
#com.alipay.sofa.tracer.jaeger.agent.host=127.0.0.1
#com.alipay.sofa.tracer.jaeger.agent.port=6831
#com.alipay.sofa.tracer.jaeger.agent.max-packet-size-bytes=65000
com.alipay.sofa.tracer.jaeger.max-queue-size=10000
com.alipay.sofa.tracer.jaeger.flush-interval-mill=1000
com.alipay.sofa.tracer.jaeger.close-enqueue-timeout-mill=1000
```

### zipkin bean 配置

在 Spring 工程中，需要配置一个 bean，用于初始化 zipkin 上报所需的信息。

```xml
<bean id="jaegerReportRegisterBean" class="com.alipay.sofa.tracer.plugins.jaeger.initialize.JaegerReportRegisterBean"/>
```



## 启动 Jaeger server

按照前提条件中使用Docker运行好Jaeger server后启动服务，正常启动后的主界面如下：

![image-20210928141247898](https://gitee.com/whutzhaochen/markdown/raw/master/img/20210928141732.png)



## 配置&启动tomcat

1、配置 Server

![image-20210928141331719](https://gitee.com/whutzhaochen/markdown/raw/master/img/20210928141748.png)

2、配置 Deployment

![image-20210928141343990](https://gitee.com/whutzhaochen/markdown/raw/master/img/20210928141745.png)

3、启动tomcat


## 访问资源&上报展示

1、访问资源

在浏览器中输入http://localhost:8089/tracer_jaeger_plugin_demo_war_exploded/hello

2、Jaeger展示

![image-20210928141708648](https://gitee.com/whutzhaochen/markdown/raw/master/img/20210928141741.png)
