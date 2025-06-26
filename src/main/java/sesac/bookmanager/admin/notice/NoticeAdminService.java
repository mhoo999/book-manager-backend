package sesac.bookmanager.admin.notice;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.admin.notice.data.*;
import sesac.bookmanager.hjdummy.DummyAdmin;
import sesac.bookmanager.hjdummy.DummyAdminRepository;
import sesac.bookmanager.hjdummy.DummyUser;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeAdminService {

    private final NoticeAdminRepository noticeAdminRepository;

    private final DummyAdminRepository adminRepository;


    public NoticeResponse createNotice(NoticeCreateRequest request) {
        // TODO: Admin 레포지토리 생성 후 메서드명 변경
        DummyAdmin admin = adminRepository.findById(request.getAdminId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Admin : " + request.getAdminId()));

        Notice newNotice = Notice.builder()
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .admin(admin)
                .build();

        Notice savedNotice = noticeAdminRepository.save(newNotice);

        return NoticeResponse.from(savedNotice);
    }

    @Transactional(readOnly = true)
    public NoticePageResponse searchNotice(NoticeSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());

        Page<NoticeResponse> searchResult = noticeAdminRepository
                .findByTitleContaining(searchRequest.getTitle(), pageable)
                .map(NoticeResponse::from);

        return NoticePageResponse.from(searchResult.getContent(), searchRequest, searchResult.getTotalElements());
    }
}
