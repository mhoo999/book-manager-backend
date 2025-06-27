package sesac.bookmanager.user.notice;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.admin.notice.data.NoticePageResponse;
import sesac.bookmanager.admin.notice.data.NoticeResponse;
import sesac.bookmanager.admin.notice.data.NoticeSearchRequest;
import sesac.bookmanager.hjdummy.DummyUserRepository;

@Service
@RequiredArgsConstructor
public class NoticeUserService {
    private final NoticeUserRepository noticeUserRepository;

    private final DummyUserRepository userRepository;

    public NoticePageResponse searchNotice(NoticeSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());

        Page<NoticeResponse> searchResult = noticeUserRepository
                .findByTitleContaining(searchRequest.getTitle(), pageable)
                .map(NoticeResponse::from);

        return NoticePageResponse.from(searchResult.getContent(), searchRequest, searchResult.getTotalElements());
    }

    public NoticeResponse getNoticeById(Integer noticeId) {
        return noticeUserRepository.findById(noticeId)
                .map(NoticeResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 공지사항이 없습니다 : " + noticeId));
    }
}
