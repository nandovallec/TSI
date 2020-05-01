% Ejercicio 3:
include "globals.mzn";
set of int: N_PROF = 1..6;
set of int: N_HORAS = 9..14;

% Horario final de la clase, indica que profesor imparte en cada hora
array [N_HORAS] of var N_PROF: horarioClase;

% Horario disponible de cada profesor
array [N_PROF,1..2] of var N_HORAS: horarioProfe=[|11,14|11,12|10,13|10,12|11,12|9,14|];


% Restricción deducida para agilizar proceso de búsqueda
constraint alldifferent(horarioClase);

% Restricción de disponibilidad de profesor en la hora indicada
constraint forall(i in N_HORAS)((horarioProfe[horarioClase[i],1] <= i)/\(horarioProfe[horarioClase[i],2] >= i));

solve satisfy;

% Salida
output [ "Clase en horario \(i):00-\(i+1):00 es impartida por Prof-\(horarioClase[i])\n" | i in N_HORAS]
