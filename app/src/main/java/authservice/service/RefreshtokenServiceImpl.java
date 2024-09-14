package authservice.service;


import authservice.Repository.RefershTokenRepository;
import authservice.entities.RefreshToken;
import authservice.entities.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RefreshtokenServiceImpl {
    @Autowired
    private RefershTokenRepository refershTokenRepository;

    public RefreshToken createRefreshToken(String token, UserInfo user){
        RefreshToken refreshToken=new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
         return refershTokenRepository.save(refreshToken);
    }
    public Optional<RefreshToken> findByToken(String token){
        return refershTokenRepository.findByToken(token);
    }

}
