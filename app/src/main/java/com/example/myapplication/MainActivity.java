package com.example.myapplication;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private LinearLayout layout;

    private int currentColor = 0;

    private float scalingFactor;

    private final int[] colors = {
            Color.rgb(255, 255, 255),
            Color.rgb(255, 0, 0),
            Color.rgb(0, 255, 0),
            Color.rgb(0, 0, 255),
            Color.rgb(255, 255, 0)
    };

    private OrientationEventListener orientationListener;
    private int lastOrientation = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.main);

        scalingFactor = calculateScalingFactor();

        ViewGroup rootLayout = findViewById(R.id.main);
        scaleLayout(rootLayout, scalingFactor);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });


        Button randomButton = findViewById(R.id.randomButton);
        Button prevButton = findViewById(R.id.prevButton);
        Button nextButton = findViewById(R.id.nextButton);

        Button changeTheme = findViewById(R.id.changeThemeButton);

        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });



        ImageView imageView = findViewById(R.id.image);


        orientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int i) {
                int newOrientation = (i >= 315 || i < 45) ? 0:
                        (i < 135) ? 90:
                                (i < 225) ? 180 : 270;

                if (newOrientation != lastOrientation)  {
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

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int red = random.nextInt(256);
                int green = random.nextInt(256);
                int blue = random.nextInt(256);

                layout.setBackgroundColor(Color.rgb(red, green, blue));
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
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

        Button nameButton = findViewById(R.id.nameButton);
        EditText inputText = findViewById(R.id.editText);
        Button inputTextButton = findViewById(R.id.showTextButton);

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Imiona członków: Kamil, Zuzanna.", Toast.LENGTH_SHORT).show();
            }
        });

        inputTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputText.getText().toString();

                Toast.makeText(MainActivity.this, "Wpisany tekst: " + input, Toast.LENGTH_SHORT).show();
            }
        });

        TextView dynamicText = findViewById(R.id.dynamicText);
        Spinner spinner = findViewById(R.id.spinner);

        String[] options = {"Domyślny tekst", "Zwiększ rozmiar", "Pogrub czcionkę", "Zmieniony tekst"};

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

                    case "Zmieniony tekst":
                        dynamicText.setText("Zmieniony dynamiczny tekst");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button diceRollButton = findViewById(R.id.diceRollButton);
        TextView result = findViewById(R.id.diceRollResult);

        diceRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();

                int diceResult = random.nextInt(6) + 1;

                result.setText("Wynik rzutu kostką: " + diceResult);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orientationListener != null)    {
            orientationListener.disable();
        }
    }

    private float calculateScalingFactor()  {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;

        if (screenWidthDp >= 600) {
            return 1.5f;
        }
        else {
            return 1.0f;
        }
    }

    private void scaleLayout(ViewGroup rootLayout, float scalingFactor) {
        for (int i = 0; i < rootLayout.getChildCount(); i++)    {
            View child = rootLayout.getChildAt(i);

            if (child instanceof ViewGroup) {
                scaleLayout((ViewGroup) child, scalingFactor);
            }
            else {
                scaleView(child, scalingFactor);
            }
        }
    }

    private void scaleView(View view, float scalingFactor)  {
        if (view instanceof TextView)   {
            TextView textView = (TextView) view;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * scalingFactor);
        }else if (view instanceof Button)   {
            Button button = (Button) view;
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.getTextSize() * scalingFactor);
        }else if (view instanceof EditText)   {
            EditText editText = (EditText) view;
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, editText.getTextSize() * scalingFactor);
        }else if (view instanceof ImageView)   {
            ImageView imageView = (ImageView) view;
            Bitmap originalBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            int newWidth = (int) (originalBitmap.getWidth() * scalingFactor);
            int newHeight = (int) (originalBitmap.getHeight() * scalingFactor);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);

            imageView.setImageBitmap(scaledBitmap);
        }

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginParams.leftMargin *= scalingFactor;
            marginParams.topMargin *= scalingFactor;
            marginParams.rightMargin *= scalingFactor;
            marginParams.bottomMargin *= scalingFactor;
        }

        view.setPadding(
                (int) (view.getPaddingLeft() * scalingFactor),
                (int) (view.getPaddingTop() * scalingFactor),
                (int) (view.getPaddingRight() * scalingFactor),
                (int) (view.getPaddingBottom() * scalingFactor)
        );
    }

}