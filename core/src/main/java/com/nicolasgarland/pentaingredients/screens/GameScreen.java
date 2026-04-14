package com.nicolasgarland.pentaingredients.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
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
	
	private Stage metaStage;
    private Skin skin;
    private Texture background;
    
    private int levelNb;
    private Level thisLevel;
    private Positions thisPositions;
    private List<Ingredient> listOfIngredients;
    private Ingredient ingrSelected;
    private TextureRegion[] elements;

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
        this.ingrSelected = null;
        this.elements = new TextureRegion[]{
        		new TextureRegion(new Texture(Gdx.files.internal("assets/skin/fire.png"))),
        		new TextureRegion(new Texture(Gdx.files.internal("assets/skin/earth.png"))),
        		new TextureRegion(new Texture(Gdx.files.internal("assets/skin/lightning.png"))),
        		new TextureRegion(new Texture(Gdx.files.internal("assets/skin/water.png"))),
        		new TextureRegion(new Texture(Gdx.files.internal("assets/skin/wind.png"))),
        		new TextureRegion(new Texture(Gdx.files.internal("assets/skin/spirit.png")))
        };
	}

	@Override
	public void show() {
	    // Charger le fond d'écran
        background = new Texture(Gdx.files.internal("assets/menu_background.png"));

        // Créer Viewport
        FitViewport viewport = new FitViewport(1920, 1080);

        // Créer les Stage
        metaStage = new Stage(viewport);

        // charger la skin
        skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));

        // Ajouter des acteurs (boutons, labels, etc.)
        Table metaTable = new Table();
        metaTable.setFillParent(true);

        metaTable.add(addActorsToPentagrStage()).expand();
        metaTable.add(addActorsToEtagereStage()).expand();
        
        metaStage.addActor(metaTable);
        metaTable.setDebug(true);

        // Définir les InputProcessor
        Gdx.input.setInputProcessor(metaStage);
	}

	private Table addActorsToEtagereStage() {
		Table mainTable = new Table();

		// titre du niveau
		Label titleLabel = new Label("Etagères", skin, "title");
		mainTable.add(titleLabel).center();
	    mainTable.row();
		
	    // table des ingrédients sur les étagères
		TextureRegion slotTextureFull = new TextureRegion(new Texture(Gdx.files.internal("assets/skin/slot.png")));

		InventorySlot[][] slots = new InventorySlot[10][10];
        Table inventoryTable = new Table();

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
            	slots[row][col] = new InventorySlot(slotTextureFull);
            	int idIng = thisPositions.etagere[row][col];
            	if(idIng != 0) slots[row][col].setItem(listOfIngredients.get(idIng-1));
                inventoryTable.add(slots[row][col]).size(InventorySlot.SLOT_SIZE);
            }
            inventoryTable.row();  // Nouvelle ligne après chaque rangée
        }
        mainTable.add(inventoryTable).center();
        mainTable.row();
        
        // description de l'ingrédient sélectionné
        Table ingrSelectedTable = new Table();
		
        ingrSelectedTable.add(new Label("Ingrédient sélectionné :", skin, "title")).colspan(3).center();
        ingrSelectedTable.row();
        if(ingrSelected != null) {
        	Image imgIcon = new Image(ingrSelected.icon);
        	imgIcon.setSize(64, 64);// pourquoi ça ne marche pas ??
            ingrSelectedTable.add(imgIcon).align(Align.left);
            ingrSelectedTable.add(new Label(ingrSelected.name, skin, "default")).align(Align.left);
            ingrSelectedTable.add(new Label(ingrSelected.famille.toString(), skin, "default")).align(Align.right);
            ingrSelectedTable.row();
            Table elemTable = new Table();
            for(int i=0 ; i<6 ; i++) {
            	for(int j=0 ; j < ingrSelected.energies[i] ; j++) {
            		elemTable.add(new Image(elements[i]));
            	}
            }
            ingrSelectedTable.add(elemTable).colspan(3).center();
            ingrSelectedTable.row();
        } else {
            ingrSelectedTable.add(new Image(slotTextureFull)).align(Align.left);
            ingrSelectedTable.add(new Label("Aucun ingrédient sélectionné", skin, "default")).align(Align.left);
            ingrSelectedTable.add().align(Align.right);
            ingrSelectedTable.row();
            ingrSelectedTable.add().colspan(3).center();
            ingrSelectedTable.row();
        }
		
		mainTable.add(ingrSelectedTable).center();
        mainTable.row();

        mainTable.setDebug(true);
        return mainTable;
	}

	private Table addActorsToPentagrStage() {
		Table mainTable = new Table();

		// bouton de règles
		TextButton rulesButton = new TextButton("Règles", skin, "default");
		rulesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showRulesDialog();
            }
        });
		mainTable.add(rulesButton).align(Align.left);
		mainTable.add();
		mainTable.row();
		// bouton d'options ?
		
		// description du niveau
		Table levelTable = new Table();
		
		levelTable.add(new Label(thisLevel.name, skin, "title")).colspan(3).center();
		levelTable.row();
        Table elemTable = new Table();
        for(int i=0 ; i<6 ; i++) {
        	for(int j=0 ; j < thisLevel.puissance[i] ; j++) {
        		elemTable.add(new Image(elements[i]));
        	}
        }
        levelTable.add(elemTable).colspan(3).center();
		levelTable.row();
		levelTable.add(new Label(""+thisLevel.objectifs[0], skin, "default"));
		levelTable.add(new Label(""+thisLevel.objectifs[1], skin, "default"));
		levelTable.add(new Label(""+thisLevel.objectifs[2], skin, "default"));
		levelTable.row();
	    
		mainTable.add(levelTable).colspan(2).center();
	    mainTable.row();
		
	    // Créer un groupe pour les acteurs du pentagramme
	    Group pentagramGroup = new Group();
	    // image pentagramme
	    Image img = new Image(new Texture(Gdx.files.internal("assets/skin/Pentagramme.PNG")));
	    Gdx.app.log("DEBUG", "pentagramme size : " + img.getWidth()+" x "+img.getHeight());
//	    img.setSize(909, 908);
	    pentagramGroup.addActor(img);
	    
	    // 10 slots
	    TextureRegion slotTexture = new TextureRegion(new Texture(Gdx.files.internal("assets/skin/slot.png")));
	    
	    float[][] slotPositionsPuissance = {
	            {-0.9511f,  0.3090f},  // Position du slot 1 (x, y)
	            { 0.9511f,  0.3090f},  // Position du slot 2
	            {-0.5878f, -0.8090f},  // Position du slot 3
	            { 0f, 1f},  // Position du slot 4
	            { 0.5878f, -0.8090f}   // Position du slot 5
	        };
	    for(int i=0; i<5; i++) {
	    	InventorySlot slotP = new InventorySlot(slotTexture);
	    	int idIng = thisPositions.pentaPuissance[i];
	    	if(idIng != 0) slotP.setItem(listOfIngredients.get(idIng-1));
	    	slotP.setPosition(	909/2*(1+slotPositionsPuissance[i][0])-slotP.getWidth()/2, 
	    						908/2*(1+slotPositionsPuissance[i][1])-slotP.getHeight()/2);
	    	pentagramGroup.addActor(slotP);
	    }
	    
	    float[][] slotPositionsControle = {
	            {-0.2225f,  0.3090f},  // Position du slot 1 (x, y)
	            { 0.2225f,  0.3090f},  // Position du slot 2
	            { 0.3633f, -0.1176f},  // Position du slot 3
	            { 0f, -0.3820f},  // Position du slot 4
	            {-0.3633f, -0.1176f}   // Position du slot 5
	        };
	    for(int i=0; i<5; i++) {
	    	InventorySlot slotC = new InventorySlot(slotTexture);
	    	int idIng = thisPositions.pentaControle[i];
	    	if(idIng != 0) slotC.setItem(listOfIngredients.get(idIng-1));
	    	slotC.setPosition(	909/2*(1+slotPositionsControle[i][0])-slotC.getWidth()/2, 
	    						908/2*(1+slotPositionsControle[i][1])-slotC.getHeight()/2);
	    	pentagramGroup.addActor(slotC);
	    }
//	    pentagramGroup.setSize(1000, 1000); // comment changer la taille ??
    	
	    mainTable.add(pentagramGroup).colspan(2).size(909, 908);
	    mainTable.row();
	    
		// bouton retour
        TextButton backButton = new TextButton("Retour", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	thisPositions.savePosition(levelNb);
                game.setScreen(new LevelSelectScreen(game));
            }
        });
        mainTable.add(backButton).width(200).height(60).pad(10).align(Align.left);
        mainTable.add().expandX();
        
        mainTable.setDebug(true);
        return mainTable;
	}
	
    private void showRulesDialog() {
        Dialog rulesDialog = new Dialog("Règles du Jeu", skin) {
            @Override
            protected void result(Object object) {
                // Called when a button is clicked
            }
        };

        // Ajouter du texte
        rulesDialog.text(Gdx.files.internal("assets/rules.txt").readString());

        // Ajouter un bouton "Fermer"
        rulesDialog.button("Fermer");

        // Afficher le dialog
        rulesDialog.show(metaStage);
    }

	@Override
	public void render(float delta) {
        // Effacer l'écran
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Mettre à jour et dessiner chaque Stage
        metaStage.act(delta);
        metaStage.draw();
	}

	@Override
	public void resize(int width, int height) {
	    // Mettre à jour les Viewports
		metaStage.getViewport().update(width, height, true);

	    // Redéfinir les ScreenBounds
		metaStage.getViewport().setScreenBounds(0, 0, width, height);
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
        metaStage.dispose();
        skin.dispose();
        background.dispose();
	}

}
