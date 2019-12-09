package learn.zzyft.sb.springboot.mapper;

import learn.zzyft.sb.springboot.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountID},#{token},#{gmtCreate},#{gmtModified}) ")
    void insert(User user);

}
