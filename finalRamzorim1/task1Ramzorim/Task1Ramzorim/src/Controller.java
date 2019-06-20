import java.awt.Color;
import java.util.Iterator;

import javax.swing.JPanel;


//import ShneyLuchot.OutState;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author לויאן
 */

public class Controller extends Thread
{

	Event64 evClickShabbat ,evClickWeekday , evWalker;
	/*evSetGroup0ToRed ,evSetGroup1ToRed,evSetGroup2ToRed,evSetGroup3ToRed ,
	evGroup0AtRed ,evGroup1AtRed ,evGroup2AtRed ,evGroup3AtRed ,
	evSetGroup0ToGreen ,evSetGroup1ToGreen ,evSetGroup2ToGreen ,evSetGroup3ToGreen;*/
	Event64 [ ]evToShabbat=new Event64 [16];
	Event64 [ ]evToWeekday=new Event64 [16];
	Event64 [ ]evSetGroupToRed=new Event64 [16];
	Event64 [ ]evSetGroupAtRed=new Event64 [16];
	Event64 [ ]evSetGroupToGreen=new Event64 [16];

	enum OutState{OUT_WEEKDAY,OUT_SHABBAT};//is in weekday or shabbat
	enum InState{IN_GREEN_GROUP0,IN_CHANGE_TO_RED_GROUP0,IN_GROUP0_RED,
		IN_GREEN_GROUP1,IN_CHANGE_TO_RED_GROUP1,IN_GROUP1_RED,
		IN_GREEN_GROUP2,IN_CHANGE_TO_RED_GROUP2,IN_GROUP2_RED,
		IN_GREEN_GROUP3,IN_CHANGE_TO_RED_GROUP3,IN_GROUP3_RED,
		IN_CONDITION0,IN_CONDITION1,IN_CONDITION2,IN_CONDITION3};//in weekday
		OutState outState;
		InState inState;
		MyTimer72 timer;
		Event64 evTimer;
		int clickedRamzor;




		public Controller(Event64 evClickShabbat, Event64 evClickWeekday, Event64 evWalker, Event64[] evToShabbat,
				Event64[] evToWeekDay, Event64[] evSetGroupToRed, Event64[] evSetGroupAtRed, Event64[] evSetGroupToGreen) {
			super();
			this.evClickShabbat = evClickShabbat;
			this.evClickWeekday = evClickWeekday;
			this.evWalker = evWalker;
			this.evToShabbat = evToShabbat;
			this.evToWeekday = evToWeekDay;
			this.evSetGroupToRed = evSetGroupToRed;
			this.evSetGroupAtRed = evSetGroupAtRed;
			this.evSetGroupToGreen = evSetGroupToGreen;
			start();
		}


		public void run()//start running over the statechart
		{
			try 
			{
				outState=OutState.OUT_WEEKDAY;
				inState=InState.IN_GREEN_GROUP0;
				sendToGreenGroup0();
				setTimer(2000);

				while(true)//the statechart is always running
				{
					switch(outState) {//outer state in statechart
					case OUT_WEEKDAY://if the current state is OUT_WEEKDAY
						while(outState==OutState.OUT_WEEKDAY)//weekday inner statechart  
						{
							switch(inState)//weekday inner statechart current state
							{
							case IN_GREEN_GROUP0://Group 0 is green
								while (true) 
								{
									if (evClickShabbat.arrivedEvent()) //if gets a change to shabbat event
									{	System.out.println("click shabbat");
									evClickShabbat.waitEvent();
									outState = OutState.OUT_SHABBAT;
									sendEvShabbatToAll();//send evShabbat to all the ramzorim
									break;
									} 
									else if (evTimer.arrivedEvent())//if the timer finished
									{
										evTimer.waitEvent();
										sendToRedGroup0();
										inState = InState.IN_CHANGE_TO_RED_GROUP0;//go to the next state
										break;
									} else
										yield();
								}
								break;
							case IN_CHANGE_TO_RED_GROUP0://Group 0 is changing to red
								while(true)
								{	

									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event
									{		
										System.out.println("click shabbat");
										evClickShabbat.waitEvent();
										outState=OutState.OUT_SHABBAT;
										sendEvShabbatToAll();//send evShabbat to all the ramzorim
										break;
									}
									else if(evGroup0AtRedAndSet()) //if all the ramzorim in Group 0 at red
									{
										setTimer(2000);//set timer of the next state
										inState=InState.IN_GROUP0_RED;
										break;
									}
									else
										yield();
								}
								break;
							case IN_GROUP0_RED://Group 0 is red
								while(true)
								{	
									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event
									{
										System.out.println("click shabbat");
										evClickShabbat.waitEvent();
										outState=OutState.OUT_SHABBAT;
										sendEvShabbatToAll();//send evShabbat to all the ramzorim
										break;
									}
									else if (evWalker.arrivedEvent())//if walker clicked button 
									{
										//clickedRamzor=((Integer)evWalker.waitEvent());
										clickedRamzor=Integer.parseInt((evWalker.waitEvent()).toString());//get the number of the clicked ramzor
										System.out.println(clickedRamzor);
										inState=InState.IN_CONDITION0;
										break;
									}
									else if(evTimer.arrivedEvent())//if the timer finished
									{
										evTimer.waitEvent();
										sendToGreenGroup1();
										setTimer(2000);//set the timer of the next state
										inState = InState.IN_GREEN_GROUP1;
										break;
									}

									else
										yield();
								}
								break;
							case IN_CONDITION0://condition state
								if(inGroup1(clickedRamzor))//if the clicked ramzor is in group1
								{
									sendToGreenGroup1();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP1;
								}
								else if(inGroup2(clickedRamzor))//if the clicked ramzor is in group2
								{
									sendToGreenGroup2();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP2;
								}
								else if(inGroup3(clickedRamzor))//if the clicked ramzor is in group3
								{
									sendToGreenGroup3();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP3;
								}
								else if(inGroup0(clickedRamzor))//if the clicked ramzor is in group0
								{
									sendToGreenGroup0();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP0;
								}
								break;
							case IN_GREEN_GROUP1://Group 1 is green
								while (true) 
								{
									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event
									{		System.out.println("click shabbat");
									evClickShabbat.waitEvent();
									outState = OutState.OUT_SHABBAT;
									sendEvShabbatToAll();//send evShabbat to all the ramzorim
									break;
									}
									else if (evTimer.arrivedEvent())//if the timer finished
									{
										evTimer.waitEvent();
										sendToRedGroup1();
										inState = InState.IN_CHANGE_TO_RED_GROUP1;
										break;
									}  else
										yield();
								}
								break;
							case IN_CHANGE_TO_RED_GROUP1://Group 1 is changing to red
								while(true)
								{
									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event
									{		System.out.println("click shabbat");
									evClickShabbat.waitEvent();
									outState=OutState.OUT_SHABBAT;
									sendEvShabbatToAll();//send evShabbat to all the ramzorim
									break;
									}
									else if(evGroup1AtRedAndSet())//if all the ramzorim in Group 1 at red
									{
										setTimer(2000);//set the timer of the next state
										inState=InState.IN_GROUP1_RED;
										break;
									}

									else
										yield();
								}
								break;
							case IN_GROUP1_RED://Group 1 is red
								while(true)
								{	
									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event
									{
										System.out.println("click shabbat");
										evClickShabbat.waitEvent();
										outState=OutState.OUT_SHABBAT;
										sendEvShabbatToAll();//send evShabbat to all the ramzorim
										break;
									}
									else if (evWalker.arrivedEvent())//if walker clicked button 
									{

										clickedRamzor=Integer.parseInt((evWalker.waitEvent()).toString());//get the number of the clicked ramzor
										System.out.println(clickedRamzor);
										inState=InState.IN_CONDITION1;
										break;
									}
									else if(evTimer.arrivedEvent())//if the timer finished
									{
										evTimer.waitEvent();
										sendToGreenGroup2();
										setTimer(2000);//set the timer of the next state
										inState = InState.IN_GREEN_GROUP2;
										break;
									}
									else
										yield();
								}
								break;
							case IN_CONDITION1://condition state
								if(inGroup2(clickedRamzor))//if the clicked ramzor is in group 2
								{
									sendToGreenGroup2();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP2;
								}
								else if(inGroup3(clickedRamzor))//if the clicked ramzor is in group 3
								{
									sendToGreenGroup3();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP3;
								}
								else if(inGroup0(clickedRamzor))//if the clicked ramzor is in group 0
								{
									sendToGreenGroup0();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP0;
								}
								else if(inGroup1(clickedRamzor))//if the clicked ramzor is in group 1
								{
									sendToGreenGroup1();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP1;
								}
								break;
							case IN_GREEN_GROUP2://Group 2 is green
								while (true) 
								{
									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event 
									{		System.out.println("click shabbat");
									evClickShabbat.waitEvent();
									outState = OutState.OUT_SHABBAT;
									sendEvShabbatToAll();//send evShabbat to all the ramzorim
									break;
									} 
									else if (evTimer.arrivedEvent())//if the timer finished
									{
										evTimer.waitEvent();
										sendToRedGroup2();
										inState = InState.IN_CHANGE_TO_RED_GROUP2;
										break;
									}else
										yield();
								}
								break;
							case IN_CHANGE_TO_RED_GROUP2://Group 2 is changing to red
								while(true)
								{
									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event 
									{		System.out.println("click shabbat");
									evClickShabbat.waitEvent();
									outState=OutState.OUT_SHABBAT;
									sendEvShabbatToAll();//send evShabbat to all the ramzorim
									break;
									}
									else if(evGroup2AtRedAndSet())//if all the ramzorim in Group 2 at red
									{
										setTimer(2000);//set timer to the next state
										inState=InState.IN_GROUP2_RED;
										break;
									}
									else
										yield();
								}
								break;
							case IN_GROUP2_RED://Group 2 is red
								while(true)
								{	
									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event 
									{
										System.out.println("click shabbat");
										evClickShabbat.waitEvent();
										outState=OutState.OUT_SHABBAT;
										sendEvShabbatToAll();//send evShabbat to all the ramzorim
										break;
									}
									else if (evWalker.arrivedEvent())//if walker clicked button 
									{
										clickedRamzor=Integer.parseInt((evWalker.waitEvent()).toString());//get the number of the clicked ramzor
										System.out.println(clickedRamzor);
										//clickedRamzor=((Integer)evWalker.waitEvent());
										inState=InState.IN_CONDITION2;
										break;
									}
									else if(evTimer.arrivedEvent()) //if the timer finished
									{
										evTimer.waitEvent();
										sendToGreenGroup3();
										setTimer(2000);//set timer to the next state
										inState = InState.IN_GREEN_GROUP3;
										break;
									}
									else
										yield();
								}
								break;
							case IN_CONDITION2://condition state
								if(inGroup3(clickedRamzor))//if the clicked ramzor is in group 3
								{
									sendToGreenGroup3();
									setTimer(2000);//set timer to the next state
									inState = InState.IN_GREEN_GROUP3;
								}
								else if(inGroup0(clickedRamzor))//if the clicked ramzor is in group 0
								{
									sendToGreenGroup0();
									setTimer(2000);//set timer to the next state
									inState = InState.IN_GREEN_GROUP0;
								}
								else if(inGroup1(clickedRamzor))//if the clicked ramzor is in group 1
								{
									sendToGreenGroup1();
									setTimer(2000);//set timer to the next state
									inState = InState.IN_GREEN_GROUP1;
								}
								else if(inGroup2(clickedRamzor))//if the clicked ramzor is in group 2
								{
									sendToGreenGroup2();
									setTimer(2000);//set timer to the next state
									inState = InState.IN_GREEN_GROUP2;
								}
								break;
							case IN_GREEN_GROUP3://Group 3 is green
								while (true) 
								{
									if (evClickShabbat.arrivedEvent()) //if gets a change to shabbat event 
									{		
										System.out.println("click shabbat");
										evClickShabbat.waitEvent();
										outState = OutState.OUT_SHABBAT;
										sendEvShabbatToAll();//send evShabbat to all the ramzorim
										break;
									}
									else if (evTimer.arrivedEvent())//if the timer finished
									{
										evTimer.waitEvent();
										sendToRedGroup3();
										inState = InState.IN_CHANGE_TO_RED_GROUP3;
										break;
									} else
										yield();
								}
								break;
							case IN_CHANGE_TO_RED_GROUP3://Group 3 is changing to red
								while(true)
								{
									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event 
									{		
									System.out.println("click shabbat");
									evClickShabbat.waitEvent();
									outState=OutState.OUT_SHABBAT;
									sendEvShabbatToAll();//send evShabbat to all the ramzorim
									break;
									}
									else if(evGroup3AtRedAndSet())//if all the ramzorim in Group 3 at red
									{
										setTimer(2000);//set the timer of the next state 
										inState=InState.IN_GROUP3_RED;
										break;
									}
									else
										yield();
								}
								break;
							case IN_GROUP3_RED://Group 3 is red
								while(true)
								{	
									if (evClickShabbat.arrivedEvent())//if gets a change to shabbat event 
									{
										System.out.println("click shabbat");
										evClickShabbat.waitEvent();
										outState=OutState.OUT_SHABBAT;
										sendEvShabbatToAll();//send evShabbat to all the ramzorim
										break;
									}
									else if (evWalker.arrivedEvent())//if walker clicked button 
									{
										clickedRamzor=Integer.parseInt((evWalker.waitEvent()).toString());//get the number of the clicked ramzor
										System.out.println(clickedRamzor);
										//clickedRamzor=((Integer)evWalker.waitEvent());
										inState=InState.IN_CONDITION3;
										break;
									}
									else if(evTimer.arrivedEvent())//if the timer finished
									{
										evTimer.waitEvent();
										sendToGreenGroup0();
										setTimer(2000);//set the timer of the next state
										inState = InState.IN_GREEN_GROUP0;
										break;
									}
									else
										yield();
								}
								break;
							case IN_CONDITION3://condition state
								if(inGroup0(clickedRamzor))//if the clicked ramzor is in group 0
								{
									sendToGreenGroup0();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP0;
								}
								else if(inGroup1(clickedRamzor))//if the clicked ramzor is in group 1
								{
									sendToGreenGroup1();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP1;
								}
								else if(inGroup2(clickedRamzor))//if the clicked ramzor is in group 2
								{
									sendToGreenGroup2();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP2;
								}
								else if(inGroup3(clickedRamzor))//if the clicked ramzor is in group 3
								{
									sendToGreenGroup3();
									setTimer(2000);//set the timer of the next state
									inState = InState.IN_GREEN_GROUP3;
								}
								break;
							default:
								break;
							}
						}
						break;
					case OUT_SHABBAT://if the current state is OUT_SHABBAT
						System.out.println("start control shabbat");
						evClickWeekday.waitEvent();//wait for evWeekday event
						outState=OutState.OUT_WEEKDAY;//sets outer state to weekday
						sendEvWeekdayToAll();
						sendToGreenGroup0();
						setTimer(2000);
						inState=InState.IN_GREEN_GROUP0;//sets inner state to red
						System.out.println("end control shabbat");
						break;
					default:
						break;
					}
				}

			}
			catch(Exception e) {}
		}


		//check if it's in group
		private boolean inGroup0(int clickedRamzor2) {
			if(clickedRamzor2==0 ||clickedRamzor2==6 ||clickedRamzor2==7 ||clickedRamzor2==9 ||clickedRamzor2==10 ||clickedRamzor2==12 ||clickedRamzor2==13)
				return true;
			return false;
		}
		private boolean inGroup1(int clickedRamzor2) {
			if(clickedRamzor2==3 ||clickedRamzor2==4 ||clickedRamzor2==5 ||clickedRamzor2==8 ||clickedRamzor2==9 ||clickedRamzor2==10 ||clickedRamzor2==11||clickedRamzor2==14 ||clickedRamzor2==15)
				return true;
			return false;
		}
		private boolean inGroup2(int clickedRamzor2) {
			if(clickedRamzor2==1 ||clickedRamzor2==4 ||clickedRamzor2==5 ||clickedRamzor2==6 ||clickedRamzor2==7 ||clickedRamzor2==9 ||clickedRamzor2==10||clickedRamzor2==12 ||clickedRamzor2==13)
				return true;
			return false;
		}
		private boolean inGroup3(int clickedRamzor2) {
			if(clickedRamzor2==2 ||clickedRamzor2==4 ||clickedRamzor2==5 ||clickedRamzor2==6 ||clickedRamzor2==7 ||clickedRamzor2==8 ||clickedRamzor2==11 ||clickedRamzor2==12 ||clickedRamzor2==13||clickedRamzor2==14 ||clickedRamzor2==15)
				return true;
			return false;
		}


		//send event to green to group 
		private void sendToGreenGroup0() {
			try {
				evSetGroupToGreen[0].sendEvent();
				evSetGroupToGreen[6].sendEvent();
				evSetGroupToGreen[7].sendEvent();
				evSetGroupToGreen[9].sendEvent(); 
				evSetGroupToGreen[10].sendEvent();
				evSetGroupToGreen[12].sendEvent();
				evSetGroupToGreen[13].sendEvent();
			}
			catch(Exception e) {}

		}
		private void sendToGreenGroup1() {
			evSetGroupToGreen[3].sendEvent();
			evSetGroupToGreen[4].sendEvent();
			evSetGroupToGreen[5].sendEvent();
			evSetGroupToGreen[8].sendEvent(); 
			evSetGroupToGreen[9].sendEvent();
			evSetGroupToGreen[10].sendEvent();
			evSetGroupToGreen[11].sendEvent();
			evSetGroupToGreen[14].sendEvent();
			evSetGroupToGreen[15].sendEvent();
		}
		private void sendToGreenGroup2() {
			evSetGroupToGreen[1].sendEvent();
			evSetGroupToGreen[4].sendEvent();
			evSetGroupToGreen[5].sendEvent();
			evSetGroupToGreen[6].sendEvent(); 
			evSetGroupToGreen[7].sendEvent();
			evSetGroupToGreen[9].sendEvent();
			evSetGroupToGreen[10].sendEvent();
			evSetGroupToGreen[12].sendEvent();
			evSetGroupToGreen[13].sendEvent();
		}
		private void sendToGreenGroup3() {
			evSetGroupToGreen[2].sendEvent();
			evSetGroupToGreen[4].sendEvent();
			evSetGroupToGreen[5].sendEvent();
			evSetGroupToGreen[6].sendEvent();
			evSetGroupToGreen[7].sendEvent();
			evSetGroupToGreen[8].sendEvent(); 
			evSetGroupToGreen[11].sendEvent();
			evSetGroupToGreen[12].sendEvent();
			evSetGroupToGreen[13].sendEvent();
			evSetGroupToGreen[14].sendEvent();
			evSetGroupToGreen[15].sendEvent();
		}


		//send event to red to group 
		private void sendToRedGroup0() {
			evSetGroupToRed[0].sendEvent();
			evSetGroupToRed[6].sendEvent();
			evSetGroupToRed[7].sendEvent();
			evSetGroupToRed[9].sendEvent(); 
			evSetGroupToRed[10].sendEvent();
			evSetGroupToRed[12].sendEvent();
			evSetGroupToRed[13].sendEvent();
		}

		private void sendToRedGroup1() {
			evSetGroupToRed[3].sendEvent();
			evSetGroupToRed[4].sendEvent();
			evSetGroupToRed[5].sendEvent();
			evSetGroupToRed[8].sendEvent(); 
			evSetGroupToRed[9].sendEvent();
			evSetGroupToRed[10].sendEvent();
			evSetGroupToRed[11].sendEvent();
			evSetGroupToRed[14].sendEvent();
			evSetGroupToRed[15].sendEvent();
		}

		private void sendToRedGroup2() {
			evSetGroupToRed[1].sendEvent();
			evSetGroupToRed[4].sendEvent();
			evSetGroupToRed[5].sendEvent();
			evSetGroupToRed[6].sendEvent(); 
			evSetGroupToRed[7].sendEvent();
			evSetGroupToRed[9].sendEvent();
			evSetGroupToRed[10].sendEvent();
			evSetGroupToRed[12].sendEvent();
			evSetGroupToRed[13].sendEvent();
		}

		private void sendToRedGroup3() {
			evSetGroupToRed[2].sendEvent();
			evSetGroupToRed[4].sendEvent();
			evSetGroupToRed[5].sendEvent();
			evSetGroupToRed[6].sendEvent(); 
			evSetGroupToRed[7].sendEvent(); 
			evSetGroupToRed[8].sendEvent(); 
			evSetGroupToRed[11].sendEvent();
			evSetGroupToRed[12].sendEvent();
			evSetGroupToRed[13].sendEvent();
			evSetGroupToRed[14].sendEvent();
			evSetGroupToRed[15].sendEvent();
		}

		
		//check if all the ramzorim in specific group at red (if so, resets events)
		private boolean evGroup0AtRedAndSet() {
			if(evSetGroupAtRed[0].arrivedEvent() && evSetGroupAtRed[6].arrivedEvent()  && evSetGroupAtRed[7].arrivedEvent()  && evSetGroupAtRed[9].arrivedEvent()  && 
					evSetGroupAtRed[10].arrivedEvent()  && evSetGroupAtRed[12].arrivedEvent()  && evSetGroupAtRed[13].arrivedEvent())
			{
				evSetGroupAtRed[0].waitEvent();
				evSetGroupAtRed[6].waitEvent(); 
				evSetGroupAtRed[7].waitEvent();
				evSetGroupAtRed[9].waitEvent(); 
				evSetGroupAtRed[10].waitEvent();
				evSetGroupAtRed[12].waitEvent();
				evSetGroupAtRed[13].waitEvent();
				return true;
			}
			else	
				return false;
		}

		private boolean evGroup1AtRedAndSet() {
			if(evSetGroupAtRed[3].arrivedEvent() && evSetGroupAtRed[4].arrivedEvent() && evSetGroupAtRed[5].arrivedEvent()  && 
					evSetGroupAtRed[8].arrivedEvent()  && evSetGroupAtRed[9].arrivedEvent() &&  evSetGroupAtRed[10].arrivedEvent() &&
					evSetGroupAtRed[11].arrivedEvent()  && evSetGroupAtRed[14].arrivedEvent()   && evSetGroupAtRed[5].arrivedEvent() )
			{
				evSetGroupAtRed[3].waitEvent();
				evSetGroupAtRed[4].waitEvent(); 
				evSetGroupAtRed[5].waitEvent();
				evSetGroupAtRed[8].waitEvent(); 
				evSetGroupAtRed[9].waitEvent();
				evSetGroupAtRed[10].waitEvent();
				evSetGroupAtRed[11].waitEvent();
				evSetGroupAtRed[14].waitEvent();
				evSetGroupAtRed[15].waitEvent();
				return true;
			}
			else	
				return false;
		}

		private boolean evGroup2AtRedAndSet() {
			if(evSetGroupAtRed[1].arrivedEvent() && evSetGroupAtRed[4].arrivedEvent() && evSetGroupAtRed[5].arrivedEvent()  && 
					evSetGroupAtRed[6].arrivedEvent()  && evSetGroupAtRed[7].arrivedEvent() &&  evSetGroupAtRed[9].arrivedEvent() &&
					evSetGroupAtRed[10].arrivedEvent()  && evSetGroupAtRed[12].arrivedEvent()   && evSetGroupAtRed[13].arrivedEvent() )
			{
				evSetGroupAtRed[1].waitEvent();
				evSetGroupAtRed[4].waitEvent(); 
				evSetGroupAtRed[5].waitEvent();
				evSetGroupAtRed[6].waitEvent(); 
				evSetGroupAtRed[7].waitEvent();
				evSetGroupAtRed[9].waitEvent();
				evSetGroupAtRed[10].waitEvent();
				evSetGroupAtRed[12].waitEvent();
				evSetGroupAtRed[13].waitEvent();
				return true;
			}
			else	
				return false;
		}

		private boolean evGroup3AtRedAndSet() {
			if(evSetGroupAtRed[2].arrivedEvent() && evSetGroupAtRed[4].arrivedEvent() && evSetGroupAtRed[5].arrivedEvent()  && evSetGroupAtRed[6].arrivedEvent()  && 
					evSetGroupAtRed[7].arrivedEvent() && evSetGroupAtRed[8].arrivedEvent()  && evSetGroupAtRed[11].arrivedEvent() &&  evSetGroupAtRed[12].arrivedEvent() &&
					evSetGroupAtRed[13].arrivedEvent()  && evSetGroupAtRed[14].arrivedEvent()   && evSetGroupAtRed[15].arrivedEvent() )
			{
				evSetGroupAtRed[2].waitEvent();
				evSetGroupAtRed[4].waitEvent(); 
				evSetGroupAtRed[5].waitEvent();
				evSetGroupAtRed[6].waitEvent();
				evSetGroupAtRed[7].waitEvent();
				evSetGroupAtRed[8].waitEvent(); 
				evSetGroupAtRed[11].waitEvent();
				evSetGroupAtRed[12].waitEvent();
				evSetGroupAtRed[13].waitEvent();
				evSetGroupAtRed[14].waitEvent();
				evSetGroupAtRed[15].waitEvent();
				return true;
			}
			else	
				return false;
		}

		
		//send all the event in specific event array
		private void sendEvWeekdayToAll() {
			/*	for( Event64 event  : evToWeekday)
			event.sendEvent();*/
			for (int i=0;i<evToWeekday.length;i++)
				evToWeekday[i].sendEvent();
		}


		private void sendEvShabbatToAll() 
		{
			/*for( Event64 event  : evToShabbat)
			event.sendEvent();*/
			System.out.println("ab");
			for (int i=0;i<evToShabbat.length;i++)
				evToShabbat[i].sendEvent();

		}


		//set timer 
		private void setTimer(int i) {
			evTimer=new Event64();
			timer= new MyTimer72(i,evTimer);	
		}

}
