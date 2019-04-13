package nl.tcilegnar.mybaseapp.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyBuildConfigTestDebug extends MyBuildConfigTest {
    @Override
    public void isRelease_DependsOnBuildType() {
        assertFalse(new MyBuildConfig().isProduction());
    }

    @Override
    public void isDemo_DependsOnBuildType() {
        assertFalse(new MyBuildConfig().isDemo());
    }

    @Override
    public void isDevelop_DependsOnBuildType() {
        assertTrue(new MyBuildConfig().isDevelop());
    }
}