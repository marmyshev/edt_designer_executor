package com.marmyshev.dt.designer.executor;

import java.nio.file.Path;

import com._1c.g5.v8.dt.platform.services.core.runtimes.RuntimeExecutionArguments;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.ILaunchableRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IThickClientLauncher;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;

public interface IDesignerLauncher extends IThickClientLauncher {

	public void startDesigner(ILaunchableRuntimeComponent component, InfobaseReference infobase,
			RuntimeExecutionArguments executionArguments);

	public void createDistributionFile(ILaunchableRuntimeComponent component, InfobaseReference infobase,
			RuntimeExecutionArguments executionArguments, Path file);
}
