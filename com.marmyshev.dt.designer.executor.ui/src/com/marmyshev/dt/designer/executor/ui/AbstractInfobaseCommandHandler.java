package com.marmyshev.dt.designer.executor.ui;

import org.eclipse.core.commands.AbstractHandler;

import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseAccessManager;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseAccessSettings;
import com._1c.g5.v8.dt.platform.services.core.runtimes.RuntimeExecutionArguments;
import com._1c.g5.v8.dt.platform.services.core.runtimes.environments.IResolvableRuntimeInstallation;
import com._1c.g5.v8.dt.platform.services.core.runtimes.environments.IResolvableRuntimeInstallationManager;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;
import com.google.inject.Inject;

public abstract class AbstractInfobaseCommandHandler extends AbstractHandler {


	@Inject
	private IResolvableRuntimeInstallationManager resolvableRuntimeInstallationManager;

	@Inject
	private IInfobaseAccessManager infobaseAccessManager;


	public AbstractInfobaseCommandHandler() {
	}

	protected IResolvableRuntimeInstallation getRuntimeInstallation(InfobaseReference infobase) {
		return this.resolvableRuntimeInstallationManager.getDefault(
				"com._1c.g5.v8.dt.platform.services.core.runtimeType.EnterprisePlatform", infobase.getVersion()); //$NON-NLS-1$
	}

	protected RuntimeExecutionArguments buildArguments(InfobaseReference infobase) {
		RuntimeExecutionArguments arguments = new RuntimeExecutionArguments();

		IInfobaseAccessSettings settings = this.infobaseAccessManager.getSettings(infobase);
		arguments.setAccess(settings.access());
		arguments.setUsername(settings.userName());
		arguments.setPassword(settings.password());
		return arguments;
	}
}
