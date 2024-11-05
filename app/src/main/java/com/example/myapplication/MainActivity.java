package com.example.myapplication;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private int currentColor = 0;

    private final int[] colors = {
            Color.rgb(255, 255, 255),
            Color.rgb(255, 0, 0),
            Color.rgb(0, 255, 0),
            Color.rgb(0, 0, 255),
            Color.rgb(255, 255, 0)
    };

    private OrientationEventListener orientationListener;
    private int lastOrientation = -1;

    private boolean isDarkTheme = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("themePrefs", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);

        setTheme(isDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button changeThemeButton = findViewById(R.id.changeTheme);
        changeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Przełącz między jasnym a ciemnym motywem
                isDarkTheme = !isDarkTheme;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isDarkTheme", isDarkTheme);
                editor.apply();

                // Przeładuj aktywność, aby zastosować nowy motyw
                recreate();
            }
        });

        Button button1 = findViewById(R.id.button1);
        Button randomColor = findViewById(R.id.randomButton);
        Button prevColor = findViewById(R.id.previousButton);
        Button nextButton = findViewById(R.id.nextButton);
        Button nameButton = findViewById(R.id.nameButton);
        Button showTextButton = findViewById(R.id.showTextButton);
        EditText editText = findViewById(R.id.editText);

        TextView dynamicText = findViewById(R.id.dynamicText);
        Spinner spinner = findViewById(R.id.spinner);

        Button diceRollButton = findViewById(R.id.diceRollButton);
        TextView diceRollResult = findViewById(R.id.diceRollText);


        ImageView imageView = findViewById(R.id.imageView);

        orientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                int newOrientation = (orientation >= 315 || orientation < 45) ? 0 :
                        (orientation < 135) ? 90 :
                                (orientation < 225) ? 180 : 270;

                if (newOrientation != lastOrientation) {
                    RotateAnimation rotateAnimation = new RotateAnimation(
                            lastOrientation, newOrientation,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                    );
                    rotateAnimation.setDuration(500);
                    rotateAnimation.setFillAfter(true);
                    imageView.startAnimation(rotateAnimation);
                    lastOrientation = newOrientation;
                }
            }
        };

        if (orientationListener.canDetectOrientation()) {
            orientationListener.enable();
        }

        diceRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int diceResult = random.nextInt(6) + 1;

                diceRollResult.setText("Wynik rzutu kostką: " + diceResult);
            }
        });

        String[] options = {"Domyślny tekst", "Zwiększ rozmiar", "Pogrub czcionkę", "Zmiana tekstu"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedOption = options[i];

                switch (selectedOption) {
                    case "Domyślny tekst":
                        dynamicText.setText("Dynamiczny tekst");
                        dynamicText.setTextSize(18);
                        dynamicText.setTypeface(Typeface.DEFAULT);
                        break;

                    case "Zwiększ rozmiar":
                        dynamicText.setTextSize(24);
                        break;

                    case "Pogrub czcionkę":
                        dynamicText.setTypeface(Typeface.DEFAULT_BOLD);
                        break;

                    case "Zmiana tekstu":
                        dynamicText.setText("Zmieniony dynamiczny tekst");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Powiadomienie", Toast.LENGTH_SHORT).show();
            }
        });

        prevColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = (currentColor - 1 + colors.length) % colors.length;
                layout.setBackgroundColor(colors[currentColor]);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = (currentColor + 1) % colors.length;
                layout.setBackgroundColor(colors[currentColor]);
            }
        });

        randomColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int RandomColor = generateRandomColor();
                layout.setBackgroundColor(RandomColor);
            }
        });

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Kamil, Zuzanna, Stanisław.", Toast.LENGTH_SHORT).show();
            }
        });

        showTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = editText.getText().toString();

                Toast.makeText(MainActivity.this, "Wprowadzono: " + inputText, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orientationListener != null) {
            orientationListener.disable();
        }
    }



    private int generateRandomColor() {
        Random random = new Random();

        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        return Color.rgb(red, green, blue);
    }


}