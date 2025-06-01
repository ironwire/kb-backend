package org.yiqixue.kbbackend.constant;

public class ApiConstants {

    // API Paths
    public static final String API_BASE = "/api";
    public static final String DOCUMENTS_PATH = API_BASE + "/documents";
    public static final String SEARCH_PATH = API_BASE + "/search";
    public static final String USERS_PATH = API_BASE + "/users";
    public static final String HEALTH_PATH = API_BASE + "/health";

    // File Upload
    public static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    public static final String[] ALLOWED_FILE_TYPES = {
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "text/plain", "text/html", "application/rtf"
    };

    // Search
    public static final int DEFAULT_SEARCH_SIZE = 10;
    public static final int MAX_SEARCH_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "relevance";
    public static final String DEFAULT_SORT_ORDER = "desc";

    // Document Status
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED = "FAILED";

    // Chinese Language
    public static final String CHINESE_ANALYZER = "ik_max_word";
    public static final String CHINESE_SEARCH_ANALYZER = "ik_smart";
    public static final double CHINESE_TEXT_THRESHOLD = 0.3;

    // Response Messages
    public static final String UPLOAD_SUCCESS = "Document uploaded successfully";
    public static final String UPLOAD_FAILED = "Failed to upload document";
    public static final String SEARCH_SUCCESS = "Search completed successfully";
    public static final String SEARCH_FAILED = "Search failed";
    public static final String DELETE_SUCCESS = "Document deleted successfully";
    public static final String DELETE_FAILED = "Failed to delete document";

    private ApiConstants() {
        // Private constructor to prevent instantiation
    }
}