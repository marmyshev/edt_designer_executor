/**
 * Copyright (C) 2016, Dmitriy Marmyshev
 */
package com.marmyshev.dt.designer.executor;

import java.io.File;

import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.impl.RuntimeExecutionCommandBuilder$ThickClientMode;

public class DesignerExecutionCommandBuilder
    extends com._1c.g5.v8.dt.platform.services.core.runtimes.execution.impl.RuntimeExecutionCommandBuilder
{

    public DesignerExecutionCommandBuilder(File file, RuntimeExecutionCommandBuilder$ThickClientMode clientMode)
    {
        super(file, clientMode);
    }

    public void visible()
    {

        appendOption(commands, "VISIBLE"); //$NON-NLS-1$
    }

    public void createDistributionFile(String file)
    {
        appendOption(commands, "CreateDistributionFile"); //$NON-NLS-1$

        appendOptionParameter(commands, file);

    }

}
