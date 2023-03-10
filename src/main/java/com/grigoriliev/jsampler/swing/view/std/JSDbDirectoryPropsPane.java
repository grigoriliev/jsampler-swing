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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import com.grigoriliev.jsampler.juife.Task;

import com.grigoriliev.jsampler.juife.event.TaskEvent;
import com.grigoriliev.jsampler.juife.event.TaskListener;

import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.task.InstrumentsDb;

import com.grigoriliev.jsampler.jlscp.DbDirectoryInfo;

import static com.grigoriliev.jsampler.swing.view.std.StdI18n.i18n;


/**
 *
 * @author Grigor Iliev
 */
public class JSDbDirectoryPropsPane extends JPanel {
	private final JLabel lName = createLabel(i18n.getLabel("JSDbDirectoryPropsPane.lName"));
	private final JLabel lType = createLabel(i18n.getLabel("JSDbDirectoryPropsPane.lType"));
	private final JLabel lLocation = createLabel(i18n.getLabel("JSDbDirectoryPropsPane.lLocation"));
	private final JLabel lContains = createLabel(i18n.getLabel("JSDbDirectoryPropsPane.lContains"));
	private final JLabel lCreated = createLabel(i18n.getLabel("JSDbDirectoryPropsPane.lCreated"));
	private final JLabel lModified = createLabel(i18n.getLabel("JSDbDirectoryPropsPane.lModified"));
	private final JLabel lDesc = createLabel(i18n.getLabel("JSDbDirectoryPropsPane.lDesc"));
	
	private final JTextArea taName = createTextArea();
	private final JTextArea taType = createTextArea();
	private final JTextArea taLocation = createTextArea();
	private final JTextArea taContains = createTextArea();
	private final JTextArea taCreated = createTextArea();
	private final JTextArea taModified = createTextArea();
	private final JTextArea taDesc = createTextArea();
	
	private DbDirectoryInfo directoryInfo;
	
	
	/**
	 * Creates a new instance of <code>JSDbDirectoryPropsPane</code>
	 */
	public
	JSDbDirectoryPropsPane(DbDirectoryInfo dirInfo) {
		setDirectoryInfo(dirInfo);
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		setLayout(gridbag);
		
		c.fill = GridBagConstraints.NONE;
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(3, 3, 3, 3);
		gridbag.setConstraints(lName, c);
		add(lName);
		
		c.gridx = 0;
		c.gridy = 2;
		gridbag.setConstraints(lType, c);
		add(lType);
		
		c.gridx = 0;
		c.gridy = 3;
		gridbag.setConstraints(lLocation, c);
		add(lLocation);
		
		c.gridx = 0;
		c.gridy = 4;
		gridbag.setConstraints(lContains, c);
		add(lContains);
		
		c.gridx = 0;
		c.gridy = 6;
		gridbag.setConstraints(lCreated, c);
		add(lCreated);
		
		c.gridx = 0;
		c.gridy = 7;
		gridbag.setConstraints(lModified, c);
		add(lModified);
		
		c.gridx = 0;
		c.gridy = 9;
		gridbag.setConstraints(lDesc, c);
		add(lDesc);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		gridbag.setConstraints(taName, c);
		add(taName);
			
		c.gridx = 1;
		c.gridy = 2;
		gridbag.setConstraints(taType, c);
		add(taType);
			
		c.gridx = 1;
		c.gridy = 3;
		gridbag.setConstraints(taLocation, c);
		add(taLocation);
			
		c.gridx = 1;
		c.gridy = 4;
		gridbag.setConstraints(taContains, c);
		add(taContains);
			
		c.gridx = 1;
		c.gridy = 6;
		gridbag.setConstraints(taCreated, c);
		add(taCreated);
			
		c.gridx = 1;
		c.gridy = 7;
		gridbag.setConstraints(taModified, c);
		add(taModified);
			
		c.gridx = 1;
		c.gridy = 9;
		gridbag.setConstraints(taDesc, c);
		add(taDesc);
		
		JSeparator sep = new JSeparator();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		gridbag.setConstraints(sep, c);
		add(sep);
		
		sep = new JSeparator();
		c.gridx = 0;
		c.gridy = 5;
		gridbag.setConstraints(sep, c);
		add(sep);
		
		sep = new JSeparator();
		c.gridx = 0;
		c.gridy = 8;
		gridbag.setConstraints(sep, c);
		add(sep);
		
		JPanel p = new JPanel();
		p.setOpaque(false);
		c.gridx = 0;
		c.gridy = 10;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		gridbag.setConstraints(p, c);
		add(p);
		
		Dimension d = getPreferredSize();
		int w = d.width > 300 ? d.width : 300;
		int h = d.height > 300 ? d.height : 300;
		setPreferredSize(new Dimension(w, h));
	}
	
	public DbDirectoryInfo
	getDirectoryInfo() { return directoryInfo; }
	
	public void
	setDirectoryInfo(DbDirectoryInfo dirInfo) {
		directoryInfo = dirInfo;
		
		taName.setText(dirInfo.getName());
		taType.setText(i18n.getLabel("JSDbDirectoryPropsPane.folder"));
		taLocation.setText(dirInfo.getParentDirectoryPath());
		taCreated.setText(dirInfo.getDateCreated().toString());
		taModified.setText(dirInfo.getDateModified().toString());
		taDesc.setText(dirInfo.getDescription());
		
		updateContentInfo();
	}
	
	private void
	updateContentInfo() {
		taContains.setText(i18n.getLabel("JSDbDirectoryPropsPane.calc"));
		
		final Task<Integer> t1, t2;
		t1 = new InstrumentsDb.GetInstrumentCount(directoryInfo.getDirectoryPath(), true);
		t2 = new InstrumentsDb.GetDirectoryCount(directoryInfo.getDirectoryPath(), true);
		
		t1.addTaskListener(new TaskListener() {
			public void
			taskPerformed(TaskEvent e) {
				if(t1.doneWithErrors()) {
					String s = i18n.getLabel("JSDbDirectoryPropsPane.unknown");
					taContains.setText(s);
					return;
				}
				
				CC.getTaskQueue().add(t2);
			}
		});
		
		t2.addTaskListener(new TaskListener() {
			public void
			taskPerformed(TaskEvent e) {
				if(t2.doneWithErrors()) {
					String s = i18n.getLabel("JSDbDirectoryPropsPane.unknown");
					taContains.setText(s);
					return;
				}
				
				int ic = t1.getResult();
				int dc = t2.getResult();
				String s = i18n.getLabel("JSDbDirectoryPropsPane.contains", ic, dc);
				taContains.setText(s);
			}
		});
		
		CC.getTaskQueue().add(t1);
	}
	
	protected JLabel
	createLabel(String text) {
		JLabel l = new JLabel(text);
		l.setFont(l.getFont().deriveFont(java.awt.Font.BOLD));
		return l;
	}
	
	protected JTextArea
	createTextArea() { return new TextArea(); }
	
	private class
	TextArea extends JTextArea {
		TextArea() {
			setLineWrap(true);
			setEditable(false);
			setOpaque(false);
			putClientProperty("substancelaf.noExtraElements", Boolean.TRUE);
			setBorder(BorderFactory.createEmptyBorder());
		}
	}
}
