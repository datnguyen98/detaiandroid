
package com.example.myapplication;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.ImageView;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends Activity {
    ImageView play,exit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        connect();
        exit();
        entergame();
    }

    private void entergame() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, UI.class);
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
        play= (ImageView) findViewById(R.id.play);
        exit= (ImageView) findViewById(R.id.exit);
    }
}

