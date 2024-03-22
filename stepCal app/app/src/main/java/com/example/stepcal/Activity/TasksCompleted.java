package com.example.stepcal.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stepcal.API.ApiInterface;
import com.example.stepcal.DTO.CompletedTask;
import com.example.stepcal.R;
import com.example.stepcal.Retrofit.RetrofitClient;

import java.util.List;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksCompleted extends AppCompatActivity {

    private ApiInterface apiInterface;
    private String authToken;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_completed);

        apiInterface = RetrofitClient.getRetrofit().create(ApiInterface.class);
        authToken = getSavedToken();

        Intent prevIntent=getIntent();
        CompletedTask completedTask= (CompletedTask) prevIntent.getSerializableExtra("complete");
        System.out.println(completedTask);
        tableLayout = findViewById(R.id.history); // assuming this is the ID of your TableLayout

        apiInterface.addTask("Bearer "+authToken,completedTask).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                System.out.println("success");
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("fail");
            }
        });
        apiInterface.taskHistory("Bearer " + authToken).enqueue(new Callback<List<CompletedTask>>() {
            @Override
            public void onResponse(@NonNull Call<List<CompletedTask>> call2, @NonNull Response<List<CompletedTask>> response2) {
                if (response2.isSuccessful() && response2.body() != null) {

                    List<CompletedTask> taskList = response2.body();
                    for(CompletedTask task:taskList){
                        System.out.println("INN");
                        System.out.println(task);
                    }
                   // addHeaderRow(); // Add table header
                    // Add each task to the table
                    for (CompletedTask task : taskList) {
                        addTableRow(task.getDate(), String.valueOf(task.getCalorie_burn()), String.valueOf(task.getCalorie_intake()));
                    }
                } else {
                    showToast("Failed to fetch task history");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CompletedTask>> call, @NonNull Throwable t) {
                showToast("Network request failed");
            }
        });
    }

    private void addHeaderRow() {
        addTableRow("DATE", "Calorie Burn", "Calorie Intake");
    }

    private void addTableRow(String name, String calorieBurn, String calorieIntake) {
        TableRow row = new TableRow(this);

        TextView nameTextView = createTextView(name);
        TextView calorieBurnTextView = createTextView(calorieBurn);
        TextView calorieIntakeTextView = createTextView(calorieIntake);


        row.addView(nameTextView);
        row.addView(calorieBurnTextView);
        row.addView(calorieIntakeTextView);


        tableLayout.addView(row);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private String getSavedToken() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}