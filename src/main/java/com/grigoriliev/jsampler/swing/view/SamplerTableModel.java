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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.table.AbstractTableModel;
import com.grigoriliev.jsampler.AudioDeviceModel;
import com.grigoriliev.jsampler.event.ListEvent;
import com.grigoriliev.jsampler.event.ListListener;

/**
 *
 * @author Grigor Iliev
 */
public class SamplerTableModel extends AbstractTableModel {
	private SamplerTreeModel.TreeNodeBase node;
	
	
	/** Creates a new instance of <code>SamplerTableModel</code>. */
	public
	SamplerTableModel() {
		this(null);
	}
	
	/** Creates a new instance of <code>SamplerTableModel</code>. */
	public
	SamplerTableModel(SamplerTreeModel.TreeNodeBase node) {
		this.node = node;
		
	}
	
	public SamplerTreeModel.TreeNodeBase
	getNode() { return node; }
	
	public void
	setNode(SamplerTreeModel.TreeNodeBase node) {
		if(this.node != null) removeListeners(this.node);
		this.node = node;
		if(node != null) addListeners(node);
		fireTableStructureChanged();
		fireTableDataChanged();
	}
	
	private void
	addListeners(SamplerTreeModel.TreeNodeBase node) {
		node.addPropertyChangeListener(getHandler());
	}
	
	private void
	removeListeners(SamplerTreeModel.TreeNodeBase node) {
		node.addPropertyChangeListener(getHandler());
	}
	
	/**
	 * Gets the number of columns in the model.
	 * @return The number of columns in the model.
	 */
	@Override
	public int
	getColumnCount() { return node == null ? 1 : node.getColumnCount(); }
	
	/**
	 * Gets the name of the column at <code>columnIndex</code>.
	 * @return The name of the column at <code>columnIndex</code>.
	 */
	@Override
	public String
	getColumnName(int col) { return node == null ? " " : node.getColumnName(col); }
	
	/**
	 * Gets the number of rows in the model.
	 * @return The number of rows in the model.
	 */
	@Override
	public int
	getRowCount() { return node == null ? 0 : node.getRowCount(); }
	
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
		return node == null ? null : node.getValueAt(row, col);
	}
	
	public SamplerTreeModel.TreeNodeBase
	getNodeAt(int idx) {
		if(node == null) return null;
		if(idx >= node.getChildCount()) return null;
		return node.getChildAt(idx);
	}
	
	
	private final EventHandler eventHandler = new EventHandler();
	
	private EventHandler
	getHandler() { return eventHandler; }
	
	private class EventHandler implements PropertyChangeListener {
		@Override
		public void
		propertyChange(PropertyChangeEvent e) {
			if(e.getPropertyName() == "SamplerTreeModel.update") {
				fireTableDataChanged();
			}
		}
	}
	
	AudioDeviceListListener audioDeviceListListener = new AudioDeviceListListener();
	
	private class AudioDeviceListListener implements ListListener<AudioDeviceModel> {
		/** Invoked when a new entry is added to a list. */
		public void
		entryAdded(ListEvent<AudioDeviceModel> e) { fireTableDataChanged(); }
	
		/** Invoked when an entry is removed from a list. */
		public void
		entryRemoved(ListEvent<AudioDeviceModel> e) { fireTableDataChanged(); }
	}
}
