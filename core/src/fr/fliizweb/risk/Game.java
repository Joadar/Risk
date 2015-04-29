package fr.fliizweb.risk;

import fr.fliizweb.risk.Screens.GameScreen;

public class Game extends com.badlogic.gdx.Game {

	@Override
	public void create () {
		GameScreen screen = new GameScreen();
		setScreen(screen);
	}

}
