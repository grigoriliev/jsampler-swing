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

import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import com.grigoriliev.jsampler.juife.swing.DefaultNavigationHistoryModel;

import com.grigoriliev.jsampler.swing.view.AbstractInstrumentsDbTree;
import com.grigoriliev.jsampler.swing.view.DbDirectoryTreeNode;
import com.grigoriliev.jsampler.swing.view.InstrumentsDbTreeModel;
import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.CC;

/**
 *
 * @author Grigor Iliev
 */
public class JSInstrumentsDbTree extends AbstractInstrumentsDbTree {
	public final AbstractAction actionGoUp = new GoUp();
	public final AbstractAction actionGoBack = new GoBack();
	public final AbstractAction actionGoForward = new GoForward();
	
	private final NavigationHistoryModel navigationHistoryModel = new NavigationHistoryModel();
	
	
	/**
	 * Creates a new instance of <code>JSInstrumentsDbTree</code>.
	 */
	public
	JSInstrumentsDbTree(InstrumentsDbTreeModel model) {
		super(model);
		CellRenderer renderer = new CellRenderer();
		setCellRenderer(renderer);
		
		setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 0));
		
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		addMouseListener(new MouseAdapter() {
			public void
			mousePressed(MouseEvent e) {
				if(e.getButton() != e.BUTTON3) return;
				setSelectionPath(getClosestPathForLocation(e.getX(), e.getY()));
			}
		});
		
		ContextMenu contextMenu = new ContextMenu();
		//addMouseListener(contextMenu);
		installKeyboardListeners();
		
		SHF.getViewConfig().addInstrumentsDbChangeListener(new ChangeListener() {
			public void
			stateChanged(ChangeEvent e) {
				setModel(SHF.getInstrumentsDbTreeModel());
				
				CC.scheduleInTaskQueue(new Runnable() {
					public void
					run() {
						setSelectedDirectory("/");
						navigationHistoryModel.clearHistory();
					}
				});
			}
		});
		
		addTreeSelectionListener((GoUp)actionGoUp);
		addTreeSelectionListener(navigationHistoryModel);
	}
	
	private void
	installKeyboardListeners() {
		AbstractAction a = new AbstractAction() {
			public void
			actionPerformed(ActionEvent e) { }
		};
		a.setEnabled(false);
		getActionMap().put("none", a);

		int modKey = CC.getViewConfig().getDefaultModKey();
		
		getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put (
			KeyStroke.getKeyStroke(KeyEvent.VK_X, modKey), "none"
		);
		
		getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put (
			KeyStroke.getKeyStroke(KeyEvent.VK_V, modKey), "none"
		);
		
		getInputMap(JComponent.WHEN_FOCUSED).put (
			KeyStroke.getKeyStroke(KeyEvent.VK_X, modKey), "none"
		);
		
		getInputMap(JComponent.WHEN_FOCUSED).put (
			KeyStroke.getKeyStroke(KeyEvent.VK_V, modKey), "none"
		);
	}
	
	public NavigationHistoryModel
	getNavigationHistoryModel() { return navigationHistoryModel; }
	
	private class NavigationHistoryModel
		extends DefaultNavigationHistoryModel<DbDirectoryTreeNode>
		implements TreeSelectionListener, ActionListener {
		
		private boolean lock = false;
		
		NavigationHistoryModel() {
			addActionListener(this);
		}
		
		public DbDirectoryTreeNode
		goBack() {
			lock = true;
			DbDirectoryTreeNode node = selectDirectory(super.goBack());
			lock = false;
			return node;
		}
		
		public DbDirectoryTreeNode
		goForward() {
			lock = true;
			DbDirectoryTreeNode node = selectDirectory(super.goForward());
			lock = false;
			return node;
		}
		
		private DbDirectoryTreeNode
		selectDirectory(DbDirectoryTreeNode node) {
			if(node == null) return null;
			
			String path = node.getInfo().getDirectoryPath();
			if(SHF.getInstrumentsDbTreeModel().getNodeByPath(path) != null) {
				setSelectedDirectory(path);
				return node;
			}
			
			removePage();
			fireActionPerformed();
			String s = StdI18n.i18n.getMessage("JSInstrumentsDbTree.unknownDirectory!", path);
			SHF.showErrorMessage(s, JSInstrumentsDbTree.this);
			return node;
		}
		
		public void
		addPage(DbDirectoryTreeNode node) {
			if(lock) return;
			if(node == null) return;
			super.addPage(node);
		}
		
		public void
		valueChanged(TreeSelectionEvent e) {
			addPage(getSelectedDirectoryNode());
		}
		
		public void
		actionPerformed(ActionEvent e) {
			actionGoBack.setEnabled(hasBack());
			actionGoForward.setEnabled(hasForward());
		}
	}
	
	private class GoUp extends AbstractAction implements TreeSelectionListener {
		GoUp() {
			super(StdI18n.i18n.getMenuLabel("instrumentsdb.go.up"));
			
			String s = StdI18n.i18n.getMenuLabel("instrumentsdb.go.up.tt");
			putValue(SHORT_DESCRIPTION, s);
			putValue(Action.SMALL_ICON, CC.getViewConfig().getBasicIconSet().getUp16Icon());
			setEnabled(false);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			DbDirectoryTreeNode node = getSelectedDirectoryNode();
			if(node == null) return;
			setSelectedDirectoryNode(node.getParent());
		}
		
		public void
		valueChanged(TreeSelectionEvent e) {
			DbDirectoryTreeNode n = getSelectedDirectoryNode();
			if(n == null) {
				setEnabled(false);
				return;
			}
			
			setEnabled(n.getParent() != null);
		}
	}
	
	private class GoBack extends AbstractAction {
		GoBack() {
			super(StdI18n.i18n.getMenuLabel("instrumentsdb.go.back"));
			
			String s = StdI18n.i18n.getMenuLabel("instrumentsdb.go.back.tt");
			putValue(SHORT_DESCRIPTION, s);
			putValue(Action.SMALL_ICON, CC.getViewConfig().getBasicIconSet().getBack16Icon());
			setEnabled(false);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			navigationHistoryModel.goBack();
		}
	}
	
	private class GoForward extends AbstractAction {
		GoForward() {
			super(StdI18n.i18n.getMenuLabel("instrumentsdb.go.forward"));
			
			String s = StdI18n.i18n.getMenuLabel("instrumentsdb.go.forward.tt");
			putValue(SHORT_DESCRIPTION, s);
			putValue(Action.SMALL_ICON, CC.getViewConfig().getBasicIconSet().getForward16Icon());
			setEnabled(false);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			navigationHistoryModel.goForward();
		}
	}
	
	private class CellRenderer extends DefaultTreeCellRenderer {
		CellRenderer() {
			setOpaque(false);
		}
		
		public Component
		getTreeCellRendererComponent (
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus
		) {
			super.getTreeCellRendererComponent (
				tree, value, sel,expanded, leaf, row,hasFocus
			);
			
			DbDirectoryTreeNode node = (DbDirectoryTreeNode)value;
			if(node.getInfo().getName() == "/") setIcon(getView().getRootIcon());
			else if(leaf) setIcon(getView().getInstrumentIcon());
			else if(expanded) setIcon(getView().getOpenIcon());
			else setIcon(getView().getClosedIcon());
			
			String s = node.getInfo().getDescription();
			if(s != null && s.length() > 0) setToolTipText(s);
			else setToolTipText(null);
			
			return this;
		}
	}
	
	class ContextMenu extends MouseAdapter {
		private final JPopupMenu cmenu = new JPopupMenu();
		JMenuItem miEdit = new JMenuItem(StdI18n.i18n.getMenuLabel("ContextMenu.edit"));
		
		ContextMenu() {
			cmenu.add(miEdit);
			miEdit.addActionListener(new ActionListener() {
				public void
				actionPerformed(ActionEvent e) {
					
				}
			});
			
			JMenuItem mi = new JMenuItem(StdI18n.i18n.getMenuLabel("ContextMenu.delete"));
			cmenu.add(mi);
			mi.addActionListener(new ActionListener() {
				public void
				actionPerformed(ActionEvent e) {
					removeSelectedDirectory();
				}
			});
			
		}
		
		public void
		mousePressed(MouseEvent e) {
			if(e.isPopupTrigger()) show(e);
		}
	
		public void
		mouseReleased(MouseEvent e) {
			if(e.isPopupTrigger()) show(e);
		}
	
		void
		show(MouseEvent e) {
			cmenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
}
