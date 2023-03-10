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
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JScrollPane;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.grigoriliev.jsampler.juife.swing.OkCancelDialog;

import com.grigoriliev.jsampler.swing.view.SHF;

/**
 *
 * @author Grigor Iliev
 */
public class JSDbDirectoryChooser extends OkCancelDialog implements TreeSelectionListener {
	private JSInstrumentsDbTree instrumentsDbTree;
	
	/**
	 * Creates a new instance of <code>JSDbDirectoryChooser</code>.
	 */
	public
	JSDbDirectoryChooser(Frame owner) {
		super(owner, StdI18n.i18n.getLabel("JSDbDirectoryChooser.title"));
		initDbDirectoryChooser();
	}
	
	/**
	 * Creates a new instance of <code>JSDbDirectoryChooser</code>.
	 */
	public
	JSDbDirectoryChooser(Dialog owner) {
		super(owner, StdI18n.i18n.getLabel("JSDbDirectoryChooser.title"));
		initDbDirectoryChooser();
	}
	
	private void
	initDbDirectoryChooser() {
		btnOk.setEnabled(false);
		instrumentsDbTree = new JSInstrumentsDbTree(SHF.getInstrumentsDbTreeModel());
		instrumentsDbTree.addTreeSelectionListener(this);
		
		JScrollPane sp = new JScrollPane(instrumentsDbTree);
		int w = sp.getPreferredSize().width;
		int h = sp.getPreferredSize().height;
		Dimension d = new Dimension(w > 300 ? w : 300, h > 200 ? h : 200);
		sp.setPreferredSize(d);
		setMainPane(sp);
		setResizable(true);
	}
	
	public String
	getSelectedDirectory() {
		return instrumentsDbTree.getSelectedDirectoryPath();
	}
	
	public void
	setSelectedDirectory(String dir) {
		instrumentsDbTree.setSelectedDirectory(dir);
	}
	
	protected void
	onOk() {
		if(!btnOk.isEnabled()) return;
		setCancelled(false);
		setVisible(false);
	}
	
	protected void
	onCancel() { setVisible(false); }
	
	public void
	valueChanged(TreeSelectionEvent e) {
		btnOk.setEnabled(instrumentsDbTree.getSelectedDirectoryNode() != null);
	}
}
