package com.marmyshev.dt.designer.executor.ui;

import org.eclipse.osgi.util.NLS;

public class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "com.marmyshev.dt.designer.executor.ui.messages"; //$NON-NLS-1$
    public static String DesignerLaunchShortcut_Name_suffix;
    public static String DesignerLaunchShortcut_Title;
    public static String DesignerStartHandler_Cannot_find_installed_thick_client;
    public static String Actions_Cannot_start_Designer;
    public static String Actions_Designer;
    public static String Actions_Designer_cannot_be_started_for_web_infobases;
    public static String Actions_Not_found_installed_1c_enterprise_platform;
    public static String DesignerStartHandler_Selected_project_has_several_infobases_Start_1C_Designer_for_all;
    public static String DesignerStartHandler_Starting_1c_designer_for_infobase;
    public static String DesignerStartHandler_Starting_1c_designer_for_selected_infobases;
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
