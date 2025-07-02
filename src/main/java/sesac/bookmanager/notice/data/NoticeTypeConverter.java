package sesac.bookmanager.notice.data;

import org.springframework.stereotype.Component;

@Component
class NoticeTypeConverter implements org.springframework.core.convert.converter.Converter<Byte, NoticeType> {
    @Override
    public NoticeType convert(Byte source) {
        return NoticeType.fromCode(source);
    }
}
