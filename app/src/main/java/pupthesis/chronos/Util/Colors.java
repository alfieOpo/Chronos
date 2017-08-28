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
            rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")
            , rgb("#16a085"), rgb("#FF4081"), rgb("#f1c40f"), rgb("#f39c12")
            , rgb("#4a5151"), rgb("#388E3C")
    };
    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
}
