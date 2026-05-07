package com.roma.inmobiliariapro.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioViewModel extends ViewModel {

    public class MapaActual implements OnMapReadyCallback {

        LatLng miUbicacion = new LatLng(-33.280576, -66.332482);

        @Override
        public void onMapReady(GoogleMap googleMap) {

            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(miUbicacion)
                            .title("Mi ubicación")
            );

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(miUbicacion)
                    .zoom(15)
                    .bearing(0)
                    .tilt(30)
                    .build();

            googleMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(cameraPosition)
            );
        }
    }

    private MutableLiveData<MapaActual> mapaActualMutable;

    public LiveData<MapaActual> getMapaActual() {

        if (mapaActualMutable == null) {
            mapaActualMutable = new MutableLiveData<>();
        }

        return mapaActualMutable;
    }

    public void cargarMapa() {

        if (mapaActualMutable == null) {
            mapaActualMutable = new MutableLiveData<>();
        }

        mapaActualMutable.setValue(new MapaActual());
    }
}