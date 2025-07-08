package sesac.bookmanager.book.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sesac.bookmanager.book.domain.Book;
import sesac.bookmanager.book.domain.BookItem;
import sesac.bookmanager.book.dto.request.CreateBookRequestDto;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.request.UpdateBookRequestDto;
import sesac.bookmanager.book.dto.response.BookDashboardResponseDto;
import sesac.bookmanager.book.dto.response.BookIdResponseDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;
import sesac.bookmanager.book.dto.response.PageBookResponseDto;
import sesac.bookmanager.book.enums.BookStatus;
import sesac.bookmanager.book.repository.BookItemRepository;
import sesac.bookmanager.book.repository.BookRepository;
import sesac.bookmanager.category.service.CategoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookItemRepository bookItemRepository;
    private final CategoryService categoryService;

    // resources/static 경로 설정
    private final String STATIC_PATH = "src/main/resources/static";

    public BookIdResponseDto createBook(CreateBookRequestDto request) {
		try {
            // 카테고리 유효성 검증
            if (!categoryService.isValidCategoryCode(request.getCategory())) {
                throw new IllegalArgumentException("존재하지 않는 카테고리 코드입니다: " + request.getCategory());
            }
		  // 파일 업로드 처리 - ISBN 기반 디렉토리
			String coverPath = null;
			if (request.getCoverFile() != null && !request.getCoverFile().isEmpty()) {
				coverPath = saveBookCover(request.getCoverFile(), request.getIsbn());
			} else if (request.getCover() != null && !request.getCover().trim().isEmpty()) {
				// URL로 제공된 경우 그대로 사용
				coverPath = request.getCover();
			}
            Book book = new Book();
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setPublisher(request.getPublisher());
            book.setPublishedAt(request.getPublishedAt().atStartOfDay());
            book.setLocation(request.getLocation());
            book.setStock(request.getStock());
			book.setCover(coverPath);
            book.setIsbn(request.getIsbn());
            book.setCategoryCode(request.getCategory()); // 6자리 전체 카테고리 코드 저장
            book.setDescription(request.getDescription());

            BookStatus status = Boolean.TRUE.equals(request.getIsAvailable())
                    ? BookStatus.RENTABLE : BookStatus.UNRENTABLE;

            if (request.getStock() > 0) {
                // 해당 카테고리의 다음 시퀀스 번호 가져오기
                int startSequence = getNextSequence(request.getCategory());

                for (int i = 0; i < request.getStock(); i++) {
                    BookItem bookItem = new BookItem();
                    // 도서 코드 생성: BK + 6자리카테고리코드 + 4자리순번
                    bookItem.setBookCode(generateBookCode(request.getCategory(), startSequence + i));
                    bookItem.setStatus(status);
                    book.addBookItem(bookItem);
                }
            }

            Book savedBook = bookRepository.save(book);
            return new BookIdResponseDto(savedBook.getId());
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new RuntimeException("도서 코드 중복이 발생했습니다. 다시 시도해주세요.");
            }
            throw e;
        }
    }

    /**
     * ISBN 기반으로 북 커버 파일 저장 (resources/static에 저장)
     * 저장 경로: src/main/resources/static/images/covers/{isbn}/cover.{확장자}
     */
    private String saveBookCover(MultipartFile file, String isbn) {
        try {
            // 파일 유효성 검증
            validateFile(file);

            // 확장자 추출 및 파일명 생성
            String extension = getFileExtension(file.getOriginalFilename());
            String fileName = "cover." + extension;

            // resources/static 기반 디렉토리 경로
            Path filePath = Paths.get(STATIC_PATH, "images", "covers", isbn, fileName);

            // 디렉토리 생성
            Files.createDirectories(filePath.getParent());

            // 파일 저장 (기존 파일 덮어쓰기)
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/images/covers/" + isbn + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 파일 크기 제한 (5MB)
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다. 최대 5MB까지 허용됩니다.");
        }

        // 파일 확장자 검증
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isValidImageFile(originalFilename)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. jpg, jpeg, png, gif만 허용됩니다.");
        }
    }

    private boolean isValidImageFile(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        return extension.matches("jpg|jpeg|png|gif|webp");
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

    /**
     * 해당 카테고리의 다음 시퀀스 번호를 반환 (동시성 안전)
     */
    private synchronized int getNextSequence(String category) {
        List<String> latestCodes = bookItemRepository.findBookCodesByCategoryOrdered(
                category,
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "bookCode"))
        );

        if (latestCodes.isEmpty()) {
            return 1;
        }

        String latestCode = latestCodes.get(0);
        String sequenceStr = latestCode.substring(latestCode.length() - 4);
        return Integer.parseInt(sequenceStr) + 1;
    }

    /**
     * 도서 코드 생성
     */
    private String generateBookCode(String category, int sequence) {
        String prefix = "BK" + category;
        String sequenceFormatted = String.format("%04d", sequence);
        return prefix + sequenceFormatted;
    }

    @Transactional(readOnly = true)
    public PageBookResponseDto searchBooks(SearchBookRequestDto request) {
        Page<BookResponseDto> page = bookRepository.searchBooks(request);
        return PageBookResponseDto.from(page.getContent(), request, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public BookResponseDto getBook(long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 도서가 존재하지 않습니다: " + bookId));
        return BookResponseDto.from(book);
    }

    public BookIdResponseDto updateBook(Long bookId, UpdateBookRequestDto request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 도서가 존재하지 않습니다: " + bookId));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setLocation(request.getLocation());

        // 업데이트 시에도 파일 처리 (ISBN 기반)
        if (request.getCoverFile() != null && !request.getCoverFile().isEmpty()) {
            String newCoverPath = saveBookCover(request.getCoverFile(), book.getIsbn());
            book.setCover(newCoverPath);
        } else if (request.getCover() != null && !request.getCover().trim().isEmpty()) {
            book.setCover(request.getCover());
        }

        return new BookIdResponseDto(book.getId());
    }

    @Transactional(readOnly = true)
    public Integer getTotalBookCount() {
        return Math.toIntExact(bookRepository.count());
    }

    @Transactional(readOnly = true)
    public BookDashboardResponseDto getDashboardInfo() {
        BookDashboardResponseDto dashboard = new BookDashboardResponseDto();
        dashboard.setTotalBookCount(getTotalBookCount());
        return dashboard;
    }

    @Transactional(readOnly = true)
    public List<BookResponseDto> getRecommendedBooks(int limit) {
        return bookRepository.findRecommendedBooks(PageRequest.of(0, limit))
                .stream()
                .map(BookResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponseDto> getNewBooks(int limit) {
        return bookRepository.findNewBooks(PageRequest.of(0, limit))
                .stream()
                .map(BookResponseDto::from)
                .collect(Collectors.toList());
    }
}