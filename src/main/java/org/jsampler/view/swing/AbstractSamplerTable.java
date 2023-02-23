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
package org.jsampler.view.swing;

import org.jsampler.view.SamplerBrowserView;
import javax.swing.Icon;
import org.jsampler.CC;
import org.jsampler.view.swing.SamplerTreeModel.TreeNodeBase;

/**
 *
 * @author Grigor Iliev
 */
public class AbstractSamplerTable extends JSTable<SamplerTableModel> {
	//private final DefaultCellEditor nameEditor;
	
	public
	AbstractSamplerTable() {
		super(new SamplerTableModel());
		setFillsViewportHeight(true);
		getTableHeader().setReorderingAllowed(false);
		
		
	}
	
	public TreeNodeBase
	getNode() { return getModel().getNode(); }
	
	public void
	setNode(TreeNodeBase node) {
		saveColumnWidths();
		getModel().setNode(node);
		setTablePrefix(node != null ? node.getClass().getName() : null);
		loadColumnWidths();
	}
	
	public TreeNodeBase
	getSelectedNode() {
		int idx = getSelectedRow();
		if(idx == -1) return null;
		idx = convertRowIndexToModel(idx);
		return getModel().getNodeAt(idx);
	}
	
	/** Gets the view used to retrieve UI information. */
	public SamplerBrowserView<Icon>
	getView() { return CC.getViewConfig().getSamplerBrowserView(); }
}
