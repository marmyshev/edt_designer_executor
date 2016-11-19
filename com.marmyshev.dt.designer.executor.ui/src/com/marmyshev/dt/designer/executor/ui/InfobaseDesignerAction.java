/**
 * Copyright (C) 2016, 1C
 */
package com.marmyshev.dt.designer.executor.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.FrameworkUtil;

import com._1c.g5.v8.dt.common.Pair;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseAssociation;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.IRuntimeInstallationManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.environments.IResolvableRuntimeInstallation;
import com._1c.g5.v8.dt.platform.services.core.runtimes.environments.IResolvableRuntimeInstallationManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.environments.MatchingRuntimeNotFound;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.ILaunchableRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponentExecutor;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponentManager;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;
import com._1c.g5.v8.dt.platform.services.model.InfobaseType;
import com._1c.g5.v8.dt.platform.services.model.RuntimeInstallation;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.marmyshev.dt.designer.executor.IAdvancedThickClientLauncher;

/**
 * @author marmyshev_d
 *
 */
public class InfobaseDesignerAction
    extends Action
{
    private static ImageDescriptor image =
        ImageDescriptor.createFromURL(FileLocator.find(FrameworkUtil.getBundle(InfobaseDesignerAction.class),
            new Path("icons/1c_designer_16x16.png"), null)); //$NON-NLS-1$

    @Inject
    @Named("com._1c.g5.v8.dt.platform.services.core.runtimeType.EnterprisePlatform")
    private IRuntimeInstallationManager runtimeInstallationManager;

    @Inject
    private IResolvableRuntimeInstallationManager resolvableRuntimeInstallationManager;

    @Inject
    private IRuntimeComponentManager runtimeComponentManager;

    @Inject
    private IInfobaseManager infobaseManager;

    public InfobaseDesignerAction()
    {
        super(Messages.InfobaseDesignerAction_Start_1C_Designer, image);
    }

    @Override
    public void runWithEvent(Event event)
    {
        Display display = event.display;
        if (display == null)
        {
            display = PlatformUI.getWorkbench().getDisplay();
        }

        Shell shell = (display == null) ? null : display.getActiveShell();

        if (runtimeInstallationManager.getAll().isEmpty())
        {
            display.asyncExec(new Runnable()
            {
                @Override
                public void run()
                {
                    MessageDialog.openInformation(shell, Messages.Actions_Designer,
                        Messages.Actions_Not_found_installed_1c_enterprise_platform);
                }
            });

            return;
        }

        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

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
        if (infobases.size() == 0)
        {
            IProject project = com._1c.g5.v8.dt.platform.services.ui.SelectionContextProject.getContextProject(page);
            if (project != null)
            {
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
                }
            }
        }

        List<InfobaseReference> webInfobases = new ArrayList<>();

        for (int i = infobases.size() - 1; i >= 0; i--)
        {
            InfobaseReference infobase = infobases.get(i);
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
                    MessageDialog.openInformation(shell, Messages.Actions_Designer,
                        Messages.Actions_Designer_cannot_be_started_for_web_infobases);
                }
            });
        }

        if (infobases.size() > 0)
        {
            // Start Designer for all selected infobases
            runStartDesigner(display, infobases);
            return;
        }

    }

    protected void runStartDesigner(Display display, List<InfobaseReference> infobases)
    {
        for (InfobaseReference infobase : infobases)
        {
            IResolvableRuntimeInstallation resolvable = getRuntimeInstallation(infobase);
            RuntimeInstallation installation;
            try
            {
                installation =
                    resolvable.get("com._1c.g5.v8.dt.platform.services.core.componentTypes.AdvancedThickClient"); //$NON-NLS-1$
            }
            catch (Exception e)
            {
                if (e instanceof MatchingRuntimeNotFound)
                {
                    display.asyncExec(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Shell shell = display.getActiveShell();
                            MessageDialog.openInformation(shell, Messages.Actions_Designer, e.getLocalizedMessage());
                        }
                    });
                }
                else
                {
                    Activator.logError(e);
                }
                continue;
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
                        MessageDialog.openInformation(shell, Messages.Actions_Designer,
                            Messages.DesignerStartHandler_Cannot_find_installed_thick_client);
                    }
                });

                continue;
            }

            if (thickClient.second instanceof IAdvancedThickClientLauncher
                && thickClient.first instanceof ILaunchableRuntimeComponent)
            {

                StartDesignerJob job = new StartDesignerJob((ILaunchableRuntimeComponent)thickClient.first,
                    (IAdvancedThickClientLauncher)thickClient.second, infobase, display);
                Activator.getDefault().getInjector().injectMembers(job);
                job.setUser(true);
                job.schedule();
            }
        }

    }

    protected IResolvableRuntimeInstallation getRuntimeInstallation(InfobaseReference infobase)
    {
        return resolvableRuntimeInstallationManager.getDefault(
            "com._1c.g5.v8.dt.platform.services.core.runtimeType.EnterprisePlatform", infobase.getVersion()); //$NON-NLS-1$
    }


}
