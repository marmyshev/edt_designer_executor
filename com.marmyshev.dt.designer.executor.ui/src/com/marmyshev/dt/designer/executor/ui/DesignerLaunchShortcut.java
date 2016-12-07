package com.marmyshev.dt.designer.executor.ui;

import java.util.UUID;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com._1c.g5.v8.dt.debug.ui.launchconfigurations.shortcuts.AbstractLaunchShortcut;
import com._1c.g5.v8.dt.launching.core.launchconfigurations.ClientTypeSelectionSupport;
import com._1c.g5.v8.dt.platform.services.core.runtimes.environments.IResolvableRuntimeInstallation;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponentManager;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;
import com._1c.g5.v8.dt.platform.services.model.RuntimeInstallation;
import com.google.common.base.Strings;
import com.google.inject.Inject;


public class DesignerLaunchShortcut
    extends AbstractLaunchShortcut
{

    @Inject
    protected IRuntimeComponentManager runtimeComponentManager;

    public DesignerLaunchShortcut()
    {
        super();
    }

    @Override
    protected ILaunchConfigurationWorkingCopy createLaunchConfiguration(IProject project, String mode)
        throws CoreException
    {
        ILaunchConfigurationWorkingCopy workingCopy = createLaunchConfigurationTemplate(getLaunchConfigurationType(),
            project, Messages.DesignerLaunchShortcut_Name_suffix);

        InfobaseReference associated = fillAssociatedInfobaseAtttibutes(workingCopy, project);

        if (associated == null)
        {
            setDefaults(workingCopy);
        }

        return workingCopy;
    }

    @Override
    protected ILaunchConfigurationWorkingCopy prepareConfiguration(ILaunchConfiguration launchConfiguration)
        throws CoreException
    {
        ILaunchConfigurationWorkingCopy result = super.prepareConfiguration(launchConfiguration);
        result.setAttribute("com._1c.g5.v8.dt.launching.core.ATTR_CLIENT_AUTO_SELECT", false); //$NON-NLS-1$
        result.setAttribute("com._1c.g5.v8.dt.launching.core.ATTR_CLIENT_TYPE", getRuntimeComponentTypeId()); //$NON-NLS-1$

        result.setAttribute("com._1c.g5.v8.dt.launching.core.ATTR_NEED_INFOBASE_PUBLICATION", false); //$NON-NLS-1$

        return result;

    }

    @Override
    protected boolean matches(IProject project, ILaunchConfiguration candidate, boolean forLaunch)
    {
        try
        {
            String resolvableAsString =
                candidate.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_RUNTIME_INSTALLATION", ""); //$NON-NLS-1$ //$NON-NLS-2$

            if (resolvableAsString != null)
            {
                boolean runtimeSupportsClientType = false;

                IResolvableRuntimeInstallation resolvable =
                    resolvableRuntimeInstallationManager.deserialize(resolvableAsString);

                if (!forLaunch && resolvable != null)
                {
                    RuntimeInstallation installation = resolvable.get(getRuntimeComponentTypeId());
                    if (installation != null)
                    {
                        if (runtimeComponentManager.supportsExecution(installation, getRuntimeComponentTypeId())
                            && getRuntimeComponentTypeId().equals(getExecutionClientTypeId(candidate, resolvable)))
                        {
                            runtimeSupportsClientType = true;
                        }
                    }
                }
                else
                {
                    runtimeSupportsClientType = matches(project, candidate, !forLaunch);
                }

                if (runtimeSupportsClientType)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch (CoreException e)
        {
            Activator.logError(e);
        }

        return false;
    }

    protected String getExecutionClientTypeId(ILaunchConfiguration configuration,
        IResolvableRuntimeInstallation resolvable) throws CoreException
    {
        String infobaseUuid = configuration.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_INFOBASE_UUID", ""); //$NON-NLS-1$ //$NON-NLS-2$

        InfobaseReference infobase = null;

        if (!Strings.isNullOrEmpty(infobaseUuid))
        {
            infobase = infobaseManager.get(UUID.fromString(infobaseUuid));
        }

        return ClientTypeSelectionSupport.getExecutionClientTypeId(
            configuration, resolvable, infobase, runtimeComponentManager);
    }

    @Override
    protected boolean validateConfiguration(ILaunchConfiguration configuration, String mode) throws CoreException
    {
        String correctProject = configuration.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_PROJECT_NAME", ""); //$NON-NLS-1$ //$NON-NLS-2$
        String infobaseUuid = configuration.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_INFOBASE_UUID", ""); //$NON-NLS-1$ //$NON-NLS-2$
        String resolvableAsString =
            configuration.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_RUNTIME_INSTALLATION", ""); //$NON-NLS-1$ //$NON-NLS-2$

        InfobaseReference correctInfobase = null;
        IResolvableRuntimeInstallation correctRuntime = null;

        if (!Strings.isNullOrEmpty(infobaseUuid))
        {
            correctInfobase = infobaseManager.get(UUID.fromString(infobaseUuid));
        }

        if (resolvableAsString != null)
        {
            correctRuntime = resolvableRuntimeInstallationManager.deserialize(resolvableAsString);
        }

        if (correctProject != null && correctInfobase != null && correctRuntime != null)
        {
            return true;
        }
        return false;

    }

    @Override
    protected boolean shouldSave(ILaunchConfiguration configuration, String mode) throws CoreException
    {
        if (configuration.isWorkingCopy())
        {
            if (((ILaunchConfigurationWorkingCopy)configuration).getParent() == null)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getLaunchConfigurationSelectionTitle()
    {
        return Messages.DesignerLaunchShortcut_Title;
    }

    @Override
    protected String getLaunchConfigurationTypeId()
    {
        return "com._1c.g5.v8.dt.launching.core.RuntimeClient"; //$NON-NLS-1$
    }

    protected void setDefaults(ILaunchConfigurationWorkingCopy configuration)
    {
        configuration.setAttribute("com._1c.g5.v8.dt.launching.core.ATTR_CLIENT_TYPE", getRuntimeComponentTypeId()); //$NON-NLS-1$
        configuration.setAttribute("com._1c.g5.v8.dt.launching.core.ATTR_DEPLOY_BEFORE_LAUNCH", true); //$NON-NLS-1$

        configuration.setAttribute("com._1c.g5.v8.dt.launching.core.ATTR_DEPLOY_FULL_CONFIGURATION", false); //$NON-NLS-1$

        configuration.setAttribute("com._1c.g5.v8.dt.launching.core.ATTR_LAUNCH_OS_INFOBASE_ACCESS", true); //$NON-NLS-1$
    }

    protected String getRuntimeComponentTypeId()
    {
        return "com._1c.g5.v8.dt.platform.services.core.componentTypes.AdvancedThickClient"; //$NON-NLS-1$

    }

}
