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

package com.grigoriliev.jsampler.swing.view;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.grigoriliev.jsampler.jlscp.Parameter;
import com.grigoriliev.jsampler.jlscp.ParameterType;


/**
 * A table for representing LSCP parameters.
 * @author Grigor Iliev
 */
public class ParameterTable extends JTable {
	
	/** Creates a new instance of <code>ParameterTable</code>. */
	public
	ParameterTable() { super(new ParameterTableModel(new Parameter[0])); }
	
	/**
	 * Gets the <code>ParameterTableModel</code> that
	 * provides the data displayed by this <code>ParameterTable</code>.
	 * @return The <code>ParameterTableModel</code> that
	 * provides the data displayed by this <code>ParameterTable</code>.
	 */
	public ParameterTableModel
	getModel() { return (ParameterTableModel) super.getModel(); }
	
	/**
	 * Sets the data model for this table to <code>dataModel</code>.
	 * @param dataModel The new data source for this table.
	 */
	public void
	setModel(ParameterTableModel dataModel) { super.setModel(dataModel); }
	
	public TableCellEditor
	getCellEditor(int row, int column) {
		TableCellEditor editor = getModel().getCellEditor(row, column);
		return editor != null ? editor : super.getCellEditor(row, column);
	}
	
	public TableCellRenderer
	getCellRenderer(int row, int column) {
		TableCellRenderer r = getModel().getCellRenderer(row, column);
		if(r == null) r = super.getCellRenderer(row, column);
		
		JComponent c = (JComponent)r;
		Parameter p = getModel().getParameter(row);
		if(column == 1 && p.getStringValue().length() > 15) {
			c.setToolTipText(p.getStringValue());
		} else {
			c.setToolTipText(p.getDescription());
		}
		
		return r;
	}
}
