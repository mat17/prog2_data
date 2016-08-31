import javax.swing.JLabel;
import javax.swing.JTextField;

public class textFieldCst {
	private JTextField txtField;
	private JLabel txtLabel;
	private String label;
	
	public JTextField getTextField(){
		return this.txtField;
	}
	
	public void setTextField(JTextField t){
		this.txtField = t;
	}
	
	public JLabel getJLabel(){
		return this.txtLabel;
	}
	
	public void setJLabel(JLabel l){
		this.txtLabel = l;
	}
	
	public String getLabel(){
		return this.label;
	}
	public void setLabel(String l){
		this.label = l;
	}
	
}
