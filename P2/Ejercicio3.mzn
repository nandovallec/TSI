% Ejemplo: N-Reinas:
include "globals.mzn";
int: n = 9;
set of int: N_PROF = 1..6;
set of int: N_HORAS = 9..14;
array [N_PROF,1..2] of var N_HORAS: horarioProfe=[|11,14|11,12|10,13|10,12|11,12|9,14|];
array [N_HORAS] of var N_PROF: horarioClase;


% Ataques en misma fila: no hay (cada reina ya está en filas
% distintas gracias al modelado del problema)

constraint alldifferent(horarioClase); % Ataques en columnas

constraint forall(i in N_HORAS)((horarioProfe[horarioClase[i],1] <= i)/\(horarioProfe[horarioClase[i],2] >= i));

solve satisfy;

output [ "Clase a las \(i) importida por \(horarioClase[i])\n" | i in N_HORAS]
