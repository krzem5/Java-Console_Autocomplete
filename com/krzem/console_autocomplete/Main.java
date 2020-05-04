package com.krzem.console_autocomplete;



public class Main{
	public static void main(String[] args){
		new Main();
	}



	public Main(){
		Autocomplete a=new Autocomplete(){
			@Override
			public String autocomplete(String s){
				String m=this.match(s,new String[]{"template","reduceA","reduceAAAAAAB"});
				if (m!=null){
					return m.substring(s.length());
				}
				return "";
			}



			@Override
			public String highlight(String s){
				return s.replace("template","{83,48,232}template{255,255,255}");
			}
		};
		while (true){
			System.out.println("INPUT: "+a.get());
		}
	}
}