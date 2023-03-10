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

import java.io.File;
import java.io.FileOutputStream;

import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.HF;
import com.grigoriliev.jsampler.JSPrefs;


/**
 *
 * @author Grigor Iliev
 */
public class JSLscpScriptDlg extends JDialog {
	private final JTextPane textPane = new JTextPane();
	private final JButton btnSaveAs =
		new JButton(StdI18n.i18n.getButtonLabel("JSLscpScriptDlg.btnSaveAs"));
	private final JButton btnClose = new JButton(StdI18n.i18n.getButtonLabel("close"));
	
	
	/**
	 * Creates a new instance of <code>JSLscpScriptDlg</code>.
	 */
	public
	JSLscpScriptDlg() { this(SHF.getMainFrame()); }
	
	/**
	 * Creates a new instance of <code>JSLscpScriptDlg</code>.
	 */
	public
	JSLscpScriptDlg(Frame owner) {
		super(owner);
		
		JPanel mainPane = new JPanel();
		
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		mainPane.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		
		JScrollPane sp = new JScrollPane(textPane);
		sp.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
		mainPane.add(sp);
		
		mainPane.add(Box.createRigidArea(new Dimension(0, 17)));
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(btnSaveAs);
		p.add(Box.createRigidArea(new Dimension(6, 0)));
		p.add(btnClose);
		p.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
		
		mainPane.add(p);
		add(mainPane);
		
		pack();
		setSize(500, 400);
		setLocationRelativeTo(getOwner());
		
		btnSaveAs.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { saveScript(); }
		});
		
		btnClose.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { setVisible(false); }
		});
	}
	
	private JSPrefs
	preferences() { return CC.getViewConfig().preferences(); }
	
	public void
	setCommands(String[] commands) {
		StringBuffer sb = new StringBuffer();
		
		for(String s : commands) sb.append(s).append("\n");
		
		textPane.setText(sb.toString());
	}
	
	private void
	saveScript() {
		File f = StdUtils.showSaveLscpFileChooser(this);
		if(f == null) return;

		boolean b = preferences().getBoolProperty("nativeFileChoosers");
		// On Mac OS the native file chooser asks whether to replace a file
		if(f.exists() && !(CC.isMacOS() && b)) {
			String msg = StdI18n.i18n.getMessage("JSLscpScriptDlg.overwriteFile?");
			if(!SHF.showYesNoDialog(SHF.getMainFrame(), msg)) return;
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(textPane.getText().getBytes("US-ASCII"));
			fos.close();
		} catch(Exception e) {
			CC.getLogger().log(Level.FINE, HF.getErrorMessage(e), e);
			SHF.showErrorMessage(e);
		}
	}
}
