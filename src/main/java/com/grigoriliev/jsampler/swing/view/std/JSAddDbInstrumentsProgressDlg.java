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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.grigoriliev.jsampler.juife.swing.JuifeUtils;

import com.grigoriliev.jsampler.juife.event.TaskEvent;
import com.grigoriliev.jsampler.juife.event.TaskListener;

import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.task.InstrumentsDb.GetScanJobInfo;

import com.grigoriliev.jsampler.jlscp.ScanJobInfo;

import com.grigoriliev.jsampler.jlscp.event.InstrumentsDbAdapter;
import com.grigoriliev.jsampler.jlscp.event.InstrumentsDbEvent;

/**
 *
 * @author Grigor Iliev
 */
public class JSAddDbInstrumentsProgressDlg extends JDialog {
	private final JProgressBar progressJobStatus = new JProgressBar(0, 100);
	private final JProgressBar progressFileStatus = new JProgressBar(0, 100);
	private final JButton btnHide =
		new JButton(StdI18n.i18n.getButtonLabel("JSAddDbInstrumentsProgressDlg.btnHide"));
	
	private final int jobId;
	private boolean finished = false;
	
	/**
	 * Creates a new instance of <code>JSAddDbInstrumentsProgressDlg</code>
	 */
	public
	JSAddDbInstrumentsProgressDlg(Frame owner, int jobId) {
		super(owner, StdI18n.i18n.getLabel("JSAddDbInstrumentsProgressDlg.title"));
		this.jobId = jobId;
		
		initAddDbInstrumentsProgressDlg();
	}
	
	/**
	 * Creates a new instance of <code>JSAddDbInstrumentsProgressDlg</code>
	 */
	public
	JSAddDbInstrumentsProgressDlg(Dialog owner, int jobId) {
		super(owner, StdI18n.i18n.getLabel("JSAddDbInstrumentsProgressDlg.title"));
		this.jobId = jobId;
		
		initAddDbInstrumentsProgressDlg();
	}
	
	private void
	initAddDbInstrumentsProgressDlg() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		progressJobStatus.setAlignmentX(CENTER_ALIGNMENT);
		p.add(progressJobStatus);
		p.add(Box.createRigidArea(new Dimension(0, 6)));
		progressFileStatus.setAlignmentX(CENTER_ALIGNMENT);
		p.add(progressFileStatus);
		p.add(Box.createRigidArea(new Dimension(0, 6)));
		btnHide.setAlignmentX(CENTER_ALIGNMENT);
		p.add(btnHide);
		
		progressJobStatus.setStringPainted(true);
		progressFileStatus.setStringPainted(true);
		
		p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		add(p);
		
		Dimension d = p.getPreferredSize();
		p.setPreferredSize(new Dimension(400, d.height));
		
		pack();
		setMinimumSize(getPreferredSize());
		setLocation(JuifeUtils.centerLocation(this, getOwner()));
		
		btnHide.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) {
				// TODO: vvv this should be done out of the event-dispatching thread
				CC.getClient().removeInstrumentsDbListener(getHandler());
				//////
				setVisible(false);
			}
		});
		
		// TODO: vvv this should be done out of the event-dispatching thread
		CC.getClient().addInstrumentsDbListener(getHandler());
		//////
	}
	
	public void
	updateStatus() {
		final GetScanJobInfo t = new GetScanJobInfo(jobId);
		t.addTaskListener(new TaskListener() {
			public void
			taskPerformed(TaskEvent e) {
				if(t.doneWithErrors()) {
					failed();
					return;
				}
				
				updateStatus(t.getResult());
			}
		});
		
		CC.scheduleTask(t);
	}
		
	private void
	updateStatus(ScanJobInfo info) {
		if(info.isFinished()) {
			finished = true;
			
			// TODO: vvv this should be done out of the event-dispatching thread
			CC.getClient().removeInstrumentsDbListener(getHandler());
			//////
			
			if(info.status < 0) {
				failed();
				return;
			}
			
			progressJobStatus.setValue(progressJobStatus.getMaximum());
			progressFileStatus.setValue(progressFileStatus.getMaximum());
			
			dispose();
			getOwner().setVisible(false);
			return;
		}
		
		if(progressJobStatus.getMaximum() != info.filesTotal * 100) {
			progressJobStatus.setMaximum(info.filesTotal * 100);
		}
		
		String s = StdI18n.i18n.getMessage (
			"JSAddDbInstrumentsProgressDlg.jobStatus", info.filesScanned, info.filesTotal
		);
		progressJobStatus.setString(s);
		
		progressFileStatus.setValue(info.status);
		progressFileStatus.setString(info.scanning);
		
		progressJobStatus.setValue((info.filesScanned * 100) + info.status);
	}
	
	private void
	failed() {
		SwingUtilities.invokeLater(new Runnable() {
			public void
			run() { failed0(); }
		});
	}
	
	private void
	failed0() {
		SHF.showErrorMessage(StdI18n.i18n.getMessage("JSAddDbInstrumentsProgressDlg.failed"), this);
		setVisible(false);
	}
	
	private final EventHandler eventHandler = new EventHandler();
	
	private EventHandler
	getHandler() { return eventHandler; }
	
	private class EventHandler extends InstrumentsDbAdapter {
		/** Invoked when the status of particular job has changed. */
		public void
		jobStatusChanged(InstrumentsDbEvent e) {
			if(e.getJobId() != jobId) return;
			SwingUtilities.invokeLater(new Runnable() {
				public void
				run() { updateStatus(); }
			});
		}
	}
}
