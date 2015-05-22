package fr.fliizweb.risk.Class;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by rcdsm on 22/05/15.
 */
public final class GameColor {

    public final static String getStrColor(Color color) {
        if(color.equals(Color.RED))
            return "RED";
        else if(color.equals(Color.GREEN))
            return "GREEN";
        else if (color.equals(Color.BLUE))
            return "BLUE";
        else if (color.equals(Color.YELLOW))
            return "YELLOW";
        else if (color.equals(Color.MAGENTA))
            return "MAGENTA";
        else if (color.equals(Color.PURPLE))
            return "PURPLE";
        else
            return "WHITE";
    }

    public final static Color getColor(String colorStr) {
        if(colorStr.equals("RED"))
            return Color.RED;
        else if(colorStr.equals("GREEN"))
            return Color.GREEN;
        else if (colorStr.equals("BLUE"))
            return Color.BLUE;
        else if (colorStr.equals("YELLOW"))
            return Color.YELLOW;
        else if (colorStr.equals("MAJENTA"))
            return Color.MAGENTA;
        else if (colorStr.equals("PURPLE"))
            return Color.PURPLE;
        else if (colorStr.equals("NEUTRAL"))
            return Color.WHITE;
        else
            return Color.WHITE;
    }


}
