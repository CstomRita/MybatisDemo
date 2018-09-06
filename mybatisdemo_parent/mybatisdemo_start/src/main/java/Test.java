import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import pojo.Item;


import java.io.Reader;
import java.util.List;

/**
 * @ Author     ：CstomRita
 * @ Date       ：Created in 下午7:08 2018/9/6
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class Test {
    private static SqlSessionFactory sqlSessionFactory;
    private static Logger logger = Logger.getLogger(Test.class);
    static {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            reader.close();
            logger.info("初始化");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public  static void main(String[] args) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Item> list = sqlSession.selectList("getAllItem");
        for(Item item : list) {
            System.out.println("Item: Id" + item.getId()+" Num:"+item.getNum()+" Price:"+item.getPrice());
        }
        sqlSession.close();
    }
}
