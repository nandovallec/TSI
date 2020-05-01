% Ejercicio 8:
include "globals.mzn"; 

set of int: N_TAREA = 1..9;
array[N_TAREA] of string: TAREA = ["Levantar muros", "Carpinteria de tejado", "Poner tejado","Instalacion electrica", "Pintado fachada", "Ventanas","Jardin", "Techado", "Pintado interior"];

% Duracion de cada tarea
array[N_TAREA] of int : duracion =  [7, 3, 1, 8, 2, 1, 1, 3, 2];

% Maxima duracion de todas las tareas
int : horizon = sum(i in N_TAREA) (duracion[i]); % horizonte temporal maximo
set of int: Time = 0..horizon;

% Tareas predecesoras
set of int: N_PRECE = 1..12;
array[N_PRECE, 1..2] of int : requisitos = [|1,2 |2,3 |1,4 |1,8 |3,5 |3,6 |3,7 |4,5 |4,6 |4,7 |6,9 |8,9 |];

% Trabajadores disponibles
int : N_TRAB = 3;

% Trabajadores necesarios para cada tarea
array[N_TAREA] of int : TRAB  =  [2, 3, 2, 2, 1, 2, 1, 1, 2];


% Definicion de variables y constantes:
array[N_TAREA] of var Time : t_comie;
array[N_TAREA] of var Time : t_fin;
var Time : t_cons;


% Distribucion de los trabajdores
constraint cumulative(t_comie, duracion, TRAB, N_TRAB);
	
% Guardar el fin de cada tarea
constraint forall(i in N_TAREA) (t_fin[i] == t_comie[i] + duracion[i]);

% Comprobacion de las precedencias de cada tarea
constraint forall(i in N_PRECE) ( t_fin[ requisitos[i,1] ] <= t_comie[ requisitos[i,2] ] );

% Guardamos el tiempo mas alto de finalización como tiempo consumido en total
constraint maximum(t_cons, t_fin);

% Hacemos la parte de satisfaccion y la de minimizacion de tiempo total
solve minimize t_cons;

% Salida
output ["Tiempo Consumido: \(t_cons) días\n"] ++ ["\(TAREA[t]): Dias[\(t_comie[t]+1)-\(t_fin[t])]\n" | t in N_TAREA];
