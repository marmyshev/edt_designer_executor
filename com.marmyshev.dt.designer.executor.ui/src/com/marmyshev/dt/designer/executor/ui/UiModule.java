package com.marmyshev.dt.designer.executor.ui;

import org.eclipse.core.runtime.Plugin;

import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.IRuntimeInstallationManager;
import com._1c.g5.wiring.AbstractServiceAwareModule;
import com.google.inject.name.Names;

/**
 * TODO: javadoc
 */
public class UiModule extends AbstractServiceAwareModule {
	public UiModule(Plugin bundle) {
		super(bundle);
	}

	@Override
	protected void doConfigure() {
		bind(IInfobaseManager.class).toService();
		bind(IRuntimeInstallationManager.class)
				.annotatedWith(Names.named("com._1c.g5.v8.dt.platform.services.core.runtimeType.EnterprisePlatform"))
				.toService();
	}
}