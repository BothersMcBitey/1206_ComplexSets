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
	private JList<ComplexSet> list;
	private DefaultListModel<ComplexSet> model;
	
	private File setFile;
	
	public SetStoragePanel(int height, ComplexSetViewerPanel target) {
		super(new GridBagLayout());
		this.target = target;
		
		setFile = new File("sets.txt");			
		if(!setFile.exists()){
			try {Files.createFile(setFile.toPath());} catch (IOException e) {}
		}
		
		init(height);
	}
	
	private void init(int height){
		GridBagConstraints g = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
		
		JButton save = new JButton("Save Julia Set");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveSet(target.getSet());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(save, "This set is already saved under the name " + target.getSet().getName());
				}
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
		sp.setPreferredSize(new Dimension(200, height - 20));
		g.gridx = 1;
		g.gridy = 0;
		g.gridheight = 2;
		add(sp, g);
	}
	
	private ComplexSet[] loadSets(){
		ArrayList<ComplexSet> sets = new ArrayList<>();
		
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(setFile))){
			Object o;
			o = in.readObject();
			System.out.println(o);
			while(o != null){
				sets.add((ComplexSet) o);
				o = in.readObject();
				System.out.println(o);
			}
		} catch (IOException | ClassNotFoundException e){
//			e.printStackTrace();
		}
		
		System.out.println(sets.size());
		
		return sets.toArray(new ComplexSet[0]);
	}
	
	private void saveSet(ComplexSet set) throws Exception{
		if(model.contains(set)) throw new Exception();
		
		ArrayList<String> names = new ArrayList<>();
		for(Object c : model.toArray()){
			names.add(((ComplexSet)c).getName());
		}
		
		String name = "";
		
		while(name.equals("") || names.contains(name)){
			name = JOptionPane.showInputDialog("Please enter a name for the set you wish to save: ");
		}
		
		set.setName(name);
		model.addElement(set);
		SwingUtilities.invokeLater(new Runnable(){public void run(){revalidate();}});

		writeSetsToFile();
	}
	
	private void writeSetsToFile(){
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(setFile))){
			for(Object set : model.toArray()){
				ComplexSet s = (ComplexSet) set;
				System.out.println(s.getName());
				out.writeObject(s);
			}
			out.flush();
			out.close();
		} catch (IOException e){
//			e.printStackTrace();
		}
	}
}
