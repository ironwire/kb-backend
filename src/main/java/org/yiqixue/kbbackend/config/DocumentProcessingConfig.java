package org.yiqixue.kbbackend.config;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentProcessingConfig {

    @Bean
    public Tika tika() {
        return new Tika();
    }

    @Bean
    public Parser parser() {
        return new AutoDetectParser();
    }

    @Bean
    public Detector detector() {
        return TikaConfig.getDefaultConfig().getDetector();
    }
}