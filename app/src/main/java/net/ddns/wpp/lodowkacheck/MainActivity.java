package net.ddns.wpp.lodowkacheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {
    public String out;
    public static final String PREFS_NAME = "invelo";
    int ilosc;
    ButtonX tab[];
    View.OnClickListener l = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ButtonX b = (ButtonX)v;
            b.nextStan();
        }
    };

    View.OnClickListener listenerFab = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Snackbar.make(view, "Nie zapisano :P Obróæ telefon aby zapisaæ :wink:", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("ilosc", ilosc);
            for(int i = 0;i < ilosc; i++)
            {
                if(i >= tab.length)
                {
                    editor.putInt("stan" + Integer.toString(i), 0);
                }
                else
                {
                    editor.putInt("stan" + Integer.toString(i), tab[i].getStan());
                }
            }
            editor.commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(listenerFab);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        ilosc = settings.getInt("ilosc",0);
        tab = new ButtonX[ilosc];
        for(int i = 0; i < ilosc; i++)
        {
            tab[i] = createButton(i, settings.getInt("stan" + Integer.toString(i), 0), settings.getString("nazwa" + Integer.toString(i),"---"));
        }
    }

    private ButtonX createButton(int i, int stan, String nazwa)
    {
        ButtonX myButton = new ButtonX(this, i, stan);
        myButton.setText(nazwa);

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearCheck);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);

        myButton.setOnClickListener(l);
        return myButton;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_new) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Nowy produkt");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    input.getText().toString();

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("nazwa" + Integer.toString(ilosc), out );
                    editor.commit();
                    ilosc++;
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class ButtonX extends Button
{
    private int ID;
    int stan;

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