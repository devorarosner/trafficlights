import java.awt.Color;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author לויאן
 */

class ShneyLuchot extends Thread
{
	Ramzor ramzor;
	JPanel panel;
	Event64 evToGreen,evToRed,evAtRed,evShabbat,evWeekday;
	enum OutState{OUT_WEEKDAY,OUT_SHABBAT};//is in weekday or shabbat
	enum InState{IN_RED,IN_GREEN};//in weekday is in red or green
	OutState outState;
	InState inState;
	MyTimer72 timer;
	Event64 evTimer;
	public ShneyLuchot(Ramzor ramzor, JPanel panel, Event64 evToGreen, Event64 evToRed, Event64 evAtRed,
			Event64 evShabbat, Event64 evWeekday) {//constructor 
		super();
		this.ramzor = ramzor;
		this.panel = panel;
		this.evToGreen = evToGreen;
		this.evToRed = evToRed;
		this.evAtRed = evAtRed;
		this.evShabbat = evShabbat;
		this.evWeekday = evWeekday;
		start();
	}

	public ShneyLuchot(Ramzor ramzor,JPanel panel)//constructor
	{
		this.ramzor=ramzor;
		this.panel=panel;
		start();
	}

	public void run()//start running over the statechart
	{
		//start state is red in weekday
		outState=OutState.OUT_WEEKDAY;
		setToRed();//default transition
		inState=InState.IN_RED;
		try 
		{
			while (true)//the statechart is always running
			{
				
				switch(outState) {//outer state in statechart
				case OUT_WEEKDAY://if the current state is OUT_WEEKDAY
					while(outState==OutState.OUT_WEEKDAY)//weekday inner statechart  
					{
						
							switch(inState)//weekday inner statechart current state
							{
							case IN_RED://if the current state is IN_RED
								while(true)
									{
									if(evShabbat.arrivedEvent())//if gets a change to shabbat event
									{
										evShabbat.waitEvent();//reset flags
										outState=OutState.OUT_SHABBAT;//sets outer state to shabbat
										turnOff();//turns off lights for shabbat
										break;
									}
								else if(evToGreen.arrivedEvent())//if gets a change to green event
									{
										evToGreen.waitEvent();//reset flags
										inState=InState.IN_GREEN;//sets inner state to green
										setToGreen();//changes lights to green
										break;
									}
									else
									{
										System.out.println("yield");
										yield();
									}
									}
								break;
							case IN_GREEN://if the current state is IN_GREEN
								while(true)
								{
								 if(evShabbat.arrivedEvent())//if gets a change to shabbat event
									{
										evShabbat.waitEvent();//reset flags
										outState=OutState.OUT_SHABBAT;//sets outer state to shabbat
										turnOff();//turns off lights for shabbat
										break;
									}
								else if(evToRed.arrivedEvent())//if gets a change to red event
								{
									evToRed.waitEvent();//reset flags
									inState=InState.IN_RED;//sets inner state to red
									setToRed();//changes lights to red
									sendEvForRed();
									break;
								}
								else
								{
									System.out.println("yield");
									yield();
								}
								 }
								break;
							default:
								break;
							}
					}
					break;
				case OUT_SHABBAT://if the current state is OUT_SHABBAT
					System.out.println("2 shabbat");
					evWeekday.waitEvent();//wait for evWeekday event
					outState=OutState.OUT_WEEKDAY;//sets outer state to weekday
					inState=InState.IN_RED;//sets inner state to red
					setToRed();//changes lights to red
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {}

	}

	private void sendEvForRed() {
		// TODO Auto-generated method stub
		evAtRed.sendEvent();
		
	}

	//change colors according to command
	private void turnOff() {
		setLight(1,Color.GRAY);
		setLight(2,Color.GRAY);
		
	}

	private void setToGreen() {
		setLight(1,Color.GRAY);
		setLight(2,Color.GREEN);
	}

	private void setToRed() {
		setLight(1,Color.RED);
		setLight(2,Color.GRAY);
	}
	public void setLight(int place, Color color)
	{
		ramzor.colorLight[place-1]=color;
		panel.repaint();
	}


}
