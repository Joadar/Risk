package fr.fliizweb.risk;

import com.badlogic.gdx.Game;

import fr.fliizweb.risk.Screens.GameParametersScreen;
import fr.fliizweb.risk.Screens.GameScreen;
import fr.fliizweb.risk.Screens.LoginScreen;
import fr.fliizweb.risk.Screens.MenuScreen;

public class Risk extends Game {

	MenuScreen menuScreen;
    LoginScreen loginScreen;
	GameParametersScreen gameParametersScreen;

	@Override
	public void create () {

        loginScreen = new LoginScreen(this);
		menuScreen = new MenuScreen(this);
		gameParametersScreen = new GameParametersScreen(this);

        setScreen(loginScreen); // On affiche l'écran de menu
	}
    public MenuScreen getMenuScreen() { return this.menuScreen; }

	public GameParametersScreen getGameParametersScreen() { return this.gameParametersScreen; }


	public GameScreen getGameScreen() { return new GameScreen(this); }

}
