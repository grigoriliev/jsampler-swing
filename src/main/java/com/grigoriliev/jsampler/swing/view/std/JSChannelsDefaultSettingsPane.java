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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JPanel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.grigoriliev.jsampler.juife.swing.InformationDialog;

import com.grigoriliev.jsampler.jlscp.SamplerEngine;

import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.HF;
import com.grigoriliev.jsampler.JSPrefs;

import static com.grigoriliev.jsampler.JSPrefs.*;


/**
 *
 * @author Grigor Iliev
 */
public class JSChannelsDefaultSettingsPane extends JPanel {
	private final JLabel lDefaultEngine =
		new JLabel(StdI18n.i18n.getLabel("JSChannelsDefaultSettingsPane.lDefaultEngine"));
	
	private final JLabel lMidiInput =
		new JLabel(StdI18n.i18n.getLabel("JSChannelsDefaultSettingsPane.lMidiInput"));
	
	private final JLabel lAudioOutput =
		new JLabel(StdI18n.i18n.getLabel("JSChannelsDefaultSettingsPane.lAudioOutput"));
	
	private final JLabel lChannelVolume =
		new JLabel(StdI18n.i18n.getLabel("JSChannelsDefaultSettingsPane.lChannelVolume"));
	
	private final JLabel lMidiMap =
		new JLabel(StdI18n.i18n.getLabel("JSChannelsDefaultSettingsPane.lMidiMap"));
	
	private final JComboBox cbDefaultEngine = new JComboBox();
	private final JComboBox cbMidiInput = new JComboBox();
	private final JComboBox cbAudioOutput = new JComboBox();
	private final JSlider slChannelVolume = new JSlider(0, 100);
	private final JComboBox cbMidiMap = new JComboBox();
	
	private final JLabel lVolume = new JLabel();
	
	private final static String strFirstDevice =
		StdI18n.i18n.getLabel("JSChannelsDefaultSettingsPane.strFirstDevice");
	
	private final static String strFirstDeviceNextChannel =
		StdI18n.i18n.getLabel("JSChannelsDefaultSettingsPane.strFirstDeviceNextChannel");
	
	private class NoMap {
		public String
		toString() { return "[None]"; }
	}
	
	private NoMap noMap = new NoMap();
	
	private class DefaultMap {
		public String
		toString() { return "[Default]"; }
	}
	
	private DefaultMap defaultMap = new DefaultMap();
	
	private static NumberFormat numberFormat = NumberFormat.getInstance();
	
	
	/** Creates a new instance of <code>JSChannelsDefaultSettingsPane</code> */
	public
	JSChannelsDefaultSettingsPane() {
		numberFormat.setMaximumFractionDigits(1);
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
	
		setLayout(gridbag);
		
		c.fill = GridBagConstraints.NONE;
	
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(3, 3, 3, 3);
		gridbag.setConstraints(lDefaultEngine, c);
		add(lDefaultEngine); 

		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(lMidiInput, c);
		add(lMidiInput); 

		c.gridx = 0;
		c.gridy = 2;
		gridbag.setConstraints(lAudioOutput, c);
		add(lAudioOutput);

		c.gridx = 0;
		c.gridy = 3;
		gridbag.setConstraints(lMidiMap, c);
		add(lMidiMap);

		c.gridx = 0;
		c.gridy = 4;
		gridbag.setConstraints(lChannelVolume, c);
		add(lChannelVolume);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(cbDefaultEngine, c);
		add(cbDefaultEngine);
		
		c.gridx = 1;
		c.gridy = 1;
		gridbag.setConstraints(cbMidiInput, c);
		add(cbMidiInput);
		
		c.gridx = 1;
		c.gridy = 2;
		gridbag.setConstraints(cbAudioOutput, c);
		add(cbAudioOutput);
		
		c.gridx = 1;
		c.gridy = 3;
		gridbag.setConstraints(cbMidiMap, c);
		add(cbMidiMap);
		
		JPanel volumePane = new JPanel();
		volumePane.setOpaque(false);
		volumePane.setLayout(new BoxLayout(volumePane, BoxLayout.X_AXIS));
		
		Dimension d = slChannelVolume.getPreferredSize();
		slChannelVolume.setMaximumSize(new Dimension(d.width > 300 ? d.width : 300, d.height));
		slChannelVolume.setOpaque(false);
		volumePane.add(slChannelVolume);
		
		volumePane.add(Box.createRigidArea(new Dimension(6, 0)));
		
		lVolume.setHorizontalAlignment(lVolume.RIGHT);
		
		// We use this to set the size of the lVolume
		// to prevent the frequent resizing of lVolume
		lVolume.setText("100000%");
		lVolume.setPreferredSize(lVolume.getPreferredSize());
		lVolume.setMinimumSize(lVolume.getPreferredSize());
		
		volumePane.add(lVolume);
		
		slChannelVolume.addChangeListener(new ChangeListener() {
			public void
			stateChanged(ChangeEvent e) { updateVolume(); }
		});
		
		int v = preferences().getIntProperty(DEFAULT_CHANNEL_VOLUME);
		slChannelVolume.setValue(v);
		
		c.gridx = 1;
		c.gridy = 4;
		gridbag.setConstraints(volumePane, c);
		add(volumePane);
		
		for(SamplerEngine e : CC.getSamplerModel().getEngines()) {
			cbDefaultEngine.addItem(e);
		}
		
		String defaultEngine = preferences().getStringProperty(DEFAULT_ENGINE);
		for(SamplerEngine e : CC.getSamplerModel().getEngines()) {
			if(e.getName().equals(defaultEngine)) cbDefaultEngine.setSelectedItem(e);
		}
		
		cbDefaultEngine.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { changeDefaultEngine(); }
		});
		
		cbMidiInput.addItem(strFirstDevice);
		cbMidiInput.addItem(strFirstDeviceNextChannel);
		
		String s = preferences().getStringProperty(DEFAULT_MIDI_INPUT);
		
		if(s.equals("firstDevice")) {
			cbMidiInput.setSelectedItem(strFirstDevice);
		} else if(s.equals("firstDeviceNextChannel")) {
			cbMidiInput.setSelectedItem(strFirstDeviceNextChannel);
		}
		
		cbMidiInput.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { changeDefaultMidiInput(); }
		});
		
		cbAudioOutput.addItem(strFirstDevice);
		
		cbAudioOutput.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { changeDefaultAudioOutput(); }
		});
		
		cbMidiMap.addItem(noMap);
		cbMidiMap.addItem(defaultMap);
		
		String midiMap = preferences().getStringProperty(DEFAULT_MIDI_INSTRUMENT_MAP);
		if(midiMap.equals("midiInstrumentMap.none")) {
			cbMidiMap.setSelectedItem(noMap);
		} else if(midiMap.equals("midiInstrumentMap.default")) {
			cbMidiMap.setSelectedItem(defaultMap);
		}
		
		cbMidiMap.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { changeDefaultMidiMap(); }
		});
	}
	
	public JDialog
	createDialog(Dialog owner) {
		String s = StdI18n.i18n.getLabel("JSChannelsDefaultSettingsPane.title");
		InformationDialog dlg = new InformationDialog(owner, s, this);
		return dlg;
	}
	
	private void
	changeDefaultEngine() {
		Object o = cbDefaultEngine.getSelectedItem();
		if(o == null) return;
		String s = ((SamplerEngine) o).getName();
		preferences().setStringProperty(DEFAULT_ENGINE, s);
	}
	
	private void
	changeDefaultMidiInput() {
		Object o = cbMidiInput.getSelectedItem();
		if(o == null) return;
		
		if(o == strFirstDevice) {
			preferences().setStringProperty(DEFAULT_MIDI_INPUT, "firstDevice");
		} else if(o == strFirstDeviceNextChannel) {
			preferences().setStringProperty(DEFAULT_MIDI_INPUT, "firstDeviceNextChannel");
		}
	}
	
	private void
	changeDefaultAudioOutput() {
		Object o = cbAudioOutput.getSelectedItem();
		if(o == null) return;
		
		if(o == strFirstDevice) {
			preferences().setStringProperty(DEFAULT_AUDIO_OUTPUT, "firstDevice");
		}
	}
	
	private void
	changeDefaultMidiMap() {
		Object o = cbMidiMap.getSelectedItem();
		if(o == null) return;
		String s = DEFAULT_MIDI_INSTRUMENT_MAP;
		if(o == noMap) {
			preferences().setStringProperty(s, "midiInstrumentMap.none");
		} else if(o == defaultMap) {
			preferences().setStringProperty(s, "midiInstrumentMap.default");
		}
	}
	
	private void
	updateVolume() {
		int volume = slChannelVolume.getValue();
		if(CC.getViewConfig().isMeasurementUnitDecibel()) {
			double dB = HF.percentsToDecibels(volume);
			lVolume.setText(numberFormat.format(dB) + "dB");
		} else {
			lVolume.setText(String.valueOf(volume) + '%');
		}
		
		if(slChannelVolume.getValueIsAdjusting()) return;
		preferences().setIntProperty(DEFAULT_CHANNEL_VOLUME, slChannelVolume.getValue());
	}
	
	private static JSPrefs
	preferences() { return CC.getViewConfig().preferences(); }
}
