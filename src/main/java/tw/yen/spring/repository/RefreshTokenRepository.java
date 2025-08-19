package tw.yen.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.yen.spring.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);
}
