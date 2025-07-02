package sesac.bookmanager.wish.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum WishStatus {
    EXAMINING((byte) 1),
    APPROVED((byte) 2),
    PURCHASE((byte) 3),
    STOCK((byte) 4),
    REJECTED((byte) 0);

    private final byte code;

    @JsonValue
    public byte getCode() {
        return code;
    }

    @JsonCreator
    public static WishStatus fromCode(int code) {
        for (WishStatus wishStatus : WishStatus.values()) {
            if (wishStatus.code == (byte) code) {
                return wishStatus;
            }
        }

        throw new IllegalArgumentException("희망도서 상태코드가 지정된 범위(0~4)를 벗어났습니다 : " + code);
    }
}
