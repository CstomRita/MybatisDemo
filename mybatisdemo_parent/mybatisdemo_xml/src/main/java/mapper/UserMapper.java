package mapper;

import org.apache.ibatis.annotations.Param;
import pojo.User;

import java.util.List;

/**
 * @ Author     ：CstomRita
 * @ Date       ：Created in 下午5:03 2018/9/8
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public interface UserMapper {
    User findUserById(Long id);
    List<User> findAllUsers();
    List<User> findUserByHeadImage(@Param("id") Long id, @Param("headImage") byte[] headImage);
}
