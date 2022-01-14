package demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import common.Location;
import common.Machine;

public class Machine_0045 extends Machine {
	//Declaring Variables required 
	private int step;

	private int numFaulty;

	private Location pos = new Location(0,0);
	private Location dir = new Location(0,1); // using Location as a 2d vector. Bad!
	
	private ArrayList<Machine> machines = new ArrayList<Machine>();

	private int numMachines;
	private int state;
	private int id;

	private int countLeftR1=0;
	private int countRightR1=0;
	private int countLeftR2=0;
	private int countRightR2=0;

	private int phaseNum;
	private static int nextphaseNum = 0;

	//Constructor
	public Machine_0045() {
		
	}

	//Machines are set up and numFaulty is declared
	@Override
	public void setMachines(ArrayList<Machine> machines) {
		this.machines.addAll(machines);
		this.numMachines = machines.size();
		this.numFaulty = (numMachines % 3 == 0) ? (numMachines / 3) - 1 : numMachines / 3;
	}

	//Set step size
	@Override
	public void setStepSize(int stepSize) {
		step = stepSize;
	}

	//Setting the state according to the boolean
	//Also setting the phaseNum for each machine and assigning id
	@Override
	public void setState(boolean isCorrect) {
		state = isCorrect ? 1 : 0;
		phaseNum = nextphaseNum++;
		this.id=machines.indexOf(this);
	}

	@Override
	public void setLeader() {
		//Round 0

		Random random = new Random();
		
		//Checking the state of the machines if it is correct then take a decision and send to the set of all machines.
		//If it is faulty then generate a number atleast 2 * numFaulty + 1 and sending then the decision using the method in start phase of the
		//game class
		if(state == 1)
		{
			int decision = random.nextInt(2);
			for(int i = 0; i < machines.size(); i++)
				machines.get(i).sendMessage(id,phaseNum,0,decision);
		}
		else
		{
			int numMessages= 2 * numFaulty + 1 + random.nextInt(numMachines - 2 * numFaulty);

			int decision = random.nextInt(2);

			ArrayList<Integer> arr = new ArrayList<Integer>(Collections.nCopies(numMachines, 1));

			int numMessagesTemp = numMessages;
			while(numMessagesTemp!=0)
			{
				int temp = random.nextInt(numMachines);
				if(arr.get(temp)==1)
				{
					arr.set(temp, 0);
					machines.get(temp).sendMessage(id, phaseNum, 0, decision);
					numMessagesTemp--;
				}
			}
		} 
	}

	//In round 1 if the machine is correct and send to all machines
	//else if the machine is not correct then choose whether the machine is silent and take a decision and pass to all machines
	public void roundOne(int decision) {
		if(state == 1)
		{
			for(int i = 0; i < machines.size(); i++)
				machines.get(i).sendMessage(id, phaseNum,1, decision);
		}
		else
		{
			Random random = new Random();

			int silent = random.nextInt(2);
			int faultDecision = random.nextInt(2);

			if(silent == 1)
			{
				for(int i = 0; i < machines.size(); i++)
					machines.get(i).sendMessage(id, phaseNum,1, faultDecision);
			}
		}

	}

	//Same implementation as round 1 just the parameter is changed
	public void roundTwo(int decision) {
		if(state == 1)
		{
			for(int i = 0; i < machines.size(); i++)
				machines.get(i).sendMessage(id, phaseNum,2, decision);
		}
		else
		{
			Random random = new Random();

			int silent = random.nextInt(2);
			int faultDecision = random.nextInt(2);

			if(silent == 1)
			{
				for(int i = 0; i < machines.size(); i++)
					machines.get(i).sendMessage(id, phaseNum,2, faultDecision);
			}
		}

	}

	//Choosing the final direction of the machine according to the current movemnet direction and making 
	//sure if the machine is faulty then randomise the decision
	public void changeDirection(int decision) {
		Random random = new Random();

		if(state == 0)	decision=random.nextInt(2);

		if(decision==0)
		{
			if(dir.getX()==0 && dir.getY()==1)
				dir.setLoc(-1,0); // the machine is moving towards north turning left goes to west
			else if(dir.getX()==0 && dir.getY()==-1)
				dir.setLoc(1,0); // the machine is moving towards south turning left goes to east
			else if(dir.getX()==1 && dir.getY()==0)
				dir.setLoc(0,1); // the machine is moving towards east turning left goes to north
			else if(dir.getX()==-1 && dir.getY()==0)
				dir.setLoc(0,-1); // the machine is moving towards west turning left goes to south
		}
		//Similar
		else
		{
			if(dir.getX()==0 && dir.getY()==1)
				dir.setLoc(1,0);
			else if(dir.getX()==0 && dir.getY()==-1)
				dir.setLoc(-1,0);
			else if(dir.getX()==1 && dir.getY()==0)
				dir.setLoc(0,-1);
			else if(dir.getX()==-1 && dir.getY()==0)
				dir.setLoc(0,1);
		}
		//As the phase is about to end then setting all things to 0.
		countLeftR1=0;
		countRightR1=0;
		countLeftR2=0;
		countRightR2=0;
	}

	@Override
	public void sendMessage(int sourceId, int phaseNum, int roundNum, int decision) {
		//Send messages if you are in round zero to round one
		if(roundNum==0)
			roundOne(decision);
		//In round one if the decision is left then increase the count and decision is right then increase the respective count
		else if(roundNum == 1)
		{
			if(decision == 0)
				countLeftR1++;
			else
				countRightR1++;
	
			//If the total decision is greater than 2 * numFaulty + 1
			if(countRightR1 + countLeftR1 >= (2 * numFaulty + 1))
				roundTwo((countLeftR1>= numFaulty + 1) ? 0 : 1);//if one of them is greater than numFaulty + 1 send the respective direction
		}
	
		//Round 2
		else if(roundNum == 2)
		{
			//In round 2 if the decision is left then increase the count and decision is right then increase the respective count
			if(decision == 0)
				countLeftR2++;
			else
				countRightR2++;
	
			//Now check if any of the decision is greater than 2 * numFaulty + 1 if it is then
			//pas it on to the end of phase
			if(countLeftR2 >= 2 * numFaulty + 1)
				changeDirection(0);
			else if(countRightR2 >= 2 * numFaulty + 1)	
				changeDirection(1);
		}
		
	}

	//Simple move function which is based on the current position and the direction
	@Override
	public
	void move() {
		pos.setLoc(pos.getX() + dir.getX()*step, pos.getY() + dir.getY()*step);
	}

	//returns roll number
	@Override
	public
	String name() {
		return "0045";
	}

	//return current location
	@Override
	public Location getPosition() {
		return new Location(pos.getX(), pos.getY());
	}
}
