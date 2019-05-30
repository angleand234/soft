package com.lengxb.concurrency;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 分发工作
 * @author ME
 *
 * 2019年5月30日下午11:19:14
 */
public class CarBuilder {
	public static void main(String[] args) throws Exception {
		CarQueue chassisQueue = new CarQueue(),
				finishingQueue =new CarQueue();
		ExecutorService exec = Executors.newCachedThreadPool();
		RobotPool robotPool = new RobotPool();
		exec.execute(new EngineRobot(robotPool));
		exec.execute(new DriveTrainRobot(robotPool));
		exec.execute(new WheelRobot(robotPool));
		exec.execute(new Assembler(chassisQueue, finishingQueue, robotPool));
		exec.execute(new Repoter(finishingQueue));
		exec.execute(new ChassisBuilder(chassisQueue));
		TimeUnit.SECONDS.sleep(7);
		exec.shutdownNow();
	}
}

class Cars {
	private final int id;
	private boolean engine =false,
			driveTrain = false,
			wheels = false;
	public Cars(int idn) {
		id = idn;
	}
	public Cars() {
		id =-1;
	}
	public synchronized int getId() {
		return id;
	}
	public synchronized void addWheels() {
		wheels =true;
	}
	public synchronized void addEngine() {
		engine = true;
	}
	public synchronized void addDriveTrain() {
		driveTrain = true;
	}
	public synchronized String toString() {
		return "Cars " + id +" ["+" engine: "+engine+" driveTrain: "+driveTrain
				+" wheels: "+wheels +" ]";
	}
}

class CarQueue extends LinkedBlockingDeque<Cars> {
	
}

class ChassisBuilder implements Runnable {
	private CarQueue carQueue;
	private int counter =0;
	public ChassisBuilder(CarQueue cq) {
		// TODO Auto-generated constructor stub
		carQueue = cq;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(!Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(500);
				Cars c = new Cars(counter++);
				System.out.println("ChassisBuilder created "+c);
				carQueue.put(c);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println("Interrupted: ChassisBuilder");
		}
		System.out.println("ChassisBuilder off");
	}
	
}

class Assembler implements Runnable {
	private CarQueue chassisQueue,finishngQueue;
	private Cars car;
	private CyclicBarrier barrier = new CyclicBarrier(4);
	private RobotPool robotPool;
	
	public Assembler(CarQueue cq,CarQueue fq,RobotPool rp) {
		// TODO Auto-generated constructor stub
		chassisQueue = cq;
		finishngQueue = fq;
		robotPool =rp;
	}
	public Cars car() {
		return car;
	}
	public CyclicBarrier barrier() {
		return barrier;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(!Thread.interrupted()) {
				car = chassisQueue.take();
				robotPool.hire(EngineRobot.class,this);
				robotPool.hire(DriveTrainRobot.class,this);
				robotPool.hire(WheelRobot.class,this);
				barrier.await();
				finishngQueue.put(car);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println("Exiting Assembler via interrupt");
		}catch (BrokenBarrierException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Assembler off");
	}
	
}

class Repoter implements Runnable {
	private CarQueue carQueue;
	public Repoter(CarQueue cq) {
		carQueue =cq;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(!Thread.interrupted()) {
				System.out.println(carQueue.take());
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println("Exiting Repoter via interrupt");
		}
		System.out.println("Repoter off");
	}
	
}

abstract class Robot implements Runnable {
	private RobotPool pool;
	public Robot(RobotPool rp) {
		pool =rp;
	}
	protected Assembler assembler;
	public Robot assignAssembler(Assembler assembler) {
		this.assembler = assembler;
		return this;
	}
	private boolean engage =false;
	public synchronized void engage() {
		engage =true;
		notifyAll();
	}
	abstract protected void performService();
	
	public void run() {
		try {
			powerDown();
			while(!Thread.interrupted()) {
				performService();
				assembler.barrier().await();
				powerDown();
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println("Exiting "+this +" via interrupt");
		}catch (BrokenBarrierException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
		System.out.println(this +" off");
	}
	private synchronized void powerDown() throws InterruptedException{
		engage = false;
		assembler = null;
		pool.release(this);
		while(!engage)
			wait();
	}
	public String toString() {
		return getClass().getName();
	}
}

class EngineRobot extends Robot {

	public EngineRobot(RobotPool rp) {
		super(rp);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void performService() {
		// TODO Auto-generated method stub
		System.out.println(this +" installing engine");
		assembler.car().addEngine();
	}
	
}

class DriveTrainRobot extends Robot {

	public DriveTrainRobot(RobotPool rp) {
		super(rp);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void performService() {
		// TODO Auto-generated method stub
		System.out.println(this +" installing DriveTrain");
		assembler.car().addDriveTrain();
	}
	
}

class WheelRobot extends Robot {

	public WheelRobot(RobotPool rp) {
		super(rp);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void performService() {
		// TODO Auto-generated method stub
		System.out.println(this +" installing Wheels");
		assembler.car().addWheels();
	}
	
}

class RobotPool {
	private Set<Robot> pool = new HashSet<Robot>();
	public synchronized void add(Robot r) {
		pool.add(r);
		notifyAll();
	}
	public synchronized void hire(Class<? extends Robot> robotType,Assembler d) throws InterruptedException{
		for(Robot r : pool) {
			if(r.getClass().equals(robotType)) {
				pool.remove(r);
				r.assignAssembler(d);
				r.engage();
				return;
			}
		}
		wait();
		hire(robotType,d);
	}
	public synchronized void release(Robot r) {
		add(r);
	}
}
