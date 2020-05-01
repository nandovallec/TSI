% Ejercicio 10:
include "globals.mzn";

set of int: N_OBJ = 1..12;
array[N_OBJ] of string : objects = ["Mapa", "Compas", "Agua", "Sandwich", "Azucar", "Lata", "Platano", "Manzana", "Queso", "Cerveza", "Protector Solar", "Camara"];

% Peso de cada objeto
array[N_OBJ] of int : pesos =  [ 9, 13, 153, 50, 15, 68, 27, 39, 23, 52, 11, 32];

% Capacidad de la mochila
int : capacidad = 275;

% Preferencia asociada a cada objeto
array[N_OBJ] of int : pref =  [ 150, 35, 200, 160, 60, 45, 60, 40, 30, 10, 70, 30];

% Array 'booleano' de objetos en la mochila
array[N_OBJ] of var 0..1: mochila;


% Restricción de capacidad
constraint sum(i in N_OBJ)(mochila[i] * pesos[i]) <= capacidad;

% Maximizamos preferencia
solve maximize sum(i in N_OBJ)(mochila[i] * pref[i]);

% Salida
output ["Preferencia alcanzada: \(sum(i in N_OBJ)(mochila[i] * pref[i])) \nMochila:"] ++ [" \(objects[i])," | i in N_OBJ where fix(mochila[i]) == 1];