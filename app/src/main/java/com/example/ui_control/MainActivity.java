package com.example.ui_control;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.text.style.LineHeightSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // 计算BMI事件
        Button buttonCompute = (Button) findViewById(R.id.buttonCompute);
        buttonCompute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textWeight = (EditText) findViewById(R.id.editTextWeightNumber);
                EditText textHeight = (EditText) findViewById(R.id.editTextHeightNumber);
                String strWeight = textWeight.getText().toString();
                String strHeight = textHeight.getText().toString();
                if (strHeight.equals("") || strWeight.equals("")) {
                    Toast.makeText(MainActivity.this, "身高和体重没填的哩", Toast.LENGTH_SHORT).show();
                    return;
                }
                double weight = Double.parseDouble(strWeight);
                double height = Double.parseDouble(strHeight);
                double BMI = weight / Math.pow(height / 100, 2);

                RadioGroup radioGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
                RadioButton radioButton = (RadioButton) findViewById(radioGroupSex.getCheckedRadioButtonId());
                String sex = radioButton.getText().toString();
                if (sex.equals("男")) {
                    BMI -= 1;
                }
                String textBMI = "";
                if (BMI < 18)
                    textBMI = "体重偏低";
                else if (18 <= BMI && BMI < 24)
                    textBMI = "健康体重";
                else if (24 <= BMI && BMI < 29)
                    textBMI = "超重";
                else if (29 <= BMI && BMI < 38)
                    textBMI = "严重超重";
                else textBMI = "极度超重";
                Toast.makeText(MainActivity.this, textBMI + ",BMI为：" + BMI, Toast.LENGTH_SHORT).show();
            }
        });

        // Reset事件
        Button buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textUsername = (EditText) findViewById(R.id.editTextUserName);
                EditText textPassword = (EditText) findViewById(R.id.editTextPassword);
                EditText textWeight = (EditText) findViewById(R.id.editTextWeightNumber);
                EditText textHeight = (EditText) findViewById(R.id.editTextHeightNumber);
                textUsername.setText("");
                textPassword.setText("");
                textWeight.setText("");
                textHeight.setText("");
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                data.get(position);
//                Toast.makeText(MainActivity.this,)
//            }
//        });

        // spinner Items = (Spinner) findViewById(R.id.spinner_class);
    }

    private String[] data = {"Item1", "Item2", "Item3", "Item4"};
}