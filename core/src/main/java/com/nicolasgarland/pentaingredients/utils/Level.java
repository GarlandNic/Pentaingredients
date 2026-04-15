package com.nicolasgarland.pentaingredients.utils;

public class Level {
	public int id;
	public String name;
	public int[] puissance;
	public int[] objectifs;
	
	public Level() {}

    public Level(int id, String name, int[] puissance, int[] objectifs) {
        this.id = id;
        this.name = name;
        this.puissance = puissance;
        this.objectifs = objectifs;
    }
}
