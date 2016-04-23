package net.ddns.wpp.lodowkacheck;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

/**
 * Created by ja on 2016-04-23.
 */
public class ButtonX extends Button
{
    private int ID;
    private int stan;

    ButtonX(Context c, int id, int _stan)
    {
        super(c);
        ID = id;
        setBackgroundColor(Color.LTGRAY);
        stan = _stan;
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

    public int getId()
    {
        return ID;
    }

    public int getStan()
    {
        return stan;
    }
}
