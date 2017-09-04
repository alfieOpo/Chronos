package pupthesis.chronos.Util;

import android.graphics.Color;

/**
 * Created by ALFIE on 8/27/2017.
 */

public class Colors {
    public static int[] getColors(int stacksize ) {



        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = MATERIAL_COLORS[i];
        }



        return colors;
    }
    public static final int[] MATERIAL_COLORS = {
            rgb("#f44336"), rgb("#E91E63"), rgb("#9C27B0"), rgb("#673AB7")
            , rgb("#3F51B5"), rgb("#00BCD4"), rgb("#2196F3"), rgb("#009688")
            , rgb("#4a5151"), rgb("#660066"), rgb("#990099"), rgb("#996600")
            , rgb("#333300"), rgb("#ffff00"), rgb("#ff00cc"), rgb("#990033")
            , rgb("#607D8B"), rgb("#666600"), rgb("#669966"), rgb("#ffcc00")
            , rgb("#9E9E9E"), rgb("#FF5722"), rgb("#795548"), rgb("#FF9800")
            , rgb("#FFC107"), rgb("#CDDC39"), rgb("#8BC34A"), rgb("#4CAF50")
    };
    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
}
