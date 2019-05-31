package com.lengxb.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.lengxb.file.Directory.TreeInfo;

public class ProcessFiles {
	public interface Strategy{
		void process(File file);
	}
	private Strategy strategy;
	private String ext;
	public ProcessFiles(Strategy stategy,String ext) {
		// TODO Auto-generated constructor stub
		this.strategy = stategy;
		this.ext = ext;
	}
	public void start(String[] args) {
		if(args.length==0)
			processDirectoryTree(new File("."));
		else
			for (String string : args) {
				File file = new File(string);
				if(file.isDirectory())
					processDirectoryTree(file);
				else if(!string.endsWith("."+ext)) {
					string += "."+ext;
					try {
						strategy.process(new File(string).getCanonicalFile());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}
	public void processDirectoryTree(File root) {
		for (File file : TreeInfo.walk(root.getAbsolutePath(),".*\\."+ext)) {
			strategy.process(file);
		}
	}
	public static void main(String[] args) {
		new ProcessFiles(new Strategy() {
			
			@Override
			public void process(File file) {
				// TODO Auto-generated method stub
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//Date time = new Date(119,4,11);
				Calendar cal = Calendar.getInstance();
				cal.clear();
				cal.set(2019, 4, 11);
				Date time = cal.getTime();
				//System.out.println(format.format(time));
				Date when = new Date(file.lastModified());
				if(time.before(when)) {
					System.out.println(file);
					//SimpleDateFormat format = new SimpleDateFormat("MM");
					System.out.println(format.format(when));
				}
			}
		}, "java").start(args);
	}
}
