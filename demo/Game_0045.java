package demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import common.Game;
import common.Machine;

public class Game_0045 extends Game {

	//Class Variables
	private ArrayList<Machine> machines = new ArrayList<>();
	private int numFaulty;
	private int numMachines;

	//Constructor
	public Game_0045() {
		
	}

	//Adds all the machines in the game class and passes the list to each machine. Also sets other class variables
	@Override
	public void addMachines(ArrayList<Machine> machines, int numFaulty) {
		this.machines.addAll(machines);

		this.numFaulty = numFaulty;
		this.numMachines = machines.size();

		for(int i=0;i<numMachines;i++)
			machines.get(i).setMachines(this.machines);
	}

	//This function basically uses an arbitraty array initially all elements set to 1 that is
	//the respective index is correct if the array element is 1 else it is 0 that is faulty
	//Also this set state of all faulty and correct machines
	//In the end it calls the leader which intern start round 0.

	
	//Uncomment this out to sync with gui else you will get randomized approach
	@Override
	public void startPhase(int leaderId, ArrayList<Boolean> areCorrect) 
	{
		
		for(int i=0;i<numMachines;i++)
			machines.get(i).setState(areCorrect.get(i));
		
		machines.get(leaderId).setLeader();
	}
	

	@Override
	public void startPhase() {
		Random random = new Random();

		ArrayList<Integer> arr = new ArrayList<Integer>(Collections.nCopies(numMachines, 1));

		int numFaultyTemp =numFaulty;

		while(numFaultyTemp!=0)
		{
			int temp = random.nextInt(numMachines);
			if(arr.get(temp)==1)
			{
				arr.set(temp, 0);
				machines.get(temp).setState(false);
				numFaultyTemp--;
			}
		}

		for(int i = 0; i < machines.size(); i++)
		{
			if(arr.get(i)==1)
				machines.get(i).setState(true);
		}

		machines.get(random.nextInt(numMachines)).setLeader();
	}

}
