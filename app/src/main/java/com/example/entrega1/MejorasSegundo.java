package com.example.entrega1;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MejorasSegundo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MejorasSegundo extends Fragment {

    private ListView listView;

    private Integer imagenes[] = {
            R.drawable.marte_mar,
            R.drawable.marte,
            R.drawable.marte,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar

    };
    private String nombres[] = {"Cohete", "Nave", "Ovni", "ISS", "Algo", "Algo", "Algo", "Algo", "Algo", "Algo", "Algo", "Algo"};

    private Integer cantidades[] = {1, 5, 20, 80, 100, 100, 100, 100, 100, 100, 100, 100, 100};

    private Integer precios[] = {10, 50, 200, 800, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MejorasSegundo() {
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
    public static MejorasSegundo newInstance(String param1, String param2) {
        MejorasSegundo fragment = new MejorasSegundo();
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
        ListView lista=getView().findViewById(R.id.lista);


        TextView titulo = new TextView(getActivity());
        titulo.setText("MEJORAS DE OXIGENO POR SEGUNDO");
        titulo.setTypeface(Typeface.DEFAULT_BOLD);

        titulo.setTextColor(Color.WHITE);
        titulo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        titulo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        lista.addHeaderView(titulo);

        AdaptadorMejorasSegundo mejoras = new AdaptadorMejorasSegundo(getActivity(), imagenes, nombres, cantidades, precios);
        lista.setAdapter(mejoras);


    }
}