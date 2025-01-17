﻿(define (domain ejercicio1)
    (:requirements :strips :typing :fluents :equality :equality :negative-preconditions)
    (:types
        Unidades Edificios Localizaciones Recurso - object      ; Tipo de objetos
        tipoUnidades tipoEdificios tipoRecursos - constants     ; Tipos posibles de objetos
    )
    (:constants 
        VCE Marine Segador - tipoUnidades
        CentroDeMando Barracones ExtractorGas BahiaIngenieria Deposito - tipoEdificios
        Mineral Gas - tipoRecursos
    )
    (:predicates
        (unidadTipo ?uni - Unidades ?tipo - tipoUnidades)                       ; Asigna tipo a unidad
        (edificioTipo ?edi - Edificios ?tipo - tipoEdificios)                   ; Asigna tipo a edificio
        (recursoTipo ?loc - Recurso ?tipo - tipoRecursos)                       ; Asigna tipo a recurso


        (unidadEn ?uni - Unidades ?loc - Localizaciones)                        ; Localizacion de unidad
        (edificioEn ?edi - Edificios ?loc - Localizaciones)                     ; Localizacion de edificio
        (recursoEn ?rec - tipoRecursos ?loc - Localizaciones)           ; Localizacion de recurso

        (puedeReclutar ?tipo - tipoEdificios ?uni - tipoUnidades)               ; Que tipo de edificio puede reclutar que tipo de unidad

        (puedeReclutarEn ?uni - tipoUnidades ?loc - Localizaciones)               ; Donde se puede reclutar que tipo de unidad

        (faltaInvestigar ?tipo - tipoUnidades)                                  ; Se necesita ivnestigar apra desbloquear la unidad
        
        (puedeInvestigar ?tipo - tipoUnidades)                                  ; Se necesita ivnestigar apra desbloquear la unidad
;        (SinCaminos)
        (reclutada ?uni - Unidades)                                              ; La unidad ya ha sido reclutada

        (construido ?edi - Edificios)                                            ; El edificio ya ha sido construido

        (hayEdificioEn ?loc - Localizaciones)                                       ; Existe un edificio en una localizacion

        (hayExtractor ?loc - Localizaciones)                                                ; Existe un extractor en la localizacion

        (hayCamino ?loc1 - Localizaciones ?loc2 - Localizaciones)            ; Camino entre dos puntos

        (extraeLoc ?vce - Unidades ?loc - Localizaciones)                    ; Se esta extrayendo en localizacion

        (extraeRecurso ?vce - Unidades ?r -tipoRecursos)                            ; Que unidad está extrayendo que recurso

        (necesita ?edi - tipoEdificios ?rec - tipoRecursos)                     ; Edificio necesita recurso
        
        (necesitaUnidad ?tipo - tipoUnidades ?rec - tipoRecursos)               ; Unidad necesita recurso
        
        (necesitaInvestigar ?tipo - tipoUnidades ?rec - tipoRecursos)              ; Que recursos consuma realizar una investigacion
    )
    
    (:functions
        (Reserva ?rec - tipoRecursos)
        (LimiteReserva)
    )
    
    (:action Navegar
        :parameters (?uni - Unidades ?loc1 - Localizaciones ?loc2 - Localizaciones)
        :precondition                                                           ; Existe un camino entre puntos, la unidad se situa en el primero y no puede estar extrayendo
            (and
                (hayCamino ?loc1 ?loc2)
                (unidadEn ?uni ?loc1)
                (not (extraeLoc ?uni ?loc1))
            )
        :effect
            (and
                (not (unidadEn ?uni ?loc1))
                (unidadEn ?uni ?loc2)
            )
    )

    (:action Asignar
        :parameters (?vce - Unidades ?loc - Localizaciones  ?r - tipoRecursos )
        :precondition                                                           ; Existe una unidad de tipo VCE en el punto, no está extrayendo, el recurso esta en la misma posicion
            (and
                (unidadEn ?vce ?loc)
                (unidadTipo ?vce VCE)
                (not (extraeLoc ?vce ?loc))
                (recursoEn ?r ?loc)

                (or 
                    (= ?r Mineral) 
                    (hayExtractor ?loc)
                )  ; si el recurso es de tipo Gas se comprueba que haya un extractor construido
            )
        :effect 
            (and
                (extraeLoc ?vce ?loc)
                (extraeRecurso ?vce ?r)
            )
    )

    (:action Desasignar
        :parameters (?vce - Unidades ?loc - Localizaciones ?r - tipoRecursos)
        :precondition
            (and
                (extraeLoc ?vce ?loc)         ; comprueba si esta extrayendo
                (recursoEn ?r ?loc)    ; y que recurso esta generando
            )
        :effect
            (and
                (not (extraeLoc ?vce ?loc))  ; desasigna el vce del recurso en el que estaba
                (not (extraeRecurso ?vce ?r))
            )
    )
    
    (:action Recolectar
        :parameters (?r - tipoRecursos)
        :precondition                                                         
            (and
                ;(exists (?vce - Unidades)
                 ;   (extraeRecurso ?vce ?r)
                ;)
                (<=                                         ; Al menos hay espacio para qué un trabajdor puede insertar su recolección (no es completamente necesaria pero reduce mucho planes infactibles)
                    (+
                    (Reserva ?r)
                     10)
                    (LimiteReserva)
                )
            )
        :effect 
            (and
                (forall (?vce - Unidades)
                    (when (and(extraeRecurso ?vce ?r))
                        (and(increase (Reserva ?r) 10))
                    )
                )
                (when (> (Reserva ?r) (LimiteReserva))
                    (assign (Reserva ?r) (LimiteReserva))
                )
            )
    )

    (:action Construir
        :parameters (?vce - Unidades ?edi - Edificios ?loc - Localizaciones )
        :precondition
            (and
                (unidadTipo ?vce VCE)                                           ; la unidad tiene que ser un VCE
                (unidadEn ?vce ?loc)                                            ; la unidad tiene que estar en la localizacion requerida
                (not (extraeLoc ?vce ?loc))                                  ; no puede estar ocupada extrayendo
                
                (not(hayEdificioEn ?loc))                                   ; no hay otro edificio
                
                (not(construido ?edi))                         ; no ha sido construido antes
                
                (exists (?tipoE - tipoEdificios)
                    (and
                        (edificioTipo ?edi ?tipoE)                      ; comprobar que existen los recursos para el tipo edificio
                        (or
                            (and (edificioTipo ?edi CentroDeMando)
                                (>= (Reserva Mineral) 150)
                                (>= (Reserva Gas) 50))
                            (and (edificioTipo ?edi Barracones)
                                (>= (Reserva Mineral) 150))
                            (and (edificioTipo ?edi ExtractorGas)
                                (>= (Reserva Mineral) 75))
                            (and (edificioTipo ?edi BahiaIngenieria)
                                (>= (Reserva Mineral) 125))
                            (and (edificioTipo ?edi Deposito)
                                (>= (Reserva Mineral) 75)
                                (>= (Reserva Gas) 25)
                            
                            )
                        )
                    )
                )
                

                
            )
        :effect
            (and
                (edificioEn ?edi ?loc)
                (construido ?edi)
                (hayEdificioEn ?loc)
                (when  (edificioTipo ?edi CentroDeMando)
                           
                    (and (decrease (Reserva Mineral) 150)
                         (decrease (Reserva Gas) 50)
                         (puedeReclutarEn VCE ?loc)
                    )
                )
                (when  (edificioTipo ?edi Barracones)
                           
                    (and (decrease (Reserva Mineral) 150)
                         (puedeReclutarEn Marine ?loc)
                         (puedeReclutarEn Segador ?loc)
                    )
                )
                (when  (edificioTipo ?edi ExtractorGas)
                           
                    (and (decrease (Reserva Mineral) 75)
                         (hayExtractor ?loc)
                    )
                )
                (when  (edificioTipo ?edi BahiaIngenieria)
                           
                    (and (decrease (Reserva Mineral) 125)
                         (puedeInvestigar Segador)
                    )
                )
                (when  (edificioTipo ?edi Deposito)
                        
                    (and (decrease (Reserva Mineral) 75)
                    (decrease (Reserva Gas) 25)
                    (increase (LimiteReserva) 100)
                    )
                )
            )
    )
    
    (:action Reclutar
        :parameters (?uniCreada - Unidades   ?loc - Localizaciones)
        :precondition
            (and
                                                ; la unidad creada tiene que ser de su tipo
                (not(reclutada ?uniCreada))                             
                
                (exists (?tipoUni - tipoUnidades)                      ; comprobar que existen los recursos para el tipo unidad
                    (and
                        (puedeReclutarEn ?tipoUni ?loc)
                        (unidadTipo ?uniCreada ?tipoUni)
                        (not (faltaInvestigar ?tipoUni))
                        (or
                            (and (= ?tipoUni VCE)
                                (>= (Reserva Mineral) 50)
                            )
                            (and (= ?tipoUni Marine)
                                (>= (Reserva Mineral) 50)
                            )
                            (and (= ?tipoUni Segador)
                                (>= (Reserva Mineral) 50)
                                (>= (Reserva Gas) 50)
                            )
                        )
                    )
                )



            )
        :effect
            (and
                (when (unidadTipo ?uniCreada VCE)
                    (and (decrease (Reserva Mineral) 50)
                    )
                    
                )
                (when (unidadTipo ?uniCreada Marine)
                    (and (decrease (Reserva Mineral) 50)
                    )
                )
                (when (unidadTipo ?uniCreada Segador)
                    (and (decrease (Reserva Mineral) 50)
                         (decrease (Reserva Gas) 50)
                )
                )
                (unidadEn ?uniCreada ?loc)
                (reclutada ?uniCreada)

            )
    )
    
    (:action Investigar
        :parameters(?tipoUni - tipoUnidades)
        :precondition
            (and
                (>= (Reserva Gas) 200)                      ; comprobar que existen los recursos para investigar (como es solo segador no hace falta bucle)
                (>= (Reserva Mineral) 50)
                (puedeInvestigar ?tipoUni)                  ; existe un edificio que pueda investigar (como es solo segador significa que existe bahia de ingenieria)
                (faltaInvestigar ?tipoUni)                  ; la unidad ha sido investigada
     
            )
        :effect
            (and
                (not(faltaInvestigar ?tipoUni))
                (decrease (Reserva Gas) 200)
                (decrease (Reserva Mineral) 50)
            )
    )

    
)


























