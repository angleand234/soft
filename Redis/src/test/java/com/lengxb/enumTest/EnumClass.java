package com.lengxb.enumTest;

public class EnumClass {
	public static void main(String[] args) {
		String a = "GROUD";
		for (Shrubbery s : Shrubbery.values()) {
			System.out.println(s.name());
			System.out.println(s.equals(Shrubbery.GROUD));
		}
	}
}	
