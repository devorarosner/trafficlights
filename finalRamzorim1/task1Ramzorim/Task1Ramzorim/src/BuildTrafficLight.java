import java.util.concurrent.TimeUnit;

import javax.swing.JRadioButton;

/*
 * Created on Mimuna 5767  upDate on Addar 5772 
 */

/**
 * @author לויאן
 */
public class BuildTrafficLight
{

	public static void main(String[] args) 
	{
		//create and places ramzorim
		final int numOfLights=4+12+1;
		Ramzor ramzorim[]=new Ramzor[numOfLights];
		ramzorim[0]=new Ramzor(3,40,430,110,472,110,514,110);
		ramzorim[1]=new Ramzor(3,40,450,310,450,352,450,394);
		ramzorim[2]=new Ramzor(3,40,310,630,280,605,250,580);
		ramzorim[3]=new Ramzor(3,40,350,350,308,350,266,350);

		ramzorim[4]=new Ramzor(2,20,600,18,600,40);
		ramzorim[5]=new Ramzor(2,20,600,227,600,205);
		ramzorim[6]=new Ramzor(2,20,600,255,600,277);
		ramzorim[7]=new Ramzor(2,20,600,455,600,433);
		ramzorim[8]=new Ramzor(2,20,575,475,553,475);
		ramzorim[9]=new Ramzor(2,20,140,608,150,590);
		ramzorim[10]=new Ramzor(2,20,205,475,193,490);
		ramzorim[11]=new Ramzor(2,20,230,475,250,475);
		ramzorim[12]=new Ramzor(2,20,200,453,200,433);
		ramzorim[13]=new Ramzor(2,20,200,255,200,277);
		ramzorim[14]=new Ramzor(2,20,200,227,200,205);
		ramzorim[15]=new Ramzor(2,20,200,18,200,40);

		ramzorim[16]=new Ramzor(1,30,555,645);

		TrafficLightFrame tlf=new TrafficLightFrame(" תשע''ב installation of traffic lights",ramzorim);

		/////////////////////////
		//creates event arrays
		Event64 [ ]evToShabbat=new Event64 [16];
		Event64 [ ]evToWeekday=new Event64 [16];
		Event64 [ ]evSetGroupToRed=new Event64 [16];
		Event64 [ ]evSetGroupAtRed=new Event64 [16];
		Event64 [ ]evSetGroupToGreen=new Event64 [16];  
		
		for (int i=0;i<evToShabbat.length;i++)
			 evToShabbat[i]=new Event64();
		for (int i=0;i<evToWeekday.length;i++)
			evToWeekday[i]=new Event64();
		for (int i=0;i<evSetGroupToRed.length;i++)
			evSetGroupToRed[i]=new Event64();
		for (int i=0;i<evSetGroupAtRed.length;i++)
			evSetGroupAtRed[i]=new Event64();
		for (int i=0;i<evSetGroupToGreen.length;i++)
			evSetGroupToGreen[i]=new Event64();
		/*for(Event64 ev : evToShabbat)
			ev=new Event64();
		
		for(Event64 ev : evToWeekday)
			ev=new Event64();
		
		for(Event64 ev : evSetGroupToRed)
			ev=new Event64();
		
		for(Event64 ev : evSetGroupAtRed)
			ev=new Event64();
		
		for(Event64 ev : evSetGroupToGreen)
			ev=new Event64();*/
		////////////////////
		
		//creates and attaches ramzorim and events
		new ShloshaAvot(ramzorim[0],tlf.myPanel,1,evSetGroupToGreen[0],evSetGroupToRed[0],evToShabbat[0],evToWeekday[0],evSetGroupAtRed[0]);
		new ShloshaAvot(ramzorim[1],tlf.myPanel,2,evSetGroupToGreen[1],evSetGroupToRed[1],evToShabbat[1],evToWeekday[1],evSetGroupAtRed[1]);
		new ShloshaAvot(ramzorim[2],tlf.myPanel,3,evSetGroupToGreen[2],evSetGroupToRed[2],evToShabbat[2],evToWeekday[2],evSetGroupAtRed[2]);
		new ShloshaAvot(ramzorim[3],tlf.myPanel,4,evSetGroupToGreen[3],evSetGroupToRed[3],evToShabbat[3],evToWeekday[3],evSetGroupAtRed[3]);

		new ShneyLuchot(ramzorim[4],tlf.myPanel,evSetGroupToGreen[4],evSetGroupToRed[4],evSetGroupAtRed[4],evToShabbat[4],evToWeekday[4]);
		new ShneyLuchot(ramzorim[5],tlf.myPanel,evSetGroupToGreen[5],evSetGroupToRed[5],evSetGroupAtRed[5],evToShabbat[5],evToWeekday[5]);
		new ShneyLuchot(ramzorim[6],tlf.myPanel,evSetGroupToGreen[6],evSetGroupToRed[6],evSetGroupAtRed[6],evToShabbat[6],evToWeekday[6]);///
		new ShneyLuchot(ramzorim[7],tlf.myPanel,evSetGroupToGreen[7],evSetGroupToRed[7],evSetGroupAtRed[7],evToShabbat[7],evToWeekday[7]);
		new ShneyLuchot(ramzorim[8],tlf.myPanel,evSetGroupToGreen[8],evSetGroupToRed[8],evSetGroupAtRed[8],evToShabbat[8],evToWeekday[8]);///
		new ShneyLuchot(ramzorim[9],tlf.myPanel,evSetGroupToGreen[9],evSetGroupToRed[9],evSetGroupAtRed[9],evToShabbat[9],evToWeekday[9]);
		new ShneyLuchot(ramzorim[10],tlf.myPanel,evSetGroupToGreen[10],evSetGroupToRed[10],evSetGroupAtRed[10],evToShabbat[10],evToWeekday[10]);
		new ShneyLuchot(ramzorim[11],tlf.myPanel,evSetGroupToGreen[11],evSetGroupToRed[11],evSetGroupAtRed[11],evToShabbat[11],evToWeekday[11]);///
		new ShneyLuchot(ramzorim[12],tlf.myPanel,evSetGroupToGreen[12],evSetGroupToRed[12],evSetGroupAtRed[12],evToShabbat[12],evToWeekday[12]);
		new ShneyLuchot(ramzorim[13],tlf.myPanel,evSetGroupToGreen[13],evSetGroupToRed[13],evSetGroupAtRed[13],evToShabbat[13],evToWeekday[13]);
		new ShneyLuchot(ramzorim[14],tlf.myPanel,evSetGroupToGreen[14],evSetGroupToRed[14],evSetGroupAtRed[14],evToShabbat[14],evToWeekday[14]);
		new ShneyLuchot(ramzorim[15],tlf.myPanel,evSetGroupToGreen[15],evSetGroupToRed[15],evSetGroupAtRed[15],evToShabbat[15],evToWeekday[15]);///

		new Echad(ramzorim[16],tlf.myPanel);
		
		//controller events
		Event64 evShabbatClick=new Event64();
		Event64 evWeekdayClick=new Event64();
		Event64 evReglClick=new Event64();
			

		MyActionListener myListener=new MyActionListener(evShabbatClick,evWeekdayClick,evReglClick);

		JRadioButton butt[]=new JRadioButton[13]; 

		for (int i=0;i<butt.length-1;i++) 
		{
			butt[i]  =new JRadioButton();
			butt[i].setName(Integer.toString(i+4));
			butt[i].setOpaque(false);
			butt[i].addActionListener(myListener);
			tlf.myPanel.add(butt[i]);
		}
		butt[0].setBounds(620, 30, 18, 18);
		butt[1].setBounds(620, 218, 18, 18);
		butt[2].setBounds(620, 267, 18, 18);
		butt[3].setBounds(620, 447, 18, 18);
		butt[4].setBounds(566, 495, 18, 18);
		butt[5].setBounds(162,608, 18, 18);
		butt[6].setBounds(213,495, 18, 18);
		butt[7].setBounds(240,457, 18, 18);
		butt[8].setBounds(220,443, 18, 18);
		butt[9].setBounds(220,267, 18, 18);
		butt[10].setBounds(220,218, 18, 18);
		butt[11].setBounds(220,30, 18, 18);

		butt[12]  =new JRadioButton();
		butt[12].setName(Integer.toString(16));
		butt[12].setBounds(50,30, 55, 20);
		butt[12].setText("שבת");
		butt[12].setOpaque(false);
		butt[12].addActionListener(myListener);
		tlf.myPanel.add(butt[12]);
		
		try {
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//creates the controller
		Controller control=new Controller(evShabbatClick,evWeekdayClick,evReglClick,evToShabbat,evToWeekday,evSetGroupToRed,evSetGroupAtRed,evSetGroupToGreen);
	}
}
