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

import javax.swing.table.AbstractTableModel;

import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.MidiInstrumentMap;
import com.grigoriliev.jsampler.SamplerModel;

import com.grigoriliev.jsampler.event.ListEvent;
import com.grigoriliev.jsampler.event.ListListener;
import com.grigoriliev.jsampler.event.MidiInstrumentMapEvent;
import com.grigoriliev.jsampler.event.MidiInstrumentMapListener;

import static com.grigoriliev.jsampler.JSI18n.i18n;

/**
 * A tabular data model for representing MIDI instrument maps.
 * @author Grigor Iliev
 */
public class MidiMapTableModel extends AbstractTableModel {
	private final MidiMapTable table;
	/**
	 * Creates a new instance of <code>MidiMapTableModel</code>.
	 */
	public
	MidiMapTableModel(MidiMapTable table) {
		this.table = table;
		SamplerModel sm = CC.getSamplerModel();
		
		for(int i = 0; i < sm.getMidiInstrumentMapCount(); i++) {
			sm.getMidiInstrumentMap(i).addMidiInstrumentMapListener(getHandler());
		}
		
		sm.addMidiInstrumentMapListListener(getHandler());
		
	}
	
	/**
	 * Gets the number of columns in the model.
	 * @return The number of columns in the model.
	 */
	@Override
	public int
	getColumnCount() { return 1; }
	
	/**
	 * Gets the number of rows in the model.
	 * @return The number of rows in the model.
	 */
	@Override
	public int
	getRowCount() { return CC.getSamplerModel().getMidiInstrumentMapCount(); }
	
	/**
	 * Gets the name of the column at <code>columnIndex</code>.
	 * @return The name of the column at <code>columnIndex</code>.
	 */
	@Override
	public String
	getColumnName(int col) { return i18n.getLabel("MidiMapTableModel.title"); }
	
	/**
	 * Gets the value for the cell at <code>columnIndex</code> and
	 * <code>rowIndex</code>.
	 * @param row The row whose value is to be queried.
	 * @param col The column whose value is to be queried.
	 * @return The value for the cell at <code>columnIndex</code> and
	 * <code>rowIndex</code>.
	 */
	@Override
	public Object
	getValueAt(int row, int col) {
		return CC.getSamplerModel().getMidiInstrumentMap(row);
	}
	
	/**
	 * Sets the value in the cell at <code>col</code>
	 * and <code>row</code> to <code>value</code>.
	 */
	@Override
	public void
	setValueAt(Object value, int row, int col) {
		
		fireTableCellUpdated(row,  col);
	}
	
	/**
	 * Returns <code>true</code> if the cell at
	 * <code>row</code> and <code>col</code> is editable.
	 */
	@Override
	public boolean
	isCellEditable(int row, int col) { return false; }
	
	
	private final Handler eventHandler = new Handler();
	
	private Handler
	getHandler() { return eventHandler; }
	
	private class Handler implements ListListener<MidiInstrumentMap>, MidiInstrumentMapListener {
		/** Invoked when an orchestra is added to the orchestra list. */
		@Override
		public void
		entryAdded(ListEvent<MidiInstrumentMap> e) {
			e.getEntry().addMidiInstrumentMapListener(getHandler());
			fireTableDataChanged();
			table.setSelectedMidiInstrumentMap(e.getEntry());
		}
	
		/** Invoked when an orchestra is removed from the orchestra list. */
		@Override
		public void
		entryRemoved(ListEvent<MidiInstrumentMap> e) {
			e.getEntry().removeMidiInstrumentMapListener(getHandler());
			fireTableDataChanged();
		}
		
		@Override
		public void
		nameChanged(MidiInstrumentMapEvent e) {
			MidiInstrumentMap m = (MidiInstrumentMap)e.getSource();
			int idx = CC.getSamplerModel().getMidiInstrumentMapIndex(m);
			fireTableRowsUpdated(idx, idx);
		}
		
		@Override
		public void
		instrumentAdded(MidiInstrumentMapEvent e) { }
		
		@Override
		public void
		instrumentRemoved(MidiInstrumentMapEvent e) { }
	}
}
