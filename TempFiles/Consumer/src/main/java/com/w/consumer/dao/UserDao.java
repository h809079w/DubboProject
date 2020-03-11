package com.w.consumer.dao;



import com.w.common.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    @Select("select * from user where id = #{id}")
    public User getUserById(@Param("id") long id);

    @Insert("insert into user(id,nickname,password,salt,head,register_date,last_login_date,login_count) values(#{id},null,#{password},#{salt},null,null,null,null)")
    public void InsertUser(User User);

    @Update("update user set password = #{password} where id = #{id}")
    public void updateUser(User toBeUpdate);
}
