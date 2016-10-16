package com.marmyshev.dt.designer.executor.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * TODO: javadoc
 */
public class Activator
    extends AbstractUIPlugin
{
    // The plug-in ID
    public static final String PLUGIN_ID = "com.marmyshev.dt.designer.executor.ui"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    private Injector injector;

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault()
    {
        return plugin;
    }

    /**
     * Writes a status to the plugin log.
     *
     * @param status status to log, cannot be <code>null</code>
     */
    public static void log(IStatus status)
    {
        plugin.getLog().log(status);
    }

    /**
     * Writes a throwable to the plugin log as error status.
     *
     * @param throwable throwable, cannot be <code>null</code>
     */
    public static void logError(Throwable throwable)
    {
        log(createErrorStatus(throwable.getMessage(), throwable));
    }

    /**
     * Creates error status by a given message and cause throwable.
     *
     * @param message status message, cannot be <code>null</code>
     * @param throwable throwable, can be <code>null</code> if not applicable
     * @return status created error status, never <code>null</code>
     */
    public static IStatus createErrorStatus(String message, Throwable throwable)
    {
        return new Status(IStatus.ERROR, PLUGIN_ID, 0, message, throwable);
    }

    /**
     * Creates warning status by a given message.
     *
     * @param message status message, cannot be <code>null</code>
     * @return status created warning status, never <code>null</code>
     */
    public static IStatus createWarningStatus(String message)
    {
        return new Status(IStatus.WARNING, PLUGIN_ID, 0, message, null);
    }

    /**
     * Creates warning status by a given message and cause throwable.
     *
     * @param message status message, cannot be <code>null</code>
     * @param throwable throwable, can be <code>null</code> if not applicable
     * @return status created warning status, never <code>null</code>
     */
    public static IStatus createWarningStatus(final String message,
        Exception throwable)
    {
        return new Status(IStatus.WARNING, PLUGIN_ID, 0, message, throwable);
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception
    {
        super.start(bundleContext);

        plugin = this;
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception
    {
        plugin = null;
        super.stop(bundleContext);
    }

    /**
     * Get active workbench page. Can return <code>null</code>.
     *
     * @return active workbench page, or <code>null</code>
     *         if there isn't one.
     */
    public static IWorkbenchPage getActivePage()
    {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        return window == null ? null : window.getActivePage();
    }

    /**
     * Get active workbench part. Can return <code>null</code>.
     *
     * @return active workbench part, or <code>null</code>
     *         if there isn't one.
     */
    public static IWorkbenchPart getActivePart()
    {
        IWorkbenchPage page = getActivePage();
        return page == null ? null : page.getActivePart();
    }

    /**
     * Returns the currently active workbench window shell or <code>null</code>
     * if none.
     *
     * @return the currently active workbench window shell or <code>null</code>
     */
    public static Shell getShell()
    {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null)
        {
            IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
            if (windows.length > 0)
            {
                return windows[0].getShell();
            }
        }
        else
        {
            return window.getShell();
        }
        return null;
    }

    /**
     * Get plugin Guice-injector. Method is synchronized.
     *
     * @return plugin Guice-injector, never <code>null</code>
     */
    public synchronized Injector getInjector()
    {
        if (injector == null)
            return injector = createInjector();
        return injector;
    }
    

    private Injector createInjector()
    {
        try
        {
            return Guice.createInjector(new UiModule(this));
        }
        catch (Exception e)
        {
            log(createErrorStatus("Failed to create injector for " //$NON-NLS-1$
                + getBundle().getSymbolicName(), e));
            throw new RuntimeException("Failed to create injector for " //$NON-NLS-1$
                + getBundle().getSymbolicName(), e);
        }
    }
}