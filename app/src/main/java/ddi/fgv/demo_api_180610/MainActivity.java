package ddi.fgv.demo_api_180610;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnGuardar;
    private Button btnBuscar;
    private Button btnEliminar;
    private Button btnActualizar;
    private EditText etCodigoBarras;
    private EditText etDescripcion;
    private EditText etMarca;
    private EditText etPrecioCompra;
    private EditText etPrecioVenta;
    private EditText etExistencias;
    private ListView lvProductos;

    private RequestQueue colaPeticiones;
    private JsonArrayRequest jsonArrayRequest;

    private ArrayList<String> origenDatos = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private String url = "http://10.10.62.4:3300/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGuardar=findViewById(R.id.btnGuardar);
        btnBuscar=findViewById(R.id.btnBuscar);
        btnEliminar=findViewById(R.id.btnEliminar);
        btnActualizar=findViewById(R.id.btnActualizar);

        etCodigoBarras=findViewById(R.id.etCodigoBarras);
        etDescripcion=findViewById(R.id.etDescripcion);
        etMarca=findViewById(R.id.etMarca);
        etPrecioCompra=findViewById(R.id.etPrecioCompra);
        etPrecioVenta=findViewById(R.id.etPrecioVenta);
        etExistencias=findViewById(R.id.etExistencia);

        lvProductos=findViewById(R.id.lbProductos);



        colaPeticiones= Volley.newRequestQueue(this);
        listarProducto();

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject producto = new JSONObject();
                try {
                    producto.put("codigobarras",etCodigoBarras.getText().toString());
                    producto.put("descripcion",etDescripcion.getText().toString());
                    producto.put("marca",etMarca.getText().toString());
                    producto.put("preciocompra",Float.parseFloat(etPrecioCompra.getText().toString()));
                    producto.put("precioventa",Float.parseFloat(etPrecioVenta.getText().toString()));
                    producto.put("existencias",Integer.parseInt(etExistencias.getText().toString()));
                } catch (JSONException e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.DELETE, url + "borrar/" + etCodigoBarras.getText().toString(), producto,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    if (response.getString("Status").equals("Producto Eliminado")){
                                        Toast.makeText(MainActivity.this, "Producto Eliminado con exito", Toast.LENGTH_SHORT).show();
                                        etCodigoBarras.setText("");
                                        etDescripcion.setText("");
                                        etMarca.setText("");
                                        etPrecioCompra.setText("");
                                        etPrecioVenta.setText("");
                                        etExistencias.setText("");
                                        adapter.clear();
                                        lvProductos.setAdapter(adapter);
                                        listarProducto();
                                    }
                                }catch (JSONException e){
                                    Toast.makeText(MainActivity.this, "No se Elimino", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Eliminado correctamente ", Toast.LENGTH_SHORT).show();
                    }
                }
                );
                colaPeticiones.add(jsonObjectRequest);
                etCodigoBarras.setText("");
                etDescripcion.setText("");
                etMarca.setText("");
                etPrecioCompra.setText("");
                etPrecioVenta.setText("");
                etExistencias.setText("");
                adapter.clear();
                lvProductos.setAdapter(adapter);
                listarProducto();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject producto = new JSONObject();
                try {
                    producto.put("descripcion",etDescripcion.getText().toString());
                    producto.put("marca",etMarca.getText().toString());
                    producto.put("preciocompra",Float.parseFloat(etPrecioCompra.getText().toString()));
                    producto.put("precioventa",Float.parseFloat(etPrecioVenta.getText().toString()));
                    producto.put("existencias",Integer.parseInt(etExistencias.getText().toString()));
                } catch (JSONException e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.PUT, url+"actualizar/"+etCodigoBarras.getText().toString(), producto,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    if(response.getString("status").equals("Producto Actualizado")){
                                        Toast.makeText(MainActivity.this, "Producto Actualizado con exito", Toast.LENGTH_SHORT).show();
                                        etCodigoBarras.setText("");
                                        etDescripcion.setText("");
                                        etMarca.setText("");
                                        etPrecioCompra.setText("");
                                        etPrecioVenta.setText("");
                                        etExistencias.setText("");
                                        adapter.clear();
                                        lvProductos.setAdapter(adapter);
                                        listarProducto();
                                    }
                                } catch(JSONException e){
                                    Toast.makeText(MainActivity.this, "No se pudo", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "la wea no funco", Toast.LENGTH_SHORT).show();
                    }
                }
                );
                colaPeticiones.add(jsonObjectRequest);
                etCodigoBarras.setText("");
                etDescripcion.setText("");
                etMarca.setText("");
                etPrecioCompra.setText("");
                etPrecioVenta.setText("");
                etExistencias.setText("");
                adapter.clear();
                lvProductos.setAdapter(adapter);
                listarProducto();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest producto = new JsonObjectRequest(
                        Request.Method.GET,
                        url + etCodigoBarras.getText().toString(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("status")){
                                    Toast.makeText(MainActivity.this, "Producto encontrado", Toast.LENGTH_SHORT).show();
                                }else{
                                    try {
                                        etDescripcion.setText(response.getString("descripcion"));
                                        etMarca.setText(response.getString("marca"));
                                        etPrecioCompra.setText(String.valueOf(response.getInt("preciocompra")));
                                        etPrecioVenta.setText(String.valueOf(response.getInt("precioventa")));
                                        etExistencias.setText(String.valueOf(response.getInt("existencias")));


                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                colaPeticiones.add(producto);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject producto = new JSONObject();
                try {
                    producto.put("codigobarras",etCodigoBarras.getText().toString());
                    producto.put("descripcion",etDescripcion.getText().toString());
                    producto.put("marca",etMarca.getText().toString());
                    producto.put("preciocompra",Float.parseFloat(etPrecioCompra.getText().toString()));
                    producto.put("precioventa",Float.parseFloat(etPrecioVenta.getText().toString()));
                    producto.put("existencias",Integer.parseInt(etExistencias.getText().toString()));
                } catch (JSONException e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, url+"insertar/", producto,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getString("status").equals("Producto insertado")){
                                Toast.makeText(MainActivity.this, "Producto insertado con exito", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                listarProducto();
                            }
                        } catch(JSONException e){
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                );
                colaPeticiones.add(jsonObjectRequest);
                etCodigoBarras.setText("");
                etDescripcion.setText("");
                etMarca.setText("");
                etPrecioCompra.setText("");
                etPrecioVenta.setText("");
                etExistencias.setText("");
                adapter.clear();
                lvProductos.setAdapter(adapter);
                listarProducto();
            }
        });
    }
    protected void listarProducto(){
        jsonArrayRequest=new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++){
                            try {
                                String codigobarras=response.getJSONObject(i).getString("codigobarras");
                                String descripcion = response.getJSONObject(i).getString("descripcion");
                                String marca = response.getJSONObject(i).getString("marca");

                                origenDatos.add(codigobarras+"::"+descripcion+"::"+marca);

                            } catch (JSONException e) {

                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        adapter=new ArrayAdapter<>(MainActivity.this,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                origenDatos);
                        lvProductos.getAdapter();
                        lvProductos.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        colaPeticiones.add(jsonArrayRequest);
    }
}