import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class VA {

    int size=0;
    int total=0;
    String cadena="";
    ArrayList<Integer> listaEntrada= new ArrayList<Integer>();
	ArrayList<ArrayList<Integer>> resultado= new ArrayList<ArrayList<Integer>>();
    //En caso de querer sobrescribir el fichero de salida poner a true:
    boolean sobrescribir=true;
    
	public static void main(String[] args) {
		//Ejemplo:
		// new VA("-t","in","out");
		new VA(args[0],args[1],args[2]);
	}
	
	public VA(String select, String f_entrada, String f_salida){
		boolean traza=(select.trim().equals("-t") || select.trim().equals("-T"));
        boolean help=(select.trim().equals("-h") || select.trim().equals("-H"));
        if(!traza && !help ){
            f_salida=f_entrada;
            f_entrada=select;
        }
        if(f_entrada.isEmpty())System.out.println("Seleccione archivo de entrada: "+f_entrada);
        //Solo se permitiran archivos con formato *.txt, en caso de que no se ponga expresamente se añadira por el programa.
        if(!f_entrada.endsWith(".txt"))f_entrada=f_entrada+".txt";
        if(!f_salida.isEmpty() && !f_salida.endsWith(".txt"))f_salida=f_salida+".txt";
        //(-h)OPCION HELP o AYUDA
        if (help){
            System.out.println("SINTAXIS:");
            System.out.println("servicio [-t] [-h] [fichero_entrada] [fichero_salida]");
            System.out.println("-t\t\t\t Traza de la ejecucion");
            System.out.println("-h\t\t\t Muestra esta ayuda");
            System.out.println("fichero_entrada\t\t Nombre del fiechero de entrada");
            System.out.println("fichero_salida\t\t Nombre del fichero de salida");
        }               
        //Caso con archivo de entrada
        else if(!f_entrada.isEmpty()){
            try {
                //Lectura del fichero de entrada
                BufferedReader lector = new BufferedReader(new FileReader(f_entrada));
                String linea = lector.readLine();
                String[] cadena = linea.split(" ");
                for(int i=0; i<cadena.length;i++) listaEntrada.add(Integer.parseInt(cadena[i]));
                linea = lector.readLine();
                size=Integer.parseInt(linea);
                linea = lector.readLine();
                total=Integer.parseInt(linea);
                lector.close();
            }
            catch (Exception e){
            	System.out.println("Ha ocurrido un error no previsto\n"+e.getMessage());
            	System.exit(1);
            }            
        }
        else{System.out.println("Comando incorrecto");}
        //Casos de salida de fichero
        try {
        	cadena=algoritmo(listaEntrada,size,total);
            //Sin archivo de salida, imprimimos por pantalla
            if(f_salida.isEmpty()){
                System.out.println(cadena);    
            }
            //Guardamos el contenido en el archivo de salida
            else if(f_salida!=null || !f_salida.isEmpty()) {
                File nuevo=new File(f_salida);
                String ruta=nuevo.getAbsolutePath();
                File archivo=new File(ruta);
                if(!sobrescribir && archivo.exists()){
                    System.out.println("Error, no se permite sobreescribrir.");
                }
                else {
                	PrintWriter writer = new PrintWriter(archivo); 
                	writer.print(cadena); 
                	writer.close(); 
                }  
            }
        }
        catch(Exception e){System.out.println("Ha ocurrido un error no previsto");}
    }
	
	
	public String algoritmo(ArrayList<Integer> nums, int size, int total) {
		String cadena="";
		//Inicializamos el conjunto vacio, nuestra lista a rellenar
		ArrayList<Integer> list = new ArrayList<Integer>();
		//Ejecutamos nuestro algoritmo de vuelta atras
		vueltaAtras(list,nums,total,size);
		//Filtramos el resultado para eliminar duplicados
		filtrarResultado(resultado);
		//Imprimimos nuestro resultado por pantalla
		for(ArrayList<Integer> a: resultado) {
			for(Integer i: a) {
				cadena+=i+"\t";						
			}
			cadena+="\n";
		}
		return cadena;
	}

	//Metodo para filtrar los duplicados del resultado.
	private void filtrarResultado(ArrayList<ArrayList<Integer>> resultadoCompleto) {
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
	private boolean sonIguales(ArrayList<Integer> ali, ArrayList<Integer> aux) {
		for(int a : ali) {
			if(!aux.contains(a)) return false;
		}
		return true;
	}

	//ALGORITMO VUELTA ATRAS
	@SuppressWarnings("unchecked")
	private void vueltaAtras(ArrayList<Integer> list, ArrayList<Integer> todos, int total, int size) {
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
					vueltaAtras(list,todosAux,total,size);
				}
			}
			list.remove(list.size()-1);			
			index++;
		}
	}

	//Comprobacion de si nuestro conjunto de datos aun no ha superado los limites y se puede completar
	private boolean completable(ArrayList<Integer> list, int total, int size) {
		int suma=0;
		for(int i : list) {suma+=i;}
		if(list.size()<size && suma<total) return true;
		return false;
	}

	//Comprobamos si nuestro conjunto es una solución válida
	private boolean esSolucion(ArrayList<Integer> list, int total, int size) {
		int suma=0;
		for(int i : list) {suma+=i;}
		if(suma==total && list.size()==size) return true;
		return false;
	}

	//Comprobamos si aun quedan opciones pendientes en nuestro conjunto
	private boolean OpcionesPendientes(ArrayList<Integer> list, int total, int size) {
		int suma=0;
		for(int i : list) {suma+=i;}
		if(list.size() <= size && suma <= total) return true;
		return false;
	}

}
