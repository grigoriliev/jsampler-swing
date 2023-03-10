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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import com.grigoriliev.jsampler.juife.swing.OkCancelDialog;
import com.grigoriliev.jsampler.swing.view.SHF;
import com.grigoriliev.jsampler.jlscp.EffectParameter;
import com.grigoriliev.jsampler.jlscp.FloatParameter;

/**
 *
 * @author Grigor IlievJSSetParameterDlg
 */
public class JSSetParameterDlg extends OkCancelDialog {
	private FloatValuePanel pane;
	
	public
	JSSetParameterDlg(EffectParameter param) {
		this(SHF.getMainFrame(), param);
	}
	
	public
	JSSetParameterDlg(JFrame owner, EffectParameter param) {
		super(owner);
		pane = new FloatValuePanel(param, param.getDescription());
		
		if(pane.spValue != null) pane.spValue.requestFocusInWindow();
		
		setMainPane(pane);
	}
	
	protected void
	onOk() {
		if(!btnOk.isEnabled()) return;
		
		setVisible(false);
		setCancelled(false);
	}
	
	protected void
	onCancel() { setVisible(false); }
	
	public float
	getNewValue() { return pane.getValue(); }
	
	public static class FloatValuePanel extends JPanel {
		private final FloatParameter param;
		private final JLabel lName = new JLabel();
		
		private JComboBox cbValue = null;
		private JSpinner spValue = null;
		
		public FloatValuePanel(FloatParameter p, String name) {
			this.param = p;
			lName.setText(name);
			
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(lName);
			add(Box.createRigidArea(new Dimension(6, 0)));
			
			// TODO: uncomment when fixed on back-end
			/*if(param.hasPossibilities()) {
				cbValue = new JComboBox();
				
				for(Float f : param.getPossibilities()) {
					cbValue.addItem(f);
				}
				
				add(cbValue);
			} else*/ {
				double val, min, max;
				val = p.getValue().floatValue();
				min = p.hasRangeMin() ? p.getRangeMin().floatValue() : Float.MIN_VALUE;
				max = p.hasRangeMax() ? p.getRangeMax().floatValue() : Float.MAX_VALUE;
				
				spValue = new JSpinner(new SpinnerNumberModel(val, min, max, 1.0));
				int w = spValue.getPreferredSize().width;
				if(w > 200) {
					int h = spValue.getPreferredSize().height;
					spValue.setPreferredSize(new Dimension(200, h));
				}
				add(spValue);
			}
		}
		
		public float
		getValue() {
			if(cbValue != null) return (Float)cbValue.getSelectedItem();
			
			return ((Double)spValue.getValue()).floatValue();
		}
	}
}
