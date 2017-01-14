/**
 * Copyright (C) 2016, Dmitriy Marmyshev
 */
package com.marmyshev.dt.designer.executor;

import java.util.ArrayList;
import java.util.Collection;

import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.ILaunchableRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.impl.AbstractFileRuntimeComponentResolver;
import com._1c.g5.v8.dt.platform.services.model.RuntimeInstallation;

public class AdvancedFileRuntimeComponentResolver extends AbstractFileRuntimeComponentResolver {

	public AdvancedFileRuntimeComponentResolver() {
		super();
	}

	@Override
	public Collection<IRuntimeComponent> resolveComponents(RuntimeInstallation installation) {

		Collection<IRuntimeComponent> result = new ArrayList<IRuntimeComponent>();

		if (installation.getInstallLocation() == null) {
			return result;
		}

		ILaunchableRuntimeComponent thickClient = resolveLaunchable(installation, "1cv8", //$NON-NLS-1$
				"com._1c.g5.v8.dt.platform.services.core.componentTypes.AdvancedThickClient"); //$NON-NLS-1$
		if (thickClient != null) {
			result.add(thickClient);
		}
		thickClient = resolveLaunchable(installation, "1cv8.exe", //$NON-NLS-1$
				"com._1c.g5.v8.dt.platform.services.core.componentTypes.AdvancedThickClient"); //$NON-NLS-1$
		if (thickClient != null) {
			result.add(thickClient);
		}
		return result;
	}

}
