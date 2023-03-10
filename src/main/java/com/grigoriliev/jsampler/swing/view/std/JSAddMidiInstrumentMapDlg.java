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

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.grigoriliev.jsampler.juife.swing.OkCancelDialog;

import com.grigoriliev.jsampler.swing.view.SHF;


/**
 *
 * @author Grigor Iliev
 */
public class JSAddMidiInstrumentMapDlg extends OkCancelDialog {
	private final JLabel lName = new JLabel(StdI18n.i18n.getLabel("JSAddMidiInstrumentMapDlg.lName"));
	private final JTextField tfName = new JTextField();
	
	/**
	 * Creates a new instance of <code>JSAddMidiInstrumentMapDlg</code>
	 */
	public
	JSAddMidiInstrumentMapDlg() {
		super(SHF.getMainFrame(), StdI18n.i18n.getLabel("JSAddMidiInstrumentMapDlg.title"));
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(lName);
		p.add(Box.createRigidArea(new Dimension(6, 0)));
		p.add(tfName);
		p.setPreferredSize(new Dimension(250, p.getPreferredSize().height));
		setMainPane(p);
		updateState();
		tfName.getDocument().addDocumentListener(getHandler());
	}
	
	/**
	 * Gets the chosen name for the new MIDI instrument map.
	 * @return The chosen name for the new MIDI instrument map.
	 */
	public String
	getMapName() { return tfName.getText(); }
	
	/**
	 * Sets the text name text field.
	 */
	public void
	setMapName(String name) { tfName.setText(name); }
	
	protected void
	onOk() {
		if(!btnOk.isEnabled()) return;
		
		setVisible(false);
		setCancelled(false);
	}
	
	protected void
	onCancel() { setVisible(false); }
	
	private void
	updateState() { btnOk.setEnabled(tfName.getText().length() != 0); }
	
	private final Handler eventHandler = new Handler();
	
	private Handler
	getHandler() { return eventHandler; }
	
	private class Handler implements DocumentListener {
		// DocumentListener
		public void
		insertUpdate(DocumentEvent e) { updateState(); }
		
		public void
		removeUpdate(DocumentEvent e) { updateState(); }
		
		public void
		changedUpdate(DocumentEvent e) { updateState(); }
	}
}
