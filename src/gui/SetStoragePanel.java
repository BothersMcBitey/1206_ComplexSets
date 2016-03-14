package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import complexMaths.ComplexSet;

@SuppressWarnings("serial")
public class SetStoragePanel extends JPanel {

	private ComplexSetViewerPanel target;
	JList<ComplexSet> list;
	
	public SetStoragePanel(int height, ComplexSetViewerPanel target) {
		super(new GridBagLayout());
		this.target = target;
		init(height);
	}
	
	private void init(int height){
		GridBagConstraints g = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
		
		JButton save = new JButton("Save Julia Set");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSet(target.getSet());
			}
		});
		add(save, g);
		
		JButton load = new JButton("Load Julia Set");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				target.updateSet(list.getSelectedValue());
			}
		});
		g.gridy = 1;
		add(load, g);
		
		list = new JList<>(loadSets());
		
		JScrollPane sp = new JScrollPane(list);
		sp.setPreferredSize(new Dimension(200, height));
		g.gridx = 1;
		g.gridy = 0;
		g.gridheight = 2;
		add(sp, g);
	}
	
	private ComplexSet[] loadSets(){
		
		return new ComplexSet[] {};
	}
	
	private void saveSet(ComplexSet set){
		
	}
}
