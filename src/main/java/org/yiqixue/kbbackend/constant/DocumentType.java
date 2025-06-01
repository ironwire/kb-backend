package org.yiqixue.kbbackend.constant;

public enum DocumentType {
    PDF("application/pdf", "PDF Document"),
    WORD("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "Word Document"),
    WORD_LEGACY("application/msword", "Word Document (Legacy)"),
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Excel Spreadsheet"),
    EXCEL_LEGACY("application/vnd.ms-excel", "Excel Spreadsheet (Legacy)"),
    POWERPOINT("application/vnd.openxmlformats-officedocument.presentationml.presentation", "PowerPoint Presentation"),
    POWERPOINT_LEGACY("application/vnd.ms-powerpoint", "PowerPoint Presentation (Legacy)"),
    TEXT("text/plain", "Text File"),
    HTML("text/html", "HTML Document"),
    RTF("application/rtf", "Rich Text Format");

    private final String mimeType;
    private final String displayName;

    DocumentType(String mimeType, String displayName) {
        this.mimeType = mimeType;
        this.displayName = displayName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DocumentType fromMimeType(String mimeType) {
        for (DocumentType type : values()) {
            if (type.mimeType.equals(mimeType)) {
                return type;
            }
        }
        return null;
    }
}