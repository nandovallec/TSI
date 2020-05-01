% Ejercicio 5:
include "globals.mzn";
set of int: N_PERIODOS = 1..6;
set of int: N_DIAS = 1..5;

% Horario final de asignaturas y profesores
array [N_PERIODOS,N_DIAS] of var 1..15: horarioAsig;
array [N_PERIODOS,N_DIAS] of var 1..10: horarioProf;

% Los A1.2 son asignaturas que se imparten en dos horas, se obliga a que A1 este justo detras de A1.2.  REC es recreo
array [1..15] of string: ASIG = ["A1","A2","A3","A4","A5","A6","A7","A8","A9","REC","A1.2","A3.2","A4.2","A5.2","A8.2"];

% Profesor para cada asignatura
array [1..15] of int: PROF_POR_ASIG = [1,4,1,2,2,3,4,4,3,0,1,1,2,2,4];

% Numero de veces que van a aparecer en el horario cada asignatura
array [1..15] of var 1..5: HORAS = [2,2,2,2,2,2,2,1,1,5,2,2,2,2,1];

% Asignamos profesores dependiendo de las asignatura, para la 4 asignamos profesores "distintos" y asi las siguientes restricciones funcionan
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

% Establece recreos
constraint forall(j in N_DIAS)(horarioAsig[4,j] == 10);

% Las asignaturas de 2 horas no pueden empezar en segunda hora por el recreo, o en ultima hora
constraint forall(j in N_DIAS)(horarioAsig[2,j] != 1 /\horarioAsig[2,j] != 3 /\horarioAsig[2,j] != 4 /\horarioAsig[2,j] != 5 /\horarioAsig[2,j] != 8);
constraint forall(j in N_DIAS)(horarioAsig[6,j] != 1 /\horarioAsig[6,j] != 3 /\horarioAsig[6,j] != 4 /\horarioAsig[6,j] != 5 /\horarioAsig[6,j] != 8);

% Emparejamos las asignaturas de dos horas con sus continuaciones
constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 1 \/ horarioAsig[i+1,j] == 11);
constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 3 \/ horarioAsig[i+1,j] == 12);
constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 4 \/ horarioAsig[i+1,j] == 13);
constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 5 \/ horarioAsig[i+1,j] == 14);
constraint forall(i in 1..5, j in N_DIAS)(horarioAsig[i,j] != 8 \/ horarioAsig[i+1,j] == 15);

% Aseguramos que cada asignatura aparece el numero de veces que debe
constraint forall(w in 1..15)(count([ horarioAsig[i,j] | i in N_PERIODOS, j in N_DIAS ], w)==HORAS[w]);

% Cada dia solo se puede impartir un bloque de la misma asignatura
constraint forall (j in N_DIAS) ( alldifferent( [ horarioAsig[i,j] | i in N_PERIODOS ]) );

% Cada dia un profesor solo puede impartir un bloque, salvo el prof 4 (resto de indices) el cual imparte mas
constraint forall (j in N_DIAS) ( alldifferent( [ horarioProf[i,j] | i in N_PERIODOS ]) );

solve satisfy;

% Salida
output [if fix(horarioAsig[i,j])!=10 then "Dia \(j) Periodo \(i) Prof\(PROF_POR_ASIG[fix(horarioAsig[i,j])]) Asign: \(ASIG[fix(horarioAsig[i,j])])\t" else "\tRecreo\t\t\t" endif ++ if j==5 then "\n" else "\t" endif | i in N_PERIODOS, j in N_DIAS]