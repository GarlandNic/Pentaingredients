package com.nicolasgarland.pentaingredients.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nicolasgarland.pentaingredients.Ingredient;
import com.nicolasgarland.pentaingredients.IngredientsList;
import com.nicolasgarland.pentaingredients.Level;
import com.nicolasgarland.pentaingredients.Main;
import com.nicolasgarland.pentaingredients.Positions;
import com.nicolasgarland.pentaingredients.actors.InventorySlot;

public class GameScreen implements Screen {
	private final Main game;
	
    private Stage pentagrStage;
    private Stage etagereStage;
    private Skin skin;
    private Texture background;
    
    private int levelNb;
    private Level thisLevel;
    private Positions thisPositions;
    private List<Ingredient> listOfIngredients;

	public GameScreen(Main game, int levelNb) {
        this.game = game;
        this.levelNb = levelNb;
        String filePath = "assets/levels/level" + levelNb + ".json";
        if (Gdx.files.internal(filePath).exists()) {
            this.thisLevel = (new Json()).fromJson(Level.class, Gdx.files.internal(filePath));
        } else {
        	Gdx.app.log("ERROR", "in loaging level : " + Gdx.files.internal(filePath).file().getAbsolutePath());
        }
        this.thisPositions = (new Positions().loadPositions(levelNb));
        this.listOfIngredients = (new IngredientsList()).loadIngredientsList();
	}

	@Override
	public void show() {
	    // Charger le fond d'écran
        background = new Texture(Gdx.files.internal("assets/menu_background.png"));

        // Créer deux Viewport (un pour chaque moitié de l'écran)
        ScreenViewport leftViewport = new ScreenViewport();
        ScreenViewport rightViewport = new ScreenViewport();

        // Définir les tailles des Viewport
        leftViewport.setWorldSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        rightViewport.setWorldSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

        // Créer les Stage
        pentagrStage = new Stage(leftViewport);
        etagereStage = new Stage(rightViewport);

        // Définir les zones de caméra pour chaque Stage
        leftViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        rightViewport.setScreenBounds(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

        // charger la skin
        skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));

        // Ajouter des acteurs (boutons, labels, etc.) à chaque Stage
        addActorsToPentagrStage();
        addActorsToEtagereStage();

        // Définir les InputProcessor pour chaque Stage
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(pentagrStage);
        multiplexer.addProcessor(etagereStage);
        Gdx.input.setInputProcessor(multiplexer);
	}

	private void addActorsToEtagereStage() {
//		Texture slotTextureFull = new Texture(Gdx.files.internal("slot.png"));
//        slotTexture = new TextureRegion(slotTextureFull);

		InventorySlot[][] slots = new InventorySlot[10][10];
        Table inventoryTable = new Table();
        inventoryTable.setPosition(50, 50);

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
            	int a = thisPositions.etagere[row][col];
            	if(a != 0) slots[row][col].setItem(listOfIngredients.get(a));
//                slots[row][col] = new InventorySlot(slotTexture);
                inventoryTable.add(slots[row][col]).size(InventorySlot.SLOT_SIZE);
            }
            inventoryTable.row();  // Nouvelle ligne après chaque rangée
        }

        etagereStage.addActor(inventoryTable);
	}

	private void addActorsToPentagrStage() {
		// titre du niveau
		Label titleLabel = new Label(thisLevel.name, skin, "title");
		titleLabel.setPosition(pentagrStage.getViewport().getScreenWidth()/2 - titleLabel.getWidth()/2, pentagrStage.getHeight()-50);
		pentagrStage.addActor(titleLabel);
		
		// bouton retour
        TextButton backButton = new TextButton("Retour", skin);
        backButton.setPosition(20, 20);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	thisPositions.savePosition(levelNb);
                game.setScreen(new LevelSelectScreen(game));
            }
        });
        pentagrStage.addActor(backButton);
	}

	@Override
	public void render(float delta) {
        // Effacer l'écran
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Mettre à jour et dessiner chaque Stage
        pentagrStage.act(delta);
        pentagrStage.draw();

        etagereStage.act(delta);
        etagereStage.draw();
	}

	@Override
	public void resize(int width, int height) {
        pentagrStage.getViewport().update(width/2, height, true);
        etagereStage.getViewport().update(width/2, height, true);
        
        // Repositionner les Viewport
        pentagrStage.getViewport().setScreenBounds(0, 0, width / 2, height);
        etagereStage.getViewport().setScreenBounds(width / 2, 0, width / 2, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
        etagereStage.dispose();
        pentagrStage.dispose();
        skin.dispose();
        background.dispose();
	}

}
