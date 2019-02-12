package model.data_structures;
/**
 * Representaci�n de un contenedor de informaci�n
 */
public class Nodo<T> {



	/**
	 * Lista encadenada sencilla cada nodo conoce el siguiente
	 */
	private Nodo<T> siguiente;

	/**
	 * Objeto (informaci�n) que almacena el nodo
	 */
	private T objeto;


	/**
	 * Constructor
	 */
	public Nodo(T pObjeto){
		objeto = pObjeto;
		siguiente = null;
	}


	/**
	 * M�todo para saber el siguiente nodo
	 */
	public Nodo<T> darSiguiente(){
		return siguiente;
	}

	/**
	 * M�todo para cambiar el siguiente nodo del nodo actual
	 */
	public void cambiarSiguiente(Nodo<T> pNuevoSiguiente){
		siguiente = pNuevoSiguiente;
	}

	/**
	 * Retorna el objeto que alamcena el nodo
	 */
	public T darObjeto(){
		return objeto;
	}


}
