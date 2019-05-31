package com.lengxb.file;

import java.io.Serializable;

public class Data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int n;
	public Data(int n) {
		this.n = n;
	}
	public String toString() {
		return Integer.toString(n);
	}
}
