package sesac.bookmanager.notice.data;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
class NoticeTypeAttributeConverter implements AttributeConverter<NoticeType, Byte> {

    @Override
    public Byte convertToDatabaseColumn(NoticeType noticeType) {
        return noticeType != null ? noticeType.getCode() : null;
    }

    @Override
    public NoticeType convertToEntityAttribute(Byte dbData) {
        return dbData != null ? NoticeType.fromCode(dbData) : null;
    }
}
