% Ejercicio 4:
include "globals.mzn";
set of int: N_PERIODOS = 1..4;
set of int: N_AULAS = 1..4;

% Horario final de asignaturas y grupos
array [N_PERIODOS,N_AULAS] of var 1..4: horarioAsig;
array [N_PERIODOS,N_AULAS] of var 1..4: horarioGrup;

% Horario auxiliar de profesores impartiendo
array [N_PERIODOS,N_AULAS] of var 1..7: horarioProf;

array [1..3] of string: ASIG = ["IA","TSI","FBD"];
array [1..4] of string: GRUP = ["G1","G2","G3","G4"];



% Cada grupo solo puede estar en un aula a la misma hora
constraint forall(i in 1..4)(alldifferent([horarioGrup[i,j]|j in 1..4]));
% Cada profesor solo puede estar en un aula a la misma hora
constraint forall(i in 1..4)(alldifferent([horarioProf[i,j]|j in 1..4]));
% Solo se permite una aparición de cada grupo para cada asignatura
constraint all_different([horarioAsig[i,j]*10+horarioGrup[i,j] | i,j in N_PERIODOS]);

% Asignamos el profesor correspondiente para cada asignatura y grupo
constraint forall(i,j in 1..4)(
if (horarioAsig[i,j]==1/\(horarioGrup[i,j]==1\/horarioGrup[i,j]==2))\/(horarioAsig[i,j]==2/\(horarioGrup[i,j]==1\/horarioGrup[i,j]==2)) then horarioProf[i,j] == 1
elseif (horarioAsig[i,j]==3/\(horarioGrup[i,j]==1\/horarioGrup[i,j]==2)) then
horarioProf[i,j] == 2
elseif (horarioAsig[i,j]==2/\(horarioGrup[i,j]==3\/horarioGrup[i,j]==4))\/(horarioAsig[i,j]==3/\(horarioGrup[i,j]==3\/horarioGrup[i,j]==4)) then
horarioProf[i,j] == 3
elseif (horarioAsig[i,j]==1/\(horarioGrup[i,j]==3\/horarioGrup[i,j]==4)) then
horarioProf[i,j] == 4
else 
horarioProf[i,j] == (4+j)
endif
);

% Comprobamos restricciones de horas de los profesores 2 y 4
constraint forall(i in 1..4)(horarioProf[2,i] != 2);
constraint forall(i in 1..4)(horarioProf[1,i] != 4);


solve satisfy;

% Salida
output [if fix(horarioAsig[i,j])<4 then "Aula \(j), Periodo \(i) Imp: Prof\(horarioProf[i,j]) : \(ASIG[fix(horarioAsig[i,j])])-\(GRUP[fix(horarioGrup[i,j])])\t" else "\t\t Recreo\t\t\t\t" endif ++ if j==4 then "\n" else "" endif | i,j in N_PERIODOS]

