package com.marmyshev.dt.designer.executor.ui;

import org.eclipse.core.runtime.Plugin;

import com._1c.g5.v8.dt.core.platform.IConfigurationManager;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseAccessManager;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.IProcessEncodingProvider;
import com._1c.g5.v8.dt.platform.services.core.runtimes.IRuntimeInstallationManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.environments.IResolvableRuntimeInstallationManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponentManager;
import com._1c.g5.wiring.AbstractServiceAwareModule;
import com._1c.g5.wiring.ServiceProperties;
import com.google.inject.name.Names;

/**
 * TODO: javadoc
 */
public class UiModule
    extends AbstractServiceAwareModule
{
    public UiModule(Plugin bundle)
    {
        super(bundle);
    }

    @Override
    protected void doConfigure()
    {
        bind(IInfobaseManager.class).toService();
        bind(IRuntimeInstallationManager.class).annotatedWith(
            Names.named("com._1c.g5.v8.dt.platform.services.core.runtimeType.EnterprisePlatform")).toService( //$NON-NLS-1$
                ServiceProperties.named("com._1c.g5.v8.dt.platform.services.core.runtimeType.EnterprisePlatform")); //$NON-NLS-1$
        bind(IRuntimeComponentManager.class).toService();
        bind(IResolvableRuntimeInstallationManager.class).toService();
        bind(IInfobaseAccessManager.class).toService();
        bind(IProcessEncodingProvider.class).toService();
        bind(IConfigurationManager.class).toService();
    }
}
