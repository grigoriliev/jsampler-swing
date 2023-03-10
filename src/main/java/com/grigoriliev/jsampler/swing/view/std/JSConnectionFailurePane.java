/*
 *   JSampler - a front-end for LinuxSampler
 *
 *   Copyright (C) 2005-2023 Grigor Iliev <grigor@grigoriliev.com>
 *
 *   This file is part of JSampler.
 *
 *   JSampler is free software: you can redistribute it and/or modify it under
 *   the terms of the GNU General Public License as published by the Free
 *   Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 *   JSampler is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 *   more details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with JSampler. If not, see <https://www.gnu.org/licenses/>. 
 */

package com.grigoriliev.jsampler.swing.view.std;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.CC;

/**
 *
 * @author grishata
 */
public class JSConnectionFailurePane extends JOptionPane {
	public static Object[] buttons = {
		StdI18n.i18n.getButtonLabel("JSConnectionFailurePane.reconnect"),
		StdI18n.i18n.getButtonLabel("JSConnectionFailurePane.quit")
	};

	public
	JSConnectionFailurePane() {
		super (
			"Connection to backend failed",
			ERROR_MESSAGE,
			DEFAULT_OPTION,
			null,
			buttons,
			buttons[1]
		);
	}

	public void
	showDialog() {
		JDialog dlg = createDialog(SHF.getMainFrame(), StdI18n.i18n.getError("error"));
		dlg.setModal(true);
		dlg.setVisible(true);
		Object val = getValue();
		if(val == null) {

		} else if(buttons[0].equals(val)) {
			CC.reconnect();
		} else if(buttons[1].equals(val)) {
			CC.getMainFrame().onWindowClose();
		}
	}
}
