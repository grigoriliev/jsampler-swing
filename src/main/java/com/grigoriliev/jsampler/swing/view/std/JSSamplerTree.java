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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.tree.TreeSelectionModel;

import com.grigoriliev.jsampler.swing.view.AbstractSamplerTree;
import com.grigoriliev.jsampler.swing.view.SamplerTreeModel;

/**
 *
 * @author Grigor Iliev
 */
public class JSSamplerTree extends AbstractSamplerTree implements SamplerBrowser.ContextMenuOwner {
	/**
	 * Creates a new instance of <code>JSSamplerTree</code>.
	 */
	public
	JSSamplerTree(SamplerTreeModel model) {
		super(model);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		addMouseListener(new MouseAdapter() {
			public void
			mousePressed(MouseEvent e) {
				if(e.getButton() != e.BUTTON3) return;
				setSelectionPath(getClosestPathForLocation(e.getX(), e.getY()));
			}
		});
		
		addMouseListener(new SamplerBrowser.ContextMenu(this));
	}
	
	@Override
	public Object
	getSelectedItem() {
		return getSelectionModel().getSelectionPath().getLastPathComponent();
	}
	
	@Override
	public Object
	getSelectedParent() { return null; }
}
