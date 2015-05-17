package fr.fliizweb.risk;

import com.badlogic.gdx.Screen;

import java.awt.Menu;

import fr.fliizweb.risk.Screens.GameScreen;
import fr.fliizweb.risk.Screens.MenuScreen;

public class Game extends com.badlogic.gdx.Game {

	@Override
	public void create () {
		Screen screen = new MenuScreen();
		setScreen(screen);
	}

}
