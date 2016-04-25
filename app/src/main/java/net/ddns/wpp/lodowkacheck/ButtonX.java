package net.ddns.wpp.lodowkacheck;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
		setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background));
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
				this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background));
				break;
			case 1:
				this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.active));
				break;
			case 2:
				this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.inactive));
		}
	}

	public int getStan()
	{
		return stan;
	}

	public void resetStan()
	{
		stan = 0;
		update();
	}
}
