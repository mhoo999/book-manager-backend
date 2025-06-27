package sesac.bookmanager.user.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.admin.notice.data.Notice;

@Repository
public interface NoticeUserRepository extends JpaRepository<Notice, Integer> {

    Page<Notice> findByTitleContaining(String title, Pageable pageable);
}
