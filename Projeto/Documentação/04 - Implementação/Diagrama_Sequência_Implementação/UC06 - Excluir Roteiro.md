# UC06 - Excluir Roteiro
---
![](https://www.plantuml.com/plantuml/png/VP2nRi8m48PtFuKbbdPWe0W47IeW4AaIGmNj0nZd81QEBptRfL7Lf-dG1-75Qa9GGw1rXaY-xx_lpYFh13cVQIQyG-Ep5H1ZTgjCBaXatAOYmPqo6xuMsaAjGkXD8f4C4E_SrIFmwi58u1-Cyp4HxfvJ9RS6hEMTS3LNQvWh0nEa12X2ZSHRm_BKgq9kDwMZ8kBXuGl4ao2dfCg5SMSj-kKv1s8qRkaA3No5Ecc46uPTgXompzecMXbmHGuSoQN8c90kS2_M2rB8MzF-R1mFsEhGGb315-X04KR1IQWrK41wSM_MPV9uTIAMG6zAGjWfYhjHt2ml84UhWge8n7WwcGuOEqdR3rSqznpUfVQAgj8D7V-FoIsxf4EBnXp_vLc33UcBCNyRDCmDh7rDBT6i5MLY_xt_malT7mjRBy5QlBPUI5Il5RXgdbavNe9i12RncVu1)

---
```plantuml
@startuml
autonumber

skinparam shadowing false
skinparam roundcorner 12
skinparam sequence {
  ArrowThickness 1
  LifeLineBorderColor #999999
  LifeLineBackgroundColor #F9F9F9
  ParticipantBorderColor #666666
  ParticipantFontStyle bold
  ParticipantBackgroundColor white
}
skinparam linetype ortho

actor Usuario #D6EAF8
boundary RoteiroController #D5F5E3
control RoteiroService #FCF3CF
control RoteiroRepository #FADBD8

Usuario -> RoteiroController : excluirRoteiro(roteiroId)
RoteiroController -> RoteiroService : excluirRoteiro(roteiroId)

RoteiroService -> RoteiroRepository : excluir(roteiroId)
RoteiroRepository --> RoteiroService : confirmação

RoteiroService --> RoteiroController : sucesso
RoteiroController --> Usuario : roteiroExcluido
@enduml
```
