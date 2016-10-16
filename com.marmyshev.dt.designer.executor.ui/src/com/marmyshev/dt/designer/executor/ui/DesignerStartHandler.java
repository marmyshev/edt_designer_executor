package com.marmyshev.dt.designer.executor.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseAssociation;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.IRuntimeInstallationManager;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DesignerStartHandler extends AbstractHandler {

	// @Inject
	// private IInfobaseSynchronizationManager infobaseSynchronizationManager;

	@Inject
	@Named("com._1c.g5.v8.dt.platform.services.core.runtimeType.EnterprisePlatform")
	private IRuntimeInstallationManager runtimeInstallationManager;

	@Inject
	private IInfobaseManager infobaseManager;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IProject project = com._1c.g5.v8.dt.platform.services.ui.SelectionContextProject
				.getContextProject(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage());
		if (project == null) {
			return null;
		}

		if (runtimeInstallationManager.getAll().isEmpty()) {
			MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "Designer",
					"Not found installed 1C:Enterprise platform");
		}

		if (!PlatformUI.getWorkbench().saveAllEditors(false)) {
			return null;
		}

		IInfobaseAssociation association = infobaseManager.getAssociation(project);
		if (association != null) {
			return true;
		}

		return null;
	}

	@Override
	public boolean isEnabled() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			return false;
		}

		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return false;
		}
		IProject project = com._1c.g5.v8.dt.platform.services.ui.SelectionContextProject.getContextProject(page);
		if (project == null) {
			return false;
		}

		IInfobaseAssociation association = infobaseManager.getAssociation(project);
		if (association != null) {
			return true;
		}
		return false;

	}

}
