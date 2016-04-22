package net.ddns.wpp.lodowkacheck;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {

    View.OnClickListener l = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ButtonX b = (ButtonX)v;
            b.nextStan();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Nie zapisano :P", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        int ilosc = 20;
        ButtonX tab[] = new ButtonX[ilosc];

        for(int i = 0; i < ilosc; i++)
        {
            tab[i] = createButton(i);
            if(i % 3 == 0)
            {
                tab[i].setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    private ButtonX createButton(int i)
    {
        ButtonX myButton = new ButtonX(this, i);
        myButton.setText("Push Me" + Integer.toString(i));

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

        return super.onOptionsItemSelected(item);
    }
}

class ButtonX extends Button
{
    private int ID;
    int stan = 0;

    ButtonX(Context c, int id)
    {
        super(c);
        ID = id;
        setBackgroundColor(Color.LTGRAY);
    }

    void nextStan()
    {
        stan = (stan + 1) % 3;
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
}