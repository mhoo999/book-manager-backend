package sesac.bookmanager.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.category.domain.Category;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Integer id;
    private String name;
    private Integer depth;
    private String largeCode;
    private String mediumCode;
    private String smallCode;
    private String fullCode;

    public static CategoryDto from(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .depth(category.getDepth())
                .largeCode(category.getLargeCode())
                .mediumCode(category.getMediumCode())
                .smallCode(category.getSmallCode())
                .fullCode(category.getFullCode())
                .build();
    }
}