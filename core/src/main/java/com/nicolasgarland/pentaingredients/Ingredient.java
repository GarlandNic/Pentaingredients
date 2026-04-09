package com.nicolasgarland.pentaingredients;

public class Ingredient {
	public static enum Famille {
		VEGETALE,
		ANIMALE,
		MINERALE,
		MAGIQUE
	}
	
	public int id;
	public String name;
	public Famille famille;
	public int cout;
	public int[] energies;
	
	public Ingredient() {};
	
	public Ingredient(int id, String name, Famille fam, int cout, int[] energies) {
		this.id = id;
		this.name = name;
		this.famille = fam;
		this.cout = cout;
		this.energies = energies;
	};
	

}
