package com.example.mobimech.homeui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobimech.adapters.OrdersRRecyclerViewAdapter
import com.example.mobimech.databinding.LogsfragBinding
import com.example.mobimech.models.OrderListItem

class LogsFrag : Fragment() {
    lateinit var logsfragBinding: LogsfragBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logsfragBinding= LogsfragBinding.inflate(inflater)

//        crating an arraylist to store users using the data class user
        val orderdt = ArrayList<OrderListItem>()

        //adding some dummy data to the list
        orderdt.add(OrderListItem("Juma","22/3/2021" ))
        orderdt.add(OrderListItem("David","23/3/2021" ))
        orderdt.add(OrderListItem("Elijah","24/3/2021" ))
        orderdt.add(OrderListItem("Paul","25/3/2021" ))

//        logsfragBinding.orderrecyclerview.adapter= OrdersRRecyclerViewAdapter(orderdt)
//        logsfragBinding.orderrecyclerview.layoutManager= LinearLayoutManager(activity)



        return logsfragBinding.root
    }

}

/**********************************MAIN ACTIVITY CONTINUATION************************/
//instance fields
DatabaseReference db;
FirebaseHelper helper;
CustomAdapter adapter;
ListView mListView;
EditText nameEditTxt, quoteEditText, descriptionEditText;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mListView = (ListView) findViewById(R.id.myListView);
    //initialize firebase database
    db = FirebaseDatabase.getInstance().getReference();
    helper = new FirebaseHelper(db, this, mListView);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mListView.smoothScrollToPosition(4);
            displayInputDialog();
        }
    });
}

//DISPLAY INPUT DIALOG
private void displayInputDialog() {
    //create input dialog
    Dialog d = new Dialog(this);
    d.setTitle("Save To Firebase");
    d.setContentView(R.layout.input_dialog);

    //find widgets
    nameEditTxt = d.findViewById(R.id.nameEditText);
    quoteEditText = d.findViewById(R.id.quoteEditText);
    descriptionEditText = d.findViewById(R.id.descEditText);
    Button saveBtn = d.findViewById(R.id.saveBtn);

    //save button clicked
    saveBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //get data from edittexts
            String name = nameEditTxt.getText().toString();
            String quote = quoteEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            //set data to POJO
            Teacher s = new Teacher();
            s.setName(name);
            s.setPropellant(quote);
            s.setDescription(description);

            //perform simple validation
            if (name != null && name.length() > 0) {
                //save data to firebase
                if (helper.save(s)) {
                    //clear edittexts
                    nameEditTxt.setText("");
                    quoteEditText.setText("");
                    descriptionEditText.setText("");

                    //refresh listview
                    ArrayList<Teacher> fetchedData = helper.retrieve();
                    adapter = new CustomAdapter(MainActivity.this, fetchedData);
                    mListView.setAdapter(adapter);
                    mListView.smoothScrollToPosition(fetchedData.size());
                }
            } else {
                Toast.makeText(MainActivity.this, "Name Must Not Be Empty Please", Toast.LENGTH_SHORT).show();
            }
        }
    });

    d.show();
}

}

