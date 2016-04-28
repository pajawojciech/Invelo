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
				setBackground(getResources().getDrawable(R.drawable.btn_empty));
				break;
			case 1:
				setBackground(getResources().getDrawable(R.drawable.btn_active));
				break;
			case 2:
				setBackground(getResources().getDrawable(R.drawable.btn_inactive));
		}
		setShadowLayer(25, 0, 0, Color.WHITE);

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
