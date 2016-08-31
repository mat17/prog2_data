import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class CreateDynamicTable extends JFrame{
	public static CreateDynamicTable frm;
	public static String openedFileName;
    private JTable table;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnSave;
    private DefaultTableModel tableModel;
    private int uid = 0; //unique identifier
    private int counter = 1;
    private int stPoskusov = 0;
    public static ArrayList<ArrayList<String>> saveToFile = new ArrayList<ArrayList<String>>();
    private JTextField filename = new JTextField(), dir = new JTextField();
    private static int x=0;
    private boolean editable = false; //spremenljivka ki vpliva na to, ali imamo ze obstojece podatke ali ne

    //openFiles
    public static ArrayList<ArrayList<String>> words = new ArrayList<ArrayList<String>>();  //2D arrayList
	public static int countLines = 0; //stej vrstice
	public static int countDel = 0; //stej delimiter -> delimiter+1 == stevilo stolpcev
	public static int countSingleLine = 0;
	
    private CreateDynamicTable() throws InterruptedException {
        createGUI();
    }

    private void createGUI() throws InterruptedException {
    	StartScreen();
    	//ustvarimo novo datoteko
    	//CreateNewFile();
    }
    

    private JButton open = new JButton("Odpri obstojeco datoteko"), save = new JButton("Ustvari novo datoteko");
    
    public void StartScreen() {
    	JFrame frame = new JFrame("Start screen");
        final JPanel p = new JPanel();
        frame.add(p);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        open.addActionListener(new OpenL());
        p.add(open);
        p.add(save);
        final Container cp = getContentPane();
        cp.add(p, BorderLayout.CENTER);
        save.addActionListener(new ActionListener() { 
            @SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
            	p.setVisible(false);
                cp.repaint();
            	p.repaint();
            	frm.repaint();
            	CreateNewFile();
            } 
        });
        open.addActionListener(new ActionListener() { 
            @SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
            	p.setVisible(false);
                cp.repaint();
            	p.repaint();
            	frm.repaint();
            } 
        });
        p.repaint();
        cp.repaint();
      }
    
    private void CreateNewFile(){
    	String s = (String)JOptionPane.showInputDialog(table, "Prosimo, vpisite vstevilo stolpcev!", "Vpisite stevilo",JOptionPane.QUESTION_MESSAGE);
    	try{
    		//parsanje v integer. Ce ni INT, izvedi CATCH blok.
    		x = Integer.parseInt(s); //shrani stevilo stolpcev v spremenljivko x
	    }
    	catch (NumberFormatException nfe) {
    		//System.out.println("Invalid number");
    		stPoskusov++;
    		if (stPoskusov > 1){
        		JOptionPane.showMessageDialog(table, "Ponovno niste vpisali stevila. Program se bo zaprl!", "Napaka!",JOptionPane.ERROR_MESSAGE);
    			System.exit(0);
    		}
    		else{
    			JOptionPane.showMessageDialog(table, "Niste vpisali stevila. Poskusite ponovno!", "Napaka!",JOptionPane.ERROR_MESSAGE);
        		CreateNewFile();
    		}
    	}
    	//risanje
    	CreateTableGUI();
    }
     
    private void CreateTableGUI(){
    	//java not show until resize - do a resize :)
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
    	frm.setSize(new Dimension((int)width/2, (int)height/2));
        frm.setLocation(200, 100);
        frm.setVisible(true); 
        frm.repaint();
    	//zacnemo risat        
        setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane();
        pane.setVisible(true);
    	final Container cp = getContentPane();
    	cp.add(pane,BorderLayout.CENTER);
    	cp.repaint();
        table = new JTable();
        table.setVisible(true);
        pane.setViewportView(table);
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        btnAdd = new JButton("Add");
        btnRemove = new JButton("Remove");
        btnSave = new JButton("Save");
        btnSave.addActionListener(new SaveL());
        eastPanel.add(btnAdd);       
        eastPanel.add(btnRemove);       
        eastPanel.add(btnSave);
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        pane.repaint();
        eastPanel.repaint();
        northPanel.repaint();
        frm.repaint();
        
        ArrayList<textFieldCst> FieldsList = createField(x);
        Object[] LabelsList = new Object[x+1];
        LabelsList[0] = "#"; //napolni labele aka imena stolpcev
        for (int i=0; i<x; i++){
        	textFieldCst tmp = FieldsList.get(i);
        	LabelsList[i+1] = tmp.getLabel();
        	northPanel.add(tmp.getJLabel());
        	northPanel.add(tmp.getTextField());
        }


        add(northPanel, BorderLayout.NORTH);
        add(eastPanel, BorderLayout.EAST);
        add(pane,BorderLayout.CENTER);
        tableModel = new DefaultTableModel(LabelsList,0) {

            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false
               return false;
            }
        };
        table.setModel(tableModel);
        table.getColumn("#").setMaxWidth(50);
        table.setModel(tableModel);
        
        
        if (editable == true){
			//podatki
	        Object[] TmpLine = new Object[countDel+1];
			Object rowData[][] = new Object[countLines][countDel];
			System.out.println(rowData[0].length);
			System.out.println("countLines"+countLines);
			System.out.println("countDel"+countDel);
			System.out.println("WORDS: "+words);
			for (int i=0; i<countLines; i++){
				TmpLine[0] = counter;
			  	for (int j=0; j<countDel; j++){
			   		rowData[i][j] = words.get(i).get(j);
			   		System.out.println(i+"i "+j+"j "+rowData[i][j]);
			   		System.out.println(words.get(i).get(j));
			   		TmpLine[j+1] = words.get(i).get(j);
			   	}
		   		counter++;
		   		uid++;
				tableModel.addRow(TmpLine);
			}
			saveToFile = words;
			System.out.println("Save to file arrayList: "+saveToFile);
        }    
        
        
        //gumb "Add"
        btnAdd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	ArrayList<String> singleline = new ArrayList<String>();
                Object[] TextList = new Object[x+1];
                TextList[0] = counter;
                for (int i=0; i<x; i++){
                	textFieldCst tmp = FieldsList.get(i);
                	TextList[i+1] = tmp.getTextField().getText();
                	singleline.add(tmp.getTextField().getText());
                }
                tableModel.addRow(TextList);
                
                //save to arraList to save to file later
                saveToFile.add(new ArrayList<String>()); 
                saveToFile.get(uid).addAll(singleline);
                uid++;
                counter++;
                
                //izbrisi zapise v poljih za dodajanje podatkov
                for (int i=0; i<x; i++){
                	textFieldCst tmp = FieldsList.get(i);
                	tmp.getTextField().setText("");
                }
            System.out.println(saveToFile);
            singleline.clear();
            }
        });
        
        //gumb "Delete"
        btnRemove.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            	if (table.getSelectedRow() >-1){
	            	int selected = table.getSelectedRow();
	            	tableModel.removeRow(table.getSelectedRow());
	            	saveToFile.remove(selected);
	            	uid--;
            	}
            	else {
            		JOptionPane.showMessageDialog(table, "Prosimo, izberite vrstico!", "Opozorilo",JOptionPane.WARNING_MESSAGE);
            		System.out.println("Izberite vrstico");
            	}
            System.out.println(saveToFile);
            }
        });
    }
    
    public ArrayList<textFieldCst> createField(int x){
    	ArrayList<textFieldCst> fields = new ArrayList<textFieldCst>(x); 
    	for (int i=0; i<x; i++){
    		textFieldCst tmp = new textFieldCst();
    		tmp.setJLabel(new JLabel("label"+i));
    		tmp.setTextField(new JTextField(20));
    		tmp.setLabel("label"+i);
    		fields.add(tmp);
    	}
    	return fields;
    }
    
    //save AS dialog in funkcija ki shrani arrayList v datoteko
    //http://www.java2s.com/Code/Java/Swing-JFC/DemonstrationofFiledialogboxes.htm
    class SaveL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          editable = false;
          JFileChooser c = new JFileChooser();
          // Demonstrate "Save" dialog:
          int rVal = c.showSaveDialog(CreateDynamicTable.this);
          if (rVal == JFileChooser.APPROVE_OPTION) {
            filename.setText(c.getSelectedFile().getName());
            dir.setText(c.getCurrentDirectory().toString());
          }
          if (rVal == JFileChooser.CANCEL_OPTION) {
            filename.setText("You pressed cancel");
            dir.setText("");
          }
          System.out.println(filename.getText());
          System.out.println(dir.getText());
          String dirfilename = dir.getText()+"/"+filename.getText();
          
          PrintWriter writer;
          try {
				writer = new PrintWriter(dirfilename, "UTF-8");
				for(ArrayList<String> innerList : saveToFile) {
		            for(String s : innerList) {
		            	if (innerList.size()-1 == countSingleLine){
		            		writer.print(s);
		            	}
		            	else{ 
		            		writer.print(s+",");
			                System.out.print(s + "\t");
		            	}
		            	countSingleLine++;
		            }
		            writer.println();
		            System.out.println();
		            countSingleLine = 0;
		        }
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }
 
    //Open dialog
    //http://www.java2s.com/Code/Java/Swing-JFC/DemonstrationofFiledialogboxes.htm
    class OpenL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          editable = true;
          JFileChooser c = new JFileChooser();
          // Demonstrate "Open" dialog:
          int rVal = c.showOpenDialog(CreateDynamicTable.this);
          if (rVal == JFileChooser.APPROVE_OPTION) {
            filename.setText(c.getSelectedFile().getName());
            dir.setText(c.getCurrentDirectory().toString());
          }
          if (rVal == JFileChooser.CANCEL_OPTION) {
            filename.setText("You pressed cancel");
            dir.setText("");
          }
          String openedFileName = dir.getText()+"/"+filename.getText();
          
      	//preberi dokument in ga shrani v arrayList
				try {
					ReadFile(openedFileName);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	
				try {
					CheckSV(openedFileName,countLines);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            CreateTableGUI();
			//System.out.println(countLines); 
			//System.out.println(countDel);
        }
    }
    
    public static void ReadFile(String filename) throws IOException, FileNotFoundException{
		if (filename.length() < 1){
			System.out.println("Prazna datoteka");
			System.exit(1);
		}
		
		String fileName = filename;
		BufferedReader file = new BufferedReader(new FileReader(fileName));
		String line = null;
		ArrayList<String> singleline = new ArrayList<String>(); //vsaka vrstica svoj arrayList
		
		while ((line=file.readLine())!=null){ //preberi vsako vrstico
			String [] tokens = line.split(",",-1); //nastavi delimiter
			if (tokens.length > countDel){
				countDel = tokens.length; //prestej delimiterje
			}
			
			for (int i=0; i< countDel; i++){
				singleline.add(tokens[i]); //shrani besede v array -> vrstica
			}
			//System.out.println("Sideline "+count+" "+singleline);
			words.add(new ArrayList<String>()); 
			words.get(countLines).addAll(singleline); //shrani vrstico v array -> 2D tabela vrstic
			countLines++;
			singleline.clear();
		}
		System.out.println(words); //dvakratna for zanka za izpis vsega - test
		x = countDel; //stevilo stolpcev...
		file.close();
	} //end ReadFile
	
	public static void CheckSV(String filename, int len) throws IOException, FileNotFoundException{
		if (filename.length() < 1){
			System.out.println("Prazna datoteka");
			System.exit(1);
		}
		
		String fileName = filename;
		BufferedReader file = new BufferedReader(new FileReader(fileName));
		String line = null;
		int x = 0;
		int numOfDel = 0;
		int[] checkFile = new int[len];
		
		while ((line=file.readLine())!=null){ //preberi vsako vrstico
			String [] tokens = line.split(",",-1); //nastavi delimiter
			numOfDel = tokens.length; //prestej delimiterje
			checkFile[x] = numOfDel; //shrani v tabelo
			x++;
		}
		file.close();
		
		//preveri veljavnost dokumenta (stevilo vejic mora biti v vsaki vrstici enako!)
		for (int i=0; i<len; i++){
			if (checkFile[i] > 0){ //izloci prazne vrstice
				if (checkFile[0] != checkFile[i]){
					System.out.println("V vrstici "+(i+1)+" se nahaja napaka. Vrstica vsebuje "+checkFile[i]+" vejic.");
					System.out.println("Prva vrstica pa vsebuje "+checkFile[0]+" vejic.");
					System.out.println("Poskusite ponovno");
					System.exit(1);
				}
			}
			else
				System.out.println("prazna vrstica "+(i+1));
		}
		
		System.out.println("Struktura programa je pravilna. V vsaki vrstici je enako stevilo vejic.");
	}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
            	
        		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        		int width = (int)screenSize.getWidth();
        		int height = (int)screenSize.getHeight();
            	
				try {
					frm = new CreateDynamicTable();
	                frm.setLocationByPlatform(true);
	                frm.pack();
	                frm.setDefaultCloseOperation(EXIT_ON_CLOSE);
	                frm.setSize(new Dimension((int)width/5, (int)height/5));
	                frm.setLocation(200, 100);
	                frm.setVisible(true); 
	                frm.repaint();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(0);
				}
            }
        });
    }
} 