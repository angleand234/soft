package com.lengxb.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class HorseRace {
	private static int counter = 0;
	static final int FINSH_LINE =75;
	private List<Horse> horses = new ArrayList<Horse>();
	private ExecutorService exec = Executors.newCachedThreadPool();
	private CyclicBarrier barrier;
	public HorseRace(int nHorses,final int pause) {
		barrier = new CyclicBarrier(nHorses,new Runnable() {
			StringBuilder s = new StringBuilder();
			@Override
			public void run() {
				counter++;
				// TODO Auto-generated method stub
				for(int i =0;i< FINSH_LINE;i++) {
					if(s.length()<FINSH_LINE)
						s.append("=");
				}
				System.out.println(s);
				for(Horse horse : horses) {
					System.out.println(horse.tracks());
				}
				for(Horse horse : horses) {
					if(horse.getStrides() >= FINSH_LINE) {
						System.out.println(horse + "won!"+counter);
						exec.shutdownNow();
						return;
					}
				}
				try {
					TimeUnit.MILLISECONDS.sleep(pause);
				} catch (InterruptedException e) {
					// TODO: handle exception
					System.out.println("barrier-action sleep interrupted");
				}
				
			}
		});
		for (int i = 0; i < nHorses; i++) {
			Horse horse = new Horse(barrier);
			horses.add(horse);
			exec.execute(horse);
		}
	}
	public static void main(String[] args) {
		int nHorses = 7;
		int pause = 200;
		new HorseRace(nHorses, pause);
	}
}

class Horse implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private int strides =0;
	private static Random rand = new Random(47);
	private static CyclicBarrier barrier;
	public Horse(CyclicBarrier b) {
		barrier = b;
	}
	public synchronized int getStrides() {
		return strides;
	}
	public void run() {
		try {
			while(!Thread.interrupted()) {
				synchronized(this) {
					strides += rand.nextInt(3);
				}
				barrier.await();
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
		}catch (BrokenBarrierException e) {
			throw new RuntimeException();
		}
	}
	public String toString() {
		return "Horse "+id +" ";
	}
	public String tracks() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i <getStrides(); i++) {
			s.append("*");
		}
		return s.toString();
	}
}
