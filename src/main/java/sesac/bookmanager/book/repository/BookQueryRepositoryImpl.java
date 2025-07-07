package sesac.bookmanager.book.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import sesac.bookmanager.book.domain.Book;
import sesac.bookmanager.book.domain.QBook;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;

import java.util.List;

@RequiredArgsConstructor
public class BookQueryRepositoryImpl implements BookQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookResponseDto> searchBooks(SearchBookRequestDto request) {

        QBook book = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(request.getTitle())) {
            builder.and(book.title.containsIgnoreCase(request.getTitle()));
        }

        if (StringUtils.hasText(request.getAuthor())) {
            builder.and(book.author.containsIgnoreCase(request.getAuthor()));
        }

        if (StringUtils.hasText(request.getPublisher())) {
            builder.and(book.publisher.containsIgnoreCase(request.getPublisher()));
        }

        if (StringUtils.hasText(request.getIsbn())) {
            builder.and(book.isbn.likeIgnoreCase(request.getIsbn()));
        }

        String[] parts = request.getSort().split(",");
        String sortBy = parts[0];
        boolean isAsc = !"desc".equalsIgnoreCase(parts[1]);
        Order direction = isAsc ? Order.ASC : Order.DESC;
        OrderSpecifier<?> order = switch (sortBy) {
            case "title" -> new OrderSpecifier<>(direction, book.title);
            case "author" -> new OrderSpecifier<>(direction, book.author);
            case "publisher" -> new OrderSpecifier<>(direction, book.publisher);
            case "isbn" -> new OrderSpecifier<>(direction, book.isbn);
            default -> new OrderSpecifier<>(direction, book.id);
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        List<Book> books = queryFactory
                .selectFrom(book)
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(book.count())
                .from(book)
                .where(builder)
                .fetchOne();

        List<BookResponseDto> content = books.stream()
                .map(BookResponseDto::from)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }
}
