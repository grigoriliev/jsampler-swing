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

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.grigoriliev.jsampler.juife.swing.OkCancelDialog;


/**
 *
 * @author Grigor Iliev
 */
public class JSDbDescriptionDlg extends OkCancelDialog {
	private final JTextArea taDesc = new JTextArea();
	
	/**
	 * Creates a new instance of <code>JSDbDescriptionDlg</code>
	 */
	public
	JSDbDescriptionDlg(Frame owner) {
		super(owner, StdI18n.i18n.getLabel("JSDbDescriptionDlg.title"));
		initDbDescriptionDlg();
	}
	
	/**
	 * Creates a new instance of <code>JSDbDescriptionDlg</code>
	 */
	public
	JSDbDescriptionDlg(Dialog owner) {
		super(owner, StdI18n.i18n.getLabel("JSDbDescriptionDlg.title"));
		initDbDescriptionDlg();
	}
	
	private void
	initDbDescriptionDlg() {
		taDesc.setLineWrap(true);
		taDesc.setWrapStyleWord(true);
		JScrollPane sp = new JScrollPane(taDesc);
		sp.setPreferredSize(new java.awt.Dimension(300, 60));
		setMainPane(sp);
		setMinimumSize(getPreferredSize());
		setResizable(true);
	}
	
	protected void
	onOk() {
		if(!btnOk.isEnabled()) return;
		setCancelled(false);
		setVisible(false);
	}
	
	protected void
	onCancel() { setVisible(false); }
	
	public String
	getDescription() { return taDesc.getText(); }
	
	public void
	setDescription(String s) { taDesc.setText(s); }
}
