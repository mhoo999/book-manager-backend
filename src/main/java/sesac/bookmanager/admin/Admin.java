package sesac.bookmanager.admin;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "admin")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer id;

    @Column(name = "account_id", nullable = false, unique = true)
    private String accountId;

    @Column(nullable = false)
    private String pwd;

    @Column
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String dept;

    @Column(nullable = false)
    private String position;
}