package sesac.bookmanager.wish.data;

import org.springframework.stereotype.Component;

@Component
class WishStatusConverter implements org.springframework.core.convert.converter.Converter<Byte, WishStatus> {
    @Override
    public WishStatus convert(Byte source) {
        return WishStatus.fromCode(source);
    }
}
