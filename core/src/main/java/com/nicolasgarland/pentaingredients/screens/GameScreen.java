package com.nicolasgarland.pentaingredients.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
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
//	private Stage metaStage;
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
        	Gdx.app.log("ERROR", "in loading level : " + Gdx.files.internal(filePath).file().getAbsolutePath());
        }
        this.thisPositions = (new Positions().loadPositions(levelNb));

        this.listOfIngredients = null;
        filePath = "assets/ingredients.lst";
        if (Gdx.files.internal(filePath).exists()) {
        	this.listOfIngredients = (new Json()).fromJson(List.class, Ingredient.class, Gdx.files.internal(filePath));
        } else {
        	Gdx.app.log("ERROR", "in loading ingredients list : " + Gdx.files.internal(filePath).file().getAbsolutePath());
        }
	}

	@Override
	public void show() {
	    // Charger le fond d'écran
        background = new Texture(Gdx.files.internal("assets/menu_background.png"));

        // Créer deux Viewport (un pour chaque moitié de l'écran)
//        FitViewport leftViewport = new FitViewport(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
//        FitViewport rightViewport = new FitViewport(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        FitViewport leftViewport = new FitViewport(800, 1200);
        FitViewport rightViewport = new FitViewport(800, 1200);
//        FitViewport viewport = new FitViewport(1600, 1200);

        // Définir les tailles des Viewport
//        leftViewport.setWorldSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
//        rightViewport.setWorldSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

        // Définir les zones de caméra pour chaque Stage
        leftViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        rightViewport.setScreenBounds(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());

        // Créer les Stage
//        metaStage = new Stage(viewport);
        pentagrStage = new Stage(leftViewport);
        etagereStage = new Stage(rightViewport);

        // charger la skin
        skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));

        // Ajouter des acteurs (boutons, labels, etc.) à chaque Stage
//        Table metaTable = new Table();
//        metaTable.setFillParent(true);
//
//      metaTable.add(addActorsToPentagrStage());
//      metaTable.add(addActorsToEtagereStage());
        addActorsToPentagrStage();
        addActorsToEtagereStage();
//        
//        metaStage.addActor(metaTable);

        // Définir les InputProcessor pour chaque Stage
//        InputMultiplexer multiplexer = new InputMultiplexer();
//        multiplexer.addProcessor(pentagrStage);
//        multiplexer.addProcessor(etagereStage);
//        Gdx.input.setInputProcessor(multiplexer);
	}

	private Table addActorsToEtagereStage() {
		Table mainTable = new Table();
        mainTable.setFillParent(true);

		// titre du niveau
		Label titleLabel = new Label("Etagères", skin, "title");
//		titleLabel.setPosition(etagereStage.getWidth()/2 - titleLabel.getWidth()/2, etagereStage.getHeight()-50);
//		etagereStage.addActor(titleLabel);
		mainTable.add(titleLabel).center();
	    mainTable.row();
		
	    // table des ingrédients sur les étagères
		Texture slotTextureFull = new Texture(Gdx.files.internal("assets/skin/slot.png"));

		InventorySlot[][] slots = new InventorySlot[10][10];
        Table inventoryTable = new Table();
//        inventoryTable.setPosition(etagereStage.getWidth()/2, etagereStage.getHeight()/2); // position du centre je pense

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
            	slots[row][col] = new InventorySlot(new TextureRegion(slotTextureFull));
            	int idIng = thisPositions.etagere[row][col];
            	if(idIng != 0) slots[row][col].setItem(listOfIngredients.get(idIng-1));
                inventoryTable.add(slots[row][col]).size(InventorySlot.SLOT_SIZE);
            }
            inventoryTable.row();  // Nouvelle ligne après chaque rangée
        }
        mainTable.add(inventoryTable).center();
        mainTable.row();
        
        // description de l'ingrédient sélectionné
		Label ingrLabel = new Label("Ingrédient sélectionné :", skin, "title");
		mainTable.add(ingrLabel).center();
        mainTable.row();

        etagereStage.addActor(mainTable);
        return mainTable;
	}

	private Table addActorsToPentagrStage() {
		Table mainTable = new Table();
        mainTable.setFillParent(true);

		// titre du niveau
		Label titleLabel = new Label(thisLevel.name, skin, "title");
//		titleLabel.setPosition(pentagrStage.getWidth()/2 - titleLabel.getWidth()/2, pentagrStage.getHeight()-50);
//		pentagrStage.addActor(titleLabel);
		mainTable.add(titleLabel).colspan(2).center();
	    mainTable.row();
		
	    // image pentagramme + 10 slots
	    mainTable.row();
	    
		// bouton retour
        TextButton backButton = new TextButton("Retour", skin);
//        backButton.setPosition(20, 20);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	thisPositions.savePosition(levelNb);
                game.setScreen(new LevelSelectScreen(game));
            }
        });
        mainTable.add(backButton).width(200).height(60).pad(10);
//      pentagrStage.addActor(backButton);
        
      pentagrStage.addActor(mainTable);
        return mainTable;
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
        
//        metaStage.act(delta);
//        metaStage.draw();
	}

	@Override
	public void resize(int width, int height) {
	    // Mettre à jour les Viewports
	    pentagrStage.getViewport().update(width / 2, height, true);
	    etagereStage.getViewport().update(width / 2, height, true);

	    // Redéfinir les ScreenBounds
	    pentagrStage.getViewport().setScreenBounds(0, 0, width / 2, height);
	    etagereStage.getViewport().setScreenBounds(width / 2, 0, width / 2, height);
//		metaStage.getViewport().update(width, height, true);
//		metaStage.getViewport().setScreenBounds(0, 0, width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
        etagereStage.dispose();
        pentagrStage.dispose();
//        metaStage.dispose();
        skin.dispose();
        background.dispose();
	}

}
