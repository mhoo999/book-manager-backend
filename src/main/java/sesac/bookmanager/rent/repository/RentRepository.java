package sesac.bookmanager.rent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.rent.domain.Rent;

@Repository
public interface RentRepository extends CrudRepository<Rent, Long>, RentQueryRepository {
}
