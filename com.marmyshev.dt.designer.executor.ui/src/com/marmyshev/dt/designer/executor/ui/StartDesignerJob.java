/**
 * Copyright (C) 2016, Dmitriy Marmyshev
 */
package com.marmyshev.dt.designer.executor.ui;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseAccessManager;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseAccessSettings;
import com._1c.g5.v8.dt.platform.services.core.runtimes.RuntimeExecutionArguments;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.ILaunchableRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;
import com._1c.g5.v8.dt.platform.services.model.InfobaseType;
import com._1c.g5.v8.dt.platform.services.ui.runtimes.RuntimeExecutionErrorDialog;
import com.google.inject.Inject;
import com.marmyshev.dt.designer.executor.IAdvancedThickClientLauncher;

public class StartDesignerJob
    extends Job
{
    private final Display display;
    private final ILaunchableRuntimeComponent component;
    private final IAdvancedThickClientLauncher designer;
    private final InfobaseReference infobase;

    @Inject
    private IInfobaseAccessManager infobaseAccessManager;

    public StartDesignerJob(ILaunchableRuntimeComponent component, IAdvancedThickClientLauncher designer,
        InfobaseReference infobase, Display display)
    {
        super(
            MessageFormat.format(Messages.DesignerStartHandler_Starting_1c_designer_for_infobase, infobase.getName()));

        this.component = component;
        this.designer = designer;
        this.infobase = infobase;
        this.display = display;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        if (infobase.getInfobaseType() == InfobaseType.WEB)
        {
            if (!display.isDisposed())
            {
                Shell shell = display.getActiveShell();
                display.syncExec(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        MessageDialog.openInformation(shell, Messages.Actions_Designer,
                            Messages.Actions_Designer_cannot_be_started_for_web_infobases);
                    }
                });
            }
            return Status.CANCEL_STATUS;
        }

        RuntimeExecutionArguments arguments = buildArguments(infobase);

        try
        {
            designer.startDesigner(component, infobase, arguments);
        }
        catch ( Exception e)
        {
            Activator.logError(e);

            if(!display.isDisposed())
            {
                Shell shell = display.getActiveShell();
                // Show error dialog
                display.asyncExec(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (!display.isDisposed())
                        {
                            RuntimeExecutionErrorDialog.openError(shell,
                                Messages.Actions_Cannot_start_Designer, e);
                        }
                    }
                });
            }
            return Activator.createErrorStatus(e.getLocalizedMessage(), e);
        }

        monitor.done();
        return Status.OK_STATUS;
    }

    protected RuntimeExecutionArguments buildArguments(InfobaseReference infobase)
    {
        RuntimeExecutionArguments arguments = new RuntimeExecutionArguments();

        IInfobaseAccessSettings settings = infobaseAccessManager.getSettings(infobase);
        arguments.setAccess(settings.access());
        arguments.setUsername(settings.userName());
        arguments.setPassword(settings.password());
        return arguments;
    }

}
