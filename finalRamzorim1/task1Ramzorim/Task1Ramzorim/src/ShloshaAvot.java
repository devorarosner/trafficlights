import java.awt.Color;

import javax.swing.JPanel;

//import ShneyLuchot.OutState;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author לויאן
 */

public class ShloshaAvot extends Thread
{
	Ramzor ramzor;
	JPanel panel;
	private boolean stop=true;
	Event64 evToOrangeRed, evToRed, evShabbat, evWeekday, evAtRed;
	enum OutState{OUT_WEEKDAY,OUT_SHABBAT};//is in weekday or shabbat
	enum InStateWeekday{IN_RED,IN_ORANGE_RED,IN_GREEN,IN_BLINKING_GREEN,IN_ORANGE};//in weekday is in red or orangeRed or green or blinkingGreen or orange
	enum InBlinkingGreenState{IN_ON_GREEN,IN_OFF_GREEN};//in weekday in blinkingGreen is in onGreen or offGreen
	enum InStateShabbat{IN_ON_ORANGE,IN_OFF_ORANGE};
	OutState outState;
	InStateWeekday inStateWeekday;
	InBlinkingGreenState inBlinkingGreenState;
	InStateShabbat inStateShabbat;
	int count;
	
	public ShloshaAvot(Ramzor ramzor, JPanel panel,int key, Event64 evToOrangRed, Event64 evToRed,
			Event64 evShabbat, Event64 evWeekday, Event64 evAtRed) {//constructor
		super();
		this.ramzor = ramzor;
		this.panel = panel;
		this.evToOrangeRed = evToOrangRed;
		this.evToRed = evToRed;
		this.evShabbat = evShabbat;
		this.evWeekday = evWeekday;
		this.evAtRed = evAtRed;
		new CarsMaker(panel,this,key);
		start();
	}

	public ShloshaAvot( Ramzor ramzor,JPanel panel,int key)//constructor
	{
		this.ramzor=ramzor;
		this.panel=panel;
//		new CarsMaker(panel,this,key);
		start();
	}

	public void run()//start running over the statechart
	{
		try 
		{
			//start state is red in weekday
			outState=OutState.OUT_WEEKDAY;
			while (true)//the statechart is always running
			{
				switch(outState)//outer state in statechart
				{
				case OUT_WEEKDAY:
					//start state is red
					inStateWeekday=InStateWeekday.IN_RED;
					setToRed();
					while(outState==OutState.OUT_WEEKDAY)//weekday statechart
					{
						switch(inStateWeekday)
						{
						case IN_RED://if the current state is IN_RED
							while(true)
							{
								if(evToOrangeRed.arrivedEvent())//if gets a change to orangeGreen event
							{
								evToOrangeRed.waitEvent();//reset flags
								setToOrangeRed();//changes lights to orangeRed
								inStateWeekday=InStateWeekday.IN_ORANGE_RED;//sets weekday statechart state to orangeRed
								break;
							}
							else if(evShabbat.arrivedEvent())//if gets a change to shabbat event
							{
								evShabbat.waitEvent();//reset flags
								outState=OutState.OUT_SHABBAT;//sets outer state to shabbat
								break;
							}
							else
								yield();
							}
							break;
						case IN_ORANGE_RED:
							while(true)
							{
							if(evShabbat.arrivedEvent())//if gets a change to shabbat event
							{
								evShabbat.waitEvent();//reset flags
								outState=OutState.OUT_SHABBAT;//sets outer state to shabbat
								break;
							}
							else
							{
								sleep(2000);
								setToGreen();//changes lights to green
								inStateWeekday=InStateWeekday.IN_GREEN;//sets weekday statechart state to green
								break;
							}
							}
							break;
						case IN_GREEN:
							while(true)
							{if(evToRed.arrivedEvent())//if gets a change to red event
							{
								evToRed.waitEvent();//reset flags
								count=0;
								inStateWeekday=InStateWeekday.IN_BLINKING_GREEN;//sets weekday statechart state to blinkingGreen
								break;
								
							}
							else if(evShabbat.arrivedEvent())//if gets a change to shabbat event
							{
								evShabbat.waitEvent();//reset flags
								outState=OutState.OUT_SHABBAT;//sets outer state to shabbat
								break;
							}
							else
								yield();
							}
							break;
						case IN_BLINKING_GREEN:
							inBlinkingGreenState=InBlinkingGreenState.IN_ON_GREEN;//start state is green in blinkingGreen
							while((outState==OutState.OUT_WEEKDAY) && (inStateWeekday==InStateWeekday.IN_BLINKING_GREEN))//if its in weekday and in blinkingGreen
							{
								if(evShabbat.arrivedEvent())//if gets a change to shabbat event
								{
									evShabbat.waitEvent();//reset flags
									outState=OutState.OUT_SHABBAT;//sets outer state to shabbat
								}
								else if(count==3)
								{
									setToOrange();//changes lights to orange
									inStateWeekday=InStateWeekday.IN_ORANGE;//sets weekday statechart state to orange
								}
								else
								{
									switch(inBlinkingGreenState)
									{
										case IN_ON_GREEN:
											sleep(200);
											setOffGreen();//changes lights to gray
											inBlinkingGreenState=InBlinkingGreenState.IN_OFF_GREEN;//sets blinkingGreen state to gray
											break;
										case IN_OFF_GREEN:
											sleep(200);
											setOnGreen();//changes lights to green
											inBlinkingGreenState=InBlinkingGreenState.IN_ON_GREEN;//sets blinkingGreen state to green
											count++;
											break;
										default:
											break;
									
									}
									
								 }
							  }
							  break;
						 case IN_ORANGE:
							 while(true)
							 {if(evShabbat.arrivedEvent())//if gets a change to shabbat event
								{
									evShabbat.waitEvent();//reset flags
									outState=OutState.OUT_SHABBAT;//sets outer state to shabbat
									break;
								}
								else
								{
									sleep(2000);
									setToRed();//changes lights to green
									sendEvAtRed();
									inStateWeekday=InStateWeekday.IN_RED;//sets weekday statechart state to red
									break;
								}
							 }
								break;
							
						}
					}
					break;
				case OUT_SHABBAT://if the current state is OUT_SHABBAT\
					System.out.println("3 shabbat");
					stop=true;
					inStateShabbat=InStateShabbat.IN_ON_ORANGE;
					setToOrange();//changes lights to orange
					while(outState==OutState.OUT_SHABBAT)
					{
						if(evWeekday.arrivedEvent())//if gets a change to weekday event
						{
							evWeekday.waitEvent();//reset flags
							outState=OutState.OUT_WEEKDAY;//sets outer state to shabbat
						}
						else {
						switch(inStateShabbat)
							{
							case IN_ON_ORANGE:
								sleep(1000);
								setOffOrange();//changes lights to gray
								inStateShabbat=InStateShabbat.IN_OFF_ORANGE;
								break;
							case IN_OFF_ORANGE:
								sleep(1000);
								setOnOrange();//changes lights to orange
								inStateShabbat=InStateShabbat.IN_ON_ORANGE;
								break;
							default:
								break;
							}
						}
					}
				default:
					break;
				}
				
			}
		} catch (Exception e) {}

	}
	

	private void sendEvAtRed() {
		evAtRed.sendEvent();
		
	}

	//change colors according to command
	private void setToOrange() {
		setLight(1,Color.LIGHT_GRAY);
		setLight(2,Color.YELLOW);
		setLight(3,Color.LIGHT_GRAY);
			
	}
	private void setOnGreen() {
		setToGreen();	
		stop=true;//for cars(stop)
	}

	private void setOffGreen() {
		setToGray();
		
	}

	private void setToGreen() {
		setLight(1,Color.LIGHT_GRAY);
		setLight(2,Color.LIGHT_GRAY);
		setLight(3,Color.GREEN);
		stop=false;//for cars(move)
		
	}

	private void setToOrangeRed() {
		setLight(1,Color.RED);
		setLight(2,Color.YELLOW);
		setLight(3,Color.LIGHT_GRAY);
		
	}

	private void setToRed() {
		setLight(1,Color.RED);
		setLight(2,Color.LIGHT_GRAY);
		setLight(3,Color.LIGHT_GRAY);
		
	}

	private void setOnOrange() {
		setToOrange();
	}

	private void setToGray() {
		setLight(1,Color.LIGHT_GRAY);
		setLight(2,Color.LIGHT_GRAY);
		setLight(3,Color.LIGHT_GRAY);
	}
	
	private void setOffOrange() {
		setToGray();
		
	}

	public void setLight(int place, Color color)
	{
		ramzor.colorLight[place-1]=color;
		panel.repaint();
	}

	public boolean isStop()
	{
		return stop;
	}
	/*sleep(1000);
				setLight(2,Color.YELLOW);
				sleep(1000);
				setLight(1,Color.LIGHT_GRAY);
				setLight(2,Color.LIGHT_GRAY);
				setLight(3,Color.GREEN);
				stop=false;
				sleep(3000);
				stop=true;
				setLight(1,Color.LIGHT_GRAY);
				setLight(2,Color.YELLOW);
				setLight(3,Color.LIGHT_GRAY);
				sleep(1000);
				setLight(1,Color.RED);
				setLight(2,Color.LIGHT_GRAY);
				setLight(3,Color.LIGHT_GRAY);*/
}
