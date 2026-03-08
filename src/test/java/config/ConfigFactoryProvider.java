package config;

import org.aeonbits.owner.ConfigFactory;

public final class ConfigFactoryProvider {
    private ConfigFactoryProvider() {}

    public static MobileConfig mobile() {
        return ConfigFactory.create(MobileConfig.class);
    }
}
