# Casos de Uso
---
![](https://www.plantuml.com/plantuml/png/TPAzRXin3CTtFGNXfPE1Ej_POWH5nmO9cfquqJOJdMIGEgSVwCCK7GfqxnFunQfJB3Ui6q_y7tya_wHrfHab3trmWHOPSWBXvIg3Pw4kSufE7pckKR27DNQFkAIx_3aGZ6cCoq0l05sXJqySbx30e2ypsEKapuNuH9fwSgv-Wyb77RRVXTC4KA48B77BpNtFaJLBwIiLg5-Ssq-7oKt0Z8iaFO8d-92vvo-u_RdzaKPzcpAn91tZUoxk-mbyTGxQChK-qWFs57Eo59JVeDIX5kmz2Gf8RLUvpOuRNL_FB3n_63J_nNVGaONkMGSCnQo0funXHTLbI1s6NSLngVXct1OR53kkTis2rh1jB-q8Yc6JTDVynF9t9NTW-TGoxpvrOU1_eRC3bpSa2mxW4xH_zy3UTOewXF58wmhlUfrRQfxR3ckX1SdsrxXys-32mhUqh6z5wWrPOSFA6Nrg_EM1qVq3go_cq6Kxjq377ZsrJhEtht1jj2VWc4b8bLhDh8ul1jx0T3g5kMhgU1pyctH_g37bYi3dUAopyiZAOojFh3oryipASoilhBpylz5wikgoHVKUHitS5KK_zE4F)

---

```plantuml
@startuml
left to right direction
skinparam packageStyle rectangle
skinparam shadowing false
skinparam actorStyle awesome

actor "Usuário" as Usuario
actor "Administrador" as Admin

rectangle "Plataforma de Otimização de Roteiros de Viagem" {

  package "Planejamento de Viagem" {
    usecase "Gerar roteiro de viagem" as UC1
    usecase "Ajustar roteiro" as UC2
    usecase "Visualizar detalhes de local" as UC3
  }

  package "Gerenciamento de Roteiros" {
    usecase "Visualizar roteiros salvos" as UC4
    usecase "Salvar roteiro" as UC5
    usecase "Excluir roteiro" as UC6
  }

  package "Perfil do Usuário" {
    usecase "Consultar perfil" as UC7
    usecase "Atualizar preferências" as UC8
    usecase "Registrar locais visitados" as UC9
  }

  package "Administração" {
    usecase "Gerenciar cidades" as UC10
    usecase "Gerenciar locais de interesse" as UC11
  }
}

' --- Associações Usuário ---
Usuario -- UC1
Usuario -- UC2
Usuario -- UC3
Usuario -- UC4
Usuario -- UC5
Usuario -- UC6
Usuario -- UC7
Usuario -- UC8
Usuario -- UC9

' --- Associações Admin ---
Admin -- UC10
Admin -- UC11



@enduml
```
