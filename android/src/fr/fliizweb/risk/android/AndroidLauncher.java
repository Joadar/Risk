package fr.fliizweb.risk.android;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import fr.fliizweb.risk.Game;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGLSurfaceView20API18 = true;
		config.useAccelerometer = false;
		config.useCompass = false;
		Game game = new Game();
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		initialize(game, config);
	}
}
