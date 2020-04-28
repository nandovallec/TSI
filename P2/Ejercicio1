% Ejemplo: N-Reinas:
include "globals.mzn";
int: n = 9;
set of int: POS = 1..10;
array [POS] of var 0..n: valores;

array[POS] of string: letras = ["A", "D", "E", "F", "I", "K", "N", "R", "S", "T"];

% Ataques en misma fila: no hay (cada reina ya está en filas
% distintas gracias al modelado del problema)
constraint alldifferent(valores); % Ataques en columnas
var int: teste = valores[10]*pow(10,4)+valores[3]*pow(10,3)+valores[9]*pow(10,2)+valores[10]*pow(10,1)+valores[3];
var int: feste = valores[4]*pow(10,4)+valores[3]*pow(10,3)+valores[9]*pow(10,2)+valores[10]*pow(10,1)+valores[3];
var int: deine = valores[2]*pow(10,4)+valores[3]*pow(10,3)+valores[5]*pow(10,2)+valores[7]*pow(10,1)+valores[3];
var int: krafte = valores[6]*pow(10,5)+valores[8]*pow(10,4)+valores[1]*pow(10,3)+valores[4]*pow(10,2)+valores[10]*pow(10,1)+valores[3];

constraint (krafte == (teste + feste + deine));

solve satisfy;

output [ "Para letra \(letras[i]) = \(valores[i])\n" | i in POS];
