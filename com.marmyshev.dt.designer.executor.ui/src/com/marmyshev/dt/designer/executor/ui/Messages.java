package com.marmyshev.dt.designer.executor.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.marmyshev.dt.designer.executor.ui.messages"; //$NON-NLS-1$
	public static String DesignerInfobaseLauncher_Starting_1c_designer_for_infobase;
	public static String DesignerStartHandler_Cannot_find_installed_thick_client;
	public static String DesignerStartHandler_Cannot_start_Designer;
	public static String DesignerStartHandler_Designer;
	public static String DesignerStartHandler_Designer_cannot_be_started_for_web_infobases;
	public static String DesignerStartHandler_Not_found_installed_1c_enterprise_platform;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
