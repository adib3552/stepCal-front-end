package com.example.stepcal.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stepcal.API.ApiInterface;
import com.example.stepcal.DTO.CompletedTask;
import com.example.stepcal.DTO.Task;
import com.example.stepcal.R;
import com.example.stepcal.Retrofit.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tasks extends AppCompatActivity {

    private ApiInterface apiInterface;

    private String userEmail;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        apiInterface= RetrofitClient.getRetrofit().create(ApiInterface.class);
        authToken=getSavedToken();

        apiInterface.getTask("Bearer "+authToken).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                if(response.isSuccessful()) {
                    Task task = response.body();
                    if (task != null) {
                        // Setting Target Calorie
                        TextView targetCalorieTextView = findViewById(R.id.target);
                        String showCalorie = "Target Calorie: " + task.getTarget_Calorie();
                        targetCalorieTextView.setText(showCalorie);

                        addExercise(task.getExercises());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {

            }
        });
    }
    public void addExercise(Map<String, Double> exercises) {
        LinearLayout exerciseLayout = findViewById(R.id.exerciseLayout);
        exerciseLayout.removeAllViews(); // Clear previous exercise items

        for (Map.Entry<String, Double> exerciseEntry : exercises.entrySet()) {
            // Create views for exercise name and repetitions EditText
            TextView exerciseNameTextView = new TextView(this);
            exerciseNameTextView.setText(exerciseEntry.getKey() + "-(Each rep: " + exerciseEntry.getValue()+") reps:-");
            exerciseNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            EditText repetitionsEditText = new EditText(this);
            repetitionsEditText.setHint("Repetitions");
            repetitionsEditText.setText("0");
            repetitionsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            repetitionsEditText.setTag(exerciseEntry.getKey());

            // Create a vertical LinearLayout for each exercise
            LinearLayout exerciseItemLayout = new LinearLayout(this);
            exerciseItemLayout.setOrientation(LinearLayout.HORIZONTAL); // Set orientation to vertical

            // Add views to the vertical layout
            exerciseItemLayout.addView(exerciseNameTextView);
            exerciseItemLayout.addView(repetitionsEditText);

            // Add the exercise item to the main layout
            exerciseLayout.setOrientation(LinearLayout.VERTICAL);
            exerciseLayout.addView(exerciseItemLayout);
        }
        Button button=new Button(this);
        button.setText("submit");
        exerciseLayout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRepetitionsFromEditTexts(exercises);
            }
        });
    }

    public void getRepetitionsFromEditTexts(Map<String,Double>exercises) {
        LinearLayout exerciseLayout = findViewById(R.id.exerciseLayout);
        int childCount = exerciseLayout.getChildCount();
        double burn=0;
        for (int i = 0; i < childCount; i++) {
            View view = exerciseLayout.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout exerciseItemLayout = (LinearLayout) view;
                int innerChildCount = exerciseItemLayout.getChildCount();

                for (int j = 0; j < innerChildCount; j++) {
                    View innerView = exerciseItemLayout.getChildAt(j);
                    double reps=0;
                    if (innerView instanceof EditText) {
                        EditText repetitionsEditText = (EditText) innerView;
                        String s = repetitionsEditText.getText().toString();
                        if(s!=null){
                            reps= Double.parseDouble(s);
                        }
                        burn+=reps*exercises.get(repetitionsEditText.getTag());
                    }
                    //System.out.println(reps);
                }
            }
        }
        CompletedTask completedTask=new CompletedTask();
        completedTask.setCalorie_burn(preciseCal(burn));
        completedTask.setCalorie_intake(FoodIntake());
        //System.out.println(completedTask);
        Intent intent=new Intent(this,TasksCompleted.class);
        intent.putExtra("complete",completedTask);
        startActivity(intent);


    }

    private String getSavedToken() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    public Double preciseCal(Double x)
    {
        String formattedX = String.format("%.2f", x);
        Double result = Double.valueOf(formattedX);
        return result;
    }

    /**
     * Food Intake Calculation
     */

    public Double FoodIntake()
    {

        // In this initializer take User input as gram measurement Except Egg
        // Egg Should be taken as number per egg is on avearge 50gm

        TextView rice= findViewById(R.id.food1);
        TextView chicken= findViewById(R.id.food2);
        TextView egg= findViewById(R.id.food3);
        TextView milk= findViewById(R.id.food4);
        TextView salad= findViewById(R.id.food5);


        Double Tot_In_Cal=0.0;
        Double Rice_in=0.0;
        Double Chicken_in=0.0;
        Double Egg_in=0.0;
        Double Milk_in=0.0;
        Double Salad_in=0.0;

        String riceText = rice.getText().toString();
        if(riceText != null)
            Rice_in=Double.parseDouble(riceText);

        String chickenText = chicken.getText().toString();
        if(chickenText != null)
            Chicken_in=Double.parseDouble(chickenText);

        String eggText = egg.getText().toString();
        if(eggText != null)
            Egg_in=Double.parseDouble(eggText);

        String milkText = milk.getText().toString();
        if(milkText != null)
            Milk_in=Double.parseDouble(milkText);

        String saladText = salad.getText().toString();
        if(saladText != null)
            Salad_in=Double.parseDouble(saladText);





        Double Rice_cal=((Rice_in*1.0)/100.0)*130.0;

        Double Chicken_cal=((Chicken_in*1.0)/100.0)*239.0;

        double Egg_Cal=(((Egg_in*50.0))/100)*155.0;

        Double Milk_cal=((Milk_in*1.0)/100)*44.0;

        Double Salad_cal=((Salad_in*1.0)/100.0)*17.0;

        Tot_In_Cal=Rice_cal+Chicken_cal+Egg_Cal+Milk_cal+Salad_cal;

        return  preciseCal(Tot_In_Cal);


    }




}
