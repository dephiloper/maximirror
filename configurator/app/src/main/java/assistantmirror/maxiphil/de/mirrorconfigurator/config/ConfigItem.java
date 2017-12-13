package assistantmirror.maxiphil.de.mirrorconfigurator.config;

import java.lang.reflect.ParameterizedType;

public class ConfigItem<T> {
    private String key;
    private T value;

    ConfigItem(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public Class getTypeParameterClass() {
        return value.getClass();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }
}
