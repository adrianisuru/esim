package esim.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

import esim.electrostatic.Charge;

public class EsimControl extends JFrame implements ActionListener {
	
	public static final int DEFAULT_SCREEN_WIDTH = 320;
	public static final int DEFAULT_SCREEN_HEIGHT = 640;
	
	JCheckBox placing;

	public EsimControl(String titleControl) {
		super(titleControl);
		setSize(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocation(0,0);
		
		placing = new JCheckBox("Add/Remove Charges");
		buildSwing();
		
		setVisible(true);
	}
	
	public void buildSwing(){
		add(placing);
		placing.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		EsimDriver.setPlacing(placing.isSelected());
		
	}

	
	
}
