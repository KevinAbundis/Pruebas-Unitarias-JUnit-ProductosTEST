package com.app.productos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //La anotación sirve para guardar en la bd real
@TestMethodOrder(OrderAnnotation.class) //Anotación para indicar el orden de ejecución de los test
public class ProductoTests {
	
	@Autowired //Inyectar dependencia
	private ProductoRepositorio repositorio;
	
	@Test //Prueba Unitaria
	@Rollback(false) //Desactivamos el rollback para guardar en bd real
					 //por defecto en las pruebas unitarias es true
	@Order(1) //Primer test a ejecutarse
	public void testGuardarProducto() {
		Producto producto = new Producto("Mouse Dell", 100);
		Producto productoGuardado = repositorio.save(producto);
		
		assertNotNull(productoGuardado);
	}
	
	@Test //Prueba Unitaria
	@Order(2) //Segundo test a ejecutarse
	public void testBuscarProductoPorNombre() {
		String nombre = "Computadora Dell";
		Producto producto = repositorio.findByNombre(nombre);
		
		assertThat(producto.getNombre()).isEqualTo(nombre);
	}
	
	@Test //Prueba Unitaria
	@Order(3) //Tercer test a ejecutarse
	public void testBuscarProductoPorNombreNoExistente() {
		String nombre = "Iphone 11";
		Producto producto = repositorio.findByNombre(nombre);
		
		assertNull(producto);
	}
	
	@Test //Prueba Unitaria
	@Rollback(false) 
	@Order(4) //Cuarto test a ejecutarse
	public void testActualizarProducto() {
		String nombreProducto = "Televisor Samsung HD"; //Nuevo valor para actualizar en bd
		Producto producto = new Producto(nombreProducto, 2000); //Valores nuevos
		producto.setId(1); //ID del producto a actualizar
		
		repositorio.save(producto);
		
		Producto productoActualizado = repositorio.findByNombre(nombreProducto);
		assertThat(productoActualizado.getNombre()).isEqualTo(nombreProducto);
	}
	
	@Test //Prueba Unitaria
	@Order(5) //Quinto test a ejecutarse
	public void testListarProductos() {
		List<Producto> productos = (List<Producto>) repositorio.findAll();
		
		for(Producto producto : productos) {
			System.out.println(producto);
		}
		
		assertThat(productos).size().isGreaterThan(0);
	}
	
	@Test //Prueba Unitaria
	@Rollback(false) 
	@Order(6) //Sexto test a ejecutarse
	public void testEliminarProducto() {
		Integer id = 3;
		
		boolean esExistenteAntesDeEliminar = repositorio.findById(id).isPresent();
		
		repositorio.deleteById(id);
		
		boolean noExisteDespuesDeEliminar = repositorio.findById(id).isPresent();
		
		assertTrue(esExistenteAntesDeEliminar);
		assertFalse(noExisteDespuesDeEliminar);
	}
}
