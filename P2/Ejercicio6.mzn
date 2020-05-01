% Ejercicio 6:
include "globals.mzn";

set of int: N_VECINOS = 1..5;
set of int: N_CARACT = 1..6;


array[N_VECINOS, N_CARACT] of var N_VECINOS: vecinos;


array[N_VECINOS] of string: region = ["vasco", "catalan", "gallego", "navarro", "andaluz"];

array[N_VECINOS] of string:  val_ani = ["perro", "caracoles", "zorro", "caballo", "cebra"];

array[N_VECINOS] of string: val_color = ["roja", "verde", "blanca", "amarilla", "azul"];

array[N_VECINOS] of string: val_prof = ["pintor", "escultor", "diplomatico", "violinista", "medico"];

array[N_VECINOS] of string: val_bebi = ["te", "cafe","leche","zumo", "agua"];

array[N_VECINOS] of string: val_loca = ["izquierda", "centroizquierda", "centro", "centroderecha", "derecha"];

% Cada vecino tiene valores únicos en cada característica
constraint forall (j in N_CARACT) (alldifferent([vecinos[i, j]|i in N_VECINOS]));


% a
constraint vecinos[1, 4] == 1;


% b
constraint vecinos[2, 2] == 1;


% c
constraint vecinos[3, 1] == 1;


% d
constraint vecinos[4, 3] == 1;


% e
constraint vecinos[5, 5] == 1;


% f
var int : verde;
constraint vecinos[verde, 4] == 2 /\ vecinos[verde, 3] == 2;


% g
var int : blanca;
constraint vecinos[blanca, 4] == 3 /\ vecinos[blanca, 5] + 1 == vecinos[verde, 5];


% h
var int : escultor;
constraint vecinos[escultor, 1] == 2 /\ vecinos[escultor, 2] == 2;


% i
var int : diplomatico;
constraint vecinos[diplomatico, 1] == 3 /\ vecinos[diplomatico, 4] == 4;


% j
var int : central;
constraint vecinos[central, 5] == 3 /\ vecinos[central, 3] == 3;


% k
var int : andaluz;
constraint vecinos[andaluz, 5] == 2 /\ vecinos[andaluz, 4] == 5;


% l
var int : violinista;
constraint vecinos[violinista, 1] == 4 /\ vecinos[violinista, 3] == 4;


% m
var int : zorro;
var int : medico;
constraint vecinos[zorro, 2] == 3 /\ vecinos[medico, 1] == 5;
constraint vecinos[zorro, 5] + 1 == vecinos[medico, 5] \/ vecinos[zorro, 5] - 1 == vecinos[medico, 5];


% n
var int : caballo;
constraint vecinos[caballo, 5] + 1 == vecinos[diplomatico, 5] \/ vecinos[caballo, 5] - 1 == vecinos[diplomatico, 5];
constraint vecinos[caballo, 2] == 4 /\ vecinos[diplomatico, 1] == 3;

% Salida
output ["Vecino:\(region[i])  \tProf:\(val_prof[fix(vecinos[i, 1])])   \t Animal:\( val_ani[fix(vecinos[i, 2])])  \t Bebida:\(val_bebi[fix(vecinos[i, 3])])\t Color:\(val_color[fix(vecinos[i, 4])])   \t Localizacion:\(val_loca[fix(vecinos[i, 5])])\n" | i in N_VECINOS];
