package sesac.bookmanager.admin.wish.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WishStatus {
    EXAMINING((byte) 1),
    APPROVED((byte) 2),
    PURCHASE((byte) 3),
    STOCK((byte) 4),
    REJECTED((byte) 0);

    private final byte code;

    public static WishStatus fromCode(byte code) {
        for (WishStatus wishStatus : WishStatus.values()) {
            if (wishStatus.code == code) {
                return wishStatus;
            }
        }

        throw new IllegalArgumentException("희망도서 상태코드가 지정된 범위(0~4)를 벗어났습니다 : " + code);
    }
}
