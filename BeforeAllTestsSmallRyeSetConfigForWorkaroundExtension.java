package ru.dobrokvashinevgeny.services.quantitycalculator.infrastructure.resources;

import io.quarkus.runtime.configuration.QuarkusConfigFactory;
import io.smallrye.config.*;
import org.junit.jupiter.api.extension.*;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class BeforeAllTestsSmallRyeSetConfigForWorkaroundExtension implements BeforeAllCallback {
	private static boolean started = false;

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		if (!started) {
			started = true;

			final SmallRyeConfigBuilder builder = new SmallRyeConfigBuilder();
			builder.addDefaultSources();
			builder.addDiscoveredConverters();
			builder.addDiscoveredSources();
			SmallRyeConfig smallRyeConfigForWorkaround = builder.build();
			QuarkusConfigFactory.setConfig(smallRyeConfigForWorkaround);

			// The following line registers a callback hook when the root test context is shut down
			context.getRoot().getStore(GLOBAL).put("quantity-calculator", this);
		}
	}
}


@EnableWeld
@ExtendWith({BeforeAllTestsSmallRyeSetConfigForWorkaroundExtension.class})
class QuantityResourceIntegrationTest {
}
