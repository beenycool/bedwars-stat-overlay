package com.bedwarsstats.cache;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {
    private static final String CACHE_DIR = "cache";
    private static final LZ4Factory factory = LZ4Factory.fastestInstance();
    private static final LZ4Compressor compressor = factory.fastCompressor();
    private static final LZ4FastDecompressor decompressor = factory.fastDecompressor();

    private final Map<String, byte[]> cache = new HashMap<>();

    public CacheManager() {
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    public void put(String key, byte[] data) {
        byte[] compressedData = compressor.compress(data);
        cache.put(key, compressedData);
        saveToFile(key, compressedData);
    }

    public byte[] get(String key) {
        byte[] compressedData = cache.get(key);
        if (compressedData == null) {
            compressedData = loadFromFile(key);
            if (compressedData != null) {
                cache.put(key, compressedData);
            }
        }
        return compressedData != null ? decompressor.decompress(compressedData, compressedData.length * 255) : null;
    }

    private void saveToFile(String key, byte[] data) {
        Path path = Paths.get(CACHE_DIR, key);
        try (OutputStream os = Files.newOutputStream(path)) {
            os.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] loadFromFile(String key) {
        Path path = Paths.get(CACHE_DIR, key);
        if (Files.exists(path)) {
            try (InputStream is = Files.newInputStream(path)) {
                return is.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
