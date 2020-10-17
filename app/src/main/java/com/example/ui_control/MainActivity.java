package com.example.ui_control;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;

public class MainActivity extends AppCompatActivity {
    int a = 0;
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

        // Item的处理
        // 使用适配器实现内容绑定
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, ItemData);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        // ListView的事件响应
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String ans = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "你选中了" + ans, Toast.LENGTH_SHORT).show();
            }
        });

        // Spinner的处理
        // 获取组件
        Spinner provinceSpinner = (Spinner) findViewById(R.id.spinnerProvince);
        final Spinner citySpinner = (Spinner) findViewById(R.id.spinnerCity);
        final Spinner DistrictSpinner = (Spinner) findViewById(R.id.spinnerDistrict);
        //绑定适配器和值
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, province);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(0, true);  // 设置初始选中项
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, city[0]);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0, true);
        ArrayAdapter<String> DistrictAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, District[0][0]);
        DistrictSpinner.setAdapter(DistrictAdapter);
        DistrictSpinner.setSelection(0, true);
        final Integer[] provincePosition = {0};
        // Spinner的事件响应
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, city[i]);
                citySpinner.setAdapter(cityAdapter);
                provincePosition[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> DistrictAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, District[provincePosition[0]][i]);
                DistrictSpinner.setAdapter(DistrictAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // ToggleButton的处理
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButtonSwitch);
        final ImageView imageView = (ImageView) findViewById(R.id.imageViewLight);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    imageView.setImageResource(R.drawable.on);
                else
                    imageView.setImageResource(R.drawable.off);
            }
        });

        city();
    }

    // Item的数据
    private String[] ItemData = {"Item1", "Item2", "Item3", "Item4"};

    // Spinner的数据
    private String[] province = new String[]{"不限", "浙江", "广东"};
    private String[][] city = new String[][]{
            {"不限"},
            {"不限", "杭州市", "舟山市", "金华市"},  // 浙江省
            {"不限", "广州市", "湛江市", "珠海市"},  // 广东省
    };
    private String[][][] District = new String[][][]{
            {{"不限"}},
            // 浙江省
            {{"不限"}, {"不限", "西湖区", "余杭区", "上城区"},  // 杭州
                    {"不限", "定海区", "普陀区"},  // 舟山
                    {"不限", "兰溪", "义乌", "东阳"}  // 金华
            },
            // 广东省
            {{"不限"}, {"不限", "海珠区", "越秀区", "白云区"},  // 广州
                    {"不限", "霞山区", "坡头区"},  // 湛江
                    {"不限", "香洲区", "斗门区", "金湾区"}, // 珠海
            }
    };

    public void city() {
        final CityPickerView mPicker = new CityPickerView();
        mPicker.init(MainActivity.this);
        //添加默认的配置
        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);

        final EditText editCity = findViewById(R.id.edit_city);
        editCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //监听选择点击事件及返回结果
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        Toast.makeText(MainActivity.this, province + " - " + city + " - " + district, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLongToast(MainActivity.this, "已取消");
                    }
                });
                mPicker.showCityPicker();
            }
        });
    }
}