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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.grigoriliev.jsampler.juife.swing.OkCancelDialog;

import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.MidiInstrumentMap;

import com.grigoriliev.jsampler.event.ListEvent;
import com.grigoriliev.jsampler.event.ListListener;

/**
 *
 * @author Grigor Iliev
 */
public class JSMidiBankChooser extends OkCancelDialog implements ListListener<MidiInstrumentMap> {
	private final JLabel lMap = new JLabel(StdI18n.i18n.getLabel("JSMidiBankChooser.lMap"));
	private final JLabel lBank = new JLabel(StdI18n.i18n.getLabel("JSMidiBankChooser.lBank"));
	
	private final JComboBox cbMap = new JComboBox();
	private final JSpinner spinnerBank;
	
	/** Creates a new instance of <code>JSMidiBankChooser</code> */
	public
	JSMidiBankChooser() {
		super(SHF.getMainFrame());
		int i = CC.getViewConfig().getFirstMidiBankNumber();
		spinnerBank = new JSpinner(new SpinnerNumberModel(i, i, 16383 + i, 1));
		
		JPanel mainPane = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		mainPane.setLayout(gridbag);
		
		c.fill = GridBagConstraints.NONE;
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(3, 3, 3, 3);
		gridbag.setConstraints(lMap, c);
		mainPane.add(lMap);
		
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(lBank, c);
		mainPane.add(lBank);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.insets = new Insets(3, 3, 3, 3);
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(cbMap, c);
		mainPane.add(cbMap);
			
		c.gridx = 1;
		c.gridy = 1;
		gridbag.setConstraints(spinnerBank, c);
		mainPane.add(spinnerBank);
		
		for(i = 0; i < CC.getSamplerModel().getMidiInstrumentMapCount(); i++) {
			cbMap.addItem(CC.getSamplerModel().getMidiInstrumentMap(i));
		}
		if(cbMap.getItemCount() == 0) {
			btnOk.setEnabled(false);
			cbMap.setEnabled(false);
		}
		
		CC.getSamplerModel().addMidiInstrumentMapListListener(this);
		
		mainPane.setMinimumSize(mainPane.getPreferredSize());
		setMainPane(mainPane);
		
		setResizable(true);
	}
	
	/**
	 * Gets the selected MIDI instrument map.
	 */
	public MidiInstrumentMap
	getSelectedMidiInstrumentMap() { return (MidiInstrumentMap)cbMap.getSelectedItem(); }
	
	public void
	setSelectedMidiInstrumentMap(MidiInstrumentMap map) {
		cbMap.setSelectedItem(map);
	}
	
	/**
	 * Sets the selected MIDI bank (always zero-based).
	 */
	public void
	setMidiBank(int bank) {
		spinnerBank.setValue(bank + CC.getViewConfig().getFirstMidiBankNumber());
	}
	
	/**
	 * Gets the selected MIDI bank (always zero-based).
	 */
	public int
	getMidiBank() {
		int i = CC.getViewConfig().getFirstMidiBankNumber();
		return Integer.parseInt(spinnerBank.getValue().toString()) - i;
	}
	
	protected void
	onOk() {
		if(!btnOk.isEnabled()) return;
		
		setVisible(false);
		setCancelled(false);
	}
	
	protected void
	onCancel() { setVisible(false); }
	
	public void
	entryAdded(ListEvent<MidiInstrumentMap> e) {
		if(cbMap.getItemCount() == 0) {
			btnOk.setEnabled(true);
			cbMap.setEnabled(true);
		}
		cbMap.addItem(e.getEntry());
	}
	
	public void
	entryRemoved(ListEvent<MidiInstrumentMap> e) {
		cbMap.removeItem(e.getEntry());
		if(cbMap.getItemCount() == 0) {
			btnOk.setEnabled(false);
			cbMap.setEnabled(false);
		}
	}
}
