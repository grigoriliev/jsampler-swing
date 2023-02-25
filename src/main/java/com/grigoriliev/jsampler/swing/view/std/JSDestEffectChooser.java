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
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import com.grigoriliev.jsampler.juife.swing.OkCancelDialog;
import com.grigoriliev.jsampler.swing.view.AbstractTreeModel;
import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.swing.view.SamplerTreeModel;
import com.grigoriliev.jsampler.AudioDeviceModel;

/**
 *
 * @author Grigor Iliev
 */
public class JSDestEffectChooser  extends OkCancelDialog implements TreeSelectionListener {
	protected AudioDeviceModel audioDev;
	protected JTree tree;
	
	private SamplerTreeModel.EffectInstanceTreeNode selectedNode = null;
	
	public
	JSDestEffectChooser(AudioDeviceModel audioDev) {
		super(SHF.getMainFrame(), StdI18n.i18n.getLabel("JSDestEffectChooser.title"));
		setName("JSDestEffectChooser");
		
		 
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		tree = new JTree(new DestEffectTreeModel(audioDev));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		p.add(new JScrollPane(tree));
		p.setPreferredSize(new java.awt.Dimension(500, 300));
		setMainPane(p);
		
		setSavedSize();
		
		setResizable(true);
		
		tree.getSelectionModel().addTreeSelectionListener(this);
		btnOk.setEnabled(false);
	}
	
	public SamplerTreeModel.EffectInstanceTreeNode
	getSelectedNode() { return selectedNode; }
	
	@Override
	protected void
	onOk() {
		if(!btnOk.isEnabled()) return;
		
		StdUtils.saveWindowBounds(getName(), getBounds());
		
		setVisible(false);
		setCancelled(false);
	}
	
	@Override
	protected void
	onCancel() { setVisible(false); }
	
	private boolean
	setSavedSize() {
		Rectangle r = StdUtils.getWindowBounds(getName());
		if(r == null) return false;
		
		setBounds(r);
		return true;
	}
		
	public void
	valueChanged(TreeSelectionEvent e) {
		if(e.getNewLeadSelectionPath() == null) {
			btnOk.setEnabled(false);
			return;
		}
			
		SamplerTreeModel.TreeNodeBase node = (SamplerTreeModel.TreeNodeBase)e.getNewLeadSelectionPath().getLastPathComponent();
		
		boolean b = false;
		if(node instanceof SamplerTreeModel.SendEffectChainTreeNode) {
			if(((SamplerTreeModel.SendEffectChainTreeNode)node).getChildCount() > 0) {
				selectedNode = ((SamplerTreeModel.SendEffectChainTreeNode)node).getChildAt(0);
				b = true;
			}
		} else if(node instanceof SamplerTreeModel.EffectInstanceTreeNode) {
			selectedNode = (SamplerTreeModel.EffectInstanceTreeNode)node;
			b = true;
		}
		
		btnOk.setEnabled(b);
	}
	
	public static class DestEffectTreeModel extends AbstractTreeModel {
		private SamplerTreeModel.AudioDeviceTreeNode root;
		
		public
		DestEffectTreeModel(AudioDeviceModel audioDev) {
			root = new SamplerTreeModel.AudioDeviceTreeNode(this, null, audioDev);
		}
	
		@Override
		public Object
		getRoot() { return root; }
	
		@Override
		public void
		valueForPathChanged(TreePath path, Object newValue) { }
	}
}
