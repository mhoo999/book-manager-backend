package sesac.bookmanager.admin.wish.data;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
class WishStatusAttributeConverter implements AttributeConverter<WishStatus, Byte> {

    @Override
    public Byte convertToDatabaseColumn(WishStatus wishStatus) {
        return wishStatus != null ? wishStatus.getCode() : null;
    }

    @Override
    public WishStatus convertToEntityAttribute(Byte dbData) {
        return dbData != null ? WishStatus.fromCode(dbData) : null;
    }
}
