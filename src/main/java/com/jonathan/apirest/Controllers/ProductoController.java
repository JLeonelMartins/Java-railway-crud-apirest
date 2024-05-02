package com.jonathan.apirest.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathan.apirest.Entities.Producto;
import com.jonathan.apirest.Repositories.ProductoRepository;



// @RestController: Esta es una anotación de Spring que combina las anotaciones @Controller y @ResponseBody, 
// lo que significa que esta clase será un controlador REST que responderá a las solicitudes HTTP con objetos serializados en JSON.
// @RequestMapping("/productos"): Esta anotación especifica la ruta base para todos los métodos de este controlador. 
// Significa que todos los métodos de este controlador responderán a las solicitudes HTTP que comiencen con /productos.
@RestController
@RequestMapping("/productos")
public class ProductoController {
    // @Autowired private ProductoRepository productoRepository;: Esta línea utiliza la anotación 
    // @Autowired para inyectar una instancia de ProductoRepository en este controlador. 
    // Esto permite que el controlador acceda a los métodos definidos en ProductoRepository 
    // para interactuar con la base de datos.
    @Autowired
    private ProductoRepository productoRepository;

    // Este archivo define un controlador REST para la entidad Producto, 
    // que proporciona puntos finales para realizar operaciones CRUD en productos 
    // utilizando Spring Boot y JPA.

    //El GetMapping sirve para traer, en este caso, una lista de productos
    @GetMapping
    //Este metodo va a traer una lista de productos
    public List<Producto> getAllProductos() {
        //a traves del ProductoRepositorie va a hacer la conexcion con la BD y lo trae automaticamente
        return productoRepository.findAll();
    }

    //El PostMapping sirve para agrega un producto
    @PostMapping
    //RequestBody quiere decir que le vamos a tener que pasar un JSON, es la peticion que hace el cliente al servidor
    public Producto createProducto(@RequestBody Producto producto) {
        //Con este save estamos guardando en la BD el producto
        return productoRepository.save(producto);
    }

    @GetMapping("/{id}")
    //El metodo recibe a traves del PathVariable el ID del producto a buscar
    public Producto getProductoById(@PathVariable Long id) {
        //Este es el retorno en caso de si el producto fue encontrado
        return productoRepository.findById(id)
                //Caso contrario si no se encontro, lanzamos una exception de error
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    //El PutMapping sirve para hacer actualizaciones
    @PutMapping("/{id}")
    //El metodo recibe a traves del PathVariable el ID del producto a buscar, y a su vez el RequestBody o JSON  a modificar
    public Producto updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        //Buscamos el producto por el ID y le decimos que lo guarde en la variable de Producto
        Producto producto = productoRepository.findById(id)
                //Caso contrario si no se encontro, lanzamos una exception de error
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        //En caso de encontrar el producto, le actualizamos el nombre y el precio
        producto.setNombre(productoDetails.getNombre());
        producto.setPrecio(productoDetails.getPrecio());
        //Y por ultimo guarda el producto actualizado
        return productoRepository.save(producto);
    }

    //Delete para borrar productos
    @DeleteMapping("/{id}")
    //Buscamos el producto por ID
    public String deleteProducto(@PathVariable Long id) {
        //Si lo encontramos lo guardamos en la variable producto
        Producto producto = productoRepository.findById(id)
                //Caso contrario si no se encontro, lanzamos una exception de error
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        //El producto encontrado se elimina
        productoRepository.delete(producto);
        //Y retornamos un String para que el usuario lo sepa
        return "Producto con el ID " + id + " eliminado correctamente.";
    }
}