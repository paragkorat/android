package com.demo.parag.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Prectice extends Activity {
    EditText edtText;
    TextView txtView;
    Button btnAdd,btnShow,btnClear,btnSearch,btnFindMax,btnReverse,btnFindIndex,btnReset,btnSort,btnRemove;
    ArrayList<Integer> arrInt=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prectice);

        edtText=findViewById(R.id.edtText);
        txtView=findViewById(R.id.txtView);
        btnAdd=findViewById(R.id.btnAdd);
        btnRemove=findViewById(R.id.btnRemove);
        btnShow=findViewById(R.id.btnShow);
        btnClear=findViewById(R.id.btnClear);
        btnSearch=findViewById(R.id.btnSearch);
        btnReverse=findViewById(R.id.btnReverse);
        btnFindMax=findViewById(R.id.btnFindMax);
        btnFindIndex=findViewById(R.id.btnFindIndex);
        btnReset=findViewById(R.id.btnReset);
        btnSort=findViewById(R.id.btnSort);


    }
    int i,j,temp;
    public void btnClick(View b) {
        try {
            if (b == btnAdd) {
                int number = Integer.parseInt(edtText.getText().toString());
                arrInt.add(number);
                clear(edtText);
                displayText();

            }else if (b == btnRemove) {
                int number = Integer.parseInt(edtText.getText().toString());
                arrInt.remove(number);
                clear(edtText);
                displayText();

            }else if (b == btnReset) {
                arrInt.clear();
                clear(edtText);
                clear(txtView);

            } else if (b == btnShow) {
                displayText();
                clear(edtText);

            } else if (b == btnClear) {
                clear(txtView);
                clear(edtText);

            } else if (b == btnSearch) {
                int number = Integer.parseInt(edtText.getText().toString());
                for (i = 0; i < arrInt.size(); i++) {
                    if (number == arrInt.get(i)) {
                        txtView.setText("your Search number is :" + arrInt.get(i));
                    }
                }
                clear(edtText);

            } else if (b == btnReverse) {
                clear(txtView);
                for (i = 0, j = arrInt.size() - 1; i < j; i++, j--) {
                    temp = arrInt.get(i);
                    //arrInt.get(i)=arrInt.get(arrInt.size()-1-i);
                    arrInt.set(i, arrInt.get(j));
                    arrInt.set(j, temp);

                }
                for (i = 0; i < arrInt.size(); i++) {
                    txtView.append("" + arrInt.get(i));
                }
                clear(edtText);

            } else if (b == btnFindIndex) {
                int number = Integer.parseInt(edtText.getText().toString());
                for (i = 0; i < arrInt.size(); i++) {
                    if (arrInt.get(i) == number) {
                        txtView.setText("index of your search number is : " + i);
                    }
                }
                clear(edtText);

            } else if (b == btnFindMax) {
                clear(txtView);
                int max;
                max = arrInt.get(0);
                for (i = 0; i < arrInt.size(); i++) {
                    if (max < arrInt.get(i)) {
                        max = arrInt.get(i);
                    }
                }
                txtView.setText("max number is :" + max);
                clear(edtText);

            } else if (b == btnSort) {
                clear(txtView);

                for (i = 0; i < arrInt.size(); i++) {
                    for (j = i + 1; j < arrInt.size(); j++) {
                        if (arrInt.get(i) > arrInt.get(j)) {
                            temp = arrInt.get(i);
                            arrInt.set(i, arrInt.get(j));
                            arrInt.set(j, temp);
                        }
                    }
                }
                for (i = 0; i < arrInt.size(); i++) {
                    txtView.append("" + arrInt.get(i));
                }
            }
            clear(edtText);

        }catch (NumberFormatException e){
            txtView.setText("Enter Valide Value");
        }
    }

    private void displayText(){
        clear(txtView);
        for(int i = 0; i<arrInt.size(); i++){
        txtView.append(""+arrInt.get(i));
        }
    }
    private void clear(EditText edt){
        edt.setText("");
    }
    private void clear(TextView txt){
        txt.setText("");
    }

}
