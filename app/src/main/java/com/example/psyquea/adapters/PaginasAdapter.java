package com.example.psyquea.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.psyquea.fragments.chatsFragment;
import com.example.psyquea.fragments.citasFragment;
import com.example.psyquea.fragments.cuentaFragment;
import com.example.psyquea.fragments.expedientesFragment;
import com.example.psyquea.fragments.usuariosFragment;

//Barra de navegacion con las distintas opciones

public class PaginasAdapter extends FragmentStateAdapter {


    public PaginasAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){

            case 0:
                return new cuentaFragment();
            case 1:
                return new usuariosFragment();
            case 2:
                return new chatsFragment();
            case 3:
                return new citasFragment();
            case 4:
                return new expedientesFragment();
            /*case 5:
                return new solicitudesFragment();
            case 6:
                return new mis_solicitudes_Fragment();*/
            default:
                return new cuentaFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
