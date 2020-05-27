(define (domain ejercicio1)
    (:requirements :strips :typing :disjunctive-preconditions :negative-preconditions)
    (:types
        Unidades Edificios Localizaciones Recurso - object      ; Tipo de objetos
        tipoUnidades tipoEdificios tipoRecursos - constants     ; Tipos posibles de objetos
    )
    (:constants 
        VCE Marine Segador - tipoUnidades
        CentroDeMando Barracones ExtractorGas - tipoEdificios
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



;        (SinCaminos)

        (hayCamino ?loc1 - Localizaciones ?loc2 - Localizaciones)            ; Camino entre dos puntos

        (extraeLoc ?vce - Unidades ?loc - Localizaciones)                    ; Se esta extrayendo en localizacion

        (necesita ?edi - tipoEdificios ?rec - tipoRecursos)                     ; Edificio necesita recurso
        
        (necesitaUnidad ?tipo - tipoUnidades ?rec - tipoRecursos)               ; Unidad necesita recurso
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
        :parameters (?vce - Unidades ?loc - Localizaciones ?r - tipoRecursos ?rec - Recurso)
        :precondition                                                           ; Existe una unidad de tipo VCE en el punto, no está extrayendo, el recurso esta en la misma posicion
            (and
                (unidadEn ?vce ?loc)
                (unidadTipo ?vce VCE)
                (not (extraeLoc ?vce ?loc))
                (recursoEn ?r ?loc)
                (recursoTipo ?rec ?r)
                (or
                 (recursoTipo ?rec Mineral)
                 (exists (?edi - Edificios)
                    (and(edificioEn ?edi ?loc) (edificioTipo ?edi ExtractorGas))
                 )
                )  ; si el recurso es de tipo Gas se comprueba que haya un extractor construido
            )
        :effect 
            (and
                (extraeLoc ?vce ?loc)
            )
    )

    (:action Construir
        :parameters (?vce - Unidades ?edi - Edificios ?loc - Localizaciones ?tipoE - tipoEdificios)
        :precondition
            (and
                (unidadTipo ?vce VCE)                                           ; la unidad tiene que ser un VCE
                (unidadEn ?vce ?loc)                                            ; la unidad tiene que estar en la localizacion requerida
                (not (extraeLoc ?vce ?loc))                                  ; no puede estar ocupada extrayendo
                
                (not (exists (?otro - Edificios)                                ; no hay otro edificio
                        (edificioEn ?otro ?loc)
                      )
                )   
                
                (not (exists (?otraLoc - Localizaciones)                         ; no ha sido construido antes
                        (edificioEn ?edi ?otraLoc)
                      )
                )
                
                (edificioTipo ?edi ?tipoE)
                
                (forall (?rec - tipoRecursos)                                   ; itera sobre recursos y si se necesitan comprueba que existan
                    (or 
                        (not(necesita ?tipoE ?rec))
                        (exists (?vce2 - Unidades ?loc2 - Localizaciones )
                            (and
                                (extraeLoc ?vce2 ?loc2)
                                (recursoEn ?rec ?loc2)
                                (necesita ?tipoE ?rec)
                            )
                        )  
                    )
                
                )
                
            )
        :effect
            (and
                (edificioEn ?edi ?loc)
            )
    )
    
    (:action Reclutar
        :parameters (?uniCreada - Unidades ?locCrear - Localizaciones ?tipoUni - tipoUnidades ?edi - Edificios ?tipoE - tipoEdificios)
        :precondition
            (and
                (unidadTipo ?uniCreada ?tipoUni)                                ; la unidad creada tiene que ser de su tipo
                (not (exists (?otraLoc - Localizaciones)
                        (unidadEn ?uniCreada ?otraLoc)
                      )
                )                             
                (puedeReclutar ?tipoE ?tipoUni)                                  ; comprueba que exista un edificio que puede reclutar la unidad en la misma loc
                (edificioTipo ?edi ?TipoE)
                (edificioEn ?edi ?locCrear)

                (forall (?rec - tipoRecursos)                                   ; itera sobre recursos y si se necesitan comprueba que existan
                    (or 
                        (not(necesitaUnidad ?tipoUni ?rec))
                        (exists (?vce - Unidades ?loc2 - Localizaciones )
                            (and
                                (extraeLoc ?vce ?loc2)
                                (recursoEn ?rec ?loc2)
                                (necesitaUnidad ?tipoUni ?rec)
                            )
                        )  
                    )
                
                )
                
            )
        :effect
            (and
                (unidadEn ?uniCreada ?locCrear)
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


























