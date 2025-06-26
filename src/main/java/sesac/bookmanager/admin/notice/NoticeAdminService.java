package sesac.bookmanager.admin.notice;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.admin.notice.data.*;
import sesac.bookmanager.hjdummy.DummyUser;
import sesac.bookmanager.hjdummy.DummyUserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeAdminService {

    private final NoticeAdminRepository noticeAdminRepository;

    private final DummyUserRepository adminRepository;


    public NoticeResponse createNotice(NoticeCreateRequest request) {
        // TODO: Admin 레포지토리 생성 후 메서드명 변경
        DummyUser admin = adminRepository.findById(request.getAdminId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Admin : " + request.getAdminId()));

        Notice newNotice = Notice.builder()
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .user(admin)
                .build();

        Notice savedNotice = noticeAdminRepository.save(newNotice);

        return NoticeResponse.from(savedNotice);
    }

    public NoticePageResponse getAllNotice(NoticeSearchRequest request) {
        return null;
    }


}
