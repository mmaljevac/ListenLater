package hr.tvz.listenlater.security.repository;

import hr.tvz.listenlater.security.entity.AuthUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserInfoRepository extends JpaRepository<AuthUserInfo, Integer> {
    Optional<AuthUserInfo> findByName(String username);
}
