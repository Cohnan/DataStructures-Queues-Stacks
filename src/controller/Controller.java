package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;


import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.data_structures.Nodo;
import model.data_structures.Queue;
import model.data_structures.Stack;
import model.vo.VODaylyStatistic;
import model.vo.VOMovingViolations;
import view.MovingViolationsManagerView;

public class Controller {

	private MovingViolationsManagerView view;

	/**
	 * Cola donde se van a cargar los datos de los archivos
	 */
	private IQueue<VOMovingViolations> movingViolationsQueue;

	/**
	 * Pila donde se van a cargar los datos de los archivos
	 */
	private IStack<VOMovingViolations> movingViolationsStack;


	public Controller() {
		view = new MovingViolationsManagerView();

		//TODO, inicializar la pila y la cola
		movingViolationsQueue = null;
		movingViolationsStack = null;
	}

	public void run() {
		Scanner sc = new Scanner(System.in);
		boolean fin = false;

		while(!fin)
		{
			view.printMenu();

			int option = sc.nextInt();

			switch(option)
			{
			case 1:
				this.loadMovingViolations();
				break;

			case 2:
				IQueue<VODaylyStatistic> dailyStatistics = this.getDailyStatistics();
				view.printDailyStatistics(dailyStatistics);
				break;

			case 3:
				view.printMensage("Ingrese el número de infracciones a buscar");
				int n = sc.nextInt();

				IStack<VOMovingViolations> violations = this.nLastAccidents(n);
				view.printMovingViolations(violations);
				break;

			case 4:	
				fin=true;
				sc.close();
				break;
			}
		}
	}



	public void loadMovingViolations() {
		CSVReader readerJan = null;
		CSVReader readerFeb = null;
		try {
			readerJan = new CSVReader(new FileReader("data/Moving_Violations_Issued_in_January_2018_ordered.csv"));
			readerFeb = new CSVReader(new FileReader("data/Moving_Violations_Issued_in_February_2018_ordered.csv"));
			
			
			
			movingViolationsStack = new Stack<VOMovingViolations>();
			movingViolationsQueue = new Queue<VOMovingViolations>();

			VOMovingViolations infraccion;
			//System.out.println(readerJan.readNext()[0]); // Handle header
			//System.out.println(readerFeb.readNext()[0]); // Handle header
			boolean primeraFila = true;
			boolean primeraFila2 = true;
			
			
			for (String[] row : readerJan) {
				
				if(primeraFila){
					primeraFila = false;
				}
				else{
				infraccion = new VOMovingViolations(row);
				movingViolationsQueue.enqueue(infraccion);
				movingViolationsStack.push(infraccion);
				}
			}
			for (String[] row : readerFeb) {
				
				if(primeraFila2){
					primeraFila2 = false;
				}
				else{
				infraccion = new VOMovingViolations(row);
				movingViolationsQueue.enqueue(infraccion);
				movingViolationsStack.push(infraccion);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			if (readerJan != null) {
				try {
					readerJan.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (readerFeb != null) {
				try {
					readerFeb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public IQueue <VODaylyStatistic> getDailyStatistics () {
		
		
		// Guarda la cola que se mandar� como respuesta
		Queue<VODaylyStatistic> respuesta = new Queue<>();
		
		// Para recorrer toda la cola, coge el primer nodo
		Iterator<VOMovingViolations> iterador = movingViolationsQueue.iterator();
		VOMovingViolations actual = iterador.next();
		
		// Se coge el primer d�a en la lista
		String dia = actual.getTicketIssueDate();
		String auxiliar2 = dia.substring(0,10);
		
		// Cola auxiliar para agrupar infracciones por fecha
		Queue<VOMovingViolations> enviar = new Queue<>();
	
		
		while(iterador.hasNext())
		{
			String dia2 = actual.getTicketIssueDate();
			String auxiliar3 = dia2.substring(0,10);

			//Si se repite la fecha se encola el elemento actual
				if(auxiliar2.equals(auxiliar3)){
					enviar.enqueue(actual);
					actual = iterador.next();
				}
				else
				{
					
					// Si no se repita, se hace una DaylyStatistic con los elementos encontrados y se revisa el siguiente dia
					VODaylyStatistic agregar = new VODaylyStatistic(enviar, auxiliar2);
					respuesta.enqueue(agregar);
					enviar = new Queue<>();
					dia = actual.getTicketIssueDate();
					auxiliar2 = dia.substring(0,10);
				}				
			
			}
	
		
		// Para los �ltimos elementos de la cola
		VODaylyStatistic agregar = new VODaylyStatistic(enviar, auxiliar2);
		respuesta.enqueue(agregar);
		return respuesta;

	}

	// Returns a stack
	public IStack <VOMovingViolations> nLastAccidents(int n) {
		// Como en la pila se agregaron primero las infracciones de enero y luego las de febrero
		// las cuales ademas estaban ordenadas descendentemente por fecha de la infraccion, basta
		// mirar los primeros elementos que salen de la cola hasta que se tengan n de ellos que
		// cumplan la condicion
		
		//if (n >= movingViolationsStack.size()) throw new IllegalArgumentException("No es posible buscar un numero tan alto de accidentes");
		
		Iterator<VOMovingViolations> iterador = movingViolationsStack.iterator();
		IStack<VOMovingViolations> respuesta = new Stack<>();
		VOMovingViolations currentV;
		
		// Si no hay suficientes, retorna el maximo
		while (iterador.hasNext() && n > 0) {
			currentV = iterador.next();
			if (currentV.getAccidentIndicator()) {
				respuesta.push(currentV);
				n -= 1;
			}
		}
		
		return respuesta;
	}
}
