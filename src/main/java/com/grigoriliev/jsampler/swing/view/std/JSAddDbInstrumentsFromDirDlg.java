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

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.grigoriliev.jsampler.juife.swing.OkCancelDialog;
import com.grigoriliev.jsampler.juife.Task;

import com.grigoriliev.jsampler.juife.event.TaskEvent;
import com.grigoriliev.jsampler.juife.event.TaskListener;

import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.JSPrefs;
import com.grigoriliev.jsampler.task.InstrumentsDb;

import static com.grigoriliev.jsampler.jlscp.Parser.*;

/**
 *
 * @author Grigor Iliev
 */
public class JSAddDbInstrumentsFromDirDlg extends OkCancelDialog {
	private final JComboBox cbSource = StdUtils.createPathComboBox();
	private JButton btnBrowse;
	private final JCheckBox checkScanSubdirs =
		new JCheckBox(StdI18n.i18n.getLabel("JSAddDbInstrumentsFromDirDlg.checkScanSubdirs"));
	private final JCheckBox checkFlat =
		new JCheckBox(StdI18n.i18n.getLabel("JSAddDbInstrumentsFromDirDlg.checkFlat"));
	private final JCheckBox checkInstrDirs =
		new JCheckBox(StdI18n.i18n.getLabel("JSAddDbInstrumentsFromDirDlg.checkInstrDirs"));
	
	private final JComboBox cbDest = StdUtils.createPathComboBox();
	private JButton btnBrowseDb;
	
	/**
	 * Creates a new instance of <code>AddDbInstrumentsFromFileDlg</code>
	 */
	public
	JSAddDbInstrumentsFromDirDlg(Frame owner, String dbDir, Icon iconBrowse) {
		super(owner, StdI18n.i18n.getLabel("JSAddDbInstrumentsFromDirDlg.title"));
		
		initAddDbInstrumentsFromDirDlg(dbDir, iconBrowse);
	}
	
	/**
	 * Creates a new instance of <code>AddDbInstrumentsFromFileDlg</code>
	 */
	public
	JSAddDbInstrumentsFromDirDlg(Dialog owner, String dbDir, Icon iconBrowse) {
		super(owner, StdI18n.i18n.getLabel("JSAddDbInstrumentsFromDirDlg.title"));
		
		initAddDbInstrumentsFromDirDlg(dbDir, iconBrowse);
	}
	
	private void
	initAddDbInstrumentsFromDirDlg(String dbDir, Icon iconBrowse) {
		btnBrowse = new JButton(iconBrowse);
		btnBrowseDb = new JButton(iconBrowse);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		JPanel p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
		p2.add(cbSource);
		p2.add(Box.createRigidArea(new Dimension(6, 0)));
		btnBrowse.setToolTipText(StdI18n.i18n.getButtonLabel("browse"));
		btnBrowse.setMargin(new Insets(0, 0, 0, 0));
		p2.add(btnBrowse);
		p2.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		p2.setMaximumSize(new Dimension(Short.MAX_VALUE, p2.getPreferredSize().height));
		p2.setAlignmentX(LEFT_ALIGNMENT);
		
		p.add(p2);
		
		p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
		p2.add(checkScanSubdirs);
		p2.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		p2.setAlignmentX(LEFT_ALIGNMENT);
		p.add(p2);
		
		p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
		p2.add(checkFlat);
		p2.setBorder(BorderFactory.createEmptyBorder(0, 15, 3, 3));
		p2.setAlignmentX(LEFT_ALIGNMENT);
		
		p.add(p2);

		p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
		p2.add(checkInstrDirs);
		p2.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		p2.setAlignmentX(LEFT_ALIGNMENT);
		p.add(p2);
		
		String s = StdI18n.i18n.getLabel("JSAddDbInstrumentsFromDirDlg.source");
		p.setBorder(BorderFactory.createTitledBorder(s));
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		
		mainPane.add(p);
		mainPane.add(Box.createRigidArea(new Dimension(0, 6)));
		
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
		p2.add(cbDest);
		p2.add(Box.createRigidArea(new Dimension(6, 0)));
		btnBrowseDb.setToolTipText(StdI18n.i18n.getButtonLabel("browse"));
		btnBrowseDb.setMargin(new Insets(0, 0, 0, 0));
		p2.add(btnBrowseDb);
		p2.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));
		p2.setMaximumSize(new Dimension(Short.MAX_VALUE, p2.getPreferredSize().height));
		p2.setAlignmentX(LEFT_ALIGNMENT);
		
		p.add(p2);
		
		s = StdI18n.i18n.getLabel("JSAddDbInstrumentsFromDirDlg.dest");
		p.setBorder(BorderFactory.createTitledBorder(s));
		
		mainPane.add(p);
		Dimension d = mainPane.getPreferredSize();
		mainPane.setPreferredSize(new Dimension(d.width > 300 ? d.width : 300, d.height));
				
		setMainPane(mainPane);
		
		setMinimumSize(this.getPreferredSize());
		setResizable(true);
		
		btnOk.setEnabled(false);
		checkScanSubdirs.doClick(0);
		checkFlat.doClick(0);
		
		String[] dirs = preferences().getStringListProperty("recentDirectories");
		for(String dir : dirs) cbSource.addItem(dir);
		cbSource.setSelectedItem(null);
		
		cbSource.setPreferredSize (
			new Dimension(200, cbSource.getPreferredSize().height)
		);
		
		cbSource.addActionListener(getHandler());
		
		dirs = preferences().getStringListProperty("recentDbDirectories");
		for(String dir : dirs) cbDest.addItem(dir);
		cbDest.setSelectedItem(dbDir);
		
		
		cbDest.setPreferredSize (
			new Dimension(200, cbDest.getPreferredSize().height)
		);
		
		cbDest.addActionListener(getHandler());
		
		btnBrowse.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { onBrowse(); }
		});
		
		checkScanSubdirs.addItemListener(getHandler());
		
		btnBrowseDb.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { onBrowseDb(); }
		});
	}
	
	protected JComboBox
	createComboBox() { return new JComboBox(); }
	
	protected JSPrefs
	preferences() { return CC.getViewConfig().preferences(); }
	
	private void
	onBrowse() {
		File f = StdUtils.showOpenDirectoryChooser(this, "lastInstrumentLocation");
		if(f == null) return;
		String path = f.getAbsolutePath();
		if(java.io.File.separatorChar == '\\') {
			path = path.replace('\\', '/');
		}
		path = toEscapedString(path);
		cbSource.setSelectedItem(path);
	}
	
	private void
	onBrowseDb() {
		JSDbDirectoryChooser dlg;
		dlg = new JSDbDirectoryChooser(JSAddDbInstrumentsFromDirDlg.this);
		Object o = cbDest.getSelectedItem();
		if(o != null && o.toString().length() > 0) dlg.setSelectedDirectory(o.toString());
		dlg.setVisible(true);
		if(dlg.isCancelled()) return;
		cbDest.setSelectedItem(dlg.getSelectedDirectory());
	}
	
	private void
	updateState() {
		Object o = cbSource.getSelectedItem();
		Object o2 = cbDest.getSelectedItem();
		boolean b = o != null && o.toString().length() > 0;
		b = b && o2 != null && o2.toString().length() > 0;
		btnOk.setEnabled(b);
	}
	
	@Override
	protected void
	onOk() {
		if(!btnOk.isEnabled()) return;
		
		btnOk.setEnabled(false);
		String dbDir = cbDest.getSelectedItem().toString();
		String fsDir = cbSource.getSelectedItem().toString();
		boolean recursive = checkScanSubdirs.isSelected();
		boolean flat = !checkFlat.isSelected();
		boolean insDir = checkInstrDirs.isSelected();
		if(recursive) runTask(new InstrumentsDb.AddInstruments(dbDir, fsDir, flat, insDir));
		else runTask(new InstrumentsDb.AddInstrumentsNonrecursive(dbDir, fsDir, insDir));
		
		StdUtils.updateRecentElements("recentDirectories", fsDir);
		StdUtils.updateRecentElements("recentDbDirectories", dbDir);
	}
	
	@Override
	protected void
	onCancel() { setVisible(false); }
	
	private void
	runTask(final Task<Integer> t) {
		t.addTaskListener(new TaskListener() {
			public void
			taskPerformed(TaskEvent e) {
				updateState();
				if(t.doneWithErrors()) return;
				showProgress(t.getResult());
			}
		});
		
		CC.getTaskQueue().add(t);
	}
	
	private void
	showProgress(int jobId) {
		final JSAddDbInstrumentsProgressDlg dlg;
		dlg = new JSAddDbInstrumentsProgressDlg(this, jobId);
		SwingUtilities.invokeLater(new Runnable() {
			public void
			run() { dlg.setVisible(true); dlg.updateStatus(); }
		});
	}
	
	private final Handler eventHandler = new Handler();
	
	private Handler
	getHandler() { return eventHandler; }
	
	private class Handler implements DocumentListener, ActionListener, ItemListener {
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
		///////
		
		@Override
		public void
		actionPerformed(ActionEvent e) { updateState(); }
		
		@Override
		public void
		itemStateChanged(ItemEvent e) {
			checkFlat.setEnabled(checkScanSubdirs.isSelected());
		}
	}
}
