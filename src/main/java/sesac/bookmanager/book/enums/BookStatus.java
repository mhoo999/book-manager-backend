package sesac.bookmanager.book.enums;

public enum BookStatus {
    RENTABLE("대여가능"),
    RENTED("대여중"),
    UNRENTABLE("대여불가");

    private String description;

    BookStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
