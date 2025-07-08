package sesac.bookmanager.rent.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import sesac.bookmanager.rent.domain.QRent;
import sesac.bookmanager.rent.domain.Rent;
import sesac.bookmanager.rent.dto.request.SearchRentRequestDto;
import sesac.bookmanager.rent.dto.response.RentResponseDto;

import java.util.List;

@RequiredArgsConstructor
public class RentQueryRepositoryImpl implements RentQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RentResponseDto> searchRents(SearchRentRequestDto request) {

        QRent rent = QRent.rent;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getRentCode() != null && request.getRentCode() > 0) {
            builder.and(rent.id.eq(request.getRentCode()));
        }

        if (request.getUserId() != null && request.getUserId() > 0) {
            builder.and(rent.user.id.eq(request.getUserId()));
        }

        if (StringUtils.hasText(request.getUsername())) {
            builder.and(rent.user.name.containsIgnoreCase(request.getUsername()));
        }

        if (StringUtils.hasText(request.getBookCode())) {
            builder.and(rent.bookItem.bookCode.containsIgnoreCase(request.getBookCode()));
        }

        if (StringUtils.hasText(request.getBookName())) {
            builder.and(rent.bookItem.book.title.containsIgnoreCase(request.getBookName()));
        }

        if (StringUtils.hasText(request.getUserPhone())) {
            builder.and(rent.user.phone.containsIgnoreCase(request.getUserPhone()));
        }

        if (StringUtils.hasText(request.getRentStatus())) {
            builder.and(rent.status.stringValue().eq(request.getRentStatus()));
        }

        String[] parts = request.getSort().split(",");
        String sortBy = parts[0];
        boolean isAsc = !"desc".equalsIgnoreCase(parts[1]);
        Order direction = isAsc ? Order.ASC : Order.DESC;
        OrderSpecifier<?> order = switch (sortBy) {
            case "username" -> new OrderSpecifier<>(direction, rent.user.name);
            case "bookCode" -> new OrderSpecifier<>(direction, rent.bookItem.bookCode);
            case "bookName" -> new OrderSpecifier<>(direction, rent.bookItem.book.title);
            default -> new OrderSpecifier<>(direction, rent.id);
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        List<Rent> rents = queryFactory
                .selectFrom(rent)
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(rent.count())
                .from(rent)
                .where(builder)
                .fetchOne();

        List<RentResponseDto> content = rents.stream()
                .map(RentResponseDto::from)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }
}
