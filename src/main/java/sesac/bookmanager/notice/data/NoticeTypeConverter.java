package sesac.bookmanager.notice.data;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class NoticeTypeConverter implements Converter<String, NoticeType> {
    @Override
    public NoticeType convert(String source) {
        byte byteValue = Byte.parseByte(source);

        return NoticeType.fromCode(byteValue);
    }
}
