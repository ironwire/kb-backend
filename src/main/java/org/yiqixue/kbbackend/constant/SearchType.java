package org.yiqixue.kbbackend.constant;

public enum SearchType {
    KEYWORD("keyword", "Keyword Search"),
    SEMANTIC("semantic", "Semantic Search"),
    HYBRID("hybrid", "Hybrid Search"),
    FUZZY("fuzzy", "Fuzzy Search");

    private final String type;
    private final String displayName;

    SearchType(String type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public String getDisplayName() {
        return displayName;
    }
}