package com.developer.sangbarca.intentanimals2609;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ImageView imgHinhGoc, imgHinhNhan;
    TextView txtDiem;
    public static ArrayList<String> mangTenHinh;

    int REQUEST_CODE_CHOOSE_IMAGE = 10;
    int diemSo = 30;
    int idHinh;

    //        Khai báo SharedPreferences lưu điểm
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        //Khai báo
        sharedPreferences = getSharedPreferences("myScore", MODE_PRIVATE);
        //Lấy dữ liệu từ sharedPreferences
        diemSo = sharedPreferences.getInt("Diem", 0); // 0: default if null data
        if(diemSo == 0){
            diemSo = 100;
        }

        txtDiem.setText("" + diemSo);

        String[] arrayName = getResources().getStringArray(R.array.dach_sach_hinh);
        mangTenHinh = new ArrayList<>(Arrays.asList(arrayName));

        Collections.shuffle(mangTenHinh);//Xáo trộn mảng

        //Lấy id từ mangTenHinh
        idHinh = getResources().getIdentifier(mangTenHinh.get(0), "drawable", getPackageName());
        imgHinhGoc.setImageResource(idHinh);

        imgHinhNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              Kiểm tra nếu nếu điểm bằng 0 thì thua
                if(!KiemTraThua()) {
                    Intent intent = new Intent(MainActivity.this, ChooseImageActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE);
                }else {
//                    Toast.makeText(MainActivity.this, "Bạn đã thua cmnr", Toast.LENGTH_SHORT).show();
                    HopThoaiXacNhan();
                }
            }
        });
    }

//    Tạo menu tùy chọn
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reload, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    Thiết lập sự kiện click chọn menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuCaiDat:
                Toast.makeText(this, "Cài đặt", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.menuChiaSe:
                Toast.makeText(this, "Chia sẻ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuReload:
                Collections.shuffle(mangTenHinh);
                idHinh = getResources().getIdentifier(mangTenHinh.get(0), "drawable", getPackageName());
                imgHinhGoc.setImageResource(idHinh);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    Nhận kết quả trả về

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CHOOSE_IMAGE && resultCode == RESULT_OK && data != null){
            String tenHinhChon = data.getStringExtra("hinhChon");
            int idHinhChon = getResources().getIdentifier(tenHinhChon, "drawable", getPackageName());
            imgHinhNhan.setImageResource(idHinhChon);

//            Kiểm tra hình vừa chọn trùng với hình gốc
            if(idHinhChon == idHinh){
                Toast.makeText(this, "Chính cmn xác!", Toast.LENGTH_SHORT).show();
                diemSo += 10;

                LuuDiem();

            }else {
                Toast.makeText(this, "Sai cmnr!", Toast.LENGTH_SHORT).show();
                diemSo -= 10;

                LuuDiem();

            }
            txtDiem.setText("" + diemSo);
        }
//        Kiểm tra nếu người dùng quay lại mà k chọn
        if(requestCode == REQUEST_CODE_CHOOSE_IMAGE && resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Định quay lại à, trừ cmn điểm nhé!", Toast.LENGTH_SHORT).show();
            diemSo -= 10;
            LuuDiem();
            txtDiem.setText("" + diemSo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void HopThoaiXacNhan(){
        AlertDialog.Builder hopThoai = new AlertDialog.Builder(this);
        hopThoai.setTitle("Thông báo");
        hopThoai.setIcon(R.mipmap.ic_launcher);
        hopThoai.setMessage("Bạn có muốn chơi lại không ?");

        //Gán nút
        hopThoai.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                diemSo = 100;
                txtDiem.setText("" + diemSo);
            }
        });
        hopThoai.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        hopThoai.setNeutralButton("Bỏ qua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        hopThoai.show();
    }

    private void LuuDiem(){
        //Cập nhật sharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Diem", diemSo); // Lưu điểm
        editor.commit(); //Xác nhận có chỉnh sửa và lưu lại
    }

    private boolean KiemTraThua() {
        if(diemSo <= 0){
            return true;
        }
        return false;
    }

    private void AnhXa(){
        imgHinhGoc = (ImageView) findViewById(R.id.imageViewGoc);
        imgHinhNhan = (ImageView) findViewById(R.id.imageViewNhan);
        txtDiem = (TextView) findViewById(R.id.textViewDiem);
    }
}
