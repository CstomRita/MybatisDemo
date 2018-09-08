import mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import pojo.User;

import java.io.Reader;
import java.util.List;

/**
 * @ Author     ：CstomRita
 * @ Date       ：Created in 下午5:16 2018/9/8
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class MapperTest {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            System.out.println("初始化");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void UserMapper(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(mapper.UserMapper.class);
            List<User> users = userMapper.findUserByHeadImage((long)1,null);
            for(User user : users){
                System.out.println(user.getUserName()+user.getPassWord());
            }
        }finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

}
