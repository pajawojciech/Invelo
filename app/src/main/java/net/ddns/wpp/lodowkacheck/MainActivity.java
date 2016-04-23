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
            Snackbar.make(view, "Zapisano, miłego dnia :D", Snackbar.LENGTH_LONG)
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
                    if(tab[i] != null)
                    {
                        editor.putInt("stan" + Integer.toString(i), tab[i].getStan());
                    }
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
            if(settings.contains("nazwa" + Integer.toString(i)))
            {
                tab[i] = createButton(i, settings.getInt("stan" + Integer.toString(i), 0), settings.getString("nazwa" + Integer.toString(i), "---"));
            }
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
                    out = input.getText().toString();

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