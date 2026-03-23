@startuml
autonumber
skinparam sequenceMessageAlign center
skinparam responseMessageBelowArrow true

actor Usuario

participant "RoteiroView" as View
participant "PerfilController" as Controller
participant "GerenciarPreferenciaService" as Service
participant "PreferenciaRepository" as Repo

Usuario -> View : Atualizar preferências
activate View

View -> Controller : atualizarPreferencias(...)
activate Controller

Controller -> Service : atualizarPreferencias(...)
activate Service

Service -> Repo : buscarPreferencias()
activate Repo
Repo --> Service : Preferencias
deactivate Repo

Service -> Service : atualizar dados

Service -> Repo : salvar(preferencias)
activate Repo
Repo --> Service : confirmação
deactivate Repo

Service --> Controller : sucesso
deactivate Service

Controller --> View : mensagem de sucesso
deactivate Controller

View --> Usuario : confirmação exibida
deactivate View

@enduml