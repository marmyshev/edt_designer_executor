/**
 * Copyright (C) 2016, Dmitriy Marmyshev
 */
package com.marmyshev.dt.designer.executor;

import java.io.File;
import java.nio.file.Path;

import com._1c.g5.v8.dt.common.Pair;
import com._1c.g5.v8.dt.platform.services.core.runtimes.RuntimeExecutionArguments;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.ILaunchableRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IThickClientLauncher;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.RuntimeVersionRequiredException;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.impl.RuntimeExecutionCommandBuilder$ThickClientMode;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.impl.ThickClientLauncher;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;

public class AdvancedThickClientLauncher
    extends ThickClientLauncher
    implements IAdvancedThickClientLauncher
{

    public AdvancedThickClientLauncher()
    {
        super();
    }

    @Override
    public void startDesigner(ILaunchableRuntimeComponent component, InfobaseReference infobase,
			RuntimeExecutionArguments executionArguments) {

		RuntimeExecutionCommandBuilder builder = new RuntimeExecutionCommandBuilder(component.getLaunchable(),
				RuntimeExecutionCommandBuilder$ThickClientMode.DESIGNER);

		builder.visible();

		builder.forInfobase(infobase, splitInfobaseConnection());

		appendInfobaseAccess(builder, executionArguments);

        try
        {
            this.executeRuntimeProcessCommand(builder);
        }
        catch (Throwable e)
        {
            if (e instanceof RuntimeVersionRequiredException)
            {
                RuntimeVersionRequiredException versionRequired = (RuntimeVersionRequiredException)e;
                Pair thickClient;
                ((IThickClientLauncher)(thickClient =
                    this.findRequired(versionRequired.getVersion(), versionRequired.getBuild())).second).updateInfobase(
                        (ILaunchableRuntimeComponent)thickClient.first, infobase, executionArguments);
            }
            else
            {
                throw e;
            }
        }
    }

    @Override
    public void createDistributionFile(ILaunchableRuntimeComponent component, InfobaseReference infobase,
        RuntimeExecutionArguments executionArguments, Path file)
    {

        RuntimeExecutionCommandBuilder builder = new RuntimeExecutionCommandBuilder(component.getLaunchable(),
            RuntimeExecutionCommandBuilder$ThickClientMode.DESIGNER);

        builder.forInfobase(infobase, splitInfobaseConnection());

        appendInfobaseAccess(builder, executionArguments);

        builder.createDistributionFile(file.toAbsolutePath().toString());

        executeRuntimeProcessCommand(builder);

    }

    @Override
    public Pair<String, Process> runClient(ILaunchableRuntimeComponent component, InfobaseReference infobase,
        RuntimeExecutionArguments executionArguments)
    {

        File launchable = component.getLaunchable();

        RuntimeExecutionCommandBuilder builder = new RuntimeExecutionCommandBuilder(component.getLaunchable(),
            RuntimeExecutionCommandBuilder$ThickClientMode.DESIGNER);

        builder.visible();

        builder.forInfobase(infobase, splitInfobaseConnection());

        appendInfobaseAccess(builder, executionArguments);

        return new Pair<String, Process>(launchable.getAbsolutePath(), builder.start());
    }

}
