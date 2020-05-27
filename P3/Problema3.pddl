﻿(define (problem starcraft)
    (:domain ejercicio1)
    (:objects 
        ; Declaracion de objetos
        loc1_1 loc1_2 loc1_3 loc1_4 loc1_5 loc2_1 loc2_2 loc2_3 loc2_4 loc2_5 loc3_1 loc3_2 loc3_3 loc3_4 loc3_5 loc4_1 loc4_2 loc4_3 loc4_4 loc4_5 loc5_1 loc5_2 loc5_3 loc5_4 loc5_5 - Localizaciones
        
        mando1 - Edificios
        vce1 vce2 vce3 - Unidades

        mineral1 mineral2 mineral3 - Recurso
        gas1 gas2 - Recurso
        
        barracon1 - Edificios
        
        extractorGas1 - Edificios
    )
    (:init
        ; Asignar tipo a cada objeto
        (unidadTipo vce1 VCE)
        (unidadTipo vce2 VCE)
        (unidadTipo vce3 VCE)

        (recursoTipo mineral1 Mineral)
        (recursoTipo mineral2 Mineral)
        (recursoTipo mineral3 Mineral)
        (recursoTipo gas1 Gas)
        (recursoTipo gas2 Gas)

        (edificioTipo barracon1 Barracones)
        (edificioTipo mando1 CentroDeMando)
        (edificioTipo extractorGas1 ExtractorGas)

        ; Asignar localización (creado) a cada objeto
        (edificioEn mando1 loc2_2)
        (unidadEn vce1 loc2_2)
        (unidadEn vce2 loc2_2)
        (unidadEn vce3 loc2_2)

        (recursoEn Mineral loc4_2)
        (recursoEn Mineral loc5_2)
        (recursoEn Mineral loc5_3)

        (recursoEn Gas loc3_4)
        (recursoEn Gas loc3_5)

        ; Asignar recursos necesarios para casos de acciones
        (necesita CentroDeMando Gas)
        (necesita CentroDeMando Mineral)
        (necesita Barracones Mineral)
        (necesita ExtractorGas Mineral)
        

        ; Conexiones entre localizaciones
        (hayCamino loc1_1 loc2_1)
        (hayCamino loc2_1 loc1_1)
        (hayCamino loc1_1 loc1_2)
        (hayCamino loc1_2 loc1_1)
        (hayCamino loc1_2 loc2_2)
        (hayCamino loc2_2 loc1_2)
        (hayCamino loc1_2 loc1_3)
        (hayCamino loc1_3 loc1_2)
        (hayCamino loc1_2 loc1_1)
        (hayCamino loc1_1 loc1_2)
        (hayCamino loc1_3 loc2_3)
        (hayCamino loc2_3 loc1_3)
        (hayCamino loc1_3 loc1_4)
        (hayCamino loc1_4 loc1_3)
        (hayCamino loc1_3 loc1_2)
        (hayCamino loc1_2 loc1_3)
        (hayCamino loc1_4 loc2_4)
        (hayCamino loc2_4 loc1_4)
        (hayCamino loc1_4 loc1_5)
        (hayCamino loc1_5 loc1_4)
        (hayCamino loc1_4 loc1_3)
        (hayCamino loc1_3 loc1_4)
        (hayCamino loc1_5 loc2_5)
        (hayCamino loc2_5 loc1_5)
        (hayCamino loc1_5 loc1_4)
        (hayCamino loc1_4 loc1_5)
        (hayCamino loc2_1 loc1_1)
        (hayCamino loc1_1 loc2_1)
        (hayCamino loc2_1 loc3_1)
        (hayCamino loc3_1 loc2_1)
        (hayCamino loc2_1 loc2_2)
        (hayCamino loc2_2 loc2_1)
        (hayCamino loc2_2 loc1_2)
        (hayCamino loc1_2 loc2_2)
        (hayCamino loc2_2 loc3_2)
        (hayCamino loc3_2 loc2_2)
        (hayCamino loc2_2 loc2_3)
        (hayCamino loc2_3 loc2_2)
        (hayCamino loc2_2 loc2_1)
        (hayCamino loc2_1 loc2_2)
        (hayCamino loc2_3 loc1_3)
        (hayCamino loc1_3 loc2_3)
        (hayCamino loc2_3 loc3_3)
        (hayCamino loc3_3 loc2_3)
        (hayCamino loc2_3 loc2_4)
        (hayCamino loc2_4 loc2_3)
        (hayCamino loc2_3 loc2_2)
        (hayCamino loc2_2 loc2_3)
        (hayCamino loc2_4 loc1_4)
        (hayCamino loc1_4 loc2_4)
        (hayCamino loc2_4 loc3_4)
        (hayCamino loc3_4 loc2_4)
        (hayCamino loc2_4 loc2_5)
        (hayCamino loc2_5 loc2_4)
        (hayCamino loc2_4 loc2_3)
        (hayCamino loc2_3 loc2_4)
        (hayCamino loc2_5 loc1_5)
        (hayCamino loc1_5 loc2_5)
        (hayCamino loc2_5 loc3_5)
        (hayCamino loc3_5 loc2_5)
        (hayCamino loc2_5 loc2_4)
        (hayCamino loc2_4 loc2_5)
        (hayCamino loc3_1 loc2_1)
        (hayCamino loc2_1 loc3_1)
        (hayCamino loc3_1 loc4_1)
        (hayCamino loc4_1 loc3_1)
        (hayCamino loc3_1 loc3_2)
        (hayCamino loc3_2 loc3_1)
        (hayCamino loc3_2 loc2_2)
        (hayCamino loc2_2 loc3_2)
        (hayCamino loc3_2 loc4_2)
        (hayCamino loc4_2 loc3_2)
        (hayCamino loc3_2 loc3_3)
        (hayCamino loc3_3 loc3_2)
        (hayCamino loc3_2 loc3_1)
        (hayCamino loc3_1 loc3_2)
        (hayCamino loc3_3 loc2_3)
        (hayCamino loc2_3 loc3_3)
        (hayCamino loc3_3 loc4_3)
        (hayCamino loc4_3 loc3_3)
        (hayCamino loc3_3 loc3_4)
        (hayCamino loc3_4 loc3_3)
        (hayCamino loc3_3 loc3_2)
        (hayCamino loc3_2 loc3_3)
        (hayCamino loc3_4 loc2_4)
        (hayCamino loc2_4 loc3_4)
        (hayCamino loc3_4 loc4_4)
        (hayCamino loc4_4 loc3_4)
        (hayCamino loc3_4 loc3_5)
        (hayCamino loc3_5 loc3_4)
        (hayCamino loc3_4 loc3_3)
        (hayCamino loc3_3 loc3_4)
        (hayCamino loc3_5 loc2_5)
        (hayCamino loc2_5 loc3_5)
        (hayCamino loc3_5 loc4_5)
        (hayCamino loc4_5 loc3_5)
        (hayCamino loc3_5 loc3_4)
        (hayCamino loc3_4 loc3_5)
        (hayCamino loc4_1 loc3_1)
        (hayCamino loc3_1 loc4_1)
        (hayCamino loc4_1 loc5_1)
        (hayCamino loc5_1 loc4_1)
        (hayCamino loc4_1 loc4_2)
        (hayCamino loc4_2 loc4_1)
        (hayCamino loc4_2 loc3_2)
        (hayCamino loc3_2 loc4_2)
        (hayCamino loc4_2 loc5_2)
        (hayCamino loc5_2 loc4_2)
        (hayCamino loc4_2 loc4_3)
        (hayCamino loc4_3 loc4_2)
        (hayCamino loc4_2 loc4_1)
        (hayCamino loc4_1 loc4_2)
        (hayCamino loc4_3 loc3_3)
        (hayCamino loc3_3 loc4_3)
        (hayCamino loc4_3 loc5_3)
        (hayCamino loc5_3 loc4_3)
        (hayCamino loc4_3 loc4_4)
        (hayCamino loc4_4 loc4_3)
        (hayCamino loc4_3 loc4_2)
        (hayCamino loc4_2 loc4_3)
        (hayCamino loc4_4 loc3_4)
        (hayCamino loc3_4 loc4_4)
        (hayCamino loc4_4 loc5_4)
        (hayCamino loc5_4 loc4_4)
        (hayCamino loc4_4 loc4_5)
        (hayCamino loc4_5 loc4_4)
        (hayCamino loc4_4 loc4_3)
        (hayCamino loc4_3 loc4_4)
        (hayCamino loc4_5 loc3_5)
        (hayCamino loc3_5 loc4_5)
        (hayCamino loc4_5 loc5_5)
        (hayCamino loc5_5 loc4_5)
        (hayCamino loc4_5 loc4_4)
        (hayCamino loc4_4 loc4_5)
        (hayCamino loc5_1 loc4_1)
        (hayCamino loc4_1 loc5_1)
        (hayCamino loc5_1 loc5_2)
        (hayCamino loc5_2 loc5_1)
        (hayCamino loc5_2 loc4_2)
        (hayCamino loc4_2 loc5_2)
        (hayCamino loc5_2 loc5_3)
        (hayCamino loc5_3 loc5_2)
        (hayCamino loc5_2 loc5_1)
        (hayCamino loc5_1 loc5_2)
        (hayCamino loc5_3 loc4_3)
        (hayCamino loc4_3 loc5_3)
        (hayCamino loc5_3 loc5_4)
        (hayCamino loc5_4 loc5_3)
        (hayCamino loc5_3 loc5_2)
        (hayCamino loc5_2 loc5_3)
        (hayCamino loc5_4 loc4_4)
        (hayCamino loc4_4 loc5_4)
        (hayCamino loc5_4 loc5_5)
        (hayCamino loc5_5 loc5_4)
        (hayCamino loc5_4 loc5_3)
        (hayCamino loc5_3 loc5_4)
        (hayCamino loc5_5 loc4_5)
        (hayCamino loc4_5 loc5_5)
        (hayCamino loc5_5 loc5_4)
        (hayCamino loc5_4 loc5_5)
    )
    (:goal
        (edificioEn barracon1 loc2_3)
    )
)