package sesac.bookmanager.wish.data;

import org.springframework.stereotype.Component;

@Component
class WishStatusConverter implements org.springframework.core.convert.converter.Converter<String, WishStatus> {
    @Override
    public WishStatus convert(String source) {
        Byte byteValue = Byte.parseByte(source);

        return WishStatus.fromCode(byteValue);
    }
}
