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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.grigoriliev.jsampler.juife.swing.OkCancelDialog;

import com.grigoriliev.jsampler.CC;

/**
 *
 * @author Grigor Iliev
 */
public class JSamplerHomeChooser extends OkCancelDialog {
	private final static JTextField tfHome = new JTextField();
	
	private final JButton btnBrowse = new JButton(StdI18n.i18n.getButtonLabel("browse"));
	
	
	/** Creates a new instance of JSamplerHomeChooser */
	public JSamplerHomeChooser(Frame owner) {
		super(owner, StdI18n.i18n.getLabel("JSamplerHomeChooser.title"));
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		Dimension d = new Dimension(Short.MAX_VALUE, tfHome.getPreferredSize().height);
		tfHome.setMaximumSize(d);
		p.add(tfHome);
		p.add(Box.createRigidArea(new Dimension(6, 0)));
		p.add(btnBrowse);
		
		p.setPreferredSize(new Dimension(350, p.getPreferredSize().height));
		
		setMainPane(p);
		
		if(CC.getJSamplerHome() != null) tfHome.setText(CC.getJSamplerHome());
		else tfHome.setText(System.getProperty("user.home") + File.separator + ".jsampler");
		
		btnBrowse.addActionListener(new ActionListener() {
			public void
			actionPerformed(ActionEvent e) { onBrowse(); }
		});
		
		btnBrowse.requestFocusInWindow();
		this.setResizable(true);
	}
	
	private void
	onBrowse() {
		File f = StdUtils.showOpenDirectoryChooser(this, null);
		if(f == null) return;
		
		tfHome.setText(f.getPath() + File.separator + ".jsampler");
		btnOk.requestFocusInWindow();
		
	}
	
	@Override
	protected void
	onOk() {
		if(tfHome.getText().length() == 0) {
			JOptionPane.showMessageDialog (
				this, StdI18n.i18n.getLabel("JSamplerHomeChooser.selectFile"),
				"",
				JOptionPane.INFORMATION_MESSAGE
			);
			
			return;
		}
		
		setVisible(false);
	}
	
	@Override
	protected void
	onCancel() { setVisible(false); }
	
	/**
	 * Gets the specified JSampler's home location.
	 * @return The specified JSampler's home location.
	 */
	public String
	getJSamplerHome() { return tfHome.getText(); }
}
