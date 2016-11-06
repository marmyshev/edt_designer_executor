/**
 * Copyright (C) 2016, 1C
 */
package com.marmyshev.dt.designer.executor.ui.actions;

import org.eclipse.osgi.util.NLS;

/**
 * @author marmyshev_d
 *
 */
public class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "com.marmyshev.dt.designer.executor.ui.actions.messages"; //$NON-NLS-1$
    public static String InfobaseDesignerAction_Start_1C_Designer;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
