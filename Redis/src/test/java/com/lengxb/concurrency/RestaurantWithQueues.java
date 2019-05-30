package com.lengxb.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import com.lengxb.enumTest.Meal2;
import com.lengxb.enumTest.Meal2.Food;

/**
 * 饭店仿真
 * @author angle
 *
 * 2019年5月30日下午6:34:16
 */
public class RestaurantWithQueues {
	public static void main(String[] args) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		Restaurant restaurant = new Restaurant(exec, 5, 2);
		exec.execute(restaurant);
		if(args.length>0)
			TimeUnit.SECONDS.sleep(new Integer(args[0]));
		else {
			System.out.println("Press 'Enter' to quit");
			System.in.read();
		}
		exec.shutdownNow();
	}
}

class Order {
	private static int counter =0;
	private final int id = counter++;
	private final Customers Customers;
	private final WaitPerson waitPerson;
	private final Food food;
	
	public Order(Customers cust,WaitPerson wp,Food f) {
		Customers = cust;
		waitPerson = wp;
		food =f;
	}
	public Food item() {
		return food;
	}
	public Customers getCustomers() {
		return Customers;
	}
	public WaitPerson getWaitPerson() {
		return waitPerson;
	}
	public String toString() {
		return "Order: "+id+"item "+food+" for: "+Customers+
				" served by: "+waitPerson;
	}
}

class Plate {
	private final Order order;
	private final Food food;
	
	public Plate(Order ord,Food f) {
		order = ord;
		food = f;
	}
	public String toString() {
		return food.toString();
	}
	public Food getFood() {
		return food;
	}
	public Order getOrder() {
		return order;
	}
}

class Customers implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private final WaitPerson waitPerson;
	private SynchronousQueue<Plate> placeSetting = 
			new SynchronousQueue<Plate>();
	public Customers(WaitPerson w) {
		waitPerson = w;
	}
	public void deliver(Plate p) throws InterruptedException {
		placeSetting.put(p);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(Meal2 course : Meal2.values()) {
			Food food = course.radomSelection();
			try {
				waitPerson.placeOrder(this,food);
				System.out.println(this +"eating "+placeSetting.take());
			} catch (InterruptedException e) {
				// TODO: handle exception
				System.out.println(this + "waiting for "+course+
						" interrupted");
				break;
			}
		}
		System.out.println(this + "finished meal, leaving");
	}
	public String toString() {
		return "Customers "+ id +" ";
	}
	
}

class WaitPerson implements Runnable {
	private static int counter =0;
	private final int id = counter++;
	private final Restaurant restaurant;
	BlockingQueue<Plate> filledOrders = new LinkedBlockingQueue<Plate>();
	
	public WaitPerson(Restaurant rest) {
		restaurant = rest;
	}
	public void placeOrder(Customers cust,Food food) {
		try {
			restaurant.orders.put(new Order(cust,this,food));
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println(this + " placeOrder interrupted");
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(!Thread.interrupted()) {
				Plate plate = filledOrders.take();
				System.out.println(this + "received "+plate+
						" delivering to "+plate.getOrder().getCustomers());
				plate.getOrder().getCustomers().deliver(plate);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println(this +" interrupted");
		}
		System.out.println(this + " off dudty");
	}
	public String toString() {
		return "WaitPerson " +id +" ";
	}
}

class Chef implements Runnable {
	private static int counter =0 ;
	private final int id = counter++;
	private final Restaurant restaurant;
	private static Random rand = new Random(47);
	
	public Chef(Restaurant rest) {
		restaurant = rest;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(!Thread.interrupted()) {
				Order order = restaurant.orders.take();
				Food requestedItem = order.item();
				TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
				Plate palte = new Plate(order,requestedItem);
				order.getWaitPerson().filledOrders.put(palte);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println(this + " interrupted");
		}
		System.out.println(this + " off duty");
	}
	public String toString() {
		return "Chef "+ id +" ";
	}
}

class Restaurant implements Runnable {
	private List<WaitPerson> waitPersons = new ArrayList<WaitPerson>();
	private List<Chef> chefs = new ArrayList<Chef>();
	private ExecutorService exec;
	private static Random rand = new Random(47);
	BlockingQueue<Order> orders = new LinkedBlockingQueue<Order>();
	
	public Restaurant(ExecutorService e,int nWaitPersons,int nChefs) {
		exec = e;
		for(int i=0;i<nWaitPersons;i++) {
			WaitPerson waitPerson = new WaitPerson(this);
			waitPersons.add(waitPerson);
			exec.execute(waitPerson);
		}
		for(int i=0;i<nChefs;i++) {
			Chef chef = new Chef(this);
			chefs.add(chef);
			exec.execute(chef);
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(!Thread.interrupted()) {
				WaitPerson wp = waitPersons.get(rand.nextInt(waitPersons.size()));
			    Customers c = new Customers(wp);
				exec.execute(c);
				TimeUnit.MILLISECONDS.sleep(100);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println("Restaurant interrupted");
		}
		System.out.println("Restaurant closing");
	}
	
}
/*class Food {
	
}

class Course {

	public Food randomSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Course[] values() {
		// TODO Auto-generated method stub
		return null;
	}
	
}*/
