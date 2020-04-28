% Ejemplo: N-Reinas:
include "globals.mzn";
int: n = 9;
set of int: POS = 1..10;
array [POS] of var 0..n: valores;


% Ataques en misma fila: no hay (cada reina ya está en filas
% distintas gracias al modelado del problema)
%constraint alldifferent(valores); % Ataques en columnas

constraint forall(i in POS)(count(valores, i-1) == valores[i]);

solve satisfy;

output [ "\(valores[i])" | i in POS];
