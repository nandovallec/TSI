% Ejemplo: N-Reinas:
include "globals.mzn";
int: n = 9;
set of int: N_PERIODOS = 1..6;
set of int: N_DIAS = 1..5;

array [N_PERIODOS,N_DIAS] of var 1..15: horarioAsig;
array [N_PERIODOS,N_DIAS] of var 1..10: horarioProf;

array [1..15] of string: ASIG = ["A1","A2","A3","A4","A5","A6","A7","A8","A9","REC","A1.2","A3.2","A4.2","5.2","8.2"];
array [1..15] of var 1..5: HORAS = [2,2,2,2,2,2,2,1,1,5,2,2,2,2,1];

constraint forall(i in N_PERIODOS, j in N_DIAS)(
if (horarioAsig[i,j]==1\/horarioAsig[i,j]==3) then 
horarioProf[i,j] == 1
elseif (horarioAsig[i,j]==4\/horarioAsig[i,j]==5) then 
horarioProf[i,j] == 2
elseif (horarioAsig[i,j]==6\/horarioAsig[i,j]==9) then
horarioProf[i,j] == 3
else 
horarioProf[i,j] == (3+i)
endif
);
constraint forall(j in N_DIAS)(horarioAsig[3,j] == 10);
constraint forall(j in N_DIAS)(horarioAsig[2,j] != 1 /\horarioAsig[2,j] != 3 /\horarioAsig[2,j] != 4 /\horarioAsig[2,j] != 5 /\horarioAsig[2,j] != 8);
constraint forall(j in N_DIAS)(horarioAsig[6,j] != 1 /\horarioAsig[6,j] != 3 /\horarioAsig[6,j] != 4 /\horarioAsig[6,j] != 5 /\horarioAsig[6,j] != 8);

constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 1 \/ horarioAsig[i+1,j] == 11);
constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 3 \/ horarioAsig[i+1,j] == 12);
constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 4 \/ horarioAsig[i+1,j] == 13);
constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 5 \/ horarioAsig[i+1,j] == 14);
constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 8 \/ horarioAsig[i+1,j] == 15);

constraint forall(w in 1..15)(count([ horarioAsig[i,j] | i in N_PERIODOS, j in N_DIAS ], w)==HORAS[w]);



constraint forall (j in N_DIAS) ( alldifferent( [ horarioAsig[i,j] | i in N_PERIODOS ]) );

output [ "\(i):\(j) Prof:\(horarioProf[i,j]) Asignatura:\(ASIG[fix(horarioAsig[i,j])])\t" ++ if j==5 then "\n" else "   " endif | i in N_PERIODOS, j in N_DIAS]