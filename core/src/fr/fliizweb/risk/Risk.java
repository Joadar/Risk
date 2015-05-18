package fr.fliizweb.risk;

import com.badlogic.gdx.Game;
import fr.fliizweb.risk.Screens.GameScreen;
import fr.fliizweb.risk.Screens.MenuScreen;

public class Risk extends Game {

	MenuScreen menuScreen;
	GameScreen gameScreen;

	@Override
	public void create () {

		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);

		setScreen(menuScreen);

		/*Screen screen = new MenuScreen();
		try {
			screen = new GameScreen();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		setScreen(screen);*/
	}

	public GameScreen getGameScreen() { return this.gameScreen; }

}
