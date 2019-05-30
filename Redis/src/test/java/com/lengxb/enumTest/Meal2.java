package com.lengxb.enumTest;

public enum Meal2 {
	APPETIZER(Food.Appetizer.class),
	MAINCOURSE(Food.Maincourse.class),
	DESSERT(Food.Dessert.class),
	COFFEE(Food.Coffee.class);
	private Food[] values;
	private Meal2(Class<? extends Food> kind) {
		// TODO Auto-generated constructor stub
		values = kind.getEnumConstants();
	}
	public interface Food{
		enum Appetizer implements Food{
			SALAD,SOUP,SPRING_ROLLS;
		}
		enum Maincourse implements Food{
			LASAGNE,BURRITO,PAD_THAI,LENTILS,HUMMOUS,VINDALOO;
		}
		enum Dessert implements Food{
			TRIAMISU,GELATO,BLACK_FOREST_CAKE,FRUIT,CREME_CARAMEL;		
		}
		enum Coffee implements Food{
			BLACK_COFFEE,DACAF_COFFEE,ESPRESSO,LATTE,CAPPUCCINO,TEA,HERB_TEA;
		}
	}
	public Food radomSelection() {
		return Enums.random(values);
	}
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			for (Meal2 meal : Meal2.values()) {
				Food  food = meal.radomSelection();
				System.out.println(food);
			}
			System.out.println("=======================");
		}
	}
}
