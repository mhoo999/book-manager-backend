package sesac.bookmanager.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac.bookmanager.user.data.User;

import java.time.LocalDateTime;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String userEmail);

    Optional<User> findByEmailAndName(String email, String name);

    @Modifying
    @Query("UPDATE User u SET u.isDeleted = true, u.deletedAt = :deletedAt WHERE u.id = :id")
    void softDeleteById(@Param("id") Integer id, @Param("deletedAt") LocalDateTime deletedAt);

}
