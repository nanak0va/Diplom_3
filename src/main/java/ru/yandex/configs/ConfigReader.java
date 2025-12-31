package ru.yandex.configs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
    private static final JsonNode root;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("application.yaml");

            if (is == null) {
                throw new RuntimeException("Файл application.yaml не найден в ресурсах!");
            }

            root = mapper.readTree(is);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения YAML: " + e.getMessage());
        }
    }

    public static String asString(String path) {
        return root.at(path).asText("");
    }

    public static Integer asInt(String path) {
        return root.at(path).asInt();
    }

    public static List<String> asList(String path) {
        List<String> list = new ArrayList<>();
        root.at(path).forEach(node -> list.add(node.asText()));
        return list;
    }
}
