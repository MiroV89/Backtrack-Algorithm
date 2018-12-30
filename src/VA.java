import java.util.ArrayList;
import java.util.Random;

public class VA {

	public static ArrayList<ArrayList<Integer>> resultado= new ArrayList<ArrayList<Integer>>();
	public static void main(String[] args) {
		//Inicializamos la lista (esto se lee de fichero de texto)
		ArrayList<Integer> todos = new ArrayList<Integer>();
		int max=50;
		int min=1;
		Random rand= new Random();;
		for(int i=1;i<25;i ++) {
			todos.add(rand.nextInt((max - min) + 1) + min);
		}
		//Asignamos el valor de suma buscado y el tamaño de la lista:
		int total=100;
		int size=7;
		//Inicializamos el conjunto vacio, nuestra lista a rellenar
		ArrayList<Integer> list = new ArrayList<Integer>();
		//Ejecutamos nuestro algoritmo va
		va(list,todos,total,size);
		//Filtramos el resultado para eliminar duplicados
		filtrarResultado(resultado);
		//Imprimimos nuestro resultado por pantalla
		for(ArrayList<Integer> a: resultado) {
				System.out.println(a);
		}
	}

	//Metodo para filtrar los duplicados del resultado.
	private static void filtrarResultado(ArrayList<ArrayList<Integer>> resultadoCompleto) {
		ArrayList<ArrayList<Integer>> resultadoFinal=new ArrayList<ArrayList<Integer>>();
		for(ArrayList<Integer> ali : resultadoCompleto) {
			boolean duplicado=false;
			for(ArrayList<Integer> aux : resultadoFinal) {
				if(sonIguales(ali,aux)) {
					duplicado=true;
				}
			}
			if(!duplicado) {
				resultadoFinal.add(ali);
			}
		}
		resultado=resultadoFinal;
		
	}

	//Metodo para comprobar si 2 listas de enteros contienen los mismos elementos
	//Utilizado para filtrarResultado
	private static boolean sonIguales(ArrayList<Integer> ali, ArrayList<Integer> aux) {
		for(int a : ali) {
			if(!aux.contains(a)) return false;
		}
		return true;
	}

	//ALGORITMO VUELTA ATRAS
	@SuppressWarnings("unchecked")
	private static void va(ArrayList<Integer> list, ArrayList<Integer> todos, int total, int size) {
		int index=0;
		while (OpcionesPendientes(list,total,size) && index<todos.size()) {
			//Estas 2 lineas fuerzan a no coger duplicados (5+5 para sumar 10 por ejemplo) de la misma lista
			ArrayList<Integer> todosAux = (ArrayList<Integer>) todos.clone();
			todosAux.remove(index);
			//Escogemos la siguiente opcion de entre todos los elementos y la añadimos a la lista temporal
			int siguienteOpcion = todos.get(index);
			list.add(siguienteOpcion);
			//Comprobamos si es una solución válida, en tal caso añadimos como resultado
			if(esSolucion(list,total,size)) {
				resultado.add((ArrayList<Integer>) list.clone());
			}
			//En caso de no ser una solución, comprobamos si puede seguir extendiendose y llamamos de nuevo al algoritmo
			else {
				if(completable(list,total,size)) {
					va(list,todosAux,total,size);
				}
			}
			list.remove(list.size()-1);			
			index++;
		}
	}

	//Comprobacion de si nuestro conjunto de datos aun no ha superado los limites y se puede completar
	private static boolean completable(ArrayList<Integer> list, int total, int size) {
		int suma=0;
		for(int i : list) {suma+=i;}
		if(list.size()<size && suma<total) return true;
		return false;
	}

	//Comprobamos si nuestro conjunto es una solución válida
	private static boolean esSolucion(ArrayList<Integer> list, int total, int size) {
		int suma=0;
		for(int i : list) {suma+=i;}
		if(suma==total && list.size()==size) return true;
		return false;
	}

	//Comprobamos si aun quedan opciones pendientes en nuestro conjunto
	private static boolean OpcionesPendientes(ArrayList<Integer> list, int total, int size) {
		int suma=0;
		for(int i : list) {suma+=i;}
		if(list.size() <= size && suma <= total) return true;
		return false;
	}

}
