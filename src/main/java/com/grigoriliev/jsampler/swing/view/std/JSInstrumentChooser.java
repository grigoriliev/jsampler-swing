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
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.grigoriliev.jsampler.juife.swing.OkCancelDialog;

import com.grigoriliev.jsampler.juife.event.TaskEvent;
import com.grigoriliev.jsampler.juife.event.TaskListener;

import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.OrchestraInstrument;
import com.grigoriliev.jsampler.JSPrefs;
import com.grigoriliev.jsampler.OrchestraModel;

import com.grigoriliev.jsampler.task.Global;
import com.grigoriliev.jsampler.task.InstrumentsDb;

import com.grigoriliev.jsampler.jlscp.Instrument;

import static com.grigoriliev.jsampler.jlscp.Parser.*;

/**
 *
 * @author Grigor Iliev
 */
public class JSInstrumentChooser extends OkCancelDialog {
	private final JRadioButton rbSelectFromOrchestra =
		new JRadioButton(StdI18n.i18n.getLabel("JSInstrumentChooser.rbSelectFromOrchestra"));
	private final JRadioButton rbSelectFromDb =
		new JRadioButton(StdI18n.i18n.getLabel("JSInstrumentChooser.rbSelectFromDb"));
	private final JRadioButton rbSelectFromFile =
		new JRadioButton(StdI18n.i18n.getLabel("JSInstrumentChooser.rbSelectFromFile"));
	
	
	private final JLabel lOrchestra =
		new JLabel(StdI18n.i18n.getLabel("JSInstrumentChooser.lOrchestra"));
	
	private final JLabel lInstrument =
		new JLabel(StdI18n.i18n.getLabel("JSInstrumentChooser.lInstrument"));
	
	private final JComboBox cbOrchestras = new JComboBox();
	private final JComboBox cbInstruments = new JComboBox();
	
	
	private final JComboBox cbDbInstrument = StdUtils.createPathComboBox();
	private final JButton btnBrowseDb;
	
	private final JLabel lFilename = new JLabel(StdI18n.i18n.getLabel("JSInstrumentChooser.lFilename"));
	private final JLabel lIndex = new JLabel(StdI18n.i18n.getLabel("JSInstrumentChooser.lIndex"));
	
	private final JComboBox cbFilename = StdUtils.createPathComboBox();
	private final JComboBox cbIndex = StdUtils.createEnhancedComboBox();
	
	private final JButton btnBrowse;
	
	private String instrumentFile = null;
	private int instrumentIndex = 0;
	private String engine = null;
	
	/**
	 * Creates a new instance of JSInstrumentChooser
	 */
	public JSInstrumentChooser(Frame owner) {
		super(owner, StdI18n.i18n.getLabel("JSInstrumentChooser.title"));
		
		btnOk.setEnabled(false);
		Icon iconBrowse = SHF.getViewConfig().getInstrumentsDbTreeView().getOpenIcon();
		btnBrowseDb = new JButton(iconBrowse);
		btnBrowse = new JButton(iconBrowse);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rbSelectFromOrchestra);
		group.add(rbSelectFromDb);
		group.add(rbSelectFromFile);
		
		rbSelectFromOrchestra.addActionListener(getHandler());
		rbSelectFromDb.addActionListener(getHandler());
		rbSelectFromFile.addActionListener(getHandler());
		rbSelectFromOrchestra.doClick(0);
		
		cbOrchestras.addFocusListener(getHandler());
		cbInstruments.addFocusListener(getHandler());
		
		cbDbInstrument.addFocusListener(getHandler());
		cbDbInstrument.addActionListener(getHandler());
		cbDbInstrument.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) {
				if(!rbSelectFromDb.isSelected()) rbSelectFromDb.doClick(0);
			}
		});
		
		String[] instrs = preferences().getStringListProperty("recentDbInstruments");
		for(String s : instrs) cbDbInstrument.addItem(s);
		cbDbInstrument.setSelectedItem(null);
		
		cbFilename.addFocusListener(getHandler());
		cbFilename.addActionListener(getHandler());
		String[] files = preferences().getStringListProperty("recentInstrumentFiles");
		for(String s : files) cbFilename.addItem(s);
		cbFilename.setSelectedItem(null);
		
		cbFilename.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) {
				if(!rbSelectFromFile.isSelected()) rbSelectFromFile.doClick(0);
				updateFileInstruments();
			}
		});
		
		cbIndex.addFocusListener(getHandler());
		cbIndex.addActionListener(getHandler());
		cbFilename.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) {
				if(!rbSelectFromFile.isSelected()) rbSelectFromFile.doClick(0);
			}
		});
		
		for(int i = 0; i < 101; i++) cbIndex.addItem(i);
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		
		rbSelectFromOrchestra.setAlignmentX(LEFT_ALIGNMENT);
		mainPane.add(rbSelectFromOrchestra);
		
		JPanel orchestraPane = createOrchestraPane();
		orchestraPane.setBorder(BorderFactory.createEmptyBorder(0, 32, 17, 0));
		mainPane.add(orchestraPane);
		
		rbSelectFromDb.setAlignmentX(LEFT_ALIGNMENT);
		mainPane.add(rbSelectFromDb);
		
		JPanel dbPane = createDbPane();
		dbPane.setBorder(BorderFactory.createEmptyBorder(0, 32, 17, 0));
		mainPane.add(dbPane);
		
		rbSelectFromFile.setAlignmentX(LEFT_ALIGNMENT);
		mainPane.add(rbSelectFromFile);
		
		JPanel filePane = createFilePane();
		filePane.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
		mainPane.add(filePane);
		
		setMainPane(mainPane);
		
		if(!CC.getSamplerModel().getServerInfo().hasInstrumentsDbSupport()) {
			rbSelectFromDb.setEnabled(false);
			cbDbInstrument.setEnabled(false);
			btnBrowseDb.setEnabled(false);
		} else {
			btnBrowseDb.requestFocusInWindow();
		}
		
		int i = preferences().getIntProperty("lastUsedOrchestraIndex", 0);
		if(CC.getOrchestras().getOrchestraCount() > i) {
			cbOrchestras.setSelectedIndex(i);
			i = preferences().getIntProperty("lastUsedOrchestraInstrumentIndex", 0);
			if(cbInstruments.getItemCount() > i) cbInstruments.setSelectedIndex(i);
		}
		
		String s = preferences().getStringProperty("lastUsedInstrumentSelectionMethod");
		if("fromOrchestra".equals(s)) {
			if(!rbSelectFromOrchestra.isSelected()) rbSelectFromOrchestra.doClick(0);
			cbInstruments.requestFocusInWindow();
		} else if("fromDb".equals(s)) {
			if(!rbSelectFromDb.isSelected()) rbSelectFromDb.doClick(0);
		} else if("fromFile".equals(s)) {
			if(!rbSelectFromFile.isSelected()) rbSelectFromFile.doClick(0);
			btnBrowse.requestFocusInWindow();
		} else {
			if(!rbSelectFromOrchestra.isSelected()) rbSelectFromOrchestra.doClick(0);
		}
		
		updateState();
		
		setMinimumSize(getPreferredSize());
		setResizable(true);
	}
	
	private JPanel
	createOrchestraPane() {
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel p = new JPanel();
		
		p.setLayout(gridbag);
		
		c.fill = GridBagConstraints.NONE;
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 0, 6, 6);
		gridbag.setConstraints(lOrchestra, c);
		p.add(lOrchestra); 
		
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 6);
		gridbag.setConstraints(lInstrument, c);
		p.add(lInstrument);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.insets = new Insets(0, 0, 6, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		gridbag.setConstraints(cbOrchestras, c);
		p.add(cbOrchestras); 
		
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 0);
		gridbag.setConstraints(cbInstruments, c);
		p.add(cbInstruments);
		
		cbOrchestras.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { orchestraChanged(); }
		});
		
		cbInstruments.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { instrumentChanged(); }
		});
		
		for(int i = 0; i < CC.getOrchestras().getOrchestraCount(); i++) {
			cbOrchestras.addItem(CC.getOrchestras().getOrchestra(i));
		}
		
		p.setAlignmentX(LEFT_ALIGNMENT);
		return p;
	}
	
	protected JSPrefs
	preferences() { return CC.getViewConfig().preferences(); }
	
	private void
	orchestraChanged() {
		OrchestraModel om = (OrchestraModel)cbOrchestras.getSelectedItem();
		String s = om == null ? null : om.getDescription();
		if(s != null && s.length() == 0) s = null;
		cbOrchestras.setToolTipText(s);
		
		cbInstruments.removeAllItems();
		if(om == null || om.getInstrumentCount() == 0) {
			cbInstruments.setEnabled(false);
			return;
		}
		
		cbInstruments.setEnabled(true);
		
		for(int i = 0; i < om.getInstrumentCount(); i++) {
			cbInstruments.addItem(om.getInstrument(i));
		}
	}
	
	private void
	instrumentChanged() {
		OrchestraInstrument instr = (OrchestraInstrument)cbInstruments.getSelectedItem();
		String s = instr == null ? null : instr.getDescription();
		if(s != null && s.length() == 0) s = null;
		cbInstruments.setToolTipText(s);
		
		btnOk.setEnabled(instr != null);
	}
	
	
	
	private JPanel
	createDbPane() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(cbDbInstrument);
		p.add(Box.createRigidArea(new Dimension(6, 0)));
		btnBrowseDb.setMargin(new Insets(0, 0, 0, 0));
		p.add(btnBrowseDb);
		
		
		
		cbDbInstrument.setPreferredSize (
			new Dimension(200, cbDbInstrument.getPreferredSize().height)
		);
		
		btnBrowseDb.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { onBrowseDb(); }
		});
		
		p.setAlignmentX(LEFT_ALIGNMENT);
		
		return p;
	}
	
	private JPanel
	createFilePane() {
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel filePane = new JPanel();
		
		filePane.setLayout(gridbag);
		
		c.fill = GridBagConstraints.NONE;
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(3, 3, 3, 3);
		gridbag.setConstraints(lFilename, c);
		filePane.add(lFilename); 
		
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(lIndex, c);
		filePane.add(lIndex);
		
		btnBrowse.setMargin(new Insets(0, 0, 0, 0));
		btnBrowse.setToolTipText(StdI18n.i18n.getLabel("JSInstrumentChooser.btnBrowse"));
		c.gridx = 2;
		c.gridy = 0;
		gridbag.setConstraints(btnBrowse, c);
		filePane.add(btnBrowse);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(cbFilename, c);
		filePane.add(cbFilename);
			
		c.gridx = 1;
		c.gridy = 1;
		gridbag.setConstraints(cbIndex, c);
		filePane.add(cbIndex);
		
		cbFilename.setPreferredSize (
			new Dimension(200, cbFilename.getPreferredSize().height)
		);
		
		btnBrowse.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { onBrowse(); }
		});
		
		filePane.setAlignmentX(LEFT_ALIGNMENT);
		return filePane;
	}
	
	protected JSDbInstrumentChooser
	createDbInstrumentChooser(java.awt.Dialog owner) {
		return new JSDbInstrumentChooser(owner);
	}
	
	@Override
	protected void
	onOk() {
		if(!btnOk.isEnabled()) return;
		
		String s = "lastUsedInstrumentSelectionMethod";
		
		if(rbSelectFromOrchestra.isSelected()) {
			OrchestraInstrument instr = (OrchestraInstrument)cbInstruments.getSelectedItem();
			instrumentFile = instr.getFilePath();
			instrumentIndex = instr.getInstrumentIndex();
			engine = instr.getEngine();
			setVisible(false);
			
			int i = cbOrchestras.getSelectedIndex();
			if(i >= 0) preferences().setIntProperty("lastUsedOrchestraIndex", i);
			
			i = cbInstruments.getSelectedIndex();
			if(i >= 0) {
				preferences().setIntProperty("lastUsedOrchestraInstrumentIndex", i);
			}
			
			preferences().setStringProperty(s, "fromOrchestra");
			
			return;
		}
		
		if(rbSelectFromFile.isSelected()) {
			instrumentFile = cbFilename.getSelectedItem().toString();
			instrumentIndex = cbIndex.getSelectedIndex();
			
			StdUtils.updateRecentElements("recentInstrumentFiles", instrumentFile);
			preferences().setStringProperty(s, "fromFile");
			setVisible(false);
			return;
		}
		
		if(!rbSelectFromDb.isSelected()) return;
		
		preferences().setStringProperty(s, "fromDb");
		
		String instr = cbDbInstrument.getSelectedItem().toString();
		preferences().setStringProperty("lastUsedDbInstrument", instr);
		final InstrumentsDb.GetInstrument t = new InstrumentsDb.GetInstrument(instr);
		
		StdUtils.updateRecentElements("recentDbInstruments", instr);
		
		t.addTaskListener(new TaskListener() {
			public void
			taskPerformed(TaskEvent e) {
				updateState();
				if(t.doneWithErrors()) return;
				
				instrumentFile = t.getResult().getFilePath();
				instrumentIndex = t.getResult().getInstrumentIndex();
				engine = t.getResult().getFormatFamily(); // TODO: fix this
				setVisible(false);
			}
		});
		
		btnOk.setEnabled(false);
		CC.getTaskQueue().add(t);
	}
	
	protected void
	onCancel() { setVisible(false); }
	
	private void
	onBrowse() {
		if(!rbSelectFromFile.isSelected()) rbSelectFromFile.doClick(0);
		File f = StdUtils.showOpenInstrumentFileChooser(this);
		if(f == null) return;
		
		String path = f.getAbsolutePath();
		if(java.io.File.separatorChar == '\\') {
			path = path.replace('\\', '/');
		}
		cbFilename.setSelectedItem(toEscapedString(path));
		btnOk.requestFocusInWindow();
	}
	
	private void
	onBrowseDb() {
		if(!rbSelectFromDb.isSelected()) rbSelectFromDb.doClick(0);
		JSDbInstrumentChooser dlg;
		dlg = createDbInstrumentChooser(JSInstrumentChooser.this);
		Object o = cbDbInstrument.getSelectedItem();
		if(o != null && o.toString().length() > 0) dlg.setSelectedInstrument(o.toString());
		else {
			String s = preferences().getStringProperty("lastUsedDbInstrument", "");
			if(s.length() > 0) dlg.setSelectedInstrument(s);
			else dlg.setSelectedDirectory("/");
		}
		dlg.setVisible(true);
		if(dlg.isCancelled()) return;
		cbDbInstrument.setSelectedItem(dlg.getSelectedInstrument());
		cbDbInstrument.requestFocus();
	}
	
	/**
	 * Gets the name of the selected instrument file.
	 * @return The name of the selected instrument file.
	 */
	public String
	getInstrumentFile() { return instrumentFile; }
	
	/**
	 * Gets the index of the instrument in the instrument file.
	 * @return The index of the instrument in the instrument file.
	 */
	public int
	getInstrumentIndex() { return instrumentIndex; }
	
	public String
	getEngine() { return engine; }
	
	private void
	updateState() {
		boolean b = false;
		if(rbSelectFromOrchestra.isSelected()) {
			b = cbInstruments.getSelectedItem() != null;
		} else if(rbSelectFromDb.isSelected()) {
			Object o = cbDbInstrument.getSelectedItem();
			b = o != null && o.toString().length() > 0;
		} else if(rbSelectFromFile.isSelected()) {
			Object o = cbFilename.getSelectedItem();
			if(o == null) b = false;
			else b = o.toString().length() > 0;
			o = cbIndex.getSelectedItem();
			if(o == null || o.toString().length() == 0) b = false;
		}
		
		btnOk.setEnabled(b);
	}
	
	private void
	updateFileInstruments() {
		Object o = cbFilename.getSelectedItem();
		if(o == null) return;
		String s = o.toString();
		final Global.GetFileInstruments t = new Global.GetFileInstruments(s);
		
		t.addTaskListener(new TaskListener() {
			public void
			taskPerformed(TaskEvent e) {
				Instrument[] instrs = t.getResult();
				if(instrs == null) {
					cbIndex.removeAllItems();
					for(int i = 0; i < 101; i++) cbIndex.addItem(i);
					return;
				}
				
				cbIndex.removeAllItems();
				for(int i = 0; i < instrs.length; i++) {
					cbIndex.addItem(i + " - " + instrs[i].getName());
				}
			}
		});
		
		CC.getTaskQueue().add(t);
	}
	
	private final EventHandler eventHandler = new EventHandler();
	
	private EventHandler
	getHandler() { return eventHandler; }
	
	private class EventHandler extends FocusAdapter
				implements ActionListener, DocumentListener {
		
		@Override
		public void
		actionPerformed(ActionEvent e) {
			updateState();
		}
		
		// DocumentListener
		@Override
		public void
		insertUpdate(DocumentEvent e) { updateState(); }
		
		@Override
		public void
		removeUpdate(DocumentEvent e) { updateState(); }
		
		@Override
		public void
		changedUpdate(DocumentEvent e) { updateState(); }
		
		// FocusListener
		@Override
		public void
		focusGained(FocusEvent e) {
			Object src = e.getSource();
			if(src == cbInstruments || src == cbOrchestras) {
				if(!rbSelectFromOrchestra.isSelected()) {
					rbSelectFromOrchestra.doClick(0);
				}
			} else if(src == cbDbInstrument) {
				if(!rbSelectFromDb.isSelected()) rbSelectFromDb.doClick(0);
			} else if(src == cbFilename || src == cbIndex) {
				if(!rbSelectFromFile.isSelected()) rbSelectFromFile.doClick(0);
			}
		}
	}
}
