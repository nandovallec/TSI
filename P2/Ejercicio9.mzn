% Ejercicio 9:
include "globals.mzn"; 

set of int: N_TAREA = 1..9;
array[N_TAREA] of string: TAREA = ["Levantar muros", "Carpinteria de tejado", "Poner tejado","Instalacion electrica", "Pintado fachada", "Ventanas","Jardin", "Techado", "Pintado interior"];

% Trabajadores disponibles
set of int:N_TRAB = 1..3;

% Duracion de cada tarea
array[N_TAREA, N_TRAB] of int : duracion =  [| 4, 7, 10| 3, 5, 7| 3, 1, 4| 2, 5, 8| 4, 2, 2| 3, 1, 1| 1, 1, 1| 1, 3, 3| 2, 2, 2|];

% Maxima duracion de todas las tareas
int : horizon = min([sum(i in N_TAREA) (duracion[i,j])| j in N_TRAB]); % horizonte temporal maximo
set of int: Time = 0..horizon;

% Tareas predecesoras
set of int: N_PRECE = 1..12;
array[N_PRECE, 1..2] of int : requisitos = [|1,2 |2,3 |1,4 |1,8 |3,5 |3,6 |3,7 |4,5 |4,6 |4,7 |6,9 |8,9 |];

% Duración minima y maxima entre las tareas
int: maxd = max([ duracion[t,m] | t in N_TAREA, m in N_TRAB ]);
int: mind = min([ duracion[t,m] | t in N_TAREA, m in N_TRAB ]);

% Definicion de variables y constantes:
array[N_TAREA] of var Time : t_comie;
array[N_TAREA] of var Time : t_fin;
array[N_TAREA] of var mind..maxd: D;
array[N_TAREA, N_TRAB] of var opt Time: t_comie_global;
var 1..100 : t_cons;

% Distribucion de los trabajdores
constraint 	cumulative(t_comie, D, [1 | i in N_TAREA], 3);

% Cada tarea se ejecuta una sola vez y obtenemos valores de comienzo y duración
constraint 	forall(t in N_TAREA)(alternative(t_comie[t], D[t], [t_comie_global[t,m] | m in N_TRAB], [duracion[t,m] | m in N_TRAB]));

% Cada trabajador solo puede estar en una tarea al mismo tiempo
constraint 	forall(m in N_TRAB)(disjunctive([t_comie_global[t,m] | t in N_TAREA], [duracion[t,m] | t in N_TAREA]) );

% Guardar el fin de cada tarea
constraint forall(t in N_TAREA) (t_fin[t] == t_comie[t] + D[t]);

% Comprobacion de las precedencias de cada tarea
constraint forall(i in N_PRECE) ( t_fin[ requisitos[i,1] ] <= t_comie[ requisitos[i,2] ] );

% Guardamos el tiempo mas alto de finalización como tiempo consumido en total
constraint maximum(t_cons, t_fin);

% Hacemos la parte de satisfaccion y la de minimizacion de tiempo total
solve minimize t_cons;

% Salida
output ["Tiempo Consumido: \(t_cons) días\n"] ++ ["\(TAREA[t]): Dias[\(t_comie[t]+1)-\(t_fin[t])]\n" | t in N_TAREA];
