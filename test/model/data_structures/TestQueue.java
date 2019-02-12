package model.data_structures;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import junit.framework.TestCase;

public class TestQueue extends TestCase{


	private Queue<String> cola;

	// Pila vacia
	private void setUpEscenario0() {
		cola = new Queue<String>();
	}

	// Cola con 1 elemento
	private void setUpEscenario1() {
		cola = new Queue<String>();
		cola.enqueue("Elemento 1");
	}

	// Pila con 2 elementos
	private void setUpEscenario2() {
		cola = new Queue<String>();
		cola.enqueue("Elemento 1");
		cola.enqueue("Elemento 2");
	}

	/*
	 * Metodos para Pruebas
	 */
	/**
	 * Prueba el constructor
	 */
	public void testQueue() {
		setUpEscenario0();
		assertEquals("La cola deber�a estar vac�a", true,cola.isEmpty());
		assertEquals("La cola deber�a estar vac�a",0,cola.size());
		assertEquals("Al quitar un elemento de la cola, debe retornar null",null, cola.dequeue());
	}


	/**
	 * Prueba el metodo isEmpty()
	 */
	public void testIsEmpty() {
		for (int i = 0; i <= 2; i++) {
			if 		(i == 0) {
				setUpEscenario0();
				assertEquals("La cola deber�a estar vac�a",true,cola.isEmpty());
			}
			else if (i == 1) {
				setUpEscenario1();
				assertEquals("La cola deberia NO estar vacia.", false,cola.isEmpty());
			}
			else if (i == 2) {
				setUpEscenario2();
				assertEquals("La cola deberia NO estar vacia.", false,cola.isEmpty());
			}

		}
	}

	/**
	 * Prueba el metodo size
	 */
	public void testSize() {
		for (int i = 0; i <= 2; i++) {
			if 		(i == 0) {
				setUpEscenario0();
				assertEquals("La cola deber�a tener tama�o 0", 0,cola.size());
			}
			else if (i == 1) {
				setUpEscenario1();
				assertEquals("La cola deber�a tener tama�o 1", 1,cola.size());
			}
			else if (i == 2) {
				setUpEscenario2();
				assertEquals("La cola deber�a tener tama�o 2", 2,cola.size());
			}

		}
	}

	/**
	 * Prueba el metodo Enqueue
	 */
	public void testEnqueue() {
		for (int i = 0; i <= 1; i++) {
			if 		(i == 0) {
				setUpEscenario0();
				cola.enqueue("Nuevo Elemento");
				assertEquals("La cola deber�a tener tama�o 1", 1,cola.size());
				assertEquals("El primer elemento deber�a ser Nuevo Elemento","Nuevo Elemento",cola.iterator().next());

			}
			else if (i == 1) {
				setUpEscenario2();
				cola.enqueue("Nuevo Elemento");
				Iterator<String> it = cola.iterator();
				assertEquals("Deber�a tener elementos sobre los cuales iterar",true,it.hasNext());
				String dato = it.next();
				assertEquals("El primer elemento deber�a ser el recien anadido","Nuevo Elemento",cola.iterator().next());
				assertEquals("La cola deber�a tener tama�o 3", 3,cola.size());
			}

		}
	}

	/**
	 * Prueba el metodo dequeue
	 */
	public void testDequeue() {
		for (int i = 0; i <2; i++) {
			//			if 		(i == 0) {
			//				setUpEscenario0();
			//				assertEquals("La pila deberia seguir de tamano 0.",0,cola.size());
			//				assertEquals("Deber�a retornar null",null,cola.dequeue());
			//				
			//			}
			if (i == 1) {
				setUpEscenario1();
				String dato = cola.dequeue();
				assertEquals("La cola deber�a estar vac�a",true,cola.isEmpty());
				assertEquals("El elemento eliminado no es el esperado.","Elemento 1",dato);
			}

		}
	}






}
