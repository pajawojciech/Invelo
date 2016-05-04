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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
	private static final String PREFS_NAME = "invelo";
	private static final String COUNT = "ilosc";
	private static final String STATE = "stan";
	private static final String NAME = "nazwa";
	private static List<ButtonX> listX;
	private static LinearLayout l;
	private static boolean redOnly = false;
	private static int moveIndex = -1;

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

		int ilosc = settings.getInt(COUNT, 0);
		for (int i = 0; i < ilosc; i++)
		{
			if (settings.contains(NAME + Integer.toString(i)))
			{
				ButtonX x = new ButtonX(this,
						settings.getInt(STATE + Integer.toString(i), 0),
						settings.getString(NAME + Integer.toString(i) , " --- "));
				x.setOnClickListener(onClickBtn);
				x.setOnLongClickListener(onLongClickBtn);
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
		editor.putInt(COUNT, listX.size());
		for(int i = 0;i < listX.size() ; i++)
		{
			editor.putInt(STATE + Integer.toString(i), listX.get(i).getStan());
			editor.putString(NAME + Integer.toString(i), listX.get(i).getText().toString());
		}
		editor.apply();
	}

	private void nowyWpis()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.new_title));

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ButtonX x = new ButtonX(getBaseContext(), 0, input.getText().toString());
				x.setOnClickListener(onClickBtn);
				x.setOnLongClickListener(onLongClickBtn);
				listX.add(x);
				odswiezLayout();
			}
		});
		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
	}

	private void edytujWpis(ButtonX bx)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.edit_title));
		final ButtonX buttonX = bx;
		final EditText input = new EditText(this);
		input.setText(buttonX.getText());
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				buttonX.setText(input.getText());
			}
		});
		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
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
		if(id == R.id.action_clear)
		{
			for(int i = 0; i < listX.size(); i++)
			{
				listX.get(i).resetStan();
			}
			return true;
		}
		if(id == R.id.action_discard)
		{
			listX = wczytajDane(PREFS_NAME);
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
			{
				if(moveIndex != -1)
				{
					int place = listX.indexOf(b);
					if(moveIndex < place)
					{
						Collections.rotate(listX.subList(moveIndex, place + 1), -1);
					}
					else
					{
						Collections.rotate(listX.subList(place, moveIndex + 1), 1);
					}
					odswiezLayout();
					moveIndex = -1;
				}
				else
				{
					b.nextStan();
				}
			}
		}
	};

	View.OnLongClickListener onLongClickBtn = new View.OnLongClickListener()
	{
		@Override
		public boolean onLongClick(View v)
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			final ButtonX buttonX = (ButtonX)v;

			final LinearLayout ll = new LinearLayout(MainActivity.this);
			final Button editBtn = new Button(getApplicationContext());
			final Button deleteBtn = new Button(getApplicationContext());
			final Button moveBtn = new Button(getApplicationContext());

			ll.setOrientation(LinearLayout.VERTICAL);

			editBtn.setText(getString(R.string.action_edit));
			deleteBtn.setText(getString(R.string.action_delete));
			moveBtn.setText(getString(R.string.action_move));

			ll.addView(editBtn);
			ll.addView(deleteBtn);
			ll.addView(moveBtn);

			builder.setView(ll);

			final AlertDialog dialog = builder.create();
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			dialog.show();

			editBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					edytujWpis(buttonX);
					dialog.dismiss();
				}
			});
			deleteBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					listX.remove(buttonX);
					dialog.dismiss();
					odswiezLayout();
				}
			});
			moveBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					moveIndex = listX.indexOf(buttonX);
					dialog.dismiss();
					Snackbar.make(MainActivity.l, getString(R.string.move), Snackbar.LENGTH_LONG).setAction("Action", null).show();
				}
			});

			return true;
		}
	};

	View.OnClickListener onClickFab = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			Snackbar.make(view, getString(R.string.saved), Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
			zapiszDane();
			odswiezLayout();
		}
	};
}