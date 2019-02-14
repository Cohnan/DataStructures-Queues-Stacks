package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import com.opencsv.CSVReader;


import model.data_structures.IQueue;
import model.data_structures.IStack;
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

	/**
	 * Carga la informacion sobre infracciones de los archivos a una pila y una cola ordenadas por fecha.
	 */
	public void loadMovingViolations() {
		CSVReader readerJan = null;
		CSVReader readerFeb = null;
		try {
			readerJan = new CSVReader(new FileReader("data/Moving_Violations_Issued_in_January_2018_ordered.csv"));
			readerFeb = new CSVReader(new FileReader("data/Moving_Violations_Issued_in_February_2018_ordered.csv"));
			
			movingViolationsStack = new Stack<VOMovingViolations>();
			movingViolationsQueue = new Queue<VOMovingViolations>();

			VOMovingViolations infraccion;
			
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

	/**
	 * Usa una cola interna para retornar las siguientes estadísticas de todos los días: 
	 * fecha del día, número de accidentes, número de infracciones y suma total de FINEAMT de las 
	 * infracciones ese día. 
	 * @return Cola con las estadisticas de cada dia.
	 * Pos: La respuesta esta ordenada por la fecha del día
	 */
	public IQueue <VODaylyStatistic> getDailyStatistics () {
		
		// Guarda la cola que se mandar� como respuesta
		Queue<VODaylyStatistic> respuesta = new Queue<>();
		
		// Para recorrer toda la cola se usa su iterador
		Iterator<VOMovingViolations> iterador = movingViolationsQueue.iterator();
		VOMovingViolations actual = iterador.next();
		
		// Se coge el primer d�a en la lista
		String dia = actual.getTicketIssueDate();
		String auxiliar = dia.substring(0,10);
		
		// Cola auxiliar para agrupar infracciones por fecha
		Queue<VOMovingViolations> colaDiaActual = new Queue<>();
	
		
		while(true)
		{
			String dia2 = actual.getTicketIssueDate();
			String auxiliar2 = dia2.substring(0,10);

				//Si se repite la fecha se encola el elemento actual
				if(auxiliar.equals(auxiliar2)){
					colaDiaActual.enqueue(actual);
					if (!iterador.hasNext()) {
						break;
					}
					actual = iterador.next();
				}
				else
				{	
				// Si no se repita, se hace una DaylyStatistic con los elementos encontrados y se revisa el siguiente dia
				// Funciona gracias a que la cola esta ordenada por fecha
					VODaylyStatistic agregar = new VODaylyStatistic(colaDiaActual, auxiliar);
					respuesta.enqueue(agregar);
					
					colaDiaActual = new Queue<>();
					dia = actual.getTicketIssueDate();
					auxiliar = dia.substring(0,10);
				}				
			
			}
	
		
		// Para el ultimo dia
		VODaylyStatistic agregar = new VODaylyStatistic(colaDiaActual, auxiliar);
		respuesta.enqueue(agregar);
		return respuesta;

	}

	/**
	 * Retorna la información de las ultimas “n” infracciones que tuvieron un accidente.
	 * @param n Indicador de cuantas infracciones, a lo sumo, se desean conocer.
	 * Si n es muy grande, se devuelve el maximo de infracciones que tuvieron accidente.
	 * @return Pila con las infracciones deseadas
	 */
	public IStack <VOMovingViolations> nLastAccidents(int n) {
		// Como en la pila se agregaron primero las infracciones de enero y luego las de febrero
		// las cuales ademas estaban ordenadas descendentemente por fecha de la infraccion, basta
		// mirar los primeros elementos que salen de la cola hasta que se tengan n de ellos que
		// cumplan la condicion
		
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
