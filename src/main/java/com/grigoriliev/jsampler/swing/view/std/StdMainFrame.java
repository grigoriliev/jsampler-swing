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

import javax.swing.Action;

import com.grigoriliev.jsampler.swing.view.SwingChannelsPane;
import com.grigoriliev.jsampler.swing.view.SwingMainFrame;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.task.Global;
import com.grigoriliev.jsampler.view.JSChannel;
import com.grigoriliev.jsampler.view.JSChannelsPane;

/**
 *
 * @author Grigor Iliev
 */
public abstract class StdMainFrame<CP extends SwingChannelsPane> extends SwingMainFrame<CP> implements com.grigoriliev.jsampler.event.ListSelectionListener {
	public
	StdMainFrame() {
		addChannelsPaneSelectionListener(this);
	}
	
	@Override
	public void
	addChannelsPane(CP chnPane) {
		super.addChannelsPane(chnPane);
		chnPane.addListSelectionListener(this);
	}
	
	@Override
	public boolean
	removeChannelsPane(CP chnPane) {
		chnPane.removeListSelectionListener(this);
		return super.removeChannelsPane(chnPane);
	}
	
	@Override
	public void
	valueChanged(com.grigoriliev.jsampler.event.ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		
		checkChannelSelection(getSelectedChannelsPane());
	}
	
	public StdA4n
	getA4n() { return StdA4n.a4n; }

	private boolean processConnectionFailure = false;

	@Override
	public void
	handleConnectionFailure() {
		if(processConnectionFailure) return;
		processConnectionFailure = true;
		CC.getTaskQueue().add(new Global.Disconnect());
		JSConnectionFailurePane p = new JSConnectionFailurePane();
		p.showDialog();
		processConnectionFailure = false;
	}
	
	private void
	checkChannelSelection(JSChannelsPane pane) {
		if(!pane.hasSelectedChannel()) {
			getA4n().duplicateChannels.putValue (
				Action.NAME, StdI18n.i18n.getMenuLabel("channels.duplicate")
			);
			getA4n().duplicateChannels.setEnabled(false);
				
			getA4n().removeChannels.putValue (
				Action.NAME, StdI18n.i18n.getMenuLabel("channels.removeChannel")
			);
			getA4n().removeChannels.setEnabled(false);
			
			getA4n().moveChannelsOnTop.setEnabled(false);
			getA4n().moveChannelsUp.setEnabled(false);
			getA4n().moveChannelsDown.setEnabled(false);
			getA4n().moveChannelsAtBottom.setEnabled(false);
			
			return;
		}
		
		getA4n().duplicateChannels.setEnabled(true);
		getA4n().removeChannels.setEnabled(true);
		
		if(pane.getSelectedChannelCount() > 1) {
			getA4n().duplicateChannels.putValue (
				Action.NAME, StdI18n.i18n.getMenuLabel("channels.duplicateChannels")
			);
			getA4n().removeChannels.putValue (
				Action.NAME, StdI18n.i18n.getMenuLabel("channels.removeChannels")
			);
		} else {
			getA4n().duplicateChannels.putValue (
				Action.NAME, StdI18n.i18n.getMenuLabel("channels.duplicate")
			);
			getA4n().removeChannels.putValue (
				Action.NAME, StdI18n.i18n.getMenuLabel("channels.removeChannel")
			);
		}
		
		getA4n().moveChannelsOnTop.setEnabled(false);
		getA4n().moveChannelsUp.setEnabled(true);
		getA4n().moveChannelsDown.setEnabled(true);
		getA4n().moveChannelsAtBottom.setEnabled(false);
		
		JSChannel[] chns = pane.getSelectedChannels();
		
		for(int i = 0; i < chns.length; i++) {
			if(pane.getChannel(i) != chns[i]) {
				getA4n().moveChannelsOnTop.setEnabled(true);
				break;
			}
		}
		
		if(chns[0] == pane.getFirstChannel()) getA4n().moveChannelsUp.setEnabled(false);
		
		if(chns[chns.length - 1] == pane.getLastChannel())
			getA4n().moveChannelsDown.setEnabled(false);
		
		for(int i = chns.length - 1, j = pane.getChannelCount() - 1; i >= 0; i--, j--) {
			if(pane.getChannel(j) != chns[i]) {
				getA4n().moveChannelsAtBottom.setEnabled(true);
				break;
			}
		}
	}
}
