package com.krzem.console_autocomplete;



public class Main{
	public static void main(String[] args){
		new Main();
	}



	public Main(){
		Autocomplete a=new MinecraftCommandAutocomplete();
		while (true){
			System.out.println("COMMAND: "+a.get());
		}
	}
}