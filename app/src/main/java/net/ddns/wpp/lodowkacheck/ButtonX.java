package net.ddns.wpp.lodowkacheck;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

/**
 * Created by ja on 2016-04-23.
 */
public class ButtonX extends Button
{
    private int stan;

    ButtonX(Context c, int _stan, String text)
    {
        super(c);
        setBackgroundColor(Color.LTGRAY);
        stan = _stan;
        setText(text);
        update();
    }

    void nextStan()
    {
        stan = (stan + 1) % 3;
        update();
    }

    private void update()
    {
        switch (stan)
        {
            case 0:
                this.setBackgroundColor(Color.LTGRAY);
                break;
            case 1:
                this.setBackgroundColor(Color.RED);
                break;
            case 2:
                this.setBackgroundColor(Color.GREEN);
        }
    }

    public int getStan()
    {
        return stan;
    }
}
