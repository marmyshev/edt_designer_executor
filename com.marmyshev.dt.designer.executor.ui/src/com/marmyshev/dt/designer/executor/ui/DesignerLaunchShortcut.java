package com.marmyshev.dt.designer.executor.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com._1c.g5.v8.dt.debug.ui.launchconfigurations.shortcuts.AbstractLaunchShortcut;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;

public class DesignerLaunchShortcut
    extends AbstractLaunchShortcut
{

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
        // TODO: Make matching method
        return true;
    }

    @Override
    protected boolean validateConfiguration(ILaunchConfiguration configuration, String mode) throws CoreException
    {
//        Object correctProject = configuration.getAttribute("com._1c.g5.v8.dt.debug.core.ATTR_PROJECT_NAME", null); //$NON-NLS-1$
        return true;

    }

    @Override
    protected boolean shouldSave(ILaunchConfiguration configuration, String mode) throws CoreException
    {
        // TODO: Make check shouldSave method
        return true;
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
