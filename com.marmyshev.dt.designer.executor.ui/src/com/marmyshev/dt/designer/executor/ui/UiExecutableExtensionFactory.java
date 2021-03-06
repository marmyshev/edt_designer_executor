/**
 * Copyright (C) 2016, Dmitriy Marmyshev
 */
package com.marmyshev.dt.designer.executor.ui;

import org.osgi.framework.Bundle;

import com._1c.g5.wiring.AbstractGuiceAwareExecutableExtensionFactory;
import com.google.inject.Injector;

/**
 * Guice module aware executable extension factory for UI plugin.
 *
 */
public class UiExecutableExtensionFactory
    extends AbstractGuiceAwareExecutableExtensionFactory
{
    @Override
    protected Bundle getBundle()
    {
        return Activator.getDefault().getBundle();
    }

    @Override
    protected Injector getInjector()
    {
        return Activator.getDefault().getInjector();
    }
}
