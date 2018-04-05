package net.nekonekoserver.net.inventorylimiter.configure;

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by Nekoneko on 2017/11/10.
 */
public class Utf8YamlConfiguration extends YamlConfiguration {

    private static Charset UTF8_CHARSET = Charset.forName("UTF-8");

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException {
        super.load(file);
    }

    public void load(Reader reader) throws IOException, InvalidConfigurationException {
        Validate.notNull(reader, "Stream cannot be null");

        StringBuilder builder = new StringBuilder();
        try (BufferedReader input = new BufferedReader(reader)) {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        }

        loadFromString(builder.toString());
    }

    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");

        Files.createParentDirs(file);
        String data = saveToString();
        FileOutputStream stream = new FileOutputStream(file);
        try (OutputStreamWriter writer = new OutputStreamWriter(stream, UTF8_CHARSET)) {
            writer.write(data);
        }
    }
}

