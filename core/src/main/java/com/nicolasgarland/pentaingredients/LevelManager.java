package com.nicolasgarland.pentaingredients;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class LevelManager {
    private Json json = new Json();
    private Array<Level> levels = new Array<>();

    public void loadLevels() {
        FileHandle file = Gdx.files.internal("levels/level1.json");
        Level level = json.fromJson(Level.class, file);
        levels.add(level);
    }

    public Level getLevel(int id) {
        return levels.get(id - 1);
    }
}
