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

import javax.swing.SwingUtilities;

import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.SamplerChannelModel;

import com.grigoriliev.jsampler.event.SamplerChannelListEvent;
import com.grigoriliev.jsampler.event.SamplerChannelListListener;

import com.grigoriliev.jsampler.juife.swing.InformationDialog;


/**
 *
 * @author Grigor Iliev
 */
public class JSFxSendsDlg extends InformationDialog {
	private JSFxSendsPane mainPane;
	private final SamplerChannelListListener channelListListener;
	
	/** Creates a new instance of <code>JSFxSendsDlg</code> */
	public
	JSFxSendsDlg(SamplerChannelModel model) {
		this(new JSFxSendsPane(model));
	}
	
	/** Creates a new instance of <code>JSFxSendsDlg</code> */
	public
	JSFxSendsDlg(JSFxSendsPane pane) {
		super(SHF.getMainFrame(), pane);
		
		mainPane = pane;
		
		String s = CC.getMainFrame().getChannelPath(pane.getChannelModel());
		setTitle(StdI18n.i18n.getLabel("JSFxSendsDlg.title", s));
		setModal(false);
		showCloseButton(false);
		
		channelListListener = new SamplerChannelListListener() {
			public void
			channelAdded(SamplerChannelListEvent e) {
				if(CC.getSamplerModel().getChannelListIsAdjusting()) return;
				updateTitle();
			}
			
			public void
			channelRemoved(SamplerChannelListEvent e) {
				//if(CC.getSamplerModel().getChannelListIsAdjusting()) return; //TODO: 
				
				updateTitle();
			}
		};
		
		CC.getSamplerModel().addSamplerChannelListListener(channelListListener);
	}
	
	protected void
	updateTitle() {
		SwingUtilities.invokeLater(new Runnable() {
			public void
			run() {
				String s = CC.getMainFrame().getChannelPath(mainPane.getChannelModel());
				setTitle(StdI18n.i18n.getLabel("JSFxSendsDlg.title", s));
			}
		});
		
	}
}
