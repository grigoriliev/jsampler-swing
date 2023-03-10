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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.JSPrefs;

import static com.grigoriliev.jsampler.JSPrefs.*;

/**
 *
 * @author Grigor Iliev
 */
public class JSViewProps {
	
	/** Forbids the instantiation of this class. */
	private JSViewProps() { }
	
	private static JSPrefs
	preferences() { return CC.getViewConfig().preferences(); }
	
	
	public static class MidiDevicesPane extends JPanel {
		private final JCheckBox checkAdditionalParams =
			new JCheckBox(StdI18n.i18n.getLabel("JSViewProps.checkAdditionalParams"));
		
		public
		MidiDevicesPane() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			checkAdditionalParams.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			add(checkAdditionalParams);
			String s = "MidiDevice.showAdditionalParameters";
			boolean b = preferences().getBoolProperty(s);
			checkAdditionalParams.setSelected(b);
			s = StdI18n.i18n.getLabel("JSViewProps.MidiDevicesPane");
			setBorder(BorderFactory.createTitledBorder(s));
			setMaximumSize(new Dimension(Short.MAX_VALUE, getPreferredSize().height));
			setAlignmentX(JPanel.LEFT_ALIGNMENT);
		}
		
		public void
		apply() {
			String s = "MidiDevice.showAdditionalParameters";
			preferences().setBoolProperty(s, checkAdditionalParams.isSelected());
		}
	}
	
	public static class AudioDevicesPane extends JPanel {
		private final JCheckBox checkAdditionalParams =
			new JCheckBox(StdI18n.i18n.getLabel("JSViewProps.checkAdditionalParams"));
		
		public
		AudioDevicesPane() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			checkAdditionalParams.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			add(checkAdditionalParams);
			String s = "AudioDevice.showAdditionalParameters";
			boolean b = preferences().getBoolProperty(s);
			checkAdditionalParams.setSelected(b);
			s = StdI18n.i18n.getLabel("JSViewProps.AudioDevicesPane");
			setBorder(BorderFactory.createTitledBorder(s));
			setMaximumSize(new Dimension(Short.MAX_VALUE, getPreferredSize().height));
			setAlignmentX(JPanel.LEFT_ALIGNMENT);
		}
		
		public void
		apply() {
			String s = "AudioDevice.showAdditionalParameters";
			preferences().setBoolProperty(s, checkAdditionalParams.isSelected());
		}
	}
	
	public static class ConfirmationMessagesPane extends JPanel {
		private final JCheckBox checkConfirmChannelRemoval =
			new JCheckBox(StdI18n.i18n.getLabel("JSViewProps.checkConfirmChannelRemoval"));
		
		private final JCheckBox checkConfirmDeviceRemoval =
			new JCheckBox(StdI18n.i18n.getLabel("JSViewProps.checkConfirmDeviceRemoval"));
		
		private final JCheckBox checkConfirmAppQuit =
			new JCheckBox(StdI18n.i18n.getLabel("JSViewProps.checkConfirmAppQuit"));
		
		public
		ConfirmationMessagesPane() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			checkConfirmChannelRemoval.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			add(checkConfirmChannelRemoval);
			boolean b = preferences().getBoolProperty(CONFIRM_CHANNEL_REMOVAL);
			checkConfirmChannelRemoval.setSelected(b);
			
			checkConfirmDeviceRemoval.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			add(checkConfirmDeviceRemoval);
			b = preferences().getBoolProperty(CONFIRM_DEVICE_REMOVAL);
			checkConfirmDeviceRemoval.setSelected(b);
			
			checkConfirmAppQuit.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			add(checkConfirmAppQuit);
			b = preferences().getBoolProperty(CONFIRM_APP_QUIT);
			checkConfirmAppQuit.setSelected(b);
			
			String s = StdI18n.i18n.getLabel("JSViewProps.ConfirmationMessagesPane");
			setBorder(BorderFactory.createTitledBorder(s));
			setMaximumSize(new Dimension(Short.MAX_VALUE, getPreferredSize().height));
			setAlignmentX(JPanel.LEFT_ALIGNMENT);
		}
		
		public void
		apply() {
			String s = CONFIRM_CHANNEL_REMOVAL;
			preferences().setBoolProperty(s, checkConfirmChannelRemoval.isSelected());
			
			s = CONFIRM_DEVICE_REMOVAL;
			preferences().setBoolProperty(s, checkConfirmDeviceRemoval.isSelected());
			
			s = CONFIRM_APP_QUIT;
			preferences().setBoolProperty(s, checkConfirmAppQuit.isSelected());
		}
	}
}
