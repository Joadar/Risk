package fr.fliizweb.risk;

import com.badlogic.gdx.Game;

import fr.fliizweb.risk.Screens.GameParametersScreen;
import fr.fliizweb.risk.Screens.GameScreen;
import fr.fliizweb.risk.Screens.MenuScreen;
import fr.fliizweb.risk.Screens.StatsScreen;

public class Risk extends Game {

	MenuScreen menuScreen;
	GameParametersScreen gameParametersScreen;
	StatsScreen statsScreen;

	@Override
	public void create () {
		menuScreen = new MenuScreen(this);
		gameParametersScreen = new GameParametersScreen(this);
		statsScreen = new StatsScreen();
		setScreen(menuScreen); // On affiche l'écran de menu
	}

    public MenuScreen getMenuScreen() { return this.menuScreen; }

	public GameParametersScreen getGameParametersScreen() { return this.gameParametersScreen; }

	public StatsScreen getStatsScreen() { return this.statsScreen; }

	public GameScreen getGameScreen() { return new GameScreen(this); }

}
