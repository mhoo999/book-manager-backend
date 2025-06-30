package sesac.bookmanager.rent.enums;

public enum RentStatus {
    REQUESTED("대여요청"),
    RENTED("대여중"),
    RETURNED("반납"),
    OVERDUE("미납"),
    REJECTED("거절");

    private final String description;

    RentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
