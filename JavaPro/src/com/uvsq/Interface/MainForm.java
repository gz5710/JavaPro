package com.uvsq.Interface;


import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.queryparser.classic.ParseException;

import com.hp.hpl.jena.rdf.model.Statement;
import com.uvsq.Jung.JUNG;
import com.uvsq.Lucene.Lucene;
import com.uvsq.RDF.RDF;
import com.uvsq.RDF.RDFManager;
import com.uvsq.SPARQL.SPARQL;

	/**
	 *
	 * @author weswes1991
	 */

public class MainForm  extends javax.swing.JFrame {

		//public static String filePath = "";
		private DefaultTableModel model = new DefaultTableModel(); 
		private Lucene lucene;
		public MainForm() {
	        initComponents();
	        lucene = new Lucene();
	    }

	    @SuppressWarnings("unchecked")
	    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	    private void initComponents() {
	        jPanel1      = new javax.swing.JPanel()	;
	        chemin_lab   = new javax.swing.JLabel();
	        chemin_txt   = new javax.swing.JTextField();
	        launch_bt    = new javax.swing.JButton();
	        search_txt   = new javax.swing.JTextField();
	        refresh_bt	 = new javax.swing.JButton(new ImageIcon(getClass().getResource("refresh.png")));
	        search_bt    = new javax.swing.JButton(new ImageIcon(getClass().getResource("search.png")));
	        jScrollPane1 = new javax.swing.JScrollPane();
	        jMenuBar1    = new javax.swing.JMenuBar();
	        jMenu1       = new javax.swing.JMenu();
	        jMenu2       = new javax.swing.JMenu();
	        maTable		 = new javax.swing.JTable(model);
	        jlabel = new  javax.swing.JLabel();
	        
	        model.addColumn("Ressource");
	        model.addColumn("Predicate");
	        model.addColumn("Object");
	        maTable.getColumnModel().getColumn(0).setPreferredWidth(0);
	        maTable.setBackground(new java.awt.Color(204, 204, 204));
	        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	        
	        jPanel1.setLayout(null);
	    
	        jlabel.setIcon(new ImageIcon(getClass().getResource("uvsq.png"))); // NOI18N     
	     
	        jlabel.setBounds(275,0,500,120);
	        
	        chemin_lab.setText("RDF Path : ");
	        chemin_lab.setBounds(5,120,70,25);
	        
	        chemin_txt.setBounds(75, 120, 470, 25);
	        chemin_txt.setToolTipText("Chemin sur dique");
	        chemin_txt.setEnabled(false);
	        
	        launch_bt.setText("Parcourir");
	        launch_bt.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	launch_btActionPerformed(evt);
	            }
	        });
	        
	        launch_bt.setBounds(545, 120, 100, 25);
	        
	        refresh_bt.setBounds(645, 120, 25, 25);
	        refresh_bt.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	refresh_btActionPerformed(evt);
	            }
	        });
	        
	        search_txt.setBounds(675, 120, 187, 25);
	       
	        search_bt.setBounds(860, 120, 28, 25);
	        search_bt.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	search_btActionPerformed(evt);
	            }
	        });
	        
	      
	        jScrollPane1.setViewportView(maTable);
	                
	        jScrollPane1.setBounds(5, 150, 885, 505);
	      
	        jMenu1.setText("File");
	        jMenuBar1.add(jMenu1);

	        jMenu2.setText("Edit");
	        jMenuBar1.add(jMenu2);
	        
	        jPanel1.add(jlabel);
	        jPanel1.add(chemin_lab);     
	        jPanel1.add(chemin_txt);
	        jPanel1.add(search_txt);
	        jPanel1.add(search_bt);
	        jPanel1.add(launch_bt);
	        jPanel1.add(refresh_bt);
	        jPanel1.add(jScrollPane1);
	        jPanel1.setBackground(Color.WHITE);
	        setJMenuBar(jMenuBar1);

	        this.setContentPane(jPanel1);
	     //  this.add(jPanel1,BorderLayout.CENTER);
	        this.setBounds(0, 0, 900, 710);
	       // this.setPreferredSize(new Dimension(900,600));
	        this.setResizable(false);
	        this.setLocationRelativeTo(null);
	        this.setTitle("ProjetJAVA");
	    
	    }// </editor-fold>                        

	    //Evennement du bouton
	    private void launch_btActionPerformed(java.awt.event.ActionEvent evt)  {
	    	cmd_browActionPerformed(evt);
	    	String file = chemin_txt.getText();
	        //filePath = file;
	    	if (!file.isEmpty()) 
	    	{
	    		RDF rdf = new RDF(file);
	    		rdfManager= new RDFManager(rdf);
	    		
	    		int result = rdfManager.readFileRDF();
	    		if(result == 1 )
	    		{
	    			while (model.getRowCount()!=0)
	        			model.removeRow(0);
	    			//succ�s
	    			try {
						lucene.index(rdfManager.getTriplets());
					} catch (IOException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    			for(Statement triplet : rdfManager.getTriplets())
	    			{
	    				model.addRow(new Object[]{triplet.getSubject().toString(), 
	    						triplet.getPredicate().getLocalName().toString(),
	    						triplet.getObject().toString()});	    					    				
	    			}	
	    		}
	    		else {}//�chec
	    	}	
	    }                                          
	    
	   
	    //Evennement du bouton
	    private void refresh_btActionPerformed(java.awt.event.ActionEvent evt)  {
	    		if(rdfManager!=null)
	    		{
	    			while (model.getRowCount()!=0)
	        			model.removeRow(0);
	    		
	    			for(Statement triplet : rdfManager.getTriplets())
	    			{
	    			model.addRow(new Object[]{triplet.getSubject().toString(),
	    					triplet.getPredicate().getLocalName().toString(),
	    					triplet.getObject().toString()});
	    			}			
	    		}
	    }            
	    
	    private void search_btActionPerformed(java.awt.event.ActionEvent evt)  
	    {
	    	try {
	    		while (model.getRowCount()!=0)
	    			model.removeRow(0);
	    		String keywords = search_txt.getText();
	    		if(!keywords.trim().equals(""))
	    		{
	    			Object[][]result = lucene.Search(keywords);
		    		for(int i=0; i<result.length;i++){
		    			model.addRow( result[i]);
		    		}	
		    		//create a simple graph
		    		//JUNG.createTree(result, "Graphe de resultat (Sans liaison)");
		    		//create a complex graph
		    		JUNG.createTreeCompl(result, "Graphe de resultat (Avec liaison)");
		    		//create a SPARQL graph
		    		//SPARQL.createTreeSPARQL(chemin_txt.getText(), keywords);    		
	    		}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    	    
	    private void cmd_browActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        JFileChooser chooser=new JFileChooser();
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("RDF", "rdf");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You chose to open this file: " + chooser.getSelectedFile().getPath());
		    }
	        File f=chooser.getSelectedFile();
	        String filename=f.getAbsolutePath();
	        chemin_txt.setText(filename);
	    } 
	    
	    // Variables declaration - do not modify                     
	    private javax.swing.JTextField chemin_txt;
	    private javax.swing.JButton launch_bt;
	    private javax.swing.JTextField search_txt;
	    private javax.swing.JButton search_bt;
	    private javax.swing.JButton refresh_bt;
	    private javax.swing.JLabel chemin_lab;
	    private javax.swing.JMenu jMenu1;
	    private javax.swing.JMenu jMenu2;
	    private javax.swing.JMenuBar jMenuBar1;
	    private javax.swing.JPanel jPanel1;
	    private javax.swing.JScrollPane jScrollPane1;
	    private javax.swing.JTable maTable;
	    private javax.swing.JLabel jlabel;
	    
	    private RDFManager rdfManager;
	    // End of variables declaration                   
		
	public static void main(String[] args) {
		//SPARQL.launchSPARQL();
		MainForm interf = new MainForm();
		interf.setVisible(true);
	}

}
