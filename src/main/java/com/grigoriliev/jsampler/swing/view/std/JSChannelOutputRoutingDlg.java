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

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;

import com.grigoriliev.jsampler.juife.swing.InformationDialog;
import com.grigoriliev.jsampler.juife.swing.JuifeUtils;

import com.grigoriliev.jsampler.AudioDeviceModel;
import com.grigoriliev.jsampler.CC;
import com.grigoriliev.jsampler.task.Channel.SetAudioOutputChannel;

import com.grigoriliev.jsampler.jlscp.SamplerChannel;


/**
 *
 * @author Grigor Iliev
 */
public class JSChannelOutputRoutingDlg extends InformationDialog {
	private final ChannelRoutingTable channelRoutingTable;
	private SamplerChannel channel;
	
	
	/**
	 * Creates a new instance of JSChannelOutputRoutingDlg
	 */
	public
	JSChannelOutputRoutingDlg(Frame owner, SamplerChannel channel) {
		super(owner, StdI18n.i18n.getLabel("JSChannelOutputRoutingDlg.title"));
		this.channel = channel;
		
		channelRoutingTable = new ChannelRoutingTable();
		JScrollPane sp = new JScrollPane(channelRoutingTable);
		
		sp.setPreferredSize (
			JuifeUtils.getUnionSize(sp.getMinimumSize(), new Dimension(200, 150))
		);
		
		setMainPane(sp);
		
		
	}
	
	class ChannelRoutingTable extends JTable {
		private String[] columnToolTips = {
			StdI18n.i18n.getLabel("JSChannelOutputRoutingDlg.ttAudioIn", channel.getChannelId()),
			StdI18n.i18n.getLabel("JSChannelOutputRoutingDlg.ttAudioOut"),
		};
		
		ChannelRoutingTable() {
			super(new ChannelRoutingTableModel());
			
			JComboBox cb = new JComboBox();
			int devId = channel.getAudioOutputDevice();
			AudioDeviceModel adm = CC.getSamplerModel().getAudioDeviceById(devId);
			
			if(adm == null) {
				setEnabled(false);
			} else {
				int chns = adm.getDeviceInfo().getAudioChannelCount();
				for(Integer i = 0; i < chns; i++) cb.addItem(i);
			}
		
			TableColumn column = getColumnModel().getColumn(1);
			column.setCellEditor(new DefaultCellEditor(cb));
		}
		
		protected JTableHeader
		createDefaultTableHeader() {
			 return new JTableHeader(columnModel) {
				public String getToolTipText(java.awt.event.MouseEvent e) {
					java.awt.Point p = e.getPoint();
					int i = columnModel.getColumnIndexAtX(p.x);
					i = columnModel.getColumn(i).getModelIndex();
					return columnToolTips[i];
				}
			 };
		}
	}
	
	class ChannelRoutingTableModel extends AbstractTableModel {
		private String[] columnNames = {
			StdI18n.i18n.getLabel("JSChannelOutputRoutingDlg.audioIn"),
			StdI18n.i18n.getLabel("JSChannelOutputRoutingDlg.audioOut")
		};
		
		ChannelRoutingTableModel() {
			
		}
		
		public int
		getColumnCount() { return columnNames.length; }
		
		public String
		getColumnName(int column) { return columnNames[column]; }
		
		public int
		getRowCount() { return channel.getAudioOutputChannels(); }
		
		public Object
		getValueAt(int row, int column) {
			switch(column) {
			case 0:
				return row;
			case 1:
				return channel.getAudioOutputRouting()[row];
			default: return null;
			}
			
		}
		
		public boolean
		isCellEditable(int row, int column) {
			switch(column) {
			case 0:
				return false;
			case 1:
				return true;
			default: return false;
			}
		}
		
		public void
		setValueAt(Object value, int row, int column) {
			if(column == 0) return;
			int c = channel.getChannelId();
			int o = (Integer)getValueAt(row, 0);
			int i = (Integer)value;
			CC.getTaskQueue().add(new SetAudioOutputChannel(c, o, i));
			channel.getAudioOutputRouting()[row] = i;
			
			fireTableCellUpdated(row, column);
		}
	}
}
