package nl.tcilegnar.mybaseapp.util;

import org.junit.Test;

public abstract class MyBuildConfigTest {
    @Test
    public abstract void isRelease_DependsOnBuildType();

    @Test
    public abstract void isDemo_DependsOnBuildType();

    @Test
    public abstract void isDevelop_DependsOnBuildType();
}