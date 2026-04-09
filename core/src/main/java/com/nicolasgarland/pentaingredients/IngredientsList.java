package com.nicolasgarland.pentaingredients;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public class IngredientsList {

	public List<Ingredient> listOfIngredients;
	
	public IngredientsList() {
		this.listOfIngredients = null;
		
	}
	
	public List<Ingredient> loadIngredientsList() {
		IngredientsList ingLst = this;
		String filePath = "assets/ingredients.lst";
        if (Gdx.files.internal(filePath).exists()) {
            ingLst = (new Json()).fromJson(IngredientsList.class, Gdx.files.internal(filePath));
        } else {
        	Gdx.app.log("ERROR", "in loaging ingredients list : " + Gdx.files.internal(filePath).file().getAbsolutePath());
        }
		return ingLst.listOfIngredients;
	}
}
