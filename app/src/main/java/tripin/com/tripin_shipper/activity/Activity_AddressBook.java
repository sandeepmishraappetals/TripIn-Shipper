package tripin.com.tripin_shipper.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import tripin.com.tripin_shipper.R;

/**
 * Created by Android on 29/08/16.
 */
public class Activity_AddressBook extends ListActivity implements View.OnClickListener{

    Button btnAdd,btnGetAll;
    TextView student_Id;
    TextView student_name;
    ImageButton Edit;

    @Override
    public void onClick(View view) {
        if (view== findViewById(R.id.btnAdd)){

            Intent intent = new Intent(this,Activity_Address_page.class);
            intent.putExtra("student_Id",0);
            startActivity(intent);

        }else {

            StudentRepo repo = new StudentRepo(this);

            ArrayList<HashMap<String, String>> studentList =  repo.getStudentList();
            if(studentList.size()!=0) {
                ListView lv = getListView();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        student_Id = (TextView) view.findViewById(R.id.student_Id);
                        student_name = (TextView) view.findViewById(R.id.student_name);
                        String studentId = student_Id.getText().toString();
                        String studentName = student_name.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(),Activity_Address_page.class);
                        objIndent.putExtra("student_Id", Integer.parseInt( studentId));
                        objIndent.putExtra("Student Name", studentName);
                        startActivity(objIndent);
                    }
                });
                ListAdapter adapter = new SimpleAdapter( Activity_AddressBook.this,studentList, R.layout.view_student_entry, new String[] { "id","name"}, new int[] {R.id.student_Id, R.id.student_name});
                setListAdapter(adapter);
            }else{
                Toast.makeText(this,"No student!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressbook);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnGetAll = (Button) findViewById(R.id.btnGetAll);
        btnGetAll.setOnClickListener(this);


            StudentRepo repo = new StudentRepo(this);

            ArrayList<HashMap<String, String>> studentList =  repo.getStudentList();
            if(studentList.size()!=0) {
                ListView lv = getListView();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        student_Id = (TextView) view.findViewById(R.id.student_Id);
                        student_name = (TextView) view.findViewById(R.id.student_name);
                        Edit = (ImageButton)view.findViewById(R.id.imageButton_edit);
                        String studentId = student_Id.getText().toString();
                        String studentName = student_name.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(),Activity_Address_page.class);
                        objIndent.putExtra("student_Id", Integer.parseInt( studentId));
                        objIndent.putExtra("Student Name", studentName);
                       /* Edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                             Intent   objIndent1 = new Intent(getApplicationContext(),Activity_Address_page.class);
                                startActivity(objIndent1);
                            }
                        });*/
                        startActivity(objIndent);
                    }
                });
                ListAdapter adapter = new SimpleAdapter( Activity_AddressBook.this,studentList, R.layout.view_student_entry, new String[] { "id","name"}, new int[] {R.id.student_Id, R.id.student_name});
                setListAdapter(adapter);
            }else{
                Toast.makeText(this,"No student!", Toast.LENGTH_SHORT).show();
            }

    }
}
