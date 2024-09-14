package authservice.service;

import authservice.Repository.UserRepository;
import authservice.entities.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsServiceimpl {
    @Autowired
     private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Save user and encrypt password
    public UserInfo saveUser(UserInfo userInfo) {
        userInfo.setPassWord(passwordEncoder.encode(userInfo.getPassWord()));
        return userRepository.save(userInfo);
    }

    // Find user by username (used in authentication)
    public UserInfo findByUsername(String username) {
        return userRepository.findByUserName(username);
    }
}
