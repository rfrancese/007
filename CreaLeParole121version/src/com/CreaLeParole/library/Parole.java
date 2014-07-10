package com.CreaLeParole.library;

public class Parole {

	TrovaParole tp;
	int lunghparola;
	int score;
	
	public Parole(){
		tp = new TrovaParole();
		lunghparola = 0;
		score = 0;
	}
	
	
	public int valutascoreparola(String parola) throws Exception{
		score = 0;
		if(tp.trovaParola(parola)){
			lunghparola=parola.length();
			
			switch(lunghparola){
			case 3:
				score=5;
			case 4:
				score=10;
			case 5:
				score=15;
			case 6:
				score=20;
			case 7:
				score=25;
			case 8:
				score=30;
			case 9:
				score=35;
			case 10:
				score=40;
			case 11:
				score=45;
			case 12:
				score=50;
			case 13:
				score=55;
			case 14:
				score=60;
			case 15:
				score=65;
			case 16:
				score=70;
			case 17:
				score=75;
			case 18:
				score=80;
			case 19:
				score=85;
			case 20:
				score=90;
			case 21:
				score=95;
			case 22:
				score=100;
			case 23:
				score=105;
			case 24:
				score=110;
			case 25:
				score=115;
			case 26:
				score=120;
			case 27:
				score=125;
			case 28:
				score=130;
			}
		}
		return score;
	}
}