package authservice.Repository;

import authservice.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefershTokenRepository  extends JpaRepository<RefreshToken,Long> {
Optional<RefreshToken> findByToken(String token);

}
