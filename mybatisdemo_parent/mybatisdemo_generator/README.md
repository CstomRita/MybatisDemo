# Mybatis的逆向工程

通过配置mybatis-generator.xml配置文件

依据配置情况为数据库中的表单生成POJO类、Mapper接口、和Mapper.xml文件

一般情况下，一个表单对应一个POJO类，SQL语句基于xml文件的方式

当然也可以选择其他的，这个是一般配置，这里就以这种配置为例子

## 添加generator依赖

pom.xml添加依赖

## mybatis-generator.xml

只需要配置这个配置文件，不再需要以前mybatis-config.xml

因为在这个配置文件中同样会配置连接池等相关的信息

<generatorConfiguration> </generatorConfiguration>

下有三个标签 

### properties用来指定配置中解析使用的外部文件

通过 resource 或 url 指定，两者只能选一

resources是classpath下的文件 

URL是本机文件


用的较少，可以不配置

### classPathEntry用来指定驱动Jar包的路径

    <classPathEntry location ="xxxx"/>

用的较少，可以不配置

### 主要是Context标签

主标签：

    id : 必写
    defaultModeType:一般flat表示一个表单生成一个POJO类
    targetRuntime：Mybatis3 / Myabtis3Simple（不会生成Example相关的方法，一般这个）
    //Example是一种SQL语句可以根据Example对象查询的一种封装，InsertByExample等等方法
    //Mybatis3Simple不会生成这种方法
    //targetRuntime影响的是Mapper接口和xml文件
   
子标签，需要注意的是子标签有严格的定义顺序，按照这个顺序定义

    property:用来配置分隔符
    plugin：用来配置插件，用的貌似不多
    commentGenerator：用来配置自动生成的注释
    jdbcConnection:用来配置连接池
    javaTypeResolver:这个用来配置数据库中JDBC类型和Java类型对应的问题，一般情况下不建议修改直接默认
    javaModelGenetator:POJO类存放的包和路径（src/main/java）
                    <!-- 是否允许子包，即targetPackage.schemaName.tableName -->
                    <property name="enableSubPackages" value="false"/>
                    <!-- 是否对model添加 构造函数 -->
                    <property name="constructorBased" value="true"/>
                    <!-- 是否对类CHAR类型的列的数据进行trim操作 -->
                    <property name="trimStrings" value="true"/>
                    <!-- 建立的Model对象是否 不可改变  即生成的Model对象不会有 setter方法，只有构造方法 -->
                    <property name="immutable" value="false"/>

    sqlMapGenerator:xml文件存放的包和路径（src/main/resource）
    javaClientGenerator:Mapper接口采用什么方式注入（一般XMLMAPPER xml文件）和存放的包和路径（src/main/java）
    table:配置通过逆向工程生成的表，可以指定一个多个或者直接 % 通配符表示全部
        <table>下子标签：<generatedKey>表示生成INSERT语句时按照主键自增的方式，返回主键值
        
一般情况下，配置文件如下，如果有特殊需求自行增删：
    
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE generatorConfiguration
            PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
            "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
    <generatorConfiguration>
        <context id="MysqlContext" targetRuntime="MyBatis3Simple" defaultModelType="flat">
    
            <property name="beginningDelimiter" value="`"/>
            <property name="endingDelimiter" value="`"/>
    
            <commentGenerator>
                <property name="suppressDate" value="true"/>
                <property name="addRemarkComments" value="true"/>
            </commentGenerator>
    
            <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                            connectionURL="jdbc:mysql://localhost:3306/mybatisdemo"
                            userId="root"
                            password="root1234"></jdbcConnection>
    
            <javaModelGenerator targetPackage="pojo" targetProject="src/main/java">
                <property name="trimStrings" value="true"/>
            </javaModelGenerator>
            
            <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources"/>
            
            <javaClientGenerator type="XMLMAPPER" targetPackage="mapper" targetProject="src/main/java"/>
    
            <table tableName="user">
                <generatedKey column="id" sqlStatement="MySql"/>
                <!--主键列名 和 数据库类型-->
            </table>
        </context>
    </generatorConfiguration>

## 如何运行？使用Maven Plugin

pom.xml添加generator插件依赖

     <plugin>
                    <groupId>org.mybatis.generator</groupId>
                    <artifactId>mybatis-generator-maven-plugin</artifactId>
                    <version>1.3.3</version>
                    <configuration>
                        <configurationFile>
                            ${basedir}/src/main/resources/mybatis-generator-config.xml
                        </configurationFile>
                        <verbose>true</verbose>
                        <overwrite>true</overwrite>
                    </configuration>
                    <dependencies>
                        <dependency>
                            <groupId>mysql</groupId>
                            <artifactId>mysql-connector-java</artifactId>
                            <version>8.0.12</version>
                        </dependency>
                    </dependencies>
                </plugin>
                
需要注意的是：maven插件中需要引用其他类的时候需要自插件中添加依赖，例如连接数据库时需要JDBC依赖

因此需要再添加依赖，而且在插件中的依赖无法识别父模块定义的version，需要特别指明version

如果还需要别的依赖，也需要在这里添加

这里讲父模块的pom.xml做了修改，在子模块中继承这个插件再配置config.xml的路径

## 生成了什么Mapper方法

通过主键的delete select update方法以及 insert 和 selectAll

int deleteByPrimaryKey(Long id);

selectByPrimaryKey(Long id);

int updateByPrimaryKey(Role record);

int insert(Role record);

List<Role> selectAll();

要求数据库表单中必须设置了主键
