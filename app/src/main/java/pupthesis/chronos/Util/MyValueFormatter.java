package pupthesis.chronos.Util;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by ALFIE on 8/27/2017.
 */
public class MyValueFormatter implements IValueFormatter
{

    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value==-0){return "";}
        else{
            return mFormat.format(value)+"m. "+entry.getData();
        }
      //  return String.valueOf(entry.getData());
    }

}