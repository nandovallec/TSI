﻿(define (problem starcraft)
    (:domain ejercicio1)
    (:objects 
        loc1_1 loc1_2 loc1_3 loc1_4 loc1_5 loc2_1 loc2_2 loc2_3 loc2_4 loc2_5 loc3_1 loc3_2 loc3_3 loc3_4 loc3_5 loc4_1 loc4_2 loc4_3 loc4_4 loc4_5 loc5_1 loc5_2 loc5_3 loc5_4 loc5_5 - Localizaciones
        
        mando1 - Edificios
        vce1 vce2 vce3 vce4 vce5 vce6 vce7 vce8 vce9 vce10 vce11 vce12 vce13 vce14 vce15 vce16 vce17 vce18 vce19 vce20- Unidades
        
        marine1 marine2 - Unidades
        segador1 - Unidades

        mineral1 mineral2 mineral3 - Recurso
        gas1 gas2 - Recurso
        
        barracon1 - Edificios

        extractorGas1 - Edificios 

        deposito1 deposito2 deposito3 deposito4 - Edificios
        
        bahia1 - Edificios
    )
    (:init
        (unidadTipo vce1 VCE)
        (unidadTipo vce2 VCE)
        (unidadTipo vce3 VCE)
        ;(unidadTipo vce4 VCE)
        ;(unidadTipo vce5 VCE)
        ;(unidadTipo vce6 VCE)
        ;(unidadTipo vce7 VCE)
        ;(unidadTipo vce8 VCE)
        ;(unidadTipo vce9 VCE)

        (unidadTipo marine1 Marine)
        (unidadTipo marine2 Marine)
        (unidadTipo segador1 Segador)

        (recursoTipo mineral1 Mineral)
        (recursoTipo mineral2 Mineral)
        (recursoTipo mineral3 Mineral)
        (recursoTipo gas1 Gas)
        (recursoTipo gas2 Gas)

        (edificioTipo barracon1 Barracones)
        (edificioTipo mando1 CentroDeMando)
        (edificioTipo extractorGas1 ExtractorGas)
        (edificioTipo bahia1 BahiaIngenieria)
        (edificioTipo deposito1 Deposito)
        (edificioTipo deposito2 Deposito)
        (edificioTipo deposito3 Deposito)
        

        
        (edificioEn mando1 loc2_2)
        (unidadEn vce1 loc2_2)
        ;(unidadEn vce2 loc2_2)
        ;(unidadEn vce3 loc2_2)

        (asignadoRecursoEn Mineral loc4_2)
        (asignadoRecursoEn Mineral loc5_2)
        (asignadoRecursoEn Mineral loc5_3)

        (asignadoRecursoEn Gas loc3_4)
        (asignadoRecursoEn Gas loc4_1)

        (necesita CentroDeMando Gas)
        (necesita CentroDeMando Mineral)

        (necesita Barracones Mineral)
        (necesita ExtractorGas Mineral)
        
        (necesita BahiaIngenieria Gas)
        (necesita BahiaIngenieria Mineral)

        (necesita Deposito Mineral)
        
        (necesitaUnidad VCE Mineral)
        (necesitaUnidad Marine Mineral)
        (necesitaUnidad Segador Mineral)
        (necesitaUnidad Segador Gas)
        
        (puedeReclutar CentroDeMando VCE)
        (puedeReclutar Barracones Marine)
        (puedeReclutar Barracones Segador)
        
        (faltaInvestigar Segador)

        (= (Reserva Mineral) 0)
        (= (Reserva Gas) 0)
        (= (LimiteReserva) 100)

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
        (and
            (unidadEn marine1 loc2_4)
            (unidadEn marine2 loc2_3)
            (unidadEn segador1 loc2_3)
            ;(> (LimiteReserva) 300)
        )
    )
)