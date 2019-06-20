import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/*
 * Created on Tevet 5770 
 */

/**
 * @author לויאן
 */


public class MyActionListener implements ActionListener
{
	Event64 evShabbat,evWeekday,evRegl;
	
	public MyActionListener(Event64 evShabbat, Event64 evWeekday, Event64 evRegl) {
		super();
		this.evShabbat = evShabbat;
		this.evWeekday = evWeekday;
		this.evRegl = evRegl;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		JRadioButton butt=(JRadioButton)e.getSource();
		if(butt.getName().equals("16"))/////number of butt
		{
				if(butt.isSelected())
				{
					evShabbat.sendEvent();
				}
				else
				{
					evWeekday.sendEvent();
				}
		}
		else
		{
			evRegl.sendEvent(butt.getName());
		//	butt.setSelected(false);
		}
		//System.out.println(butt.getName());
		//		butt.setEnabled(false);
		//		butt.setSelected(false);
	}
	

}
