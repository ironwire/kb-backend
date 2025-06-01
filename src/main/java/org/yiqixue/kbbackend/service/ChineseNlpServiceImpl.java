package org.yiqixue.kbbackend.service;

import com.huaban.analysis.jieba.JiebaSegmenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChineseNlpServiceImpl implements ChineseNlpService {

    private final JiebaSegmenter segmenter;
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fff]");

    public ChineseNlpServiceImpl() {
        this.segmenter = new JiebaSegmenter();
    }

    @Override
    public List<String> segmentText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }
        return segmenter.sentenceProcess(text);
    }

    @Override
    public String convertToSimplified(String text) {
        // This is a placeholder - you would integrate with a proper conversion library
        // like opencc4j or similar
        return text; // TODO: Implement proper conversion
    }

    @Override
    public String convertToTraditional(String text) {
        // This is a placeholder - you would integrate with a proper conversion library
        return text; // TODO: Implement proper conversion
    }

    @Override
    public List<String> extractKeywords(String text, int count) {
        List<String> segments = segmentText(text);
        return segments.stream()
                .filter(word -> word.length() > 1)
                .filter(word -> !isStopWord(word))
                .distinct()
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public String generateSummary(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }

        String[] sentences = text.split("[。！？]");
        StringBuilder summary = new StringBuilder();

        for (String sentence : sentences) {
            if (summary.length() + sentence.length() <= maxLength) {
                summary.append(sentence).append("。");
            } else {
                break;
            }
        }

        return summary.toString();
    }

    @Override
    public boolean isChineseText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        long chineseCharCount = text.chars()
                .filter(ch -> CHINESE_PATTERN.matcher(String.valueOf((char) ch)).find())
                .count();

        return (double) chineseCharCount / text.length() > 0.3;
    }

    private boolean isStopWord(String word) {
        // Simple stop word detection - in production, use a comprehensive stop word list
        Set<String> stopWords = Set.of("的", "了", "在", "是", "我", "有", "和", "就",
                "不", "人", "都", "一", "一个", "上", "也", "很",
                "到", "说", "要", "去", "你", "会", "着", "没有", "看");
        return stopWords.contains(word);
    }
}