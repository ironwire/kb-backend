package org.yiqixue.kbbackend.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class ChineseTextUtil {

    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fff]");
    private static final Pattern CHINESE_PUNCTUATION = Pattern.compile("[\\u3000-\\u303f\\uff00-\\uffef]");
    private static final Pattern ENGLISH_PATTERN = Pattern.compile("[a-zA-Z]");

    public static boolean containsChinese(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        return CHINESE_PATTERN.matcher(text).find();
    }

    public static boolean isPrimarilyChinese(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        long chineseCount = text.chars()
                .filter(ch -> CHINESE_PATTERN.matcher(String.valueOf((char) ch)).find())
                .count();

        return (double) chineseCount / text.length() > 0.5;
    }

    public static String cleanChineseText(String text) {
        if (text == null) {
            return null;
        }

        // Remove extra whitespaces
        text = text.replaceAll("\\s+", " ");

        // Remove special characters but keep Chinese punctuation
        text = text.replaceAll("[^\\u4e00-\\u9fff\\u3000-\\u303f\\uff00-\\uffef\\w\\s]", "");

        return text.trim();
    }

    public static int countChineseCharacters(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        return (int) text.chars()
                .filter(ch -> CHINESE_PATTERN.matcher(String.valueOf((char) ch)).find())
                .count();
    }

    public static int countEnglishWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        String[] words = text.split("\\s+");
        int count = 0;

        for (String word : words) {
            if (ENGLISH_PATTERN.matcher(word).find()) {
                count++;
            }
        }

        return count;
    }

    public static String extractChineseText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        StringBuilder chinese = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (CHINESE_PATTERN.matcher(String.valueOf(ch)).find() ||
                    CHINESE_PUNCTUATION.matcher(String.valueOf(ch)).find()) {
                chinese.append(ch);
            }
        }

        return chinese.toString();
    }

    public static boolean isValidChineseLength(String text, int minLength, int maxLength) {
        if (text == null) {
            return false;
        }

        int chineseCount = countChineseCharacters(text);
        return chineseCount >= minLength && chineseCount <= maxLength;
    }
}