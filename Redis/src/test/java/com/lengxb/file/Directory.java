package com.lengxb.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class Directory {
	public static File[] local(File dir,String regex) {
		return dir.listFiles(new FilenameFilter() {
			private Pattern pattern = Pattern.compile(regex);
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				System.out.println(name);
				return pattern.matcher(new File(name).getName()).matches();
			}
			
		});
		
	}
	public static File[] local(String path,String regex) {
		return local(new File(path),regex);
		
	}
	public static class TreeInfo implements Iterable<File>{
		public List<File> files = new ArrayList<File>();
		public List<File> dirs = new ArrayList<File>();

		@Override
		public Iterator<File> iterator() {
			// TODO Auto-generated method stub
			return files.iterator();
		}
		void addAll(TreeInfo another) {
			files.addAll(another.files);
			dirs.addAll(another.dirs);
		}
		public static TreeInfo walk(String start,String regex) {
			return recurseDirs(new File(start), regex);
			
		}
		public static TreeInfo walk(File start,String regex) {
			return recurseDirs(start, regex);
			
		}
		public static TreeInfo walk(File start) {
			return recurseDirs(start, ".*");
		}
		public static TreeInfo walk(String start) {
			return recurseDirs(new File(start), ".*");
		}
		static TreeInfo recurseDirs(File startDir,String regex){
			TreeInfo result = new TreeInfo();
			for (File file : startDir.listFiles()) {
				if(file.isDirectory()) {
					result.dirs.add(file);
					result.addAll(recurseDirs(file, regex));
				}else if(file.getName().matches(regex)) {
					result.files.add(file);
				}
			}
			return result;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "dirs: "+ PPrint.format(dirs)+"\n\nfiles: "+PPrint.format(files);
		}
		
		
	}
	public static void main(String[] args) {
		if(args.length==0)
			System.out.println(TreeInfo.walk("src"));
		else
			for (String string : args) {
				System.out.println(TreeInfo.walk(string));
			}
	}
}
class PPrint{
	public static String format(Collection<?> c) {
		if(c.size()==0)
			return "[]";
		StringBuilder result = new StringBuilder("¡¾");
		for (Object object : c) {
			if(c.size()!=1)
				result.append("\n ");
			result.append(object);
		}
		if(c.size()!=1)
			result.append("\n ");
		result.append("¡¿");
		return result.toString();
	}
}


















