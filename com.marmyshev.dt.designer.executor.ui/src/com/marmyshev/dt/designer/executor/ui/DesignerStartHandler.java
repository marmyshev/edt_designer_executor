package com.marmyshev.dt.designer.executor.ui;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseAssociation;
import com._1c.g5.v8.dt.platform.services.core.infobases.IInfobaseManager;
import com._1c.g5.v8.dt.platform.services.core.runtimes.IRuntimeInstallationManager;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;
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

	public DesignerStartHandler() {
		super();
		super.setBaseEnabled(false);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (runtimeInstallationManager.getAll().isEmpty()) {
			MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), Messages.DesignerStartHandler_designer,
					Messages.DesignerStartHandler_not_found_installed_1c_enterprise_platform);
			return null;
		}

		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		ISelection selection = page.getSelection();
		boolean infobaseSelected = false;

		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (Iterator<Object> iterator = strucSelection.iterator(); iterator.hasNext();) {
				Object element = iterator.next();
				if (element instanceof InfobaseReference) {
					InfobaseReference infobaseReference = (InfobaseReference) element;

					// Run Designer
					DesignerInfobaseLauncher launcher = new DesignerInfobaseLauncher(infobaseReference);
					launcher.run();

					infobaseSelected = true;
				}
			}
		}

		if (infobaseSelected) {
			return null;
		}

		IProject project = com._1c.g5.v8.dt.platform.services.ui.SelectionContextProject.getContextProject(page);
		if (project == null) {
			return null;
		}



		if (!PlatformUI.getWorkbench().saveAllEditors(false)) {
			return null;
		}

		IInfobaseAssociation association = infobaseManager.getAssociation(project);
		if (association != null) {

			// Run Designer with default infobase
			DesignerInfobaseLauncher launcher = new DesignerInfobaseLauncher(association.getDefaultInfobase());
			launcher.run();
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
		if (project != null) {
			IInfobaseAssociation association = infobaseManager.getAssociation(project);
			if (association != null) {
				return true;
			}
		}

		ISelection selection = page.getSelection();

		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (Iterator<Object> iterator = strucSelection.iterator(); iterator.hasNext();) {
				Object element = iterator.next();
				if (element instanceof InfobaseReference) {
					return true;
				}
			}
		}

		return false;

	}

}
