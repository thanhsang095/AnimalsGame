package com.developer.sangbarca.intentanimals2609;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Collections;

public class ChooseImageActivity extends Activity {
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
        tableLayout = (TableLayout) findViewById(R.id.myTableLayout);
        tableLayout.setBackgroundColor(Color.CYAN);

        int numCol = 3;
        int numRow = 6;

        Collections.shuffle(MainActivity.mangTenHinh);

        for(int i = 1; i <= numRow; i++){
            TableRow tableRow = new TableRow(this);
            for(int j = 1; j <= numCol; j++) {
                ImageView imageView = new ImageView(this);
                final int vitri = numCol * (i - 1) + j - 1; //xáo trộn vị trí trong tableLayout
                int idHinh = getResources().getIdentifier(MainActivity.mangTenHinh.get(vitri), "drawable", getPackageName());
                imageView.setImageResource(idHinh);

//              ***  Converts dp to pixel ***
                int dpUnit = 90;
                Resources r = getResources();
                float px  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpUnit, r.getDisplayMetrics());
//              *** Apply to tableRow ***
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams((int)px, (int)px);
                imageView.setLayoutParams(layoutParams);
                tableRow.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("hinhChon", MainActivity.mangTenHinh.get(vitri));
                        setResult(RESULT_OK, intent); // Trả về kết quả
                        finish(); // Đóng activity hiện tại, trở về activity trước
                    }
                });
            }
            tableLayout.addView(tableRow);
        }

    }
}
