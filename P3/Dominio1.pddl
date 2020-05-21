﻿(define (domain ejercicio1)
    (:requirements :strips :typing)
    (:types
        Unidades Edificios Localizaciones Recurso - object      ; Tipo de objetos
        tipoUnidades tipoEdificios tipoRecursos - constants     ; Tipos posibles de objetos
    )
    (:constants 
        VCE - tipoUnidades
        CentroDeMando Barracones - tipoEdificios
        Mineral Gas - tipoRecursos
    )
    (:predicates
        (unidadTipo ?uni - Unidades ?tipo - tipoUnidades)                       ; Asigna tipo a unidad
        (edificioTipo ?edi - Edificios ?tipo - tipoEdificios)                   ; Asigna tipo a edificio
        (recursoTipo ?loc - Recurso ?tipo - tipoRecursos)                       ; Asigna tipo a recurso



        (unidadEn ?uni - Unidades ?loc - Localizaciones)                        ; Localizacion de unidad
        (edificioEn ?edi - Edificios ?loc - Localizaciones)                     ; Localizacion de edificio
        (asignadoRecursoEn ?rec - tipoRecursos ?loc - Localizaciones)           ; Localizacion de recurso


        (SinCaminos)

        (hayCamino ?loc1 - Localizaciones ?loc2 - Localizaciones)            ; Camino entre dos puntos

        (extrayendoEn ?vce - Unidades ?loc - Localizaciones)                    ; Se esta extrayendo en localizacion

        (necesita ?edi - tipoEdificios ?rec - tipoRecursos)                     ; Edificio necesita recurso
    )
    
    (:action Navegar
        :parameters (?uni - Unidades ?loc1 - Localizaciones ?loc2 - Localizaciones)
        :precondition                                                           ; Existe un camino entre puntos, la unidad se situa en el primero y no puede estar extrayendo
            (and
                (hayCamino ?loc1 ?loc2)
                (unidadEn ?uni ?loc1)
                (not (extrayendoEn ?uni ?loc1))
            )
        :effect
            (and
                (not (unidadEn ?uni ?loc1))
                (unidadEn ?uni ?loc2)
            )
    )

    (:action Asignar
        :parameters (?vce - Unidades ?loc - Localizaciones ?r - tipoRecursos)
        :precondition                                                           ; Existe una unidad de tipo VCE en el punto, no está extrayendo, el recurso esta en la misma posicion
            (and
                (unidadEn ?vce ?loc)
                (unidadTipo ?vce VCE)
                (not (extrayendoEn ?vce ?loc))
                (asignadoRecursoEn ?r ?loc)
            )
        :effect 
            (and
                (extrayendoEn ?vce ?loc)
            )
    )

    (:action Construir
        :parameters (?vce - Unidades ?edi ?otro - Edificios ?loc - Localizaciones)
        :precondition
            (and
                (unidadTipo ?vce VCE)                                           ; la unidad tiene que ser un VCE
                (unidadEn ?vce ?loc)                                            ; la unidad tiene que estar en la localizacion requerida
                (not (extrayendoEn ?vce ?loc))                                  ; no puede estar ocupada extrayendo
                (not (edificioEn ?otro ?loc))                                   ; no puede haber ya un edificio
                
                (exists (?vce2 - Unidades ?rec - tipoRecursos ?loc2 - Localizaciones ?tipoE - tipoEdificios)
                    (and
                        (extrayendoEn ?vce2 ?loc2)
                        (asignadoRecursoEn ?rec ?loc2)
                        (necesita ?tipoE ?rec)
                        (edificioTipo ?edi ?tipoE)
                    )
                )
            )
        :effect
            (and
                (edificioEn ?edi ?loc)
            )
    )
    
;    (:action CrearCamino
;       :parameters()
;        :precondition
;            (and
;                (SinCaminos)
;            )
;        :effect
;            (and
;                (forall (?l1 - Localizaciones)
;                    (forall (?l2 - Localizaciones)
;                        (when (and(not (hayCamino ?l1 ?l2 ) ))
;                            (hayCamino ?l1 ?l2)
;                        )
;                    )
;                )
;                (not(SinCaminos))
;            )
;    
;    )
    
    
)

























