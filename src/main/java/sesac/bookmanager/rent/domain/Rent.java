package sesac.bookmanager.rent.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sesac.bookmanager.rent.enums.RentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rent_history")
@Entity
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookItem_id", nullable = false)
    private BookItem bookItem;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(name = "rental_date")
    private LocalDateTime rentalDate;

    @Column(name = "expected_return_date")
    private LocalDateTime expectedReturnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RentStatus status;

    private String description;

}
