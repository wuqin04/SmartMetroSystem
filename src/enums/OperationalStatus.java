package enums;

public enum OperationalStatus {
    ACTIVE,             // Fully operational, passengers can buy tickets
    INACTIVE,           // Soft-deleted or permanently closed
    UNDER_MAINTENANCE   // Temporarily closed (e.g., track repairs, emergency)
}