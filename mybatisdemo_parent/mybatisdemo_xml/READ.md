## 第二节 Mybatis的基本用法

Mybatis中映射SQL语句主要有两种方式

一种是基于XML的方式：一个xxMapper.java的接口对应一个xxMapper.xml的映射文件

映射文件中写每一个接口方法对应的SQL语句 

第二种方式是基于注解的方式

## POJO

在POJO中不要使用基本数据类型int long之类的，使用他们的封装类 Integer Long....

因为封装类会自动赋值为null，但是基本数据类型赋值为0

在动态SQL后可能会引发某些问题

## 基于XML的方式UserMapper为例

### 目前基于XML的方式仍是主流

一个xxMapper.java的接口对应一个xxMapper.xml的映射文件，我们分开来看

### xxMapper.java的接口

在这里面定义接口方法

我们实际调用的时候调用的也是接口的方法，实现某个xxMapper接口，调用其中方法


### xxMapper.xml的映射文件

在<mapper> </mapper>下写我们的SQL语句

增 insert 删 delete 改 update 查 select

#### 1 我们先来看四种操作的返回值

增、删、改的返回值都是int类型
他们的基本结构大部分情况下仅涉及一个id（如果主键自增返回主键的话则需要其他设置）

    <insert/delete/update id = "" >
     SQL语句
     </insert/delete/update>

查的返回值则一般情况下是JavaBean，因此select语句中会包含resultType/resultMap指定返回值类型，这时接口方法的返回值定义的则是JavaBean的list

#### 2 查 select

    <select id = "" resultType/resultMap=""">
    SQL语句
    </select>

##### resultType、resultMap都是定义返回值类型的
区别在于resultType是程序中确实存在的一个Java类，resultMap则是在Mapper.xml文件中设置的一种返回类型

如何将查询结果装配到JavaBean中，主要思想是令SQL查询出的列名与JavaBean中的参数名相同，mybatis可以自动装配
PS 不区分大小写
如果列名和参数名不同时我们可以通过下面两种方式
具体实现：

一 在SQL语句中利用别名查询

select user_name username form person这样SQL查询结果将user_name变成了username,此时在resultType中指定JavaBean全类名

二 设置resultMap返回

    <resultMap id="" type="">
    <result property="参数名",column="列名",jdbcType=""/>
    </resultMap>
  
resultMap的主要结构就是这样的，id就是需要传入select语句resultMap的id，type是需要装配的JavaBean对象

property是查询出的每一个列的结果赋值给JavaBean的某一个属性中去

jdbcType是MySQL数据库中某一列创建表单时设置的数据类型，他是为了处理这个数值为空的情况，它是MySQL需要的而不是Mybatis需要的
对于不会为空的其他列，这个jdbcType可以不写

三 一个小Tip

在MySQL中我们常用下划线表示某一个列名例如：user_name
在Java中我们常用小驼峰表示一个属性名，例如：userName
Mybatis提供一个全局设置，可以将下划线命名的自动映射成驼峰命名的属性
需要在mybatis-config.xml中设置：

    <setting>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
    </setting>
    
##### 如果一个JavaBean对象A中包含了另外一个JavaBean对象B，在SQL语句中要使用 B.属性的方式获取B的基本属性

####3 现在我们再来看一看接口参数的使用

这一点上增删改查的方式是一样的

参数列表主要是两种情况：一个参数、多个参数

##### 一个参数
当只有一个参数时，Mybatis并不在乎这个参数叫什么名字，他会直接拿出使用

一个参数下也分为：一个基本参数类型，一个JavaBean对象

一个基本类型： #{XX} 便可以在xml的SQL语句中使用了

一个JavaBean对象：稍微注意的就是用.属性取值 #{XX.属性}

##### 多个参数

当接口方法参数列表有多个参数时，需要使用@Param注解

在定义接口方法参数列表时在每个参数前加@Param注解

    List<User> findUser(@Param("A") int id, @Param("B") User user);
    
@Param注解的作用是将参数列表自动封装成 A-id,B-user这样键值对Map的形式

在xml中使用 #{A} #{B.属性}便可以获取其中的值了

#### 4 如何调用接口方法运行

同样利用SQLSession调用getMapper(XXMapper.class)获取XXMapper对象，调用其方法

为了不影响数据库在finally中调用SQLSessionFactory.rollback()撤销对数据库的更改
因为SQLSessionFactory.openSession是不自动提交的，通过回滚操作不会影响数据库

## 基于注解的方式

Mybatis注解方式是直接写在接口上，不再借助xml映射文件
在mybatis-config.xml也不需配置mapper映射目录了
当然XML和注解方式可以混合使用，只要使用xml方式mapper映射目录还是需要配置的
注解方式需要手动拼接字符串而且重新编译代码不方便维护，因此除非程序简单而且数据库表基本不变，否则不建议使用
主要还是XML的方式

有了上面的基础，这里直接写出对应的注解

增： @Insert({"SQL语句"})

删： @Delete({"SQL语句"})

改： @Update({"SQL语句"})

查： @Select({"SQL语句"})
    Select语句由于涉及到SQl查询结果对JavaBean的一个装配
    因此他还有一个注解@Results来设置resultMap
    
    @Results(id="A",value={
        @Result(property="属性名",column="列名")
    })
    
在其他方法前通过@ResultMap("A")就可以共享这个ResultMap配置了
装配的JavaBean对象类型就是方法的返回值
   
这个@ResultMap注解还可以结合XML方式，@ResultMap("id")，这个Id可以是XML中<resultMap>元素的Id属性值
