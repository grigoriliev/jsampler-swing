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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.view.JSProgress;

import com.grigoriliev.jsampler.juife.swing.JuifeUtils;


/**
 *
 * @author Grigor Iliev
 */
public class JSProgressDlg extends JDialog implements JSProgress {
	private final JPanel mainPane = new JPanel();
	private final JLabel l = new JLabel(" ");
	private JProgressBar pb  = new JProgressBar();
	private final JButton btnCancel = new JButton(StdI18n.i18n.getButtonLabel("cancel"));
	
	
	/**
	 * Creates a new instance of JSProgressDlg
	 */
	public
	JSProgressDlg() {
		super((Frame)null, "", true);
		
		pb.setIndeterminate(true);
		//pb.setStringPainted(true);
		
		l.setAlignmentX(CENTER_ALIGNMENT);
		pb.setAlignmentX(CENTER_ALIGNMENT);
		btnCancel.setAlignmentX(CENTER_ALIGNMENT);
		
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		
		mainPane.add(l);
		mainPane.add(Box.createRigidArea(new Dimension(0, 6)));
		mainPane.add(pb);
		mainPane.add(Box.createRigidArea(new Dimension(0, 17)));
		mainPane.add(btnCancel);
		
		mainPane.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		add(mainPane);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		btnCancel.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { onCancel(); }
		});
		
		Dimension d = getPreferredSize();
		d.width = d.width > 300 ? d.width : 300;
		setPreferredSize(d);
		
		pack();
		setResizable(false);
		
		setLocation(JuifeUtils.centerLocation(this, SHF.getMainFrame()));
	}
	
	private void
	onCancel() {
		int i = CC.getTaskQueue().getPendingTaskCount();
		if(i > 0) {
			String s;
			if(i == 1) s = StdI18n.i18n.getMessage("JSProgressDlg.cancel?");
			else s = StdI18n.i18n.getMessage("JSProgressDlg.cancel2?", i);
			if(!SHF.showYesNoDialog(SHF.getMainFrame(), s)) {
				CC.getTaskQueue().start();
				return;
			}
		}
		
		CC.getTaskQueue().removePendingTasks();
		com.grigoriliev.jsampler.juife.Task t = CC.getTaskQueue().getRunningTask();
		if(t != null) t.stop();
		
		setVisible(false);
	}
	
	/**
	 * Sets the progress string.
	 * @param s The value of the progress string.
	 */
	@Override
	public void
	setString(String s) { l.setText(s); }
	
	private void
	initProgressDlg() {
		pack();
		Dimension d = getPreferredSize();
		d.width = d.width > 300 ? d.width : 300;
		setSize(d);
		setResizable(false);
		
		setLocation(JuifeUtils.centerLocation(this, SHF.getMainFrame()));
	}
	
	/** Starts to indicate that an operation is ongoing. */
	@Override
	public void
	start() {
		setLocation(JuifeUtils.centerLocation(this, SHF.getMainFrame()));
		
		SwingUtilities.invokeLater(new Runnable() {
			public void
			run() {
				initProgressDlg();
				setVisible(true);
			}
		});
	}
	
	/** Stops the indication that an operation is ongoing. */
	@Override
	public void
	stop() {
		SwingUtilities.invokeLater(new Runnable() {
			public void
			run() { dispose(); }
		});
	}
}
