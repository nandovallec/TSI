% Ejemplo: N-Reinas:
include "globals.mzn";
int: n = 9;
set of int: N_PERIODOS = 1..4;
set of int: N_AULAS = 1..4;

array [N_PERIODOS,N_AULAS] of var 1..4: horarioAsig;
array [N_PERIODOS,N_AULAS] of var 1..4: horarioGrup;
array [N_PERIODOS,N_AULAS] of var 1..7: horarioProf;

array [1..3] of string: ASIG = ["IA","TSI","FBD"];
array [1..4] of string: GRUP = ["G1","G2","G3","G4"];




% Ataques en misma fila: no hay (cada reina ya está en filas
% distintas gracias al modelado del problema)

constraint forall(i in 1..4)(alldifferent([if horarioAsig[i,j]< 4 then horarioAsig[i,j] else j*4+j endif|j in 1..4]));
constraint forall(i in 1..4)(alldifferent([horarioGrup[i,j]|j in 1..4]));
constraint forall(i in 1..4)(alldifferent([horarioProf[i,j]|j in 1..4]));

constraint all_different([horarioAsig[i,j]*10+horarioGrup[i,j] | i,j in N_PERIODOS]); % Ataques en columnas

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
constraint forall(i in 1..4)(horarioProf[2,i] != 2);
constraint forall(i in 1..4)(horarioProf[1,i] != 4);
% constraint forall(i in N_HORAS)((horarioProfe[horarioClase[i],1] <= i)/\(horarioProfe[horarioClase[i],2] >= i));

solve satisfy;


output [ "Clase a las \(i),\(j) impartida \(horarioProf[i,j]) por " ++ if fix(horarioAsig[i,j])<4 then "\(ASIG[fix(horarioAsig[i,j])])-\(GRUP[fix(horarioGrup[i,j])])" else " Rec " endif ++ if j==4 then "\n" else "   " endif | i,j in N_PERIODOS]
%output [ "Clase a las \(i),\(j) importida por \(horarioAsig[i,j])" ++ if j==4 then "\n" else "   " endif | i,j in N_PERIODOS]
%output ["xx \(horarioAsig[i,j]) " ++ if fix(horarioAsig[i,j])> 2 then "\(horarioAsig[i,j])\n" else "00\n" endif|i,j in 1..4]
