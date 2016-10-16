package com.marmyshev.dt.designer.executor.ui;

import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;

public class DesignerInfobaseLauncher {

	private InfobaseReference infobaseReference;

	public DesignerInfobaseLauncher(InfobaseReference infobaseReference) {

		this.infobaseReference = infobaseReference;
	}

	public void run() {
		// TODO Auto-generated method stub
		System.out.println(
				Messages.DesignerInfobaseLauncher_starting_1c_designer_for_infobase + infobaseReference.getName());

	}

}
