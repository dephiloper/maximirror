package assistantmirror.maxiphil.de.mirrorconfigurator.config;

/**
 * Created by cap on 13.12.17.
 */

public class ConfigItemBool implements ConfigItem {
    private String key;
    private boolean value;

    public ConfigItemBool(String key, boolean value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
