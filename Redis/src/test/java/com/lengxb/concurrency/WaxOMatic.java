package com.lengxb.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 车辆打蜡仿真
 * @author ME
 *
 * 2019年6月2日下午10:25:21
 */
public class WaxOMatic {
	public static void main(String[] args) throws Exception {
		Car car = new Car();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new WaxOff(car));
		exec.execute(new WaxOn(car));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
		
	}
}

class Car {
	private boolean waxOn = false;
	public synchronized void waxed() {
		waxOn = true;
		notifyAll();
	}
	public synchronized void buffed() {
		waxOn = false;
		notifyAll();
	}
	public synchronized void waitForWaxing() throws InterruptedException{
		while(waxOn == false) {
			wait();
		}
	}
	public synchronized void waitForBuffing() throws InterruptedException{
		while(waxOn == true) {
			System.out.println("this is fac0");
			wait();
			System.out.println("this is fac");
		}
	}
}
/**
 * 打蜡
 * @author ME
 *
 * 2019年6月2日下午10:26:41
 */
class WaxOn implements Runnable {
	private Car car;
	public WaxOn(Car car) {
		this.car = car;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(!Thread.interrupted()) {
				System.out.println("Wax On!");
				TimeUnit.MILLISECONDS.sleep(200);
				car.waxed();
				car.waitForBuffing();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exiting via interrupt");
		}
		System.out.println("Ending Wax On task");
	}
	
}
/**
 * 抛光
 * @author ME
 *
 * 2019年6月2日下午10:26:54
 */
class WaxOff implements Runnable {
	private Car car;
	public WaxOff(Car car) {
		this.car = car;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(!Thread.interrupted()) {
				car.waitForWaxing();
				System.out.println("Wax off!");
				TimeUnit.MILLISECONDS.sleep(200);
				car.buffed();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exiting via interrupt");
		}
		System.out.println("Ending Wax Off task");
	}
	
}
