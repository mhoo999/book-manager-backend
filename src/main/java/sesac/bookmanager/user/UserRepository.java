package sesac.bookmanager.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Modifying
    @Query("UPDATE User u SET u.isDeleted = false WHERE u.id = :id")
    void restoreById(@Param("id") Long id);

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    int countByIsDeletedFalse();
    int countByIsDeletedTrue();

    Page<User> findByIsDeletedTrue(Pageable pageable);
    Page<User> findByIsDeletedFalse(Pageable pageable);

    @Query("""
    SELECT u FROM User u
    WHERE (:keyword IS NULL OR 
          (:type = 'name' AND u.name LIKE %:keyword%) OR 
          (:type = 'id' AND STR(u.id) LIKE CONCAT('%', :keyword, '%')))
    """)
    Page<User> findAllWithSearch(@Param("type") String type,
                                 @Param("keyword") String keyword,
                                 Pageable pageable);

    @Query("""
    SELECT u FROM User u
    WHERE u.isDeleted = true AND
          (:keyword IS NULL OR 
          (:type = 'name' AND u.name LIKE %:keyword%) OR 
          (:type = 'id' AND STR(u.id) LIKE CONCAT('%', :keyword, '%')))
    """)
    Page<User> findByIsDeletedTrueWithSearch(@Param("type") String type,
                                             @Param("keyword") String keyword,
                                             Pageable pageable);

    @Query("""
    SELECT u FROM User u
    WHERE u.isDeleted = false AND
          (:keyword IS NULL OR 
          (:type = 'name' AND u.name LIKE %:keyword%) OR 
          (:type = 'id' AND STR(u.id) LIKE CONCAT('%', :keyword, '%')))
    """)
    Page<User> findByIsDeletedFalseWithSearch(@Param("type") String type,
                                              @Param("keyword") String keyword,
                                              Pageable pageable);
}
