/**
 * Copyright (C) 2016, Dmitriy Marmyshev
 */
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
                candidate.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_RUNTIME_INSTALLATION", (String)null); //$NON-NLS-1$

            IResolvableRuntimeInstallation resolvable = resolvableAsString == null ? null
                : this.resolvableRuntimeInstallationManager.deserialize(resolvableAsString);

            RuntimeInstallation installation =
                resolvable == null ? null : resolvable.get(new String[] { this.getRuntimeComponentTypeId() });

            boolean runtimeSupportsClientType = installation == null
                || this.runtimeComponentManager.supportsExecution(installation, this.getRuntimeComponentTypeId());

            if (!forLaunch && resolvable != null)
            {
                runtimeSupportsClientType &=
                    this.getRuntimeComponentTypeId().equals(this.getExecutionClientTypeId(candidate, resolvable));
            }

            return super.matches(project, candidate, forLaunch) && runtimeSupportsClientType;

        }
        catch (Exception e)
        {
            Activator.logError(e);
            return false;
        }
    }

    protected String getExecutionClientTypeId(ILaunchConfiguration configuration,
        IResolvableRuntimeInstallation resolvable) throws CoreException
    {
        String infobaseUuid =
            configuration.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_INFOBASE_UUID", (String)null); //$NON-NLS-1$

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
        boolean correctProject =
            configuration.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_PROJECT_NAME", (String)null) != null; //$NON-NLS-1$
        String infobaseUuid =
            configuration.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_INFOBASE_UUID", (String)null); //$NON-NLS-1$
        String resolvableAsString =
            configuration.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_RUNTIME_INSTALLATION", (String)null); //$NON-NLS-1$

        boolean correctInfobase = false;
        boolean correctRuntime = false;

        if (!Strings.isNullOrEmpty(infobaseUuid))
        {
            correctInfobase = infobaseManager.get(UUID.fromString(infobaseUuid)) != null;
        }

        if (resolvableAsString != null)
        {
            correctRuntime = resolvableRuntimeInstallationManager.deserialize(resolvableAsString) != null;
        }

        return correctProject && correctInfobase && correctRuntime;
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
