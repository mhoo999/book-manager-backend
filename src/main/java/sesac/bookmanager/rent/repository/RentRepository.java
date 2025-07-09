package sesac.bookmanager.rent.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.rent.domain.Rent;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RentRepository extends CrudRepository<Rent, Long>, RentQueryRepository {
    @Query("SELECT COUNT(r) FROM Rent r WHERE DATE(r.rentalDate) = :date")
    Optional<Integer> countRentsByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(r) FROM Rent r WHERE r.status = 'RENTED'")
    Optional<Integer> countActiveRents();

    @Query("SELECT COUNT(r) FROM Rent r WHERE r.status = 'OVERDUE'")
    Optional<Integer> countOverdueRents();

    @Query("SELECT COUNT(r) FROM Rent r WHERE r.user.id = :userId AND r.status = 'RENTED'")
    int countActiveRentsByUserId(@Param("userId") int userId);

    @Query("SELECT COUNT(r) FROM Rent r WHERE r.user.id = :userId AND r.status = 'OVERDUE'")
    int countOverdueRentsByUserId(@Param("userId") int userId);
}
