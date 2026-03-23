@startuml
autonumber
skinparam sequenceMessageAlign center
skinparam responseMessageBelowArrow true

actor Usuario

participant "RoteiroView" as View
participant "RoteiroController" as Controller
participant "GerenciarRoteiroService" as Service
participant "RoteiroRepository" as RoteiroRepo
participant "LugarRepository" as LugarRepo

Usuario -> View : Solicitar ajuste
activate View

View -> Controller : ajustarRoteiro(...)
activate Controller

Controller -> Service : ajustarRoteiro(...)
activate Service

Service -> RoteiroRepo : buscarRoteiro()
activate RoteiroRepo
RoteiroRepo --> Service : Roteiro
deactivate RoteiroRepo

alt Inclusão de novo local
Service -> LugarRepo : buscarLugar()
activate LugarRepo
LugarRepo --> Service : Lugar
deactivate LugarRepo
end

Service -> Service : aplicar ajustes
Service -> Service : recalcular custo/tempo

Service -> RoteiroRepo : salvar(roteiro)
activate RoteiroRepo
RoteiroRepo --> Service : confirmação
deactivate RoteiroRepo

Service --> Controller : Roteiro atualizado
deactivate Service

Controller --> View : exibirRoteiro()
deactivate Controller

View --> Usuario : Mostra roteiro atualizado
deactivate View

@enduml