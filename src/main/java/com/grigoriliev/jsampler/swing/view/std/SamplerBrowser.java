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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.grigoriliev.jsampler.swing.view.SamplerTreeModel;
import com.grigoriliev.jsampler.AudioDeviceModel;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.EffectInstance;

import com.grigoriliev.jsampler.jlscp.EffectParameter;

import static com.grigoriliev.jsampler.swing.view.std.StdViewConfig.getViewConfig;

/**
 *
 * @author Grigor Iliev
 */
public class SamplerBrowser {
	
	
	private static class SamplerMenu extends JPopupMenu {
		public
		SamplerMenu() {
			JMenuItem mi = new JMenuItem(StdA4n.a4n.refresh);
			add(mi);
			
			mi = new JMenuItem(StdA4n.a4n.resetSampler);
			add(mi);
		}
	}
	
	private static class DestEffectMenu extends JPopupMenu {
		public
		DestEffectMenu() {
			String s = StdI18n.i18n.getMenuLabel("SamplerBrowser.action.setDestEffect");
			JMenuItem mi = new JMenuItem(s);
			add(mi);
			mi.addActionListener(new ActionListener() {
				public void
				actionPerformed(ActionEvent e) {
					SamplerTreeModel.DestEffectTreeNode node = getDestEffectTreeNode();
					if(node == null) return;
					chooseDestEffect(node);
				}
			});
			
			s = StdI18n.i18n.getMenuLabel("SamplerBrowser.action.removeDestEffect");
			mi = new JMenuItem(s);
			add(mi);
			mi.addActionListener(new ActionListener() {
				public void
				actionPerformed(ActionEvent e) {
					SamplerTreeModel.DestEffectTreeNode node = getDestEffectTreeNode();
					if(node == null) return;
					int fxSendId = node.getFxSend().getFxSendId();
					node.getChannel().removeBackendFxSendEffect(fxSendId);
				}
			});
		}
		
		private void
		chooseDestEffect(SamplerTreeModel.DestEffectTreeNode node) {
			AudioDeviceModel audioDev = node.getAudioDevice();
			JSDestEffectChooser dlg = getViewConfig().createDestEffectChooser(audioDev);
			
			dlg.setVisible(true);
			if(dlg.isCancelled()) return;
			
			int fxSendId = node.getFxSend().getFxSendId();
			int chainId = dlg.getSelectedNode().getParent().getId();
			int chainPos = dlg.getSelectedNode().getParent().getIndex(dlg.getSelectedNode());
			node.getChannel().setBackendFxSendEffect(fxSendId, chainId, chainPos);
		}
		
		private SamplerTreeModel.DestEffectTreeNode
		getDestEffectTreeNode() {
			if(ContextMenu.getCurrentOwner() == null) return null;
			Object o = ContextMenu.getCurrentOwner().getSelectedItem();
			
			SamplerTreeModel.DestEffectTreeNode node = null;
			if(o == null) return null;
			
			if(o instanceof SamplerTreeModel.DestEffectDirTreeNode) {
				node = ((SamplerTreeModel.DestEffectDirTreeNode)o).getChildAt(0);
			} else if(o instanceof SamplerTreeModel.DestEffectTreeNode) {
				node = (SamplerTreeModel.DestEffectTreeNode)o;
			}
			
			return node;
		}
	}
	
	private static class AudioDeviceMenu extends JPopupMenu {
		public
		AudioDeviceMenu() {
			String s = StdI18n.i18n.getMenuLabel("SamplerBrowser.action.removeAudioDev");
			JMenuItem mi = new JMenuItem(s);
			add(mi);
			mi.addActionListener(new ActionListener() {
				public void
				actionPerformed(ActionEvent e) {
					SamplerTreeModel.AudioDeviceTreeNode node = getAudioDeviceTreeNode();
					if(node == null) return;
					
					int id = node.getAudioDevice().getDeviceId();
					CC.getSamplerModel().removeBackendAudioDevice(id);
				}
			});
		}
		
		private SamplerTreeModel.AudioDeviceTreeNode
		getAudioDeviceTreeNode() {
			if(ContextMenu.getCurrentOwner() == null) return null;
			Object o = ContextMenu.getCurrentOwner().getSelectedItem();
			if(o == null || !(o instanceof SamplerTreeModel.AudioDeviceTreeNode)) return null;
			return (SamplerTreeModel.AudioDeviceTreeNode)o;
		}
	}
	
	private static class AddSendEffectChainAction extends AbstractAction {
		AddSendEffectChainAction() {
			super(StdI18n.i18n.getMenuLabel("SamplerBrowser.action.addChain"));
		}
		
		@Override
		public void
		actionPerformed(ActionEvent e) {
			SamplerTreeModel.SendEffectChainsTreeNode node = getSendEffectChainsTreeNode();
			if(node == null) return;
			node.getAudioDevice().addBackendSendEffectChain();
		}
		
		private SamplerTreeModel.SendEffectChainsTreeNode
		getSendEffectChainsTreeNode() {
			Object o = getSelectedObject();
			if(o == null || !(o instanceof SamplerTreeModel.SendEffectChainsTreeNode)) return null;
			return (SamplerTreeModel.SendEffectChainsTreeNode)o;
		}
	}
	
	private static Object
	getSelectedObject() {
		if(ContextMenu.getCurrentOwner() == null) return null;
		Object o = ContextMenu.getCurrentOwner().getSelectedItem();
		if(o == null) o = ContextMenu.getCurrentOwner().getSelectedParent();
		return o;
	}
	
	private static class SendEffectChainDirMenu extends JPopupMenu {
		public
		SendEffectChainDirMenu() {
			JMenuItem mi = new JMenuItem(new AddSendEffectChainAction());
			add(mi);
		}
	}
	
	private static class SendEffectChainMenu extends JPopupMenu {
		public
		SendEffectChainMenu() {
			String s = StdI18n.i18n.getMenuLabel("SamplerBrowser.action.removeChain");
			JMenuItem mi = new JMenuItem(s);
			add(mi);
			mi.addActionListener(new ActionListener() {
				public void
				actionPerformed(ActionEvent e) {
					SamplerTreeModel.SendEffectChainTreeNode node = getSendEffectChainTreeNode();
					if(node == null) return;
					
					node.getAudioDevice().removeBackendSendEffectChain(node.getId());
				}
			});
			
			mi = new JMenuItem(new AddSendEffectInstsAction());
			add(mi);
		}
	}
	
	private static class AddSendEffectInstsAction extends AbstractAction {
		AddSendEffectInstsAction() {
			super(StdI18n.i18n.getMenuLabel("SamplerBrowser.action.appendEffectInstance"));
		}
		
		@Override
		public void
		actionPerformed(ActionEvent e) {
			SamplerTreeModel.SendEffectChainTreeNode node = getSendEffectChainTreeNode();
			if(node == null) return;
			
			JSAddEffectInstancesDlg dlg = new JSAddEffectInstancesDlg();
			dlg.setVisible(true);
			if(dlg.isCancelled()) return;
			
			node.getAudioDevice().addBackendEffectInstances (
				dlg.getSelectedEffects(), node.getId(), -1
			);
		}
	}
		
	private static SamplerTreeModel.SendEffectChainTreeNode
	getSendEffectChainTreeNode() {
		Object o = getSelectedObject();
		if(o == null || !(o instanceof SamplerTreeModel.SendEffectChainTreeNode)) return null;
		return (SamplerTreeModel.SendEffectChainTreeNode)o;
	}
	
	private static class EffectInstanceMenu extends JPopupMenu {
		public
		EffectInstanceMenu() {
			String s = StdI18n.i18n.getMenuLabel("SamplerBrowser.action.removeEffectInstance");
			JMenuItem mi = new JMenuItem(s);
			add(mi);
			mi.addActionListener(new ActionListener() {
				public void
				actionPerformed(ActionEvent e) {
					SamplerTreeModel.EffectInstanceTreeNode node = getEffectInstanceTreeNode();
					if(node == null) return;
					
					node.getParent().getAudioDevice().removeBackendEffectInstance (
						node.getParent().getId(), node.getInstanceId()
					);
				}
			});
			
			s = StdI18n.i18n.getMenuLabel("SamplerBrowser.action.insertEffectInstance");
			mi = new JMenuItem(s);
			add(mi);
			mi.addActionListener(new ActionListener() {
				public void
				actionPerformed(ActionEvent e) {
					SamplerTreeModel.EffectInstanceTreeNode node = getEffectInstanceTreeNode();
					if(node == null) return;
					
					JSAddEffectInstancesDlg dlg = new JSAddEffectInstancesDlg();
					dlg.setVisible(true);
					if(dlg.isCancelled()) return;
					
					SamplerTreeModel.SendEffectChainTreeNode parent = node.getParent();
					int idx = parent.getEffectChain().getIndex(node.getInstanceId());
					
					parent.getAudioDevice().addBackendEffectInstances (
						dlg.getSelectedEffects(), parent.getId(), idx
					);
				}
			});
		}
		
		private SamplerTreeModel.EffectInstanceTreeNode
		getEffectInstanceTreeNode() {
			if(ContextMenu.getCurrentOwner() == null) return null;
			Object o = ContextMenu.getCurrentOwner().getSelectedItem();
			if(o == null || !(o instanceof SamplerTreeModel.EffectInstanceTreeNode)) return null;
			return (SamplerTreeModel.EffectInstanceTreeNode)o;
		}
	}
	
	private static class EffectParameterMenu extends JPopupMenu {
		public
		EffectParameterMenu() {
			String s = StdI18n.i18n.getMenuLabel("SamplerBrowser.action.editEffectPrm");
			JMenuItem mi = new JMenuItem(s);
			add(mi);
			mi.addActionListener(new ActionListener() {
				public void
				actionPerformed(ActionEvent e) { editParameter(); }
			});
		}
		
		private void
		editParameter() {
			EffectParameter prm = getEffectParameter();
			if(prm == null) return;
			
			JSSetParameterDlg dlg = new JSSetParameterDlg(prm);
			
			dlg.setVisible(true);
			if(dlg.isCancelled()) return;
			
			EffectInstance ei;
			ei = CC.getSamplerModel().getEffectInstanceById(prm.getEffectInstanceId());
			if(ei == null) return;
			ei.setBackendParameter(prm.getIndex(), dlg.getNewValue());
		}
		
		private EffectParameter
		getEffectParameter() {
			if(ContextMenu.getCurrentOwner() == null) return null;
			Object o = ContextMenu.getCurrentOwner().getSelectedItem();
			if(o == null || !(o instanceof EffectParameter)) return null;
			return (EffectParameter)o;
		}
	}
	
	public static class ContextMenu extends MouseAdapter {
		private static ContextMenuOwner currentOwner;
		
		private static SamplerMenu samplerMenu = null;
		private static DestEffectMenu destEffectMenu = null;
		private static AudioDeviceMenu auDevMenu = null;
		private static SendEffectChainDirMenu chainDirMenu = null;
		private static SendEffectChainMenu chainMenu = null;
		private static EffectInstanceMenu instanceMenu = null;
		private static EffectParameterMenu effectPrmMenu = null;
		
		private final ContextMenuOwner owner;
	
		public
		ContextMenu(ContextMenuOwner owner) {
			this.owner = owner;
		}
		
		public static ContextMenuOwner
		getCurrentOwner() { return currentOwner; }
		
		@Override
		public void
		mousePressed(MouseEvent e) {
			if(e.isPopupTrigger()) show(e);
		}
	
		@Override
		public void
		mouseReleased(MouseEvent e) {
			if(e.isPopupTrigger()) show(e);
		}
	
		void
		show(MouseEvent e) {
			Object o = owner.getSelectedItem();
			if(o == null) {
				if(owner.getSelectedParent() == null) return;
				show0(e);
				return;
			}
			
			currentOwner = owner;
			
			if(o instanceof SamplerTreeModel.SamplerTreeNode) {
				if(samplerMenu == null) samplerMenu = new SamplerMenu();
				samplerMenu.show(e.getComponent(), e.getX(), e.getY());
			} else if(o instanceof SamplerTreeModel.DestEffectDirTreeNode) {
				if(destEffectMenu == null) destEffectMenu = new DestEffectMenu();
				destEffectMenu.show(e.getComponent(), e.getX(), e.getY());
			} else if(o instanceof SamplerTreeModel.DestEffectTreeNode) {
				if(destEffectMenu == null) destEffectMenu = new DestEffectMenu();
				destEffectMenu.show(e.getComponent(), e.getX(), e.getY());
			} else if(o instanceof SamplerTreeModel.AudioDeviceTreeNode) {
				if(auDevMenu == null) auDevMenu = new AudioDeviceMenu();
				auDevMenu.show(e.getComponent(), e.getX(), e.getY());
			} else if(o instanceof SamplerTreeModel.SendEffectChainsTreeNode) {
				if(chainDirMenu == null) chainDirMenu = new SendEffectChainDirMenu();
				chainDirMenu.show(e.getComponent(), e.getX(), e.getY());
			} else if(o instanceof SamplerTreeModel.SendEffectChainTreeNode) {
				if(chainMenu == null) chainMenu = new SendEffectChainMenu();
				chainMenu.show(e.getComponent(), e.getX(), e.getY());
			} else if(o instanceof SamplerTreeModel.EffectInstanceTreeNode) {
				if(instanceMenu == null) instanceMenu = new EffectInstanceMenu();
				instanceMenu.show(e.getComponent(), e.getX(), e.getY());
			} else if(o instanceof EffectParameter) {
				if(effectPrmMenu == null) effectPrmMenu = new EffectParameterMenu();
				effectPrmMenu.show(e.getComponent(), e.getX(), e.getY());
			}
			
		}
	
		void
		show0(MouseEvent e) {
			Object o = owner.getSelectedParent();
			
			if(o instanceof SamplerTreeModel.SendEffectChainsTreeNode) {
				if(inChainDirMenu == null) inChainDirMenu = new InSendEffectChainDirMenu();
				inChainDirMenu.show(e.getComponent(), e.getX(), e.getY());
			} else if(o instanceof SamplerTreeModel.SendEffectChainTreeNode) {
				if(inChainMenu == null) inChainMenu = new InSendEffectChainMenu();
				inChainMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
		
		private static InSendEffectChainDirMenu inChainDirMenu = null;
		private static InSendEffectChainMenu inChainMenu = null;
	}
	
	private static class InSendEffectChainDirMenu extends JPopupMenu {
		public
		InSendEffectChainDirMenu() {
			JMenuItem mi = new JMenuItem(new AddSendEffectChainAction());
			add(mi);
		}
	}
	
	private static class InSendEffectChainMenu extends JPopupMenu {
		public
		InSendEffectChainMenu() {
			JMenuItem mi = new JMenuItem(new AddSendEffectInstsAction());
			add(mi);
		}
	}
	
	public static interface ContextMenuOwner {
		public Object getSelectedItem();
		public Object getSelectedParent();
	}
}
