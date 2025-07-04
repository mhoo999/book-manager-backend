package sesac.bookmanager.notice;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.notice.data.*;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final AdminRepository adminRepository;


    public NoticeResponse createNotice(NoticeCreateRequest request, Admin admin) {
        Admin checkAdmin = adminRepository.findById(admin.getId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Admin : " + admin.getId()));

        Notice newNotice = Notice.builder()
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .views(0)
                .admin(checkAdmin)
                .build();

        Notice savedNotice = noticeRepository.save(newNotice);

        return NoticeResponse.from(savedNotice);
    }

    @Transactional(readOnly = true)
    public NoticePageResponse searchNotice(NoticeSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());

        Page<NoticeResponse> searchResult = noticeRepository
                .findByTitleContaining(searchRequest.getTitle(), pageable)
                .map(NoticeResponse::from);

        return NoticePageResponse.from(searchResult.getContent(), searchRequest, searchResult.getTotalElements());
    }

    public NoticeResponse getNoticeByIdWithViewCounting(Integer noticeId) {
        Notice viewNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 공지사항이 없습니다 : " + noticeId));

        viewNotice.setViews(viewNotice.getViews() + 1);

        return NoticeResponse.from(viewNotice);
    }

    @Transactional(readOnly = true)
    public NoticeResponse getNoticeById(Integer noticeId) {
        Notice viewNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 공지사항이 없습니다 : " + noticeId));

        viewNotice.setViews(viewNotice.getViews() + 1);

        return NoticeResponse.from(viewNotice);
    }

    public NoticeResponse updateNotice(Integer noticeId, NoticeUpdateRequest updateRequest) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 공지사항이 없습니다 : " + noticeId));

        notice.setTitle(updateRequest.getTitle());
        notice.setContent(updateRequest.getContent());
        notice.setType(updateRequest.getType());

        return NoticeResponse.from(notice);
    }

    public void deleteNotice(Integer noticeId) {
        noticeRepository.deleteById(noticeId);
    }
}
