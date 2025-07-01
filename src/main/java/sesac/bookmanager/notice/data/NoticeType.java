package sesac.bookmanager.notice.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum NoticeType {
    DEFAULT((byte) 0),
    DAYOFF((byte) 1),
    SCHEDULE((byte) 2),
    RECRUIT((byte) 3);

    private final byte code;

    @JsonValue
    public byte getCode() {
        return code;
    }

    @JsonCreator
    public static NoticeType fromCode(byte code) {
        for (NoticeType noticeType : NoticeType.values()) {
            if (noticeType.code == code) {
                return noticeType;
            }
        }

        throw new IllegalArgumentException("공지사항 타입코드가 지정된 범위(0~3)를 벗어났습니다 : " + code);
    }
}
