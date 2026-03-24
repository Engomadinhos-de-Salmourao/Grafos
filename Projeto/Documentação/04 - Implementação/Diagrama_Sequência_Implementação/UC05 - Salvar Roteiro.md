# UC05 - Salvar Roteiro
---
![](https://www.plantuml.com/plantuml/png/ZP71Jjmm48RlVWfBNEY10qMWfOUA9Pj8I1mGspx0n9abrZgUT6opYge-JGzz45wiGqYaaAPedKEacU-V_n_x9aJWc1gd84NogIcHbGfxwrjWQ7IeeQA3zKzw1oxWfCEKV6M8FR8-_poLu8-4tg3-gRHUCzFXMstDtcC8-bnAztQ7zzRZBN65d94ZrYVN_PbsmUoVUe-1AAxbUoKU9B0rjWKVtusuwizxe20Vjx5pg4jorKmySpZKDgBwDTd4IOxOjQY9OqrAWOd2VGy9s98-sLpbwsAbojSHm9r-f8YMAHDF9kUG1RaiBlCBPTvA8x55VhO6PQUikCYAUViHMmfMh3eXrflRpKgXZpQE3cAThpRhl51gp7BsTS7zYmxWde67pYc__U-gJ-fVM2RCqdqWLpDq54-pYxvCmG0_4DzLC_CPUhRWFXO-C1Ee0HQBZ1hQIbWwFJBRkDiOw-XGXlpESWClVrv-BqJxtpiaWo7GqhMBOdX4uOOq_Ihg1dsL6lSN)
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
entity Roteiro #E8DAEF

Usuario -> RoteiroController : salvarRoteiro(roteiroId)
RoteiroController -> RoteiroService : salvarRoteiro(roteiroId)

RoteiroService -> RoteiroRepository : buscarPorId(roteiroId)
RoteiroRepository --> RoteiroService : Roteiro

RoteiroService -> Roteiro : marcarComoSalvo()

RoteiroService -> RoteiroRepository : salvar(roteiro)
RoteiroRepository --> RoteiroService : confirmação

RoteiroService --> RoteiroController : sucesso
RoteiroController --> Usuario : roteiroSalvo
@enduml
```
