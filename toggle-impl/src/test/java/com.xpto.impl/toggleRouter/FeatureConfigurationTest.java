package com.xpto.impl.toggleRouter;


import com.xpto.impl.Feature;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FeatureConfigurationTest {

    private static FeatureConfiguration featureConfiguration;

    @Test
    public void shouldBeEnabledWhenNoServiceIsSetAndFeatureIsEnabled() {
        Feature feature = new Feature("1", "isButtonBlue", "1", "", true, true);
        featureConfiguration = new FeatureConfiguration(Optional.empty(), feature);
        assertThat(featureConfiguration.isEnabled()).isTrue();
    }

    @Test
    public void shouldBeDisabledWhenNoServiceIsSetAndFeatureIsDisabled() {
        Feature feature = new Feature("1", "isButtonBlue", "1", "", true, false);
        featureConfiguration = new FeatureConfiguration(Optional.empty(), feature);
        assertThat(featureConfiguration.isEnabled()).isFalse();
    }

    @Test
    public void shouldBeEnabledWhenServiceHasPermission() {
        Feature feature = new Feature("1", "isButtonGreen", "1", "abc", true, true);
        featureConfiguration = new FeatureConfiguration(Optional.of("abc"), feature);
        assertThat(featureConfiguration.isEnabled()).isTrue();
    }

    @Test
    public void shouldBeDisableWhenServiceHasNoPermission() {
        Feature feature = new Feature("1", "isButtonGreen", "1", "abc", true, true);
        featureConfiguration = new FeatureConfiguration(Optional.of("trump"), feature);
        assertThat(featureConfiguration.isEnabled()).isFalse();
    }

    @Test
    public void shouldBeDisabledServicesDontMatchFeatureEnabled(){
        Feature feature = new Feature("1", "isButtonRed", "1", "abc", false, true);
        featureConfiguration = new FeatureConfiguration(Optional.of("trump"), feature);
        assertThat(featureConfiguration.isEnabled()).isFalse();
    }

    @Test
    public void shouldBeEnabledServicesDontMatchFeatureDisabled(){
        Feature feature = new Feature("1", "isButtonRed", "1", "abc", false, false);
        featureConfiguration = new FeatureConfiguration(Optional.of("trump"), feature);
        assertThat(featureConfiguration.isEnabled()).isTrue();
    }
}
