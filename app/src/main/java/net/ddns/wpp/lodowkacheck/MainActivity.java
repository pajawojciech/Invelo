package net.ddns.wpp.lodowkacheck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String PREFS_NAME = "invelo";
    private static List<ButtonX> listX;
	private static LinearLayout l;
	private static boolean redOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onClickFab);

		if(listX == null)
		{
            listX = wczytajDane(PREFS_NAME);
		}
		odswiezLayout();
	}

    private ArrayList<ButtonX> wczytajDane(String name)
    {
        SharedPreferences settings = getSharedPreferences(name, MODE_PRIVATE);
        ArrayList<ButtonX> lista = new ArrayList<>();

        int ilosc = settings.getInt("ilosc", 0);
        for (int i = 0; i < ilosc; i++)
        {
            if (settings.contains("nazwa" + Integer.toString(i)))
            {
                ButtonX x = new ButtonX(this,
						settings.getInt("stan" + Integer.toString(i), 0),
						settings.getString("nazwa" + Integer.toString(i) , " --- "));
                x.setOnClickListener(onClickBtn);
                lista.add(x);
            }
        }
        return lista;
    }
    
    private void odswiezLayout()
    {
		if(l != null)
		{
			l.removeAllViews();
		}
		l = (LinearLayout) findViewById(R.id.linearCheck);

		for (int i = 0; i < listX.size(); i++)
		{
			ButtonX x = listX.get(i);
			if(!redOnly || x.getStan() == 1)
			{
				l.addView(x);
			}
		}

    }

	private void zapiszDane()
	{
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("ilosc", listX.size());
		for(int i = 0;i < listX.size() ; i++)
		{
			editor.putInt("stan" + Integer.toString(i), listX.get(i).getStan());
			editor.putString("nazwa" + Integer.toString(i), listX.get(i).getText().toString());
		}
		editor.apply();
	}

	private void nowyWpis()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Nowy produkt");

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ButtonX x = new ButtonX(getBaseContext(), 0, input.getText().toString());
				x.setOnClickListener(onClickBtn);
				listX.add(x);
				odswiezLayout();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        getMenuInflater().inflate(R.menu.menu_main, menu);
		menu.getItem(1).setChecked(redOnly);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        int id = item.getItemId();
        if (id == R.id.action_settings)
		{
            return true;
        }
        if (id == R.id.action_new)
		{
			nowyWpis();
            return true;
        }
		if(id == R.id.action_redonly)
		{
			redOnly = !redOnly;
			item.setChecked(redOnly);
			odswiezLayout();
			return true;
		}
        return super.onOptionsItemSelected(item);
    }

	View.OnClickListener onClickBtn = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			ButtonX b = (ButtonX)v;
			b.nextStan();
		}
	};

	View.OnClickListener onClickFab = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			Snackbar.make(view, "Zapisano, miłego dnia :D", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
			zapiszDane();
			odswiezLayout();
		}
	};
}