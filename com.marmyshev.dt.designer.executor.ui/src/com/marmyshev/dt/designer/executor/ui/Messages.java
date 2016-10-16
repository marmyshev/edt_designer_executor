package com.marmyshev.dt.designer.executor.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.marmyshev.dt.designer.executor.ui.messages"; //$NON-NLS-1$
	public static String DesignerInfobaseLauncher_starting_1c_designer_for_infobase;
	public static String DesignerStartHandler_designer;
	public static String DesignerStartHandler_not_found_installed_1c_enterprise_platform;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
