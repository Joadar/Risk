package fr.fliizweb.risk;

import com.badlogic.gdx.Game;

import fr.fliizweb.risk.Screens.GameScreen;
import fr.fliizweb.risk.Screens.MenuScreen;

public class Risk extends Game {

	MenuScreen menuScreen;

	@Override
	public void create () {
		menuScreen = new MenuScreen(this);
        setScreen(menuScreen); // On affiche l'écran de menu
	}

    public MenuScreen getMenuScreen() { return this.menuScreen; }

	public GameScreen getGameScreen() { return new GameScreen(this); }

}
