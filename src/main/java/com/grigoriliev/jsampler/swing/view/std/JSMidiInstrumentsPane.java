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

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.MidiInstrument;
import com.grigoriliev.jsampler.MidiInstrumentMap;

import com.grigoriliev.jsampler.jlscp.MidiInstrumentInfo;

/**
 *
 * @author Grigor Iliev
 */
public class JSMidiInstrumentsPane extends JPanel implements TreeSelectionListener {
	protected final JSMidiInstrumentTree midiInstrumentTree = new JSMidiInstrumentTree();
	
	protected final Action actionAddInstrument = new AddInstrumentAction();
	protected final Action actionEditInstrument = new EditInstrumentAction();
	protected final Action actionRemove = new RemoveAction();
	
	/** Creates a new instance of <code>JSMidiInstrumentsPane</code> */
	public
	JSMidiInstrumentsPane() {
		this(null);
	}
	
	/** Creates a new instance of <code>JSMidiInstrumentsPane</code> */
	public
	JSMidiInstrumentsPane(MidiInstrumentMap map) {
		setMidiInstrumentMap(map);
		
		setLayout(new BorderLayout());
		JScrollPane sp = new JScrollPane(midiInstrumentTree);
		add(sp);
		
		midiInstrumentTree.addTreeSelectionListener(this);
	}
	
	public void
	setMidiInstrumentMap(MidiInstrumentMap map) {
		midiInstrumentTree.setMidiInstrumentMap(map);
		actionAddInstrument.setEnabled(map != null);
	}
	
	public void
	valueChanged(TreeSelectionEvent e) {
		actionRemove.setEnabled(midiInstrumentTree.getSelectionCount() > 0);
		boolean b = midiInstrumentTree.getSelectedInstrument() != null;
		actionEditInstrument.setEnabled(b);
	}
	
	public void
	addInstrument() { }
	
	public void
	editInstrument(MidiInstrument instr) {
		JSEditMidiInstrumentDlg dlg = new JSEditMidiInstrumentDlg(instr.getInfo());
		dlg.setVisible(true);
		
		if(dlg.isCancelled()) return;
		
		MidiInstrumentInfo info = dlg.getInstrument();
		CC.getSamplerModel().mapBackendMidiInstrument (
			info.getMapId(), info.getMidiBank(), info.getMidiProgram(), info
		);
	}
	
	private class AddInstrumentAction extends AbstractAction {
		AddInstrumentAction() {
			super(StdI18n.i18n.getLabel("JSMidiInstrumentsPane.addInstrument"));
			
			String s = "JSMidiInstrumentsPane.addInstrument.tt";
			putValue(SHORT_DESCRIPTION, StdI18n.i18n.getLabel(s));
		}
		
		public void
		actionPerformed(ActionEvent e) {
			addInstrument();
		}
	}
	
	private class EditInstrumentAction extends AbstractAction {
		EditInstrumentAction() {
			super(StdI18n.i18n.getLabel("JSMidiInstrumentsPane.editInstrument"));
			
			String s = StdI18n.i18n.getLabel("JSMidiInstrumentsPane.editInstrument.tt");
			putValue(SHORT_DESCRIPTION, s);
			
			setEnabled(false);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			editInstrument(midiInstrumentTree.getSelectedInstrument());
			
		}
	}
	
	private class RemoveAction extends AbstractAction {
		RemoveAction() {
			String s = StdI18n.i18n.getLabel("JSMidiInstrumentsPane.remove.tt");
			putValue(SHORT_DESCRIPTION, s);
			
			setEnabled(false);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			midiInstrumentTree.removeSelectedInstrumentOrBank();
		}
	}
}
