package com.example.entrega1;

import android.graphics.Typeface;
import android.os.Bundle;

import android.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Mejoras#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mejoras extends Fragment {

    private ListView listView;

    private Integer imagenes[] = {
            R.drawable.marte,
            R.drawable.marte,
            R.drawable.marte,
            R.drawable.marte_mar

    };
    private String nombres[] = {
            "Mar",
            "Rios",
            "Hierba",
            "Arena"
    };

    private Integer cantidades[] = {
            1,
            5,
            20,
            80
    };

    private Integer precios[] = {
            10,
            50,
            200,
            800
    };





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Mejoras() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mejoras.
     */
    // TODO: Rename and change types and number of parameters
    public static Mejoras newInstance(String param1, String param2) {
        Mejoras fragment = new Mejoras();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mejoras, container, false);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        // Setting header
        TextView textView = new TextView(this.getActivity());
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText("Lista de mejoras");

        Log.i("algo", getView().toString());
        ListView listView=getView().findViewById(R.id.lista);
        listView.addHeaderView(textView);

        // For populating list data
        AdaptadorMejoras customCountryList = new AdaptadorMejoras(getActivity(), imagenes, nombres, cantidades, precios);
        listView.setAdapter(customCountryList);

    }
}