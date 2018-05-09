package com.gwtask.gwtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    static HistoryAdapter adapter;
    private RecyclerView rv;
    private LinearLayoutManager hlm;
    private LinearLayout hl;
    private Button btnFindWord;
    private EditText txtZodis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        hl = (LinearLayout) findViewById(R.id.historyLayout);
        rv = (RecyclerView)findViewById(R.id.rv_history);
        hlm = new LinearLayoutManager(this);
        rv.setLayoutManager(hlm);
        adapter = new HistoryAdapter(Util.history);
        rv.setAdapter(adapter);


        btnFindWord = (Button)findViewById(R.id.btnFind);
        txtZodis = (EditText)findViewById(R.id.editWord);

        paimamIstorija();
        adapter.notifyDataSetChanged();

        btnFindWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtZodis.getText().length() > 0)
                {
                    if(DetectConnection.checkInternetConnection(getApplicationContext()))
                    {
                        Boolean pavyko = false;
                        try{
                            pavyko = new checkWordRequest().execute(txtZodis.getText().toString()).get();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), R.string.toast_error, Toast.LENGTH_SHORT).show();
                        }
                        if(pavyko == false) Toast.makeText(getApplicationContext(), R.string.toast_error, Toast.LENGTH_SHORT).show();
                        else
                        {
                            txtZodis.setText("");
                            afterPaieska();
                        }
                    }
                    else Toast.makeText(getApplicationContext(), R.string.toast_no_internet_error, Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), R.string.error_empty_word, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void afterPaieska(){
        Intent intent = new Intent(this, WordInfo.class);
        this.startActivity(intent);
    }

    private void paimamIstorija(){
        String gautiIssaugotiZodziai = getFromDB();
        if(gautiIssaugotiZodziai != "0")
        {
            String[] skelbimai = gautiIssaugotiZodziai.split(";;;");
            for(int i=1; i<skelbimai.length; i++)
            {
                String[] zodisTemp = skelbimai[i].split(";;");
                if(zodisTemp.length == 3)
                {
                    Zodis tempZ = new Zodis(zodisTemp[0],zodisTemp[1], "0",zodisTemp[2]);
                    Util.history.add(tempZ);
                }
            }
        }
    }
    private String getFromDB()
    {
        try{
            FileInputStream fis = MainActivity.this.openFileInput("paieskos.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            return "0";
        }
    }

    @Override
    public void onBackPressed() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    };
}
