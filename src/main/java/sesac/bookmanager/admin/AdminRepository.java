package sesac.bookmanager.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAccountId(String userAccountId);

    boolean existsByAccountId(String adminAccountId);
}
