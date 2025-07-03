package sesac.bookmanager.category.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @JdbcTypeCode(SqlTypes.TINYINT)
    private Integer depth;

    @Column(name = "large_code", nullable = false, length = 2)
    private String largeCode;

    @Column(name = "medium_code", nullable = false, length = 2)
    private String mediumCode;

    @Column(name = "small_code", nullable = false, length = 2)
    private String smallCode;

    @Column(name = "full_code", nullable = false, length = 6, unique = true)
    private String fullCode;
}