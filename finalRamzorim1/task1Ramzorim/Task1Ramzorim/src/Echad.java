import java.awt.Color;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author לויאן
 */
class Echad extends Thread
{
	Ramzor ramzor;
	JPanel panel;
	enum State {ON,OFF};//is in on or off
	State state;
	public Echad(Ramzor ramzor,JPanel panel)//constructor
	{
		this.ramzor=ramzor;
		this.panel=panel;
		start();
	}

	public void run()//start running over the statechart
	{
		state=State.ON;//start state in on
		setOn();
		try 
		{
			while (true)//the statechart is always running
			{
				switch(state) {
				  case ON: //if the current state is ON
					  sleep(2000);//trigger
				      setOff();//changes lights to off
				      state=State.OFF;//sets state to off
				      break;
				  case OFF: //if the current state is OFF
					 sleep(2000);//trigger
			         setOn();//changes lights to on
			         state=State.ON;//sets state to on
			         break;
			      default:break;
				}
			}
		} catch (InterruptedException e) {}

	}
	//change colors according to command
	private void setOff() {
		setLight(1,Color.GRAY);
	}

	private void setOn() {
		setLight(1,Color.YELLOW);
		
	}

	public void setLight(int place, Color color)
	{
		ramzor.colorLight[place-1]=color;
		panel.repaint();
	}
}
