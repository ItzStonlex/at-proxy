package net.advanceteam.proxy.common.config;

import lombok.Getter;
import net.advanceteam.proxy.AdvanceProxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public final class FileConfiguration {

    private static final char SEPARATOR = '.';
    
    @Getter
    private Map<String, Object> self;
    
    @Getter
    private final FileConfiguration defaults;


    public FileConfiguration(FileConfiguration defaults) {
        this(new LinkedHashMap<String, Object>(), defaults);
    }

    FileConfiguration(Map<?, ?> map, FileConfiguration defaults) {
        this.self = new LinkedHashMap<>();
        this.defaults = defaults;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = (entry.getKey() == null) ? "null" : entry.getKey().toString();

            if (entry.getValue() instanceof Map) {
                this.self.put(key, new FileConfiguration((Map) entry.getValue(), (defaults == null) ? null : defaults.getSection(key)));
            } else {
                this.self.put(key, entry.getValue());
            }
        }
    }

    private FileConfiguration getSectionFor(String path) {
        int index = path.indexOf(SEPARATOR);
        if (index == -1) {
            return this;
        }

        String root = path.substring(0, index);
        Object section = self.get(root);
        
        if (section == null) {
            section = new FileConfiguration((defaults == null) ? null : defaults.getSection(root));
            self.put(root, section);
        }

        return (FileConfiguration) section;
    }

    private String getChild(String path) {
        int index = path.indexOf(SEPARATOR);
        return (index == -1) ? path : path.substring(index + 1);
    }
    
    public <T> T get(String path, Class<T> returnClassType) {
        return (T) getOrDefault(path);
    }

    public Object get(String path) {
        return self.get(path);
    }
    
    public <T> T getOrDefault(String path, T def) {
        FileConfiguration section = getSectionFor(path);
        Object val;
        if (section == this) {
            val = self.get(path);
        } else {
            val = section.getOrDefault(getChild(path), def);
        }

        if (val == null && def instanceof FileConfiguration) {
            self.put(path, def);
        }

        return (val != null) ? (T) val : def;
    }

    public boolean contains(String path) {
        return getOrDefault(path, null) != null;
    }

    public Object getOrDefault(String path) {
        return getOrDefault(path, getDefault(path));
    }

    public Object getDefault(String path) {
        return (defaults == null) ? null : defaults.getOrDefault(path);
    }

    public void set(String path, Object value) {
        if (value instanceof Map) {
            value = new FileConfiguration((Map) value, (defaults == null) ? null : defaults.getSection(path));
        }

        FileConfiguration section = getSectionFor(path);
        if (section == this) {
            if (value == null) {
                self.remove(path);
            } else {
                self.put(path, value);
            }
        } else {
            section.set(getChild(path), value);
        }
    }
    
    public FileConfiguration getSection(String path) {
        Object def = getDefault(path);
        return (FileConfiguration) getOrDefault(path, (def instanceof FileConfiguration) ? def : new FileConfiguration((defaults == null) ? null : defaults.getSection(path)));
    }
    
    public Collection<String> getKeys() {
        return new LinkedHashSet<>(self.keySet());
    }
    
    public byte getByte(String path) {
        Object def = getDefault(path);
        return getByte(path, (def instanceof Number) ? ((Number) def).byteValue() : 0);
    }

    public byte getByte(String path, byte def) {
        Number val = getOrDefault(path, def);
        
        return (val instanceof Number) ? val.byteValue() : def;
    }

    public List<Byte> getByteList(String path) {
        List<?> list = getList(path);
        List<Byte> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Number) {
                result.add(((Number) object).byteValue());
            }
        }

        return result;
    }

    public short getShort(String path) {
        Object def = getDefault(path);
        return getShort(path, (def instanceof Number) ? ((Number) def).shortValue() : 0);
    }

    public short getShort(String path, short def) {
        Number val = getOrDefault(path, def);
        
        return (val instanceof Number) ? val.shortValue() : def;
    }

    public List<Short> getShortList(String path) {
        List<?> list = getList(path);
        List<Short> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Number) {
                result.add(((Number) object).shortValue());
            }
        }

        return result;
    }

    public int getInt(String path) {
        Object def = getDefault(path);
        
        return getInt(path, (def instanceof Number) ? ((Number) def).intValue() : 0);
    }

    public int getInt(String path, int def) {
        Number val = getOrDefault(path, def);
        
        return (val instanceof Number) ? val.intValue() : def;
    }

    public List<Integer> getIntList(String path) {
        List<?> list = getList(path);
        List<Integer> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Number) {
                result.add(((Number) object).intValue());
            }
        }

        return result;
    }

    public long getLong(String path) {
        Object def = getDefault(path);
        return getLong(path, (def instanceof Number) ? ((Number) def).longValue() : 0);
    }

    public long getLong(String path, long def) {
        Number val = getOrDefault(path, def);
        
        return (val instanceof Number) ? val.longValue() : def;
    }

    public List<Long> getLongList(String path) {
        List<?> list = getList(path);
        List<Long> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Number) {
                result.add(((Number) object).longValue());
            }
        }

        return result;
    }

    public float getFloat(String path) {
        Object def = getDefault(path);
        return getFloat(path, (def instanceof Number) ? ((Number) def).floatValue() : 0);
    }

    public float getFloat(String path, float def) {
        Number val = getOrDefault(path, def);
        
        return (val instanceof Number) ? val.floatValue() : def;
    }

    public List<Float> getFloatList(String path) {
        List<?> list = getList(path);
        List<Float> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Number) {
                result.add(((Number) object).floatValue());
            }
        }

        return result;
    }

    public double getDouble(String path) {
        Object def = getDefault(path);
        return getDouble(path, (def instanceof Number) ? ((Number) def).doubleValue() : 0);
    }

    public double getDouble(String path, double def) {
        Number val = getOrDefault(path, def);
        
        return (val instanceof Number) ? val.doubleValue() : def;
    }

    public List<Double> getDoubleList(String path) {
        List<?> list = getList(path);
        List<Double> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Number) {
                result.add(((Number) object).doubleValue());
            }
        }

        return result;
    }

    public boolean getBoolean(String path) {
        Object def = getDefault(path);
        return getBoolean(path, (def instanceof Boolean) ? (Boolean) def : false);
    }

    public boolean getBoolean(String path, boolean def) {
        Boolean val = getOrDefault(path, def);
        
        return (val instanceof Boolean) ? val : def;
    }

    public List<Boolean> getBooleanList(String path) {
        List<?> list = getList(path);
        List<Boolean> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Boolean) {
                result.add((Boolean) object);
            }
        }

        return result;
    }

    public char getChar(String path) {
        Object def = getDefault(path);
        return getChar(path, (def instanceof Character) ? (Character) def : '\u0000');
    }

    public char getChar(String path, char def) {
        Character val = getOrDefault(path, def);
        
        return (val instanceof Character) ? val : def;
    }

    public List<Character> getCharList(String path) {
        List<?> list = getList(path);
        List<Character> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Character) {
                result.add((Character) object);
            }
        }

        return result;
    }

    public String getString(String path) {
        Object def = getDefault(path);
        return getString(path, (def instanceof String) ? (String) def : "");
    }

    public String getString(String path, String def) {
        String val = getOrDefault(path, def);
        
        return (val instanceof String) ? val : def;
    }

    public List<String> getStringList(String path) {
        List<?> list = getList(path);
        List<String> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof String) {
                result.add((String) object);
            }
        }

        return result;
    }

    public List<?> getList(String path) {
        Object def = getDefault(path);
        
        return getList(path, (def instanceof List<?>) ? (List<?>) def : Collections.EMPTY_LIST);
    }

    public List<?> getList(String path, List<?> def) {
        Object val = getOrDefault(path, def);
        
        return (val instanceof List<?>) ? (List<?>) val : def;
    }
    
    public void save(File file) throws IOException {
        AdvanceProxy.getInstance().getConfigManager()
                .save(this, file);
    }
    
    public void load(File file) throws IOException {
        this.self = AdvanceProxy.getInstance().getConfigManager()
                .load(file).getSelf();
    }
}
