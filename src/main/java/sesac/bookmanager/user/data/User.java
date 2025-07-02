package sesac.bookmanager.user.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "user")
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "pwd")
    private String pwd;

    @Column(name="is_deleted",nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", nullable = false, updatable = false,insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public User(int userId, String email, String name) {
        this.id = userId;
        this.email = email;
        this.name = name;
    }

    public User() {

    }
}