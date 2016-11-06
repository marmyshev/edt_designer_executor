/**
 * Copyright (C) 2016, 1C
 */
package com.marmyshev.dt.internal.designer.executor.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.marmyshev.dt.designer.executor.ui.actions.InfobaseDesignerAction;

/**
 * @author marmyshev_d
 *
 */
public class InfobasesViewActionProvider
    extends CommonActionProvider
{
    @Inject
    private Provider<InfobaseDesignerAction> infobaseDesignerActionProvider;

    private List<IAction> actions;

    public InfobasesViewActionProvider()
    {
        super();
    }

    /**
     * <p>
     * Initialize the current ICommonActionProvider with the supplied
     * information.
     * </p>
     * <p>
     * init() is guaranteed to be called before any other method of the
     * ActionGroup super class.
     *
     * @param aSite
     *            The configuration information for the instantiated Common
     *            Action Provider.
     */
    @Override
    public void init(ICommonActionExtensionSite aSite)
    {
        super.init(aSite);

        actions = new ArrayList<IAction>();

        StructuredViewer viewer = aSite.getStructuredViewer();
        if (viewer instanceof CommonViewer)
        {
            createActions((CommonViewer)viewer, aSite.getViewSite().getSelectionProvider());
        }
    }

    /**
     * Adds the applicable actions to a context menu,
     * based on the state of the <code>ActionContext</code>.
     *
     * @param menu the context menu manager
     */
    @Override
    public void fillContextMenu(IMenuManager menu)
    {
        for (Iterator<IAction> iterator = actions.iterator(); iterator.hasNext();)
        {
            IAction action = iterator.next();
            menu.appendToGroup("group.edit", action); //$NON-NLS-1$

        }
    }

    protected void createActions(CommonViewer viewer, ISelectionProvider provider)
    {
        InfobaseDesignerAction startDesigner = infobaseDesignerActionProvider.get();

        actions.add(startDesigner);

    }

}
