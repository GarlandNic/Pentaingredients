package com.nicolasgarland.pentaingredients;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;

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
	}

	public Texture getIcon() {
		String filePath = "assets/ingredients/ingr" + this.id + ".png";
        if (Gdx.files.internal(filePath).exists()) {
        	return (new Texture(Gdx.files.internal(filePath)));
        } else {
        	Gdx.app.log("ERROR", "in loaging img : " + Gdx.files.internal(filePath).file().getAbsolutePath());
        	return null;
        }
	};

}
