% Ejercicio 1:
include "globals.mzn";
int: digitos_posibles = 9;
set of int: POS = 1..10;

% Valor de cada letra
array [POS] of var 0..digitos_posibles: valores;

% Posibles letras
array[POS] of string: letras = ["A", "D", "E", "F", "I", "K", "N", "R", "S", "T"];

% Todos los valores son diferentes
constraint alldifferent(valores);

% Calculamos valor de cada palabra
var int: teste = valores[10]*pow(10,4)+valores[3]*pow(10,3)+valores[9]*pow(10,2)+valores[10]*pow(10,1)+valores[3];
var int: feste = valores[4]*pow(10,4)+valores[3]*pow(10,3)+valores[9]*pow(10,2)+valores[10]*pow(10,1)+valores[3];
var int: deine = valores[2]*pow(10,4)+valores[3]*pow(10,3)+valores[5]*pow(10,2)+valores[7]*pow(10,1)+valores[3];
var int: krafte = valores[6]*pow(10,5)+valores[8]*pow(10,4)+valores[1]*pow(10,3)+valores[4]*pow(10,2)+valores[10]*pow(10,1)+valores[3];

% Igualamos las palabras para encontrar valores de las letras
constraint (krafte == (teste + feste + deine));

solve satisfy;

% Salida
output ["Para letra \(letras[i]) = \(valores[i])\n" | i in POS];
