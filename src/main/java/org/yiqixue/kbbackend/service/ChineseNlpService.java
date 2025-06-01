package org.yiqixue.kbbackend.service;

import java.util.List;

public interface ChineseNlpService {
    List<String> segmentText(String text);
    String convertToSimplified(String text);
    String convertToTraditional(String text);
    List<String> extractKeywords(String text, int count);
    String generateSummary(String text, int maxLength);
    boolean isChineseText(String text);
}