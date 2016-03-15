package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import complexMaths.ComplexSet;

@SuppressWarnings("serial")
public class SetStoragePanel extends JPanel {

	private ComplexSetViewerPanel target;
	JList<ComplexSet> list;
	DefaultListModel<ComplexSet> model;
	private File setFile;
	private boolean fileExisted = true;
	ObjectOutputStream out;
	
	public SetStoragePanel(int height, ComplexSetViewerPanel target) {
		super(new GridBagLayout());
		this.target = target;
		setFile = new File("sets");
		if(!setFile.exists()){
			fileExisted = false;
			try {
				Files.createFile(setFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		init(height);
		try {
			out = new ObjectOutputStream(new FileOutputStream(setFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
		model = new DefaultListModel<ComplexSet>();
		for(ComplexSet set : loadSets()){
			model.addElement(set);
		}
		list = new JList<ComplexSet>(model);
		
		JScrollPane sp = new JScrollPane(list);
		sp.setPreferredSize(new Dimension(200, height));
		g.gridx = 1;
		g.gridy = 0;
		g.gridheight = 2;
		add(sp, g);
	}
	
	private ComplexSet[] loadSets(){
		ArrayList<ComplexSet> sets = new ArrayList<>();
 		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(setFile));
			Object o;
			while((o  = in.readObject()) != null){				
				sets.add((ComplexSet) o);
			}
		} catch (IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		return sets.toArray(new ComplexSet[1]);
	}
	
	private void saveSet(ComplexSet set){
		set.setName(JOptionPane.showInputDialog("Please enter a name for the set you wish to save: "));
		
		try{
			out.writeObject(set);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		model.addElement(set);
		SwingUtilities.invokeLater(new Runnable(){public void run(){revalidate();}});
	}
	
	@Override
	protected void finalize(){
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
