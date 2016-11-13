package com.marmyshev.dt.designer.executor;

import java.nio.file.Path;

import com._1c.g5.v8.dt.common.Pair;
import com._1c.g5.v8.dt.platform.services.core.runtimes.RuntimeExecutionArguments;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.ILaunchableRuntimeComponent;
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
        RuntimeExecutionArguments executionArguments)
    {

        if (!splitInfobaseConnection())
        {
            RuntimeExecutionCommandBuilder builder = new RuntimeExecutionCommandBuilder(component.getLaunchable(),
                RuntimeExecutionCommandBuilder$ThickClientMode.DESIGNER);

            builder.visible();

            builder.forInfobase(infobase, true);

            appendInfobaseAccess(builder, executionArguments);

            executeRuntimeProcessCommand(builder);

        }

        // TODO: Find new version from exeption

        // thickClient =
        // findRequired(com._1c.g5.v8.dt.platform.services.core.runtimes.execution.RuntimeVersionRequiredException.getVersion(),
        // com._1c.g5.v8.dt.platform.services.core.runtimes.execution.RuntimeVersionRequiredException.getBuild());

    }

    @Override
    public void createDistributionFile(ILaunchableRuntimeComponent component, InfobaseReference infobase,
        RuntimeExecutionArguments executionArguments, Path file)
    {

        RuntimeExecutionCommandBuilder builder = new RuntimeExecutionCommandBuilder(component.getLaunchable(),
            RuntimeExecutionCommandBuilder$ThickClientMode.DESIGNER);

        builder.forInfobase(infobase, true);

        appendInfobaseAccess(builder, executionArguments);

        builder.createDistributionFile(file.toAbsolutePath().toString());

        executeRuntimeProcessCommand(builder);

    }

    @Override
    public Pair<String, Process> runClient(ILaunchableRuntimeComponent component, InfobaseReference infobase,
        RuntimeExecutionArguments executionArguments)
    {

        RuntimeExecutionCommandBuilder builder = new RuntimeExecutionCommandBuilder(component.getLaunchable(),
            RuntimeExecutionCommandBuilder$ThickClientMode.DESIGNER);

        builder.visible();

        builder.forInfobase(infobase, true);

        appendInfobaseAccess(builder, executionArguments);

        // TODO: Catch RuntimeExecption

        Pair<String, Process> thickClient =
            new Pair<String, Process>(component.getLaunchable().getAbsolutePath(), builder.start());
        return thickClient;

    }

}
