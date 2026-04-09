package com.nicolasgarland.pentaingredients.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nicolasgarland.pentaingredients.Ingredient;

public class InventorySlot extends Actor {
    public static final int SLOT_SIZE = 64;  // Taille d'une case (en pixels)
    private Ingredient item;              // Objet dans cette case (null si vide)
    private TextureRegion slotTexture;       // Texture de fond de la case
    private boolean isSelected;               // Case sélectionnée ?

    public InventorySlot(TextureRegion slotTexture) {
        this.slotTexture = slotTexture;
        this.item = null;
        this.isSelected = false;
        setSize(SLOT_SIZE, SLOT_SIZE);

        // Ajouter un écouteur pour les clics
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isSelected = !isSelected;  // Basculer la sélection
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Dessiner le fond de la case
        batch.draw(slotTexture, getX(), getY(), getWidth(), getHeight());

        // Dessiner une bordure si la case est sélectionnée
        if (isSelected) {
            batch.setColor(Color.YELLOW);
            batch.draw(slotTexture, getX() - 2, getY() - 2, getWidth() + 4, getHeight() + 4);
            batch.setColor(Color.WHITE);
        }

        // Dessiner l'objet s'il y en a un
        if (item != null) {
            batch.draw(item.getIcon(), getX() + 5, getY() + 5, getWidth() - 10, getHeight() - 10);
        }
    }

    // Méthodes pour gérer l'objet
    public void setItem(Ingredient item) {
        this.item = item;
    }

    public Ingredient getItem() {
        return item;
    }

    public void clear() {
        this.item = null;
    }

    public boolean hasItem() {
        return item != null;
    }
}
