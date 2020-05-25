(define (domain ejercicio1)
    (:requirements :strips :typing :fluents :equality :equality)
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
        (asignadoRecursoEn ?rec - tipoRecursos ?loc - Localizaciones)           ; Localizacion de recurso

        (puedeReclutar ?tipo - tipoEdificios ?uni - tipoUnidades)

        (faltaInvestigar ?tipo - tipoUnidades)                                  ; Se necesita ivnestigar apra desbloquear la unidad
        
;        (SinCaminos)

        (hayCamino ?loc1 - Localizaciones ?loc2 - Localizaciones)            ; Camino entre dos puntos

        (extrayendoEn ?vce - Unidades ?loc - Localizaciones)                    ; Se esta extrayendo en localizacion

        (necesita ?edi - tipoEdificios ?rec - tipoRecursos)                     ; Edificio necesita recurso
        
        (necesitaUnidad ?tipo - tipoUnidades ?rec - tipoRecursos)               ; Unidad necesita recurso
        
        (necesitaInvestigar ?tipo - tipoUnidades ?rec - tipoRecursos)
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
                (not (extrayendoEn ?uni ?loc1))
            )
        :effect
            (and
                (not (unidadEn ?uni ?loc1))
                (unidadEn ?uni ?loc2)
            )
    )

    (:action Asignar
        :parameters (?vce - Unidades ?loc - Localizaciones ?edi - Edificios ?r - tipoRecursos )
        :precondition                                                           ; Existe una unidad de tipo VCE en el punto, no está extrayendo, el recurso esta en la misma posicion
            (and
                (unidadEn ?vce ?loc)
                (unidadTipo ?vce VCE)
                (not (extrayendoEn ?vce ?loc))
                (asignadoRecursoEn ?r ?loc)
                ;(recursoTipo ?rec ?r)
                (or (= ?r Mineral) (and(edificioEn ?edi ?loc)(edificioTipo ?edi ExtractorGas) ))
            )
        :effect 
            (and
                (extrayendoEn ?vce ?loc)
            )
    )

    (:action Desasignar
        :parameters (?vce - Unidades ?loc - Localizaciones ?r - tipoRecursos)
        :precondition
            (and
                (extrayendoEn ?vce ?loc)         ; comprueba si esta extrayendo
                (asignadoRecursoEn ?r ?loc)    ; y que recurso esta generando
            )
        :effect
            (and
                (not (extrayendoEn ?vce ?loc))  ; desasigna el vce del recurso en el que estaba
            )
    )
    
    (:action Recolectar
        :parameters (?vce - Unidades ?loc - Localizaciones ?r - tipoRecursos)
        :precondition                                                           ; Existe una unidad de tipo VCE en el punto, no está extrayendo, el recurso esta en la misma posicion
            (and
                (extrayendoEn ?vce ?loc)
                (asignadoRecursoEn ?r ?loc)
                (<
                    (Reserva ?r)
                    (LimiteReserva)
                )
            )
        :effect 
            (and
                (forall (?vce2 - Unidades ?loc2 - Localizaciones)
                    (when
                        (extrayendoEn ?vce2 ?loc2)
                        (asignadoRecursoEn ?r ?loc2)
                    )
                )
                (increase (Reserva ?r) 40)
            )
    )

    (:action Construir
        :parameters (?vce - Unidades ?edi - Edificios ?loc - Localizaciones ?tipoE - tipoEdificios)
        :precondition
            (and
                (unidadTipo ?vce VCE)                                           ; la unidad tiene que ser un VCE
                (unidadEn ?vce ?loc)                                            ; la unidad tiene que estar en la localizacion requerida
                (not (extrayendoEn ?vce ?loc))                                  ; no puede estar ocupada extrayendo
                
                (not (exists (?otro - Edificios)
                        (edificioEn ?otro ?loc)
                      )
                )   
                
                (not (exists (?otraLoc - Localizaciones)
                        (edificioEn ?edi ?otraLoc)
                      )
                )
                
                (edificioTipo ?edi ?tipoE)
                
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
        :effect
            (and
                (edificioEn ?edi ?loc)
                (when (and (edificioTipo ?edi CentroDeMando)
                           )
                    (and (decrease (Reserva Mineral) 150)
                         (decrease (Reserva Gas) 50)
                    ;(edificioEn ?edi ?loc)
                    )
                )
                (when (and (edificioTipo ?edi Barracones)
                           )
                    (and (decrease (Reserva Mineral) 150)
                    ;(edificioEn ?edi ?loc)
                    )
                )
                (when (and (edificioTipo ?edi ExtractorGas)
                           )
                    (and (decrease (Reserva Mineral) 75)
                    ;(edificioEn ?edi ?loc)
                    )
                )
                (when (and (edificioTipo ?edi BahiaIngenieria)
                           )
                    (and (decrease (Reserva Mineral) 125)
                    ;(edificioEn ?edi ?loc)
                    )
                )
                (when (and (edificioTipo ?edi Deposito)
                        )
                    (and (decrease (Reserva Mineral) 75)
                    (decrease (Reserva Gas) 25)
                    (increase (LimiteReserva) 100)
                    ;(edificioEn ?edi ?loc)
                    )
                )
            )
    )
    
    (:action Reclutar
        :parameters (?uniCreada - Unidades ?tipoUni - tipoUnidades ?tipoE - tipoEdificios ?loc - Localizaciones)
        :precondition
            (and
                (unidadTipo ?uniCreada ?tipoUni)                                ; la unidad creada tiene que ser de su tipo
                (not (exists (?otraLoc - Localizaciones)
                        (unidadEn ?uniCreada ?otraLoc)
                      )
                )                             
                (puedeReclutar ?tipoE ?tipoUni)
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

                (exists (?edi - Edificios  )
                    (and
                        (edificioTipo ?edi ?tipoE)
                        (edificioEn ?edi ?loc)
                    )
                )
            )
        :effect
            (and
                (when (= ?tipoUni VCE)
                    (and (decrease (Reserva Mineral) 50)
                    )
                    
                )
                (when (= ?tipoUni Marine)
                    (and (decrease (Reserva Mineral) 50)
                    )
                )
                (when (= ?tipoUni Segador)
                    (and (decrease (Reserva Mineral) 50)
                         (decrease (Reserva Gas) 50)
                )
                )
                (unidadEn ?uniCreada ?loc)

            )
    )
    
    (:action Investigar
        :parameters(?tipoUni - tipoUnidades)
        :precondition
            (and
                (>= (Reserva Gas) 200)
                (>= (Reserva Mineral) 50)
                (exists (?edi - Edificios ?loc - Localizaciones )
                    (and
                        (edificioTipo ?edi BahiaIngenieria)
                        (edificioEn ?edi ?loc)
                    )
                )
                (faltaInvestigar ?tipoUni)
     
            )
        :effect
            (and
                (not(faltaInvestigar ?tipoUni))
                (decrease (Reserva Gas) 200)
                (decrease (Reserva Mineral) 50)
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


























