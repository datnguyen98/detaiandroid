package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Game2 extends Activity {
    private static int ROW_COUNT = -1;
    private static int COL_COUNT = -1;
    private Context context;
    private Drawable backImage;
    private int[][] cards;
    private List<Drawable> images;
    private Card firstCard;
    private Card secondedCard;
    private ButtonListener buttonListener;
    private static Object lock = new Object();
    private TableLayout mainTable;
    TextView time;
    private UpdateCardsHandler handler;
    CountDownTimer cTimer = null;
    // private static final int NEW_MENU_ID = Menu.FIRST;
    //  private static final int EXIT_MENU_ID = Menu.FIRST+1;
    private int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog();
        handler = new UpdateCardsHandler(); //phuong thuc goi ham check card, dam bao viec check card dien ra lap lai nhieu lan cho den khi het card
        loadImages(); //load card
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home2);
        Button btnnew = (Button) findViewById(R.id.btnnew);
        btnnew.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                singleChoiceDialog();
            }
        });
        backImage = getResources().getDrawable(R.drawable.iconn);
        buttonListener = new ButtonListener();
        mainTable = (TableLayout) findViewById(R.id.TableLayout03);
        context = mainTable.getContext();
        time = (TextView) findViewById(R.id.timer);
        newGame(4, 4); //mac dinh ban dau la Easy game

    }

    public void progressDialog() {
        final ProgressDialog progressDialog = ProgressDialog.show(Game2.this, "",
                "Loading...", true);

        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(300);
                    progressDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void newGame(int c, int r) {
        ROW_COUNT = r;
        COL_COUNT = c;
        count = 0;

        cards = new int[COL_COUNT][ROW_COUNT];

        mainTable.removeView(findViewById(R.id.TableRow01));
        mainTable.removeView(findViewById(R.id.TableRow02));

        TableRow tr = ((TableRow) findViewById(R.id.TableRow03));
        tr.removeAllViews();

        mainTable = new TableLayout(context);
        tr.addView(mainTable);

        for (int y = 0; y < ROW_COUNT; y++) {
            mainTable.addView(createRow(y));
        }

        firstCard = null;
        loadCards();


    }

    private void loadImages() { //lay image tu drawable
        images = new ArrayList<Drawable>();


        images.add(getResources().getDrawable(R.drawable.card1a));
        images.add(getResources().getDrawable(R.drawable.card2a));
        images.add(getResources().getDrawable(R.drawable.card3a));
        images.add(getResources().getDrawable(R.drawable.card4a));
        images.add(getResources().getDrawable(R.drawable.card5a));
        images.add(getResources().getDrawable(R.drawable.card6a));
        images.add(getResources().getDrawable(R.drawable.card7a));
        images.add(getResources().getDrawable(R.drawable.card8a));
        images.add(getResources().getDrawable(R.drawable.card9a));
        images.add(getResources().getDrawable(R.drawable.card10a));
        images.add(getResources().getDrawable(R.drawable.card11a));
        images.add(getResources().getDrawable(R.drawable.card12a));
        images.add(getResources().getDrawable(R.drawable.card13a));
        images.add(getResources().getDrawable(R.drawable.card14a));
        images.add(getResources().getDrawable(R.drawable.card15a));
        images.add(getResources().getDrawable(R.drawable.card16a));
        images.add(getResources().getDrawable(R.drawable.card17a));
        images.add(getResources().getDrawable(R.drawable.card18a));
        images.add(getResources().getDrawable(R.drawable.card19a));
        images.add(getResources().getDrawable(R.drawable.card20a));
        images.add(getResources().getDrawable(R.drawable.card21a));

    }

    private void loadCards() { //load card vao game
        int size = ROW_COUNT * COL_COUNT;

        Log.i("loadCards()", "size=" + size);

        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < size; i++) {
            list.add(new Integer(i));
        }
        Random r = new Random();

        for (int i = size - 1; i >= 0; i--) {
            int t = 0;

            if (i > 0) {
                t = r.nextInt(i);
            }

            t = list.remove(t).intValue();
            cards[i % COL_COUNT][i / COL_COUNT] = t % (size / 2);

            Log.i("loadCards()", "card[" + (i % COL_COUNT) +
                    "][" + (i / COL_COUNT) + "]=" + cards[i % COL_COUNT][i / COL_COUNT]);
        }

    }

    private TableRow createRow(int y) {
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);

        for (int x = 0; x < COL_COUNT; x++) {
            row.addView(createImageButton(x, y));
        }
        return row;
    }

    //tao card button
    private View createImageButton(int x, int y) {
        Button button = new Button(context);
        button.setBackgroundDrawable(backImage);
        button.setId(100 * x + y);
        button.setOnClickListener(buttonListener);
        return button;
    }

    class ButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            synchronized (lock) {
                if (firstCard != null && secondedCard != null) {
                    return;

                }

                int id = v.getId();
                int x = id / 100;
                int y = id % 100;
                turnCard((Button) v, x, y);


            }

        }

        private void turnCard(Button button, int x, int y) {
            button.setBackgroundDrawable(images.get(cards[x][y]));
            if (firstCard == null) {

                firstCard = new Card(button, x, y);
            } else {

                if (firstCard.x == x && firstCard.y == y) {
                    return;
                }
                secondedCard = new Card(button, x, y);

                TimerTask tt = new TimerTask() {

                    @Override
                    public void run() {

                        synchronized (lock) {
                            handler.sendEmptyMessage(0);
                        }

                    }
                };

                Timer t = new Timer(false);
                t.schedule(tt, 800);
            }

        }

    }

    class UpdateCardsHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                checkCards();
            }
        }

        public void checkCards() {

            if (cards[secondedCard.x][secondedCard.y] == cards[firstCard.x][firstCard.y]) {
                firstCard.button.setVisibility(View.INVISIBLE);
                secondedCard.button.setVisibility(View.INVISIBLE);

                count++; //moi mot cap card mat di thi count se +1
                if (count == (ROW_COUNT * COL_COUNT) / 2) {
                    winDialog(); //khi count = voi (so row * so cot)/2 thi win game
                    //vi du Easy (4hang*4cot)/2=8. mo khoa het 8 cap card thi win game
                }
            } else {
                secondedCard.button.setBackgroundDrawable(backImage);
                firstCard.button.setBackgroundDrawable(backImage);
            } // neu sai thi card ko mat di

            firstCard = null;
            secondedCard = null;

        }
    }


    private void winDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Ban co muon choi tiep ko");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                singleChoiceDialog();

            }
        })
                .setNegativeButton("Thoat", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void singleChoiceDialog() {
        final CharSequence[] items = {"Easy", "Medium", "Hard", "Exelent"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chon muc do choi");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        newGame(4, 4);
                        startTimer(60000, 1000);

                        break;
                    case 1:
                        newGame(4, 5);
                        startTimer(90000, 10);
                        break;
                    case 2:
                        newGame(4, 6);
                        startTimer(120000, 1000);
                        break;
                    case 3:
                        newGame(7, 4);
                        startTimer(150000, 1000);
                }

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void lostdialog() {
        AlertDialog.Builder lose = new AlertDialog.Builder(this);
        lose.setTitle("Ban da thua");
    }
    //////// set time
    void startTimer(int a, int b) {

        cTimer = new CountDownTimer(a, b) {

            public void onTick(long millisUntilFinished) {


            }

            public void onFinish() {
                cancelTimer();
                lostdialog();
            }
        };
        cTimer.start();
    }
    //cancel timer
    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }
}
