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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.grigoriliev.jsampler.swing.view.OrchestraTable;
import com.grigoriliev.jsampler.swing.view.OrchestraTableModel;
import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.OrchestraModel;

/**
 *
 * @author Grigor Iliev
 */
public class JSManageOrchestrasPane extends JPanel {
	protected final OrchestraTable orchestraTable;
	
	protected final Action actionAddOrchestra = new AddOrchestraAction();
	protected final Action actionEditOrchestra = new EditOrchestraAction();
	protected final Action actionDeleteOrchestra = new DeleteOrchestraAction();
	protected final Action actionOrchestraUp = new OrchestraUpAction();
	protected final Action actionOrchestraDown = new OrchestraDownAction();
	
	/** Creates a new instance of <code>JSManageOrchestrasPane</code> */
	public
	JSManageOrchestrasPane() {
		setLayout(new BorderLayout());
		orchestraTable = new OrchestraTable(new OrchestraTableModel(CC.getOrchestras()));
		JScrollPane sp = new JScrollPane(orchestraTable);
		add(sp);
		
		installListeneres();
	}
	
	private void
	installListeneres() {
		OrchestraSelectionHandler l = new OrchestraSelectionHandler();
		orchestraTable.getSelectionModel().addListSelectionListener(l);
		
		orchestraTable.addMouseListener(new MouseAdapter() {
			public void
			mouseClicked(MouseEvent e) {
				if(e.getClickCount() < 2) return;
				
				if(orchestraTable.getSelectedOrchestra() == null) return;
				editOrchestra(orchestraTable.getSelectedOrchestra());
			}
		});
		
		KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		orchestraTable.getInputMap(JComponent.WHEN_FOCUSED).put(k, "deleteOrchestra");
		orchestraTable.getActionMap().put("deleteOrchestra", actionDeleteOrchestra);
	}
	
	/**
	 * Invoked when the user initiates the creation of new orchestra.
	 * @return The model of the orchestra to add
	 * or <code>null</code> if the user cancelled the task.
	 */
	public OrchestraModel
	createOrchestra() {
		JSAddOrEditOrchestraDlg dlg = new JSAddOrEditOrchestraDlg();
		dlg.setVisible(true);
		
		if(dlg.isCancelled()) return null;
		
		return dlg.getOrchestra();
	}
	
	public void
	editOrchestra(OrchestraModel model) {
		JSAddOrEditOrchestraDlg dlg = new JSAddOrEditOrchestraDlg(model);
		dlg.setVisible(true);
	}
	
	private class OrchestraSelectionHandler implements ListSelectionListener {
		public void
		valueChanged(ListSelectionEvent e) {
			if(e.getValueIsAdjusting()) return;
			
			if(orchestraTable.getSelectedOrchestra() == null) {
				actionEditOrchestra.setEnabled(false);
				actionDeleteOrchestra.setEnabled(false);
				actionOrchestraUp.setEnabled(false);
				actionOrchestraDown.setEnabled(false);
				return;
			}
			
			actionEditOrchestra.setEnabled(true);
			actionDeleteOrchestra.setEnabled(true);
			
			OrchestraModel orchestraModel = orchestraTable.getSelectedOrchestra();
			int idx = orchestraTable.getSelectedRow();
			actionOrchestraUp.setEnabled(idx != 0);
			actionOrchestraDown.setEnabled(idx != orchestraTable.getRowCount() - 1);
		}
	}
	
	private class AddOrchestraAction extends AbstractAction {
		AddOrchestraAction() {
			super("");
			
			String s = StdI18n.i18n.getLabel("JSManageOrchestrasPane.ttAddOrchestra");
			putValue(SHORT_DESCRIPTION, s);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			OrchestraModel newOrch = createOrchestra();
			if(newOrch == null) return;
			
			OrchestraModel om = orchestraTable.getSelectedOrchestra();
			int idx = CC.getOrchestras().getOrchestraIndex(om);
			if(idx < 0) CC.getOrchestras().addOrchestra(newOrch);
			else CC.getOrchestras().insertOrchestra(newOrch, idx);
			
			orchestraTable.setSelectedOrchestra(newOrch);
		}
	}
	
	private class EditOrchestraAction extends AbstractAction {
		EditOrchestraAction() {
			super("");
			
			String s = StdI18n.i18n.getLabel("JSManageOrchestrasPane.ttEditOrchestra");
			putValue(SHORT_DESCRIPTION, s);
			
			setEnabled(false);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			editOrchestra(orchestraTable.getSelectedOrchestra());
		}
	}
	
	private class DeleteOrchestraAction extends AbstractAction {
		DeleteOrchestraAction() {
			super("");
			
			String s = StdI18n.i18n.getLabel("JSManageOrchestrasPane.ttDeleteOrchestra");
			putValue(SHORT_DESCRIPTION, s);
			
			setEnabled(false);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			OrchestraModel om = orchestraTable.getSelectedOrchestra();
			if(om == null) return;
			if(om.getInstrumentCount() > 0) {
				String s;
				s = StdI18n.i18n.getMessage("JSManageOrchestrasPane.removeOrchestra?");
				if(!SHF.showYesNoDialog(SHF.getMainFrame(), s)) return;
			}
			
			int i = orchestraTable.getSelectedRow();
			CC.getOrchestras().removeOrchestra(om);
			if(orchestraTable.getRowCount() > i) {
				orchestraTable.getSelectionModel().setSelectionInterval(i, i);
			}
		}
	}
	
	private class OrchestraUpAction extends AbstractAction {
		OrchestraUpAction() {
			super("");
			
			String s = StdI18n.i18n.getLabel("JSManageOrchestrasPane.ttOrchestraUp");
			putValue(SHORT_DESCRIPTION, s);
			
			setEnabled(false);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			OrchestraModel om = orchestraTable.getSelectedOrchestra();
			CC.getOrchestras().moveOrchestraUp(om);
			orchestraTable.setSelectedOrchestra(om);
		}
	}
	
	private class OrchestraDownAction extends AbstractAction {
		OrchestraDownAction() {
			super("");
			
			String s = StdI18n.i18n.getLabel("JSManageOrchestrasPane.ttOrchestraDown");
			putValue(SHORT_DESCRIPTION, s);
			
			setEnabled(false);
		}
		
		public void
		actionPerformed(ActionEvent e) {
			OrchestraModel om = orchestraTable.getSelectedOrchestra();
			CC.getOrchestras().moveOrchestraDown(om);
			orchestraTable.setSelectedOrchestra(om);
		}
	}
}
