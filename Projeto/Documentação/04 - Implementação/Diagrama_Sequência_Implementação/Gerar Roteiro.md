@startuml
autonumber
skinparam sequenceMessageAlign center
skinparam responseMessageBelowArrow true

actor Usuario

participant "RoteiroView" as View
participant "RoteiroController" as Controller
participant "GerarRoteiroService" as Service
participant "UsuarioRepository" as UsuarioRepo
participant "PreferenciaRepository" as PrefRepo
participant "DestinoRepository" as DestinoRepo
participant "LugarRepository" as LugarRepo

Usuario -> View : Solicitar geração de roteiro
activate View

View -> Controller : gerarRoteiro(...)
activate Controller

Controller -> Service : gerarRoteiro(...)
activate Service

Service -> UsuarioRepo : buscarUsuario()
activate UsuarioRepo
UsuarioRepo --> Service : Usuario
deactivate UsuarioRepo

Service -> PrefRepo : buscarPreferencias()
activate PrefRepo
PrefRepo --> Service : Preferencias
deactivate PrefRepo

Service -> DestinoRepo : buscarDestino()
activate DestinoRepo
DestinoRepo --> Service : Destino
deactivate DestinoRepo

Service -> LugarRepo : buscarLugares()
activate LugarRepo
LugarRepo --> Service : Lista<Lugar>
deactivate LugarRepo

Service -> Service : calcular roteiro otimizado

Service --> Controller : Roteiro
deactivate Service

Controller --> View : exibirRoteiro()
deactivate Controller

View --> Usuario : Exibe resultado
deactivate View

@enduml