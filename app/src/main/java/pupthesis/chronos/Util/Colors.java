package pupthesis.chronos.Util;

import com.github.mikephil.charting.utils.ColorTemplate;

/**
 * Created by ALFIE on 8/27/2017.
 */

public class Colors {
    public static int[] getColors() {

        int stacksize = 3;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }
}
