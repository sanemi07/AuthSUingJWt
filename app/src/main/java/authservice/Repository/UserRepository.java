package authservice.Repository;

import authservice.entities.UserInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserInfo,Long> {

    UserInfo findByUserName(String userName);
}
