package com.marmyshev.dt.designer.executor.ui;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com._1c.g5.v8.dt.common.Pair;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseAssociation;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.IRuntimeInstallationManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.RuntimeExecutionArguments;
import com._1c.g5.v8.dt.platform.services.core.runtimes.environments.IResolvableRuntimeInstallation;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.ILaunchableRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponentExecutor;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponentManager;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;
import com._1c.g5.v8.dt.platform.services.model.InfobaseType;
import com._1c.g5.v8.dt.platform.services.model.RuntimeInstallation;
import com._1c.g5.v8.dt.platform.services.ui.runtimes.RuntimeExecutionErrorDialog;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.marmyshev.dt.designer.executor.IAdvancedThickClientLauncher;

public class DesignerStartHandler
    extends AbstractInfobaseCommandHandler
{

    @Inject
    @Named("com._1c.g5.v8.dt.platform.services.core.runtimeType.EnterprisePlatform")
    private IRuntimeInstallationManager runtimeInstallationManager;

    @Inject
    private IInfobaseManager infobaseManager;

    @Inject
    private IRuntimeComponentManager runtimeComponentManager;

    public DesignerStartHandler()
    {
        super();
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {

        Shell shell = HandlerUtil.getActiveShell(event);
        Display display;
        if (shell != null)
        {
            display = shell.getDisplay();
        }
        else
        {
            display = PlatformUI.getWorkbench().getDisplay();
        }

        IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
        IWorkbenchPage page;
        if (activePart == null)
        {

            page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

        }
        else
        {
            page = activePart.getSite().getPage();
        }

        if (runtimeInstallationManager.getAll().isEmpty())
        {
            display.asyncExec(new Runnable()
            {

                @Override
                public void run()
                {
                    MessageDialog.openInformation(shell, Messages.DesignerStartHandler_Designer,
                        Messages.DesignerStartHandler_Not_found_installed_1c_enterprise_platform);
                }
            });

            return null;
        }

        ISelection selection = page.getSelection();
        List<InfobaseReference> infobases = new ArrayList<>();

        if (selection != null & selection instanceof IStructuredSelection)
        {
            IStructuredSelection strucSelection = (IStructuredSelection)selection;
            for (Iterator<Object> iterator = strucSelection.iterator(); iterator.hasNext();)
            {
                Object element = iterator.next();
                if (element instanceof InfobaseReference)
                {

                    if (!infobases.contains(element))
                    {
                        infobases.add((InfobaseReference)element);
                    }
                }
            }
        }

        if (infobases.size() > 0)
        {
            // Start Designer for all selected infobases
            runStartDesigner(display, infobases);
            return null;
        }

        IProject project = com._1c.g5.v8.dt.platform.services.ui.SelectionContextProject.getContextProject(page);
        if (project == null)
        {
            return null;
        }

        if (!PlatformUI.getWorkbench().saveAllEditors(true))
        {
            return null;
        }

        IInfobaseAssociation association = infobaseManager.getAssociation(project);
        if (association != null)
        {

            if (association.getInfobases().size() > 1)
            {

                if (MessageDialog.openQuestion(shell, null,
                    Messages.DesignerStartHandler_Selected_project_has_several_infobases_Start_1C_Designer_for_all))
                {
                    infobases.addAll(association.getInfobases());
                }
                else
                {
                    infobases.add(association.getDefaultInfobase());
                }

            }
            else
            {
                // Run Designer with default infobase
                infobases.add(association.getDefaultInfobase());
            }
            runStartDesigner(display, infobases);
        }

        return null;
    }

    @Override
    public boolean isEnabled()
    {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null)
        {
            return false;
        }

        IWorkbenchPage page = window.getActivePage();
        if (page == null)
        {
            return false;
        }
        IProject project = com._1c.g5.v8.dt.platform.services.ui.SelectionContextProject.getContextProject(page);
        if (project != null)
        {
            IInfobaseAssociation association = infobaseManager.getAssociation(project);
            if (association != null)
            {
                return true;
            }
        }

        ISelection selection = page.getSelection();

        if (selection != null & selection instanceof IStructuredSelection)
        {
            IStructuredSelection strucSelection = (IStructuredSelection)selection;
            for (Iterator<Object> iterator = strucSelection.iterator(); iterator.hasNext();)
            {
                Object element = iterator.next();
                if (element instanceof InfobaseReference)
                {
                    return true;
                }
            }
        }

        return false;

    }

    protected void runStartDesigner(Display display, List<InfobaseReference> infobases)
    {

        Shell shell = display.getActiveShell();

        List<InfobaseReference> webInfobases = new ArrayList<>();

        for (InfobaseReference infobase : infobases)
        {
            if (infobase.getInfobaseType() == InfobaseType.WEB)
            {
                webInfobases.add(infobase);
                infobases.remove(infobase);
            }
        }

        if (webInfobases.size() > 0)
        {
            display.syncExec(new Runnable()
            {

                @Override
                public void run()
                {
                    MessageDialog.openInformation(shell, Messages.DesignerStartHandler_Designer,
                        Messages.DesignerStartHandler_Designer_cannot_be_started_for_web_infobases);
                }
            });
        }

        if (infobases.size() == 0)
        {
            return;
        }

        ProgressMonitorDialog progress = new ProgressMonitorDialog(shell);
        try
        {
            progress.run(true, true, new IRunnableWithProgress()
            {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {

                    if (infobases.size() == 1)
                    {
                        InfobaseReference infobase = infobases.get(0);
                        monitor.setTaskName(MessageFormat.format(
                            Messages.DesignerStartHandler_Starting_1c_designer_for_infobase, infobase.getName()));
                        startDesigner(display, infobase);

                    }
                    else
                    {
                        monitor.setTaskName(Messages.DesignerStartHandler_Starting_1c_designer_for_selected_infobases);
                        for (InfobaseReference infobase : infobases)
                        {
                            startDesigner(display, infobase);
                        }
                    }

                    monitor.done();

                }
            });
        }
        catch (InvocationTargetException | InterruptedException e)
        {
            Activator.logError(e);

            // Show error dialog
            display.asyncExec(new Runnable()
            {

                @Override
                public void run()
                {
                    if (!display.isDisposed())
                    {
                        RuntimeExecutionErrorDialog.openError(shell,
                            Messages.DesignerStartHandler_Cannot_start_Designer, e);
                    }
                }
            });
        }

    }

    protected void startDesigner(Display display, InfobaseReference infobase)
    {

        if (infobase.getInfobaseType() == InfobaseType.WEB)
        {
            return;
        }

        if (runtimeInstallationManager.getAll().isEmpty())
        {
            // TODO: add error message
            return;
        }

        IResolvableRuntimeInstallation resolvable = getRuntimeInstallation(infobase);
        RuntimeInstallation installation;
        try
        {
            installation = resolvable.get("com._1c.g5.v8.dt.platform.services.core.componentTypes.AdvancedThickClient"); //$NON-NLS-1$

        }
        catch (Exception e)
        {
            Activator.logError(e);
            return;
        }

        Pair<IRuntimeComponent, IRuntimeComponentExecutor> thickClient =
            runtimeComponentManager.getComponentAndExecutor(installation,
                "com._1c.g5.v8.dt.platform.services.core.componentTypes.AdvancedThickClient"); //$NON-NLS-1$

        if (thickClient == null)
        {
            // add error message
            display.asyncExec(new Runnable()
            {

                @Override
                public void run()
                {
                    Shell shell = display.getActiveShell();
                    MessageDialog.openInformation(shell, Messages.DesignerStartHandler_Designer,
                        Messages.DesignerStartHandler_Cannot_find_installed_thick_client);
                }
            });

            return;
        }

        if (thickClient.second instanceof IAdvancedThickClientLauncher
            && thickClient.first instanceof ILaunchableRuntimeComponent)
        {
            RuntimeExecutionArguments arguments = buildArguments(infobase);

            DesignerStartJob job = new DesignerStartJob((ILaunchableRuntimeComponent)thickClient.first,
                (IAdvancedThickClientLauncher)thickClient.second, infobase, arguments);
            job.setUser(true);
            job.schedule();
        }

    }

    private class DesignerStartJob
        extends Job
    {

        private ILaunchableRuntimeComponent component;
        private IAdvancedThickClientLauncher thickClient;
        private InfobaseReference infobase;
        private RuntimeExecutionArguments arguments;

        public DesignerStartJob(ILaunchableRuntimeComponent component, IAdvancedThickClientLauncher thickClient,
            InfobaseReference infobase, RuntimeExecutionArguments arguments)
        {
            super(MessageFormat.format(Messages.DesignerStartHandler_Starting_1c_designer_for_infobase,
                infobase.getName()));
            this.component = component;
            this.thickClient = thickClient;
            this.infobase = infobase;
            this.arguments = arguments;

        }

        @Override
        protected IStatus run(IProgressMonitor monitor)
        {

            thickClient.startDesigner(component, infobase, arguments);

            monitor.done();
            return Status.OK_STATUS;
        }

    }

}
