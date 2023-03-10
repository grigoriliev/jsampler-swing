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

package com.grigoriliev.jsampler.swing.view;

import javax.swing.JPanel;
import com.grigoriliev.jsampler.SamplerChannelModel;
import com.grigoriliev.jsampler.view.JSChannelsPane;
import com.grigoriliev.jsampler.view.SessionViewConfig.ChannelConfig;

public abstract class SwingChannelsPane<C extends SwingChannel> extends JPanel implements JSChannelsPane<C> {
	private String title;
	
	/** Creates a new instance of <code>SwingChannelsPane</code>. */
	public
	SwingChannelsPane(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the title of this channels' pane.
	 * @return The title of this channels' pane.
	 */
	public String
	getTitle() { return title; }
	
	/**
	 * Sets the title of this channels' pane.
	 * @param title The new title of this channels' pane.
	 */
	public void
	setTitle(String title) {
		if(this.title.equals(title)) return;
		
		String oldTitle = this.title;
		this.title = title;
		firePropertyChange(TITLE, oldTitle, title);
	}
	
	/**
	 * Returns the title of this <code>JSChannelsPane</code>.
	 * @return The title of this <code>JSChannelsPane</code>.
	 */
	public String
	toString() { return getTitle(); }
	
	/**
	 * Adds new channel to this channels pane.
	 * @param channelModel The sampler channel model to be used by the new channel.
	 * @param config The view config of the sampler channel.
	 */
	public void
	addChannel(SamplerChannelModel channelModel, ChannelConfig config) {
		addChannel(channelModel);
	}
}
