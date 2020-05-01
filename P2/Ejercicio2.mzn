% Ejercicio 2:
include "globals.mzn";
int: digitos_posibles = 9;

% Creamos array de 10 posiciones
set of int: POS = 1..10;
array [POS] of var 0..digitos_posibles: valores;

% Imponemos la condición sobre cada posicion del array
constraint forall(i in POS)(count(valores, i-1) == valores[i]);

solve satisfy;

% Salida
output [ "Numero resultante: "]++ ["\(valores[i])" | i in POS];
