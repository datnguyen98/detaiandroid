
package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UI extends Activity {
    ImageView exit;
    Button i,h;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.diff);
        connect();
        exit();
        entergame();
        entergame2();
    }

    private void entergame() {

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(UI.this, Game.class);
                startActivity(i);
            }
        });

    }
    private void entergame2() {

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(UI.this, Game2.class);
                startActivity(i);
            }
        });

    }

    private void exit() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void connect() {
       i= (Button)findViewById(R.id.item);
       h= (Button)findViewById(R.id.hero);
        exit= (ImageView) findViewById(R.id.exit);
    }
}

