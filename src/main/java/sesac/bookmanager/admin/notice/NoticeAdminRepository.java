package sesac.bookmanager.admin.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.admin.notice.data.Notice;
import sesac.bookmanager.admin.notice.data.NoticeResponse;

@Repository
public interface NoticeAdminRepository extends JpaRepository<Notice, Integer> {

    Page<Notice> findByTitleContaining(String title, Pageable pageable);
}
