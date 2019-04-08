package nl.tcilegnar.mybaseapp.util;

import nl.tcilegnar.mybaseapp.BuildConfig;

@SuppressWarnings("ConstantConditions")
public class MyBuildConfig {
    public boolean isProduction() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("release");
    }

    public boolean isDemo() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("demo");
    }

    public boolean isDevelop() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug");
    }
}
