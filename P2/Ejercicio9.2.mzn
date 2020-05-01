% EJERCICIO 7: Construccion de casa %
include "globals.mzn"; 

	
int: horizon = 20; % time horizon
set of int: Time = 0..horizon;

% Tareas necesarias para construirla.
int: num_TAREA = 9;
set of int: N_TAREA = 1..9;
array[N_TAREA] of string: TAREA = ["Levantar muros", "Carpinteria de tejado", "Poner tejado",
                                        "Instalacion electrica", "Pintado fachada", "Ventanas",
                                        "Jardin", "Techado", "Pintado interior"];

% Trabajadores disponibles para hacer las tareas.
set of int:N_TRAB = 1..3;

% Duracion de cada una de las tareas anteriores.
array[N_TAREA, N_TRAB] of int : duracion =  [| 4, 7, 10, | 3, 5, 7,
                                                     | 3, 1, 4,
                                                     | 2, 5, 8,
                                                     | 4, 2, 2,
                                                     | 3, 1, 1,
                                                     | 1, 1, 1,
                                                     | 1, 3, 3,
                                                     | 2, 2, 2 |];

% Precedencia entre las tareas.
set of int: N_PRECE = 1..12;
array[N_PRECE, 1..2] of int : requisitos = [|1,2 |2,3 |1,4 |1,8 |3,5 |3,6 |3,7 |4,5  |4,6 |4,7 |6,9 |8,9 |];

int: maxd = max([ duracion[t,m] | t in N_TAREA, m in N_TRAB ]);
int: mind = min([ duracion[t,m] | t in N_TAREA, m in N_TRAB ]);

% Definicion de variables y constantes:
array[N_TAREA] of var Time : t_comie;
array[N_TAREA] of var Time : t_fin;
	array[N_TAREA] of var mind..maxd: D;
array[N_TAREA, N_TRAB] of var opt Time: O;
var 1..100 : total_time;
var 1..100 : t_cons;


% Distribucion de los trabajdores por tarea ??????????????
constraint 	cumulative(t_comie, D, [1 | i in N_TAREA], 3);

constraint 	forall(t in N_TAREA)(alternative(t_comie[t], D[t], [O[t,m] | m in N_TRAB], [duracion[t,m] | m in N_TRAB]));

constraint 	forall(m in N_TRAB)( disjunctive([O[t,m] | t in N_TAREA], [duracion[t,m] | t in N_TAREA]) );

% Comprobacion de las duraciones de cada tarea
constraint forall(t in N_TAREA) (t_fin[t] == t_comie[t] + D[t]);

% Comprobacion de las precedencias de cada tarea
constraint forall(p in N_PRECE) ( t_comie[ requisitos[p,1] ] + D[ requisitos[p,1] ] <= t_comie[ requisitos[p,2] ] );


constraint maximum(t_cons, t_fin);
% Hacemos la parte de satisfaccion y la de minimizacion de tiempo total.
solve minimize t_cons;

% Resultado:
output ["Tiempo Consumido: \(t_cons) días\n"] ++ ["\(TAREA[t]): Dias[\(t_comie[t]+1)-\(t_fin[t])]\n" | t in N_TAREA];
