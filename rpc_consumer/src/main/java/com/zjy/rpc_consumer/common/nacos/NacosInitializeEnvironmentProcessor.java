package com.zjy.rpc_consumer.common.nacos;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NacosInitializeEnvironmentProcessor implements EnvironmentPostProcessor, Ordered {

    private final Logger logger = LoggerFactory.getLogger(NacosInitializeEnvironmentProcessor.class);

    private static final String CLASSPATH = "classpath:";

    private static final String[] SOURCE_FILE_EXTENSIONS = new String[]{"properties", "xml"};

    private static final String NACOS_COFIG_REGEX = "\\@(.+?)\\@";


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        findAndReplace(environment);
    }

    private void findAndReplace(ConfigurableEnvironment environment) {
        try {
            List<File> sources = getAllSources();
            if (sources == null || sources.size() == 0) {
                logger.info("【nacos-config-replace】not find source files,replace task finish!");
                return;
            }
            sources.forEach(file -> writeNacosConfigToFile(file, environment));
        } catch (Exception e) {
            logger.error("【nacos-config-replace】find source files error", e);
        }
        logger.info("【nacos-config-replace】replace source file success,replace task finish!");
    }


    private void writeNacosConfigToFile(File file, ConfigurableEnvironment environment) {
        try {
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            content = matchConfig(content, environment);
            FileUtils.write(file, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("【nacos-config-replace】replace source files error,file:{}", file.getName(), e);
        }
    }

    private String matchConfig(String source, ConfigurableEnvironment environment) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(NACOS_COFIG_REGEX);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            // 键名
            String name = matcher.group(1);
            String value = Objects.toString(environment.getProperty(name), name);
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private List<File> getAllSources() throws IOException {
        File file = new File(ResourceUtils.getURL(CLASSPATH).getPath());
        List<File> files = new ArrayList<>();
        // 判断是否存在目录
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        files.addAll(FileUtils.listFiles(file, SOURCE_FILE_EXTENSIONS, true));
        return files;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
